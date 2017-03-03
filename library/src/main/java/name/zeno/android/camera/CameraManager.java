package name.zeno.android.camera;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

import name.zeno.android.util.ZDimen;
import name.zeno.android.util.ZLog;

/**
 * Camera 管理类
 * <p>
 * Create Date: 16/6/19
 * <p>
 * 遇到的问题
 * <ul>
 * <li>设置预览大小和图像大小失败：<a href='http://blog.csdn.net/fulinwsuafcie/article/details/39348869'>Camera setParameters failed</a></li>
 * </ul>
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "deprecation"})
public class CameraManager
{
  private static final String TAG = "CameraManager";
  private Camera camera;
  Camera.CameraInfo cameraInfo;
  private Camera.Parameters parameters;
  private AutoFocusManager  autoFocusManager;
  private int requestedCameraId = -1;

  private boolean initialized;
  private boolean previewing;

  /** 打开摄像头 */
  public Camera open(int cameraId)
  {
    int numCameras = Camera.getNumberOfCameras();
    if (numCameras == 0) {
      ZLog.e(TAG, "该设备没有摄像头");
      return null;
    }

    boolean explicitRequest = cameraId >= 0;
    if (!explicitRequest) {
      //未指定摄像头时， 选择摄像头
      int index = 0;
      while (index < numCameras) {
        cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(index, cameraInfo);
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
          break;
        }
        index++;
      }
      cameraId = index;
    }

    Camera camera;
    if (cameraId < numCameras) {
      camera = Camera.open(cameraId);
    } else {
      if (explicitRequest) {
        ZLog.e(TAG, "指定的摄像头不存在:" + cameraId);
        camera = null;
      } else {
        camera = Camera.open(0);
      }
    }

    return camera;
  }

  /** 打开 camera */
  public synchronized void openDriver(SurfaceHolder holder) throws IOException
  {
    Camera theCamera = camera;
    if (theCamera == null) {
      theCamera = open(requestedCameraId);
      if (theCamera == null) {
        throw new IOException();
      }
    }
    theCamera.setPreviewDisplay(holder);
    camera = theCamera;

    if (!initialized) {
      initialized = true;
      parameters = camera.getParameters();
      parameters.setPictureFormat(ImageFormat.JPEG);
      parameters.setJpegQuality(100);
      Point windowSize = ZDimen.getWindowPixelsSize();

      Camera.Size size;
      size = getSize(parameters.getSupportedPreviewSizes());
      parameters.setPreviewSize(size.width, size.height);
      size = getSize(parameters.getSupportedPictureSizes());
      parameters.setPictureSize(size.width, size.height);

      theCamera.setParameters(parameters);
    }
  }

  private Camera.Size getSize(List<Camera.Size> sizeList)
  {
    Camera.Size r = null;
    int deltaW = 10000;
    // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
    for (Camera.Size size : sizeList) {
      int d = Math.abs(size.width - 800);
      if ((double) size.height / (double) size.width == 0.75 && deltaW > d) {
        r = size;
        deltaW = d;
      }
    }

    if (r == null) {
      r = sizeList.get(0);
    }

    ZLog.e(TAG, r.width + "," + r.height);
    return r;
  }

  /** camera 是否打开 */
  public synchronized boolean isOpen()
  {
    return camera != null;
  }

  /** 关闭 camera */
  public synchronized void closeDriver()
  {
    if (camera != null) {
      camera.release();
      camera = null;
    }
  }

  /** 开始预览 */
  public synchronized void startPreview()
  {
    Camera theCamera = camera;
    if (theCamera != null && !previewing) {
      theCamera.startPreview();
      previewing = true;
      autoFocusManager = new AutoFocusManager(camera);
    }
  }

  /** 关闭预览 */
  public synchronized void stopPreview()
  {

    if (autoFocusManager != null) {
      autoFocusManager.stop();
      autoFocusManager = null;
    }

    if (camera != null && previewing) {
      camera.stopPreview();
      previewing = false;
    }
  }

  /**
   * 拍照
   */
  public synchronized void takePicture(Camera.PictureCallback jpeg)
  {
    if (camera != null) {
      camera.cancelAutoFocus();
      camera.takePicture(null, null, jpeg);
    }
  }

  //设置屏幕方向
  public int setCameraDisplayOrientation(Activity activity)
  {
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }

    int result;
    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (cameraInfo.orientation + degrees) % 360;
      result = (360 - result) % 360;  // compensate the mirror
    } else {  // back-facing
      result = (cameraInfo.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);

    return result;
  }

  public static int setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera)
  {
    Camera.CameraInfo info = new Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, info);
    int rotation = activity.getWindowManager().getDefaultDisplay()
        .getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360;  // compensate the mirror
    } else {  // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);

    return result;
  }

}
