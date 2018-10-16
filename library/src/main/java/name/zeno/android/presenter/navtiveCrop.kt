package name.zeno.android.presenter

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import io.reactivex.Observable
import name.zeno.android.core.nav
import name.zeno.android.data.models.CropOptions
import name.zeno.android.exception.ZException
import name.zeno.android.util.PhotoCaptureHelper
import java.io.File

/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/7/6
 */

/**
 * 使用系统提供的图片裁剪
 */
fun <T : ZFragment> T.cropByNative(
    targetPath: String,
    options: (CropOptions.() -> Unit)? = null
): Observable<String> {
  val cropOptions = CropOptions()
  options?.invoke(cropOptions)

  return Observable.create<String> {

    val file = File(targetPath)
    if (!file.parentFile.exists()) {
      file.parentFile.mkdirs()
    }
    val imageUri: Uri
    val outputUri: Uri

    val path = PhotoCaptureHelper.getCachePath(ctx)
    val fileName = PhotoCaptureHelper.newFileName()

    val intent = Intent("com.android.camera.action.CROP")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      //添加这一句表示对目标应用临时授权该Uri所代表的文件
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      //通过FileProvider创建一个content类型的Uri
      imageUri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
      outputUri = Uri.fromFile(File(path, fileName))
    } else {
      imageUri = Uri.fromFile(file)
      outputUri = Uri.fromFile(File(path, fileName))
    }
    intent.setDataAndType(imageUri, "image/*")
    intent.putExtra("crop", "true")
    //设置宽高比例
    intent.putExtra("aspectX", cropOptions.aspectX)
    intent.putExtra("aspectY", cropOptions.aspectY)
    //设置裁剪图片宽高
    intent.putExtra("outputX", cropOptions.outputX)
    intent.putExtra("outputY", cropOptions.outputY)
    intent.putExtra("scale", cropOptions.scale)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
    intent.putExtra("noFaceDetection", true)

    if (intent.resolveActivity(ctx.packageManager) != null) {
      nav(intent) { ok, _ ->
        if (ok) {
          it.onNext("$path/$fileName")
          it.onComplete()
        } else {
          it.onError(ZException("取消裁剪").code(-1))
        }
      }
    } else {
      it.onError(ZException("未找到默认的图片处理应用").code(-2))
    }

  }

}


/**
 * 使用系统提供的图片裁剪
 */
fun <T : ZActivity> T.cropByNative(
    targetPath: String,
    options: (CropOptions.() -> Unit)? = null
): Observable<String> {
  return Observable.create<String> {
    val cropOptions = CropOptions()
    options?.invoke(cropOptions)


    val file = File(targetPath)
    if (!file.parentFile.exists()) {
      file.parentFile.mkdirs()
    }
    val imageUri: Uri
    val outputUri: Uri

    val path = PhotoCaptureHelper.getCachePath(ctx)
    val fileName = PhotoCaptureHelper.newFileName()

    val intent = Intent("com.android.camera.action.CROP")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      //添加这一句表示对目标应用临时授权该Uri所代表的文件
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      //通过FileProvider创建一个content类型的Uri
      imageUri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", file)
      outputUri = Uri.fromFile(File(path, fileName))
    } else {
      imageUri = Uri.fromFile(file)
      outputUri = Uri.fromFile(File(path, fileName))
    }
    intent.setDataAndType(imageUri, "image/*")
    intent.putExtra("crop", "true")
    //设置宽高比例
    intent.putExtra("aspectX", cropOptions.aspectX)
    intent.putExtra("aspectY", cropOptions.aspectY)
    //设置裁剪图片宽高
    intent.putExtra("outputX", cropOptions.outputX)
    intent.putExtra("outputY", cropOptions.outputY)
    intent.putExtra("scale", cropOptions.scale)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
    intent.putExtra("noFaceDetection", true)

    if (intent.resolveActivity(ctx.packageManager) != null) {
      nav(intent) { ok, _ ->
        if (ok) {
          it.onNext("$path/$fileName")
          it.onComplete()
        } else {
          it.onError(ZException("取消裁剪").code(-1))
        }
      }
    } else {
      it.onError(ZException("未找到默认的图片处理应用").code(-2))
    }

  }

}
