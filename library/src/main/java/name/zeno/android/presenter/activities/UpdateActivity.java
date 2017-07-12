package name.zeno.android.presenter.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import name.zeno.android.data.CommonConnector;
import name.zeno.android.data.models.UpdateInfo;
import name.zeno.android.exception.ZException;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.presenter.ZNav;
import name.zeno.android.system.ZPermission;
import name.zeno.android.third.rxjava.RxUtils;
import name.zeno.android.third.rxjava.ZObserver;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * 下载更新apk
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/21.
 */
public class UpdateActivity extends ZActivity
{
  public static final String EXTRA_UPDATE_INFO = "extra_update_info";

  private UpdateInfo updateInfo;
  private String     localFilePath;

  private RxPermissions rxPermissions;

  public static Intent getCallingIntent(Context context, UpdateInfo updateInfo)
  {
    Intent intent = new Intent(context, UpdateActivity.class);
    intent.putExtra(EXTRA_UPDATE_INFO, updateInfo);
    return intent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(new View(this));
    rxPermissions = new RxPermissions(this);
    updateInfo = getIntent().getParcelableExtra(EXTRA_UPDATE_INFO);
    showUpdate(updateInfo);
  }


  public void showUpdate(UpdateInfo version)
  {
    MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
        .title("发现新版本,是否现在升级?")
        .content(version.getVersionInfo())
        .positiveText("好")
        .negativeText("取消")
        .cancelable(false)
        .onNegative((dialog, which) -> finish())
        .onPositive((dialog, which) -> preDownload(version));

    if (version.isForceUpdate()) {
      builder.title("发现新版本,需要升级后才能继续使用,是否现在升级?");
      builder.content(version.getVersionInfo());
      builder.negativeText("退出应用");
      builder.onNegative((dialog, which) -> ZNav.exit(this));
    }

    builder.show();
  }

  void preDownload(UpdateInfo updateInfo)
  {
    rxPermissions.request(ZPermission.READ_EXTERNAL_STORAGE, ZPermission.WRITE_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (granted) {
            download(updateInfo.getVersionUrl(), updateInfo.getVersionCode());
          } else {
            permisionDenied(updateInfo.isForceUpdate());
          }
        });
  }

  void permisionDenied(boolean forceUpdate)
  {
    if (forceUpdate) {
      ZNav.exit(this);
    } else {
      finish();
    }
  }

  void download(String fileUrl, int versionName)
  {
    // /downloads/{packageName}_{versionName}.apk
    localFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
        + File.separator + getPackageName() + "_" + versionName + ".apk";

    MaterialDialog dialog = new MaterialDialog.Builder(this)
        .title("下载更新")
        .content("正在下载,请稍候")
        .progress(false, 100)
        .cancelable(false)
        .build();
    dialog.show();

    Observable.create((ObservableOnSubscribe<Float>) subscriber -> {
      long contentLength;
      long completeSize = 0;

      InputStream      is               = null;
      RandomAccessFile randomAccessFile = null;

      try {
        Response<ResponseBody> response = CommonConnector.download(fileUrl).execute();
        int                    code     = response.raw().code();
        if (code != 200) {
          if (code == 404) {
            throw new ZException(ZException.ERR_NOT_FOUND, "文件链接无效");
          } else {
            throw new ZException(ZException.ERR_DEFAULT, "未知错误:" + code);
          }
        }

        ResponseBody body = response.body();
        contentLength = body.contentLength();
        is = body.source().inputStream();
        randomAccessFile = new RandomAccessFile(localFilePath, "rwd");

        if (randomAccessFile.length() != 0 && randomAccessFile.length() == contentLength) {
          subscriber.onComplete();
          return;
        }

        int    length = -1;
        int    limit  = -1;
        byte[] buffer = new byte[1024 * 1024];
        while ((length = is.read(buffer)) != -1) {
          randomAccessFile.write(buffer, 0, length);
          completeSize += length;

          if (completeSize < contentLength) {
            if (limit % 30 == 0) {
              //limit / 30 ==0 :限制更新频率
              float progress = (float) completeSize / (float) contentLength;
              subscriber.onNext(progress);
            }
            limit++;
          }
        }

        subscriber.onComplete();

      } catch (Exception e) {
        subscriber.onError(e);
      } finally {
        try {
          if (is != null) {
            is.close();
          }
          if (randomAccessFile != null) {
            randomAccessFile.close();
          }
        } catch (IOException e) {
          subscriber.onError(e);
        }
      }
    }).compose(RxUtils.applySchedulers())
        .subscribe(new ZObserver<Float>()
        {
          boolean err = false;

          @Override
          public void handleError(ZException e)
          {
            showMessage(e.getMessage());
            dialog.setContent(e.getMessage());
            dialog.setProgress(0);
            err = true;
          }

          @Override
          public void onComplete()
          {
            File file = new File(localFilePath);
            if (!err) {
              dialog.dismiss();
              new MaterialDialog.Builder(UpdateActivity.this)
                  .content("下载完成")
                  .positiveText("安装")
                  .negativeText("退出")
                  .autoDismiss(false)
                  .cancelable(false)
                  .onPositive((dialog, which) -> installAPK(file))
                  .onNegative((dialog, which) -> ZNav.exit(UpdateActivity.this))
                  .show();
              installAPK(file);
            }
          }

          @Override
          public void onNext(Float aFloat)
          {
            dialog.setProgress((int) (aFloat * 100));
          }
        });
  }

  private void installAPK(File file)
  {
    if (!file.exists()) return;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      //通过FileProvider创建一个content类型的Uri
      Uri uri = FileProvider.getUriForFile(this, updateInfo.getFileProvider(), file);

      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setDataAndType(uri, "application/vnd.android.package-archive");
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      startActivity(intent);
    } else {
      Intent intent = new Intent(Intent.ACTION_VIEW);
      Uri    uri    = Uri.parse("file://" + file.toString());
      intent.setDataAndType(uri, "application/vnd.android.package-archive");
      //在服务中开启activity必须设置flag
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
  }
}
