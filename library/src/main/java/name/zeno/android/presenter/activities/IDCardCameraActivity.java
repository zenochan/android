package name.zeno.android.presenter.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import name.zeno.android.camera.CameraManager;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.third.rxjava.RxUtils;
import name.zeno.android.util.ZBitmap;
import name.zeno.android.util.R;

/**
 * Create Date: 16/6/19
 * <p>
 * 在manifest 根节点添加
 * <uses-feature android:name="android.hardware.camera"/>
 * <uses-feature android:name="android.hardware.camera.autofocus"/>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @see <a href='https://github.com/lizhangqu/Camera'>Android Camera身份证取景</a>
 */
@SuppressWarnings("FieldCanBeLocal")
public class IDCardCameraActivity extends ZActivity implements SurfaceHolder.Callback
{
  public static final String EXTRA_PARAM_PATH = "param_path";

  private SurfaceView surfaceView;
  private ImageView   takeIv;

  private CameraManager cameraManager;
  private Camera.PictureCallback jpegCallBack = (bytes, camera) -> {
    resolveImage(bytes);
    cameraManager.stopPreview();
    cameraManager.closeDriver();
  };

  private        boolean hasSurface;
  private static String  path;
  private int rotateDegree = 0;

  public static Intent getCallingIntent(Context context, String path)
  {
    Intent intent = new Intent(context, IDCardCameraActivity.class);
    intent.putExtra(EXTRA_PARAM_PATH, path);
    return intent;
  }

  @Override public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
  {

  }

  @Override public void surfaceCreated(SurfaceHolder surfaceHolder)
  {

    if (!hasSurface) {
      hasSurface = true;
      initCamera(surfaceHolder);
    }
  }


  @Override public void surfaceDestroyed(SurfaceHolder surfaceHolder)
  {
    hasSurface = false;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_camera);
    surfaceView = (SurfaceView) findViewById(R.id.surface);
    takeIv = (ImageView) findViewById(R.id.iv_take);
    takeIv.setOnClickListener(v -> {
      cameraManager.takePicture(jpegCallBack);
      takeIv.setEnabled(false);
    });
    path = getIntent().getStringExtra(EXTRA_PARAM_PATH);
  }

  @Override protected void onResume()
  {
    super.onResume();
    cameraManager = new CameraManager();
    SurfaceHolder holder = surfaceView.getHolder();
    if (hasSurface) {
      //activity 在 paused 时不会 stopped，因此 surface 仍然存在
      //surfaceCreated 不会调用，因此在这里初始化 camera
      initCamera(holder);
    } else {
      //重置callback，等待surfaceCreated（）来初始化camera
      holder.addCallback(this);
    }

  }

  @Override protected void onPause()
  {
    cameraManager.stopPreview();
    cameraManager.closeDriver();
    if (!hasSurface) {
      surfaceView.getHolder().removeCallback(this);
    }
    super.onPause();
  }

  /**
   * 初始化 camera
   */
  private void initCamera(SurfaceHolder surfaceHolder)
  {
    if (surfaceHolder == null) {
      throw new IllegalArgumentException("未提供 surfaceHoder");
    }

    if (cameraManager.isOpen()) {
      return;
    }

    try {
      cameraManager.openDriver(surfaceHolder);
      cameraManager.startPreview();
      rotateDegree = cameraManager.setCameraDisplayOrientation(this);
    } catch (Exception ignore) {
      Log.e(TAG, ignore.getMessage(), ignore);
      new AlertDialog.Builder(this)
          .setMessage("打开摄像机失败")
          .setPositiveButton("好", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            finish();
          })
          .show();
    }

  }

  private void resolveImage(byte[] bytes)
  {
    Observable.create((ObservableOnSubscribe<Boolean>) subscriber -> {
      final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
      int          width  = bitmap.getWidth();
      int          height = bitmap.getHeight();
      Matrix       matrix = new Matrix();
      matrix.setRotate(rotateDegree);
//      if (width > 960) {
//        float scale = 960F / width;
//        matrix.postScale(scale, scale);
//      }
      int unit = height / 3;
      int x, y, w, h;
      x = (int) ((float) width / 2 - 1.5F * unit);
      y = (int) (unit * 0.5F);
      w = unit * 3;
      h = unit * 2;

      final Bitmap tem      = Bitmap.createBitmap(bitmap, x, y, w, h, matrix, true);
      String       filePath = path.substring(0, path.lastIndexOf("/") + 1);
      String       fileName = path.substring(path.lastIndexOf("/") + 1);
      ZBitmap.savePhotoToSDCard(tem, filePath, fileName);

      subscriber.onNext(true);
    }).compose(RxUtils.applySchedulers()).subscribe(new Observer<Boolean>()
    {
      @Override public void onSubscribe(Disposable d) { }

      @Override public void onComplete() { }

      @Override public void onError(Throwable e) { }

      @Override public void onNext(Boolean aBoolean)
      {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAM_PATH, path);
        setResult(RESULT_OK, intent);
        finish();
      }
    })
    ;
  }
}
