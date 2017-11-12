package name.zeno.android.util

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.support.annotation.DrawableRes
import android.support.annotation.FloatRange
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

object ZBitmap {
  private val TAG = "ZBitmap"

  fun bitmap(context: Context, @DrawableRes resId: Int): Bitmap {
    return BitmapFactory.decodeResource(context.resources, resId)
  }

  fun bitmap(view: ImageView): Bitmap {
    val d = view.drawable
    val bd = d as BitmapDrawable
    return bd.bitmap
  }

  @JvmOverloads
  fun bitmap(drawable: Drawable, whiteBg: Boolean = false): Bitmap {
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight,
        if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    //绘制白底
    if (whiteBg) {
      canvas.drawRGB(255, 255, 255)
    }
    drawable.draw(canvas)

    return bitmap
  }


  // 根据图片url获取图片对象
  fun bitmap(urlPath: String?): Bitmap? {
    val cache = File(Environment.getExternalStorageDirectory(), "cache")
    if (urlPath != null && (cache.exists() || cache.mkdirs())) {
      val name = Encode.md5(urlPath)
      val file = File(cache, name!!)
      if (file.exists()) {
        // 如果图片存在本地缓存目录，则不去服务器下载
        return BitmapFactory.decodeFile(file.absolutePath)
      } else {
        // 从网络上获取图片
        try {
          val url = URL(urlPath)
          val conn = url.openConnection() as HttpURLConnection
          conn.connectTimeout = 5000
          conn.requestMethod = "GET"
          conn.doInput = true
          if (conn.responseCode == 200) {

            val inputStream = conn.inputStream
            val fos = FileOutputStream(file)
            val buffer = ByteArray(1024)

            var len: Int
            do {
              len = inputStream.read(buffer)
              if (len != -1) fos.write(buffer, 0, len)
            } while (len != -1)

            inputStream.close()
            fos.close()
            // 返回一个URI对象
            return BitmapFactory.decodeFile(file.absolutePath)
          }
        } catch (e: Exception) {
          e.printStackTrace()
          Log.e(TAG, "download image failed", e)
        }

      }
      return null
    } else {
      return null
    }
  }

  /**
   * @param path 存储空间位置
   */
  fun bitmap(path: String, w: Int, h: Int): Bitmap? {
    var result: Bitmap? = null
    try {
      val opts = BitmapFactory.Options()
      opts.inJustDecodeBounds = true
      opts.inPreferredConfig = Bitmap.Config.ARGB_8888
      BitmapFactory.decodeFile(path, opts)
      val width = opts.outWidth
      val height = opts.outHeight
      var scaleWidth = 0f
      var scaleHeight = 0f
      if (width > w || height > h) {
        scaleWidth = width.toFloat() / w
        scaleHeight = height.toFloat() / h
      }
      opts.inJustDecodeBounds = false
      val scale = Math.max(scaleWidth, scaleHeight)
      opts.inSampleSize = scale.toInt()
      val weak = WeakReference(BitmapFactory.decodeFile(path, opts))
      val b = weak.get()!!
      val bMapRotate = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), null, true)

