package name.zeno.android.presenter.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_clip_image.*
import name.zeno.android.presenter.ZActivity
import name.zeno.android.system.ZStatusBar
import name.zeno.android.system.navbarHeight
import name.zeno.android.third.rxjava.RxUtils
import name.zeno.android.third.rxjava.ZObserver
import name.zeno.android.util.PhotoCaptureHelper
import name.zeno.android.util.R
import name.zeno.android.util.ZBitmap
import java.io.File

/**
 * @param data [String] path
 * @return data [String] path
 */
class ClipActivity : ZActivity() {
  private val loadingDialog: ProgressDialog by lazy {
    val dialog = ProgressDialog(this)
    dialog.setTitle("请稍后...")
    dialog
  }

  private val cachePath: String by lazy { PhotoCaptureHelper.getCachePath(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_clip_image)
    btn_submit.setOnClickListener { onSubmit() }
    btn_cancel.setOnClickListener { finish() }

    ZStatusBar.setImage(this)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      (btn_cancel.layoutParams as? ConstraintLayout.LayoutParams)?.bottomMargin = navbarHeight
      (btn_submit.layoutParams as? ConstraintLayout.LayoutParams)?.bottomMargin = navbarHeight
    }

    loadBmp()
  }

  private fun loadBmp() {
    val path = intent.getStringExtra("data")

    if (TextUtils.isEmpty(path) || !File(path).exists()) {
      Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show()
      finish()
      return
    }
    val bitmap = ZBitmap.bitmap(path, 600, 600)
    if (bitmap == null) {
      Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show()
      finish()
      return
    }
    layout_clip_img.setBitmap(bitmap)
  }

  @SuppressLint("MissingPermission")
  private fun onSubmit() {
    loadingDialog.show()
    Observable.create<String> { emitter ->
      var bitmap = layout_clip_img.clip()
      bitmap = ZBitmap.zoom(bitmap, 500.0, 500.0)
      val fileName = (+System.currentTimeMillis()).toString() + ".jpg"
      ZBitmap.savePhotoToSDCard(bitmap, cachePath, fileName)
      val filePath = cachePath + fileName
      emitter.onNext(filePath)
      emitter.onComplete()

    }.compose(RxUtils.applySchedulers()).subscribe(object : ZObserver<String>() {
      override fun onComplete() = loadingDialog.dismiss()
      override fun onNext(value: String) {
        val result = Intent()
        result.putExtra("data", value)
        setResult(Activity.RESULT_OK, result)
        finish()
      }
    })
  }
}
