package name.zeno.android.kt

import android.graphics.*

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/12/19
 */

fun Bitmap.gray(config: Bitmap.Config = Bitmap.Config.RGB_565): Bitmap {
  val pixels = IntArray(width * height)
  getPixels(pixels, 0, width, 0, 0, width, height)

  val alpha = -0x1000000
  var grey: Int
  for (i in pixels.indices) {
    grey = pixels[i]
    grey = (grey.red * 0.3 + grey.green * 0.59 + grey.blue * 0.11).toInt()
    grey = grey or (alpha or (grey shl 16) or (grey shl 8))
    pixels[i] = grey
  }

  val greyBmp = Bitmap.createBitmap(width, height, config)
  greyBmp.setPixels(pixels, 0, width, 0, 0, width, height)
  return greyBmp
}

fun Bitmap.circle(): Bitmap {
  val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(output)
  val color = -0xbdbdbe
  val rect = Rect(0, 0, width, height)
  val paint = Paint()
  paint.isAntiAlias = true
  canvas.drawARGB(0, 0, 0, 0)
  paint.color = color
  val x = width
  canvas.drawCircle((x / 2).toFloat(), (x / 2).toFloat(), (x / 2).toFloat(), paint)
  paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
  canvas.drawBitmap(this, rect, rect, paint)
  return output

}

/***
 * # 图片缩放
 *
 * @param newWidth  ：缩放后宽度
 * @param newHeight ：缩放后高度
 */
fun Bitmap.zoom(newWidth: Int = 300, newHeight: Int = newWidth): Bitmap {
  // 计算宽高缩放率
  val scaleWidth = newWidth.toFloat() / width
  val scaleHeight = newHeight.toFloat() / height

  // 创建操作图片用的matrix对象
  val matrix = Matrix()
  // 缩放图片动作
  matrix.postScale(scaleWidth, scaleHeight)

  return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}