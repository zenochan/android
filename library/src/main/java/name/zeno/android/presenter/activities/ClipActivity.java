package name.zeno.android.presenter.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.system.ZStatusBar;
import name.zeno.android.third.rxjava.RxUtils;
import name.zeno.android.util.PhotoCaptureHelper;
import name.zeno.android.util.R;
import name.zeno.android.util.R2;
import name.zeno.android.util.ZBitmap;
import name.zeno.android.widget.ClipImageLayout;

public class ClipActivity extends ZActivity
{
  public static final String EXTRA_PATH = "path";

  @BindView(R2.id.layout_clip_img) ClipImageLayout mClipImageLayout;

  private ProgressDialog loadingDialog;

  private String cachePath;

  public static Intent callIntent(Context context, String path)
  {
    Intent intent = new Intent(context, ClipActivity.class);
    intent.putExtra(EXTRA_PATH, path);

    return intent;
  }

  public static void start(Activity activity, int requestCode, String path)
  {
    Intent intent = new Intent(activity, ClipActivity.class);
    intent.putExtra(EXTRA_PATH, path);
    activity.startActivityForResult(intent, requestCode);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ZStatusBar.setImage(this);
    setContentView(R.layout.activity_clip_image);
    ButterKnife.bind(this);
    initWidget();
    loadBmp();
  }

  private void initWidget()
  {
    loadingDialog = new ProgressDialog(this);
    loadingDialog.setTitle("请稍后...");
    cachePath = PhotoCaptureHelper.getCachePath(this);
  }

  private void loadBmp()
  {
    String path = getIntent().getStringExtra("path");
    if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
      Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }
    Bitmap bitmap = ZBitmap.bitmap(path, 600, 600);
    if (bitmap == null) {
      Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
      finish();
      return;
    }
    mClipImageLayout = (ClipImageLayout) findViewById(R.id.layout_clip_img);
    mClipImageLayout.setBitmap(bitmap);
  }

  private void onSubmit()
  {
    loadingDialog.show();
    Observable.create((ObservableOnSubscribe<String>) subscriber -> {
      Bitmap bitmap = mClipImageLayout.clip();
      bitmap = ZBitmap.zoom(bitmap, 300, 300);
      String fileName = +System.currentTimeMillis() + ".jpg";
      ZBitmap.savePhotoToSDCard(bitmap, cachePath, fileName);
      String filePath = cachePath + fileName;
      subscriber.onNext(filePath);
      subscriber.onComplete();
    }).compose(RxUtils.applySchedulers()).subscribe(new Observer<String>()
    {
      @Override public void onSubscribe(Disposable d) { }

      @Override public void onComplete()
      {
        loadingDialog.dismiss();
      }

      @Override public void onError(Throwable e) { }

      @Override public void onNext(String s)
      {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PATH, s);
        setResult(RESULT_OK, intent);
        finish();
      }
    });
  }


  @OnClick(R2.id.btn_submit) void submit()
  {
    onSubmit();
  }

  @OnClick(R2.id.btn_cancel) void cancel()
  {
    finish();
  }
}
