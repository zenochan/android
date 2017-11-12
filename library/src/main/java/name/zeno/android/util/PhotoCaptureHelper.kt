package name.zeno.android.util

import android.app.Activity
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.annotation.RequiresPermission
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import com.orhanobut.logger.Logger
import name.zeno.android.presenter.activities.IDCardCameraActivity
import name.zeno.android.system.ZPermission
import java.io.File
import java.lang.ref.WeakReference

/**
 * # 获取图片帮助类
 *
 * ### 使用
 * - 将方法[onActivityResult],[onDestroy] 与 activity/fragment 周期同步
 *
 *
 * - 使用 [getImageFromAlbum] 从相册获取图片;<br></br>
 * - 使用 [getImageFromCamera] 从相机获取图片;
 *
 *
 * - 使用 [listener] 设置回调监听器监听完成后的图片存储路径
 *
 *
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/3
 */
class PhotoCaptureHelper(
    var fragment: Fragment? = null,
    var activity: Activity? = null
) {


  private var context: WeakReference<Context> = WeakReference(activity ?: fragment!!.context!!)
  private var cachePath: String? = null
  private var fileName: String? = null

  val nav: (Intent, code: Int) -> Unit
  var listener: ((path: String?) -> Unit)? = null

  init {
    cachePath = getCachePath(context.get()!!)
    nav = { intent, code ->
      fragment?.startActivityForResult(intent, code)
      activity?.startActivityForResult(intent, code)
    }
  }

  /** 从相册选择图片  */
  @RequiresPermission("android.permission.READ_EXTERNAL_STORAGE")
  fun getImageFromAlbum() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "image/*"
    nav(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_ALBUM)
  }

  /** 从相机获取图片  */
  @RequiresPermission(allOf = arrayOf("android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"))
  fun getImageFromCamera(fileProvider: String) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    fileName = newFileName()

    val photoFile = File(cachePath, fileName!!)
    val imageUri: Uri = when {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
        //通过FileProvider创建一个content类型的Uri
        FileProvider.getUriForFile(context.get()!!, fileProvider, photoFile)
      }
      else -> Uri.fromFile(photoFile)
    }

    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
    nav(intent, REQUEST_CODE_CAMERA)
  }

  @RequiresPermission(allOf = arrayOf(ZPermission.WRITE_EXTERNAL_STORAGE, ZPermission.CAMERA))
  fun getIdCardFromCamera() {
    fileName = newFileName()
    val intent = IDCardCameraActivity.getCallingIntent(context.get()!!, cachePath!! + fileName!!)
    nav(intent, REQUEST_CODE_CAMERA)
  }

  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (resultCode != Activity.RESULT_OK) {
      Logger.e("resultCode: " + resultCode)
      return
    }

    when (requestCode) {
      REQUEST_CODE_ALBUM -> {
        if (data == null) {
          onImageSelected(null)
          return
        }
        onImageFromAlbum(data)
      }
      REQUEST_CODE_CAMERA -> onImageSelected(cachePath!! + fileName!!)
    }
  }

  fun onDestroy() {
    activity = null
    fragment = null
    context.clear()
  }


  private fun onImageSelected(filePath: String?) {
    listener?.invoke(filePath)
  }

  private fun onImageFromAlbum(data: Intent) {
    val filePath: String?
    val uri: Uri?
    //Android 4.4 +
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      uri = data.data
      filePath = PictureUtils.getPath(context.get()!!, uri!!)
    } else {
      uri = data.data
      val projection = arrayOf(MediaStore.Images.Media.DATA)
      val cursor = CursorLoader(context.get(), uri, projection, null, null, null).loadInBackground()
      val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
      cursor.moveToFirst()
      filePath = cursor.getString(columnIndex)// 图片在的路径
    }
    onImageSelected(filePath)
  }

  companion object {
    /** 从相册选择  */
    val REQUEST_CODE_ALBUM = 0x2001
    /** 拍照  */
    val REQUEST_CODE_CAMERA = 0x2002

    private val TAG = "ImageCaptureHelper"

    @JvmStatic
    fun newFileName(): String {
      return String.format("%s-%d.jpg", TAG, System.currentTimeMillis())
    }

    @JvmStatic
    fun getCachePath(context: Context): String =
        (context.externalCacheDir ?: context.cacheDir).absolutePath + "/"
  }
}