      if (bMapRotate != null) {
        result = bMapRotate
      }
    } catch (e: Exception) {
      Log.e("", "", e)
      e.printStackTrace()
    }

    return result
  }

  /**
   * 将方形bitmap转换为圆形bitmap
   *
   * @param bitmap 原图
   * @return 圆形bitmap
   */
  fun circleBitmap(bitmap: Bitmap): Bitmap {
    val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val color = -0xbdbdbe
    val rect = Rect(0, 0, bitmap.width, bitmap.height)
    val paint = Paint()
    paint.isAntiAlias = true
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = color
    val x = bitmap.width
    canvas.drawCircle((x / 2).toFloat(), (x / 2).toFloat(), (x / 2).toFloat(), paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)
    return output
  }

  //  转为灰阶图片
  fun grayBitmap(bmp: Bitmap): Bitmap {
    val width = bmp.width
    val height = bmp.height
    val pixels = IntArray(width * height)
    bmp.getPixels(pixels, 0, width, 0, 0, width, height)

    val alpha = -0x1000000
    var r: Int
    var g: Int
    var b: Int
    var grey: Int
    for (i in pixels.indices) {
      grey = pixels[i]
      r = grey and 0xFF0000 shr 16
      g = grey and 0x00FF00 shr 8
      b = grey and 0x0000FF
      grey = (r * 0.3 + g * 0.59 + b * 0.11).toInt()
      grey = grey or (alpha or (grey shl 16) or (grey shl 8))
      pixels[i] = grey
    }

    val greyBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    greyBmp.setPixels(pixels, 0, width, 0, 0, width, height)
    return greyBmp
  }

  /***
   * 图片的缩放方法
   *
   * @param bitmap ：源图片资源
   */
  fun zoom(bitmap: Bitmap, @FloatRange(from = 0.0) scale: Float): Bitmap? {
    if (scale < 0) {
      return bitmap
    }
    // 获取这个图片的宽和高
    val width = bitmap.width
    val height = bitmap.height
    // 创建操作图片用的matrix对象
    val matrix = Matrix()
    // 缩放图片动作
    matrix.postScale(scale, scale)
    return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
  }

  /***
   * 图片的缩放方法
   *
   * @param bgImage   ：源图片资源
   * @param newWidth  ：缩放后宽度
   * @param newHeight ：缩放后高度
   */
  fun zoom(bgImage: Bitmap, newWidth: Double, newHeight: Double): Bitmap {
    // 获取这个图片的宽和高
    val width = bgImage.width.toFloat()
    val height = bgImage.height.toFloat()
    // 创建操作图片用的matrix对象
    val matrix = Matrix()
    // 计算宽高缩放率
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // 缩放图片动作
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(bgImage, 0, 0, width.toInt(), height.toInt(), matrix, true)
  }

  /**
   * @param maxSize 图片允许最大空间   单位：KB
   */
  fun zoom(src: Bitmap, maxSize: Int): Bitmap {
    var bigImage = src
    var maxSize = maxSize
    if (maxSize < 0) {
      maxSize = 400
    }
    maxSize /= 3
    //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
    val baos = ByteArrayOutputStream()
    bigImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    //将字节换成KB
    val mid = (b.size / 1024).toDouble()
    //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
    if (mid > maxSize) {
      //获取bitmap大小 是允许最大大小的多少倍
      val i = mid / maxSize
      Log.w("A", "" + i)
      //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
      bigImage = zoom(bigImage, bigImage.width / Math.sqrt(i), bigImage.height / Math.sqrt(i))
    }

    return bigImage
  }

  fun blur(bmp: Bitmap, view: ImageView) {
    val scaleFactor = 3f
    val radius = 2f

    var overlay: Bitmap? = Bitmap.createBitmap(
        (bmp.width / scaleFactor).toInt(),
        (bmp.height / scaleFactor).toInt(),
        Bitmap.Config.ARGB_8888)
    val canvas = Canvas(overlay!!)
    canvas.translate(-view.left / scaleFactor, -view.top / scaleFactor)
    canvas.scale(1 / scaleFactor, 1 / scaleFactor)
    val paint = Paint()
    paint.flags = Paint.FILTER_BITMAP_FLAG
    canvas.drawBitmap(bmp, 0f, 0f, paint)

    overlay = doBlur(overlay, radius.toInt(), true)
    view.setImageBitmap(overlay)
  }

  fun doBlur(sentBitmap: Bitmap, radius: Int, canReuseInBitmap: Boolean): Bitmap? {
    val bitmap: Bitmap
    if (canReuseInBitmap && sentBitmap.isMutable) {
      bitmap = sentBitmap
    } else {
      bitmap = sentBitmap.copy(sentBitmap.config, true)
    }

    if (radius < 1) {
      return null
    }

    val w = bitmap.width
    val h = bitmap.height

    val pix = IntArray(w * h)
    bitmap.getPixels(pix, 0, w, 0, 0, w, h)

    val wm = w - 1
    val hm = h - 1
    val wh = w * h
    val div = radius + radius + 1

    val r = IntArray(wh)
    val g = IntArray(wh)
    val b = IntArray(wh)
    var rsum: Int
    var gsum: Int
    var bsum: Int
    var x: Int
    var y: Int
    var i: Int
    var p: Int
    var yp: Int
    var yi: Int
    var yw: Int
    val vmin = IntArray(Math.max(w, h))

    var divsum = div + 1 shr 1
    divsum *= divsum
    val dv = IntArray(256 * divsum)
    i = 0
    while (i < 256 * divsum) {
      dv[i] = i / divsum
      i++
    }

    yi = 0
    yw = yi

    val stack = Array(div) { IntArray(3) }
    var stackpointer: Int
    var stackstart: Int
    var sir: IntArray
    var rbs: Int
    val r1 = radius + 1
    var routsum: Int
    var goutsum: Int
    var boutsum: Int
    var rinsum: Int
    var ginsum: Int
    var binsum: Int

    y = 0
    while (y < h) {
      bsum = 0
      gsum = bsum
      rsum = gsum
      boutsum = rsum
      goutsum = boutsum
      routsum = goutsum
      binsum = routsum
      ginsum = binsum
      rinsum = ginsum
      i = -radius
      while (i <= radius) {
        p = pix[yi + Math.min(wm, Math.max(i, 0))]
        sir = stack[i + radius]
        sir[0] = p and 0xff0000 shr 16
        sir[1] = p and 0x00ff00 shr 8
        sir[2] = p and 0x0000ff
        rbs = r1 - Math.abs(i)
        rsum += sir[0] * rbs
        gsum += sir[1] * rbs
        bsum += sir[2] * rbs
        if (i > 0) {
          rinsum += sir[0]
          ginsum += sir[1]
          binsum += sir[2]
        } else {
          routsum += sir[0]
          goutsum += sir[1]
          boutsum += sir[2]
        }
        i++
      }
      stackpointer = radius

      x = 0
      while (x < w) {

        r[yi] = dv[rsum]
        g[yi] = dv[gsum]
        b[yi] = dv[bsum]

        rsum -= routsum
        gsum -= goutsum
        bsum -= boutsum

        stackstart = stackpointer - radius + div
        sir = stack[stackstart % div]

        routsum -= sir[0]
        goutsum -= sir[1]
        boutsum -= sir[2]

        if (y == 0) {
          vmin[x] = Math.min(x + radius + 1, wm)
        }
        p = pix[yw + vmin[x]]

        sir[0] = p and 0xff0000 shr 16
        sir[1] = p and 0x00ff00 shr 8
        sir[2] = p and 0x0000ff

        rinsum += sir[0]
        ginsum += sir[1]
        binsum += sir[2]

        rsum += rinsum
        gsum += ginsum
        bsum += binsum

        stackpointer = (stackpointer + 1) % div
        sir = stack[stackpointer % div]

        routsum += sir[0]
        goutsum += sir[1]
        boutsum += sir[2]

        rinsum -= sir[0]
        ginsum -= sir[1]
        binsum -= sir[2]

        yi++
        x++
      }
      yw += w
      y++
    }
    x = 0
    while (x < w) {
      bsum = 0
      gsum = bsum
      rsum = gsum
      boutsum = rsum
      goutsum = boutsum
      routsum = goutsum
      binsum = routsum
      ginsum = binsum
      rinsum = ginsum
      yp = -radius * w
      i = -radius
      while (i <= radius) {
        yi = Math.max(0, yp) + x

        sir = stack[i + radius]

        sir[0] = r[yi]
        sir[1] = g[yi]
        sir[2] = b[yi]

        rbs = r1 - Math.abs(i)

        rsum += r[yi] * rbs
        gsum += g[yi] * rbs
        bsum += b[yi] * rbs

        if (i > 0) {
          rinsum += sir[0]
          ginsum += sir[1]
          binsum += sir[2]
        } else {
          routsum += sir[0]
          goutsum += sir[1]
          boutsum += sir[2]
        }

        if (i < hm) {
          yp += w
        }
        i++
      }
      yi = x
      stackpointer = radius
      y = 0
      while (y < h) {
        // Preserve alpha channel: ( 0xff000000 & pix[yi] )
        pix[yi] = -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]

        rsum -= routsum
        gsum -= goutsum
        bsum -= boutsum

        stackstart = stackpointer - radius + div
        sir = stack[stackstart % div]

        routsum -= sir[0]
        goutsum -= sir[1]
        boutsum -= sir[2]

        if (x == 0) {
          vmin[y] = Math.min(y + r1, hm) * w
        }
        p = x + vmin[y]

        sir[0] = r[p]
        sir[1] = g[p]
        sir[2] = b[p]

        rinsum += sir[0]
        ginsum += sir[1]
        binsum += sir[2]

        rsum += rinsum
        gsum += ginsum
        bsum += binsum

        stackpointer = (stackpointer + 1) % div
        sir = stack[stackpointer]

        routsum += sir[0]
        goutsum += sir[1]
        boutsum += sir[2]

        rinsum -= sir[0]
        ginsum -= sir[1]
        binsum -= sir[2]

        yi += w
        y++
      }
      x++
    }

    bitmap.setPixels(pix, 0, w, 0, 0, w, h)

    return bitmap
  }

  /**
   * Save image to the SD card
   */
  fun savePhotoToSDCard(photoBitmap: Bitmap?, path: String, photoName: String) {
    if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
      Log.e("ImageUtil", "SDCardDisabled")
      return
    }

    val dir = File(path)
    if (dir.exists() || dir.mkdirs()) {
      try {
        val photoFile = File(path, photoName)
        val fileOutputStream = FileOutputStream(photoFile)
        if (photoBitmap != null && photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
          fileOutputStream.flush()
        }
        fileOutputStream.close()
      } catch (e: IOException) {
        Log.e("ImageUtil", "io exception")
      }

    }
  }

  // 获取 Activity 截图
  fun screenshot(activity: Activity): Bitmap {
    // 获取windows中最顶层的view
    val view = activity.window.decorView
    view.buildDrawingCache()

    // 获取状态栏高度
    val rect = Rect()
    view.getWindowVisibleDisplayFrame(rect)
    val statusBarHeights = rect.top

    // 获取屏幕宽和高
    val display = activity.windowManager.defaultDisplay
    //    int widths = display.getWidth();
    //    int heights = display.getHeight();
    val size = Point()
    display.getSize(size)

    // 允许当前窗口保存缓存信息
    view.isDrawingCacheEnabled = true

    // 去掉状态栏
    //    Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
    //        statusBarHeights, widths, heights - statusBarHeights);
    val bmp = Bitmap.createBitmap(view.drawingCache, 0,
        statusBarHeights, size.x, size.y - statusBarHeights)

    // 销毁缓存信息
    view.destroyDrawingCache()

    return bmp
  }

}
