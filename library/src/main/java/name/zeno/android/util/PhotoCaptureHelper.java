package name.zeno.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import com.orhanobut.logger.Logger;

import java.io.File;

import lombok.Getter;
import lombok.Setter;
import name.zeno.android.presenter.activities.IDCardCameraActivity;
import name.zeno.android.listener.Action1;

/**
 * <h1>获取图片帮助类</h1>
 * <h3>使用</h3>
 * 将方法{@link #onActivityResult(int, int, Intent)} 与 activity/fragment 周期同步
 * <p>
 * 使用 {@link #getImageFromAlbum()} 从相册获取图片;<br/>
 * 使用 {@link #getImageFromCamera(String)} 从相机获取图片;
 * <p>
 * 使用 {@link #setListener(Action1)} 设置回调监听器监听完成后的图片存储路径
 * <p>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/3
 */
@SuppressWarnings("unused")
public class PhotoCaptureHelper
{
  /** 从相册选择 */
  public static final int REQUEST_CODE_ALBUM  = 0x2001;
  /** 拍照 */
  public static final int REQUEST_CODE_CAMERA = 0x2002;

  private static final String TAG = "ImageCaptureHelper";

  private Fragment fragment;
  private Activity activity;

  private Context context;
  private String  cachePath;
  private String  fileName;

  @Setter @Getter
  private Action1<String> listener;


  public PhotoCaptureHelper(Activity activity)
  {
    this.activity = activity;
    this.context = activity;
    init(this.activity);
  }

  public PhotoCaptureHelper(Fragment fragment)
  {
    this.fragment = fragment;
    this.context = fragment.getContext();
    init(this.context);
  }

  /** 从相册选择图片 */
  @RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
  public void getImageFromAlbum()
  {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType("image/*");
    if (fragment != null) {
      fragment.startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_ALBUM);
    } else {
      activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_ALBUM);
    }
  }

  /** 从相机获取图片 */
  @RequiresPermission(allOf = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"})
  public void getImageFromCamera(String fileProvider)
  {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    fileName = newFileName();

    File photoFile = new File(cachePath, fileName);
    Uri  imageUri;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      //通过FileProvider创建一个content类型的Uri
      imageUri = FileProvider.getUriForFile(context, fileProvider, photoFile);
    } else {
      imageUri = Uri.fromFile(photoFile);
    }

    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    if (fragment != null) {
      fragment.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    } else {
      activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
  }

  @RequiresPermission(allOf = {
      "android.permission.WRITE_EXTERNAL_STORAGE",
      "android.permission.CAMERA"
  })
  public void getIdCardFromCamera()
  {
    fileName = newFileName();
    Intent intent = IDCardCameraActivity.getCallingIntent(context, cachePath + fileName);
    if (fragment != null) {
      fragment.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    } else {
      activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (resultCode != Activity.RESULT_OK) {
      Logger.e("resultCode: " + resultCode);
      return;
    }

    switch (requestCode) {
      case REQUEST_CODE_ALBUM:
        if (data == null) {
          onImageSelected(null);
          return;
        }
        onImageFromAlbum(data);
        break;
      case REQUEST_CODE_CAMERA:
        onImageSelected(cachePath + fileName);
        break;
    }
  }

  public void onDestroy()
  {
    activity = null;
    fragment = null;
    context = null;
  }

  private void init(Context context)
  {
    cachePath = getCachePath(context);
  }

  private void onImageSelected(String filePath)
  {
    if (listener != null) {
      listener.call(filePath);
    }
  }

  private void onImageFromAlbum(@NonNull Intent data)
  {
    String filePath;
    Uri    uri;
    //Android 4.4 +
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      uri = data.getData();
      filePath = PictureUtils.getPath(context, uri);
    } else {
      uri = data.getData();
      String[] projection  = {MediaStore.Images.Media.DATA};
      Cursor   cursor      = new CursorLoader(context, uri, projection, null, null, null).loadInBackground();
      int      columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      filePath = cursor.getString(columnIndex);// 图片在的路径
    }
    onImageSelected(filePath);
  }

  public static String newFileName()
  {
    return String.format("%s-%d.jpg", TAG, System.currentTimeMillis());
  }

  public static String getCachePath(Context context)
  {
    File cacheDir = context.getExternalCacheDir();
    if (cacheDir == null) {
      cacheDir = context.getCacheDir();
    }
    return cacheDir.getAbsolutePath() + "/";
  }

}
