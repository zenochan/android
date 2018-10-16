package name.zeno.android.presenter.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_camera.*
import name.zeno.android.camera.CameraManager
import name.zeno.android.presenter.ZActivity
import name.zeno.android.third.rxjava.RxUtils
import name.zeno.android.util.R
import name.zeno.android.util.ZBitmap
import name.zeno.ktrxpermission.ZPermission
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * # [Android Camera身份证取景](https://github.com/lizhangqu/Camera)
 *
 * 在manifest 根节点添加
 *
 * ```
 * <uses-feature android:name="android.hardware.camera"></uses-feature>
 * <uses-feature android:name="android.hardware.camera.autofocus"></uses-feature>
 * ```
 * @author 陈治谋 (513500085@qq.com)
 * @since  16/6/19
 */
class IDCardCameraActivity : ZActivity(), SurfaceHolder.Callback {

  private var surfaceView: SurfaceView? = null

  private var cameraManager: CameraManager? = null
  @SuppressLint("MissingPermission")
  private val jpegCallBack: Camera.PictureCallback = Camera.PictureCallback { data, _ ->
    resolveImage(data)
    cameraManager?.stopPreview()
    cameraManager?.closeDriver()
  }

  private var hasSurface: Boolean = false
  private var rotateDegree = 0

  override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {

  }

  override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
    if (!hasSurface) {
      hasSurface = true
      initCamera(surfaceHolder)
    }
  }


  override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
    hasSurface = false
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_camera)
    surfaceView = findViewById(R.id.surface)
    iv_take.onClick {
      cameraManager!!.takePicture(jpegCallBack)
      it?.isEnabled = false
    }
    path = intent.getStringExtra(EXTRA_PARAM_PATH)
  }

  override fun onResume() {
    super.onResume()
    cameraManager = CameraManager()
    val holder = surfaceView!!.holder
    if (hasSurface) {
      //activity 在 paused 时不会 stopped，因此 surface 仍然存在
      //surfaceCreated 不会调用，因此在这里初始化 camera
      initCamera(holder)
    } else {
      //重置callback，等待surfaceCreated（）来初始化camera
      holder.addCallback(this)
    }

  }

  override fun onPause() {
    cameraManager!!.stopPreview()
    cameraManager!!.closeDriver()
    if (!hasSurface) {
      surfaceView!!.holder.removeCallback(this)
    }
    super.onPause()
  }

  /**
   * 初始化 camera
   */
  private fun initCamera(surfaceHolder: SurfaceHolder?) {
    if (surfaceHolder == null) {
      throw IllegalArgumentException("未提供 surfaceHoder")
    }

    if (cameraManager!!.isOpen) {
      return
    }

    try {
      cameraManager!!.openDriver(surfaceHolder)
      cameraManager!!.startPreview()
      rotateDegree = cameraManager!!.setCameraDisplayOrientation(this)
    } catch (ignore: Exception) {
      Log.e(TAG, ignore.message, ignore)
      AlertDialog.Builder(this)
          .setMessage("打开摄像机失败")
          .setPositiveButton("好") { dialogInterface, i ->
            dialogInterface.dismiss()
            finish()
          }
          .show()
    }

  }

  @SuppressLint("MissingPermission")
  @RequiresPermission(ZPermission.WRITE_EXTERNAL_STORAGE)
  private fun resolveImage(bytes: ByteArray) {
    Observable.create { subscriber: Emitter<Boolean> ->
      val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
      val width = bitmap.width
      val height = bitmap.height
      val matrix = Matrix()
      matrix.setRotate(rotateDegree.toFloat())
      //      if (width > 960) {
      //        float scale = 960F / width;
      //        matrix.postScale(scale, scale);
      //      }
      val unit = height / 3
      val x: Int
      val y: Int
      val w: Int
      val h: Int
      x = (width.toFloat() / 2 - 1.5f * unit).toInt()
      y = (unit * 0.5f).toInt()
      w = unit * 3
      h = unit * 2

      val tem = Bitmap.createBitmap(bitmap, x, y, w, h, matrix, true)
      val filePath = path!!.substring(0, path!!.lastIndexOf("/") + 1)
      val fileName = path!!.substring(path!!.lastIndexOf("/") + 1)
      ZBitmap.savePhotoToSDCard(tem, filePath, fileName)

      subscriber.onNext(true)
    }.compose(RxUtils.applySchedulers()).subscribe(object : Observer<Boolean> {
      override fun onSubscribe(d: Disposable) {}

      override fun onComplete() {}

      override fun onError(e: Throwable) {}

      override fun onNext(aBoolean: Boolean) {
        val intent = Intent()
        intent.putExtra(EXTRA_PARAM_PATH, path)
        setResult(RESULT_OK, intent)
        finish()
      }
    })
  }

  companion object {
    val EXTRA_PARAM_PATH = "param_path"
    private var path: String? = null

    fun getCallingIntent(context: Context, path: String): Intent {
      val intent = Intent(context, IDCardCameraActivity::class.java)
      intent.putExtra(EXTRA_PARAM_PATH, path)
      return intent
    }
  }
}
