package name.zeno.android.camera

import android.app.Activity
import android.graphics.ImageFormat
import android.graphics.Point
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder

import java.io.IOException

import name.zeno.android.util.ZDimen
import name.zeno.android.util.ZLog

/**
 * Camera 管理类
 *
 *
 * Create Date: 16/6/19
 *
 *
 * 遇到的问题
 *
 *  * 设置预览大小和图像大小失败：[Camera setParameters failed](http://blog.csdn.net/fulinwsuafcie/article/details/39348869)
 *
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class CameraManager {
  private var camera: Camera? = null
  internal lateinit var cameraInfo: Camera.CameraInfo
  private var parameters: Camera.Parameters? = null
  private var autoFocusManager: AutoFocusManager? = null
  private val requestedCameraId = -1

  private var initialized: Boolean = false
  private var previewing: Boolean = false

  /** camera 是否打开  */
  val isOpen: Boolean
    @Synchronized get() = camera != null

  /** 打开摄像头  */
  fun open(cameraId: Int): Camera? {
    var cameraId = cameraId
    val numCameras = Camera.getNumberOfCameras()
    if (numCameras == 0) {
      ZLog.e(TAG, "该设备没有摄像头")
      return null
    }

    val explicitRequest = cameraId >= 0
    if (!explicitRequest) {
      //未指定摄像头时， 选择摄像头
      var index = 0
      while (index < numCameras) {
        cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(index, cameraInfo)
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
          break
        }
        index++
      }
      cameraId = index
    }

    val camera: Camera?
    if (cameraId < numCameras) {
      camera = Camera.open(cameraId)
    } else {
      if (explicitRequest) {
        ZLog.e(TAG, "指定的摄像头不存在:" + cameraId)
        camera = null
      } else {
        camera = Camera.open(0)
      }
    }

    return camera
  }

  /** 打开 camera  */
  @Synchronized
  @Throws(IOException::class)
  fun openDriver(holder: SurfaceHolder) {
    var theCamera = camera
    if (theCamera == null) {
      theCamera = open(requestedCameraId)
      if (theCamera == null) {
        throw IOException()
      }
    }
    theCamera.setPreviewDisplay(holder)
    camera = theCamera

    if (!initialized) {
      initialized = true
      parameters = camera!!.parameters
      parameters!!.pictureFormat = ImageFormat.JPEG
      parameters!!.jpegQuality = 100
      val windowSize = ZDimen.windowPixelsSize

      var size: Camera.Size
      size = getSize(parameters!!.supportedPreviewSizes)
      parameters!!.setPreviewSize(size.width, size.height)
      size = getSize(parameters!!.supportedPictureSizes)
      parameters!!.setPictureSize(size.width, size.height)

      theCamera.parameters = parameters
    }
  }

  private fun getSize(sizeList: List<Camera.Size>): Camera.Size {
    var r: Camera.Size? = null
    var deltaW = 10000
    // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
    for (size in sizeList) {
      val d = Math.abs(size.width - 800)
      if (size.height.toDouble() / size.width.toDouble() == 0.75 && deltaW > d) {
        r = size
        deltaW = d
      }
    }

    if (r == null) {
      r = sizeList[0]
    }

    ZLog.e(TAG, r.width.toString() + "," + r.height)
    return r
  }

  /** 关闭 camera  */
  @Synchronized
  fun closeDriver() {
    if (camera != null) {
      camera!!.release()
      camera = null
    }
  }

  /** 开始预览  */
  @Synchronized
  fun startPreview() {
    val theCamera = camera
    if (theCamera != null && !previewing) {
      theCamera.startPreview()
      previewing = true
      autoFocusManager = AutoFocusManager(camera!!)
    }
  }

  /** 关闭预览  */
  @Synchronized
  fun stopPreview() {

    if (autoFocusManager != null) {
      autoFocusManager!!.stop()
      autoFocusManager = null
    }

    if (camera != null && previewing) {
      camera!!.stopPreview()
      previewing = false
    }
  }

  /**
   * 拍照
   */
  @Synchronized
  fun takePicture(jpeg: Camera.PictureCallback) {
    if (camera != null) {
      camera!!.cancelAutoFocus()
      camera!!.takePicture(null, null, jpeg)
    }
  }

  //设置屏幕方向
  fun setCameraDisplayOrientation(activity: Activity): Int {
    val rotation = activity.windowManager.defaultDisplay.rotation
    var degrees = 0
    when (rotation) {
      Surface.ROTATION_0 -> degrees = 0
      Surface.ROTATION_90 -> degrees = 90
      Surface.ROTATION_180 -> degrees = 180
      Surface.ROTATION_270 -> degrees = 270
    }

    var result: Int
    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (cameraInfo.orientation + degrees) % 360
      result = (360 - result) % 360  // compensate the mirror
    } else {  // back-facing
      result = (cameraInfo.orientation - degrees + 360) % 360
    }
    camera!!.setDisplayOrientation(result)

    return result
  }

  companion object {
    private val TAG = "CameraManager"

    fun setCameraDisplayOrientation(activity: Activity, cameraId: Int, camera: Camera): Int {
      val info = Camera.CameraInfo()
      Camera.getCameraInfo(cameraId, info)
      val rotation = activity.windowManager.defaultDisplay
          .rotation
      var degrees = 0
      when (rotation) {
        Surface.ROTATION_0 -> degrees = 0
        Surface.ROTATION_90 -> degrees = 90
        Surface.ROTATION_180 -> degrees = 180
        Surface.ROTATION_270 -> degrees = 270
      }

      var result: Int
      if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        result = (info.orientation + degrees) % 360
        result = (360 - result) % 360  // compensate the mirror
      } else {  // back-facing
        result = (info.orientation - degrees + 360) % 360
      }
      camera.setDisplayOrientation(result)

      return result
    }
  }

}
