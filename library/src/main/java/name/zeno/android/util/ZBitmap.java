package name.zeno.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("unused")
public class ZBitmap
{
  private static final String TAG = "ZBitmap";

  public static Bitmap bitmap(Context context, @DrawableRes int resId)
  {
    return BitmapFactory.decodeResource(context.getResources(), resId);
  }

  public static Bitmap bitmap(ImageView view)
  {
    Drawable       d  = view.getDrawable();
    BitmapDrawable bd = (BitmapDrawable) d;
    return bd.getBitmap();
  }

  public static Bitmap bitmap(Drawable drawable)
  {
    return bitmap(drawable, false);
  }

  public static Bitmap bitmap(Drawable drawable, boolean whiteBg)
  {
    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

    //绘制白底
    if (whiteBg) {
      canvas.drawRGB(255, 255, 255);
    }
    drawable.draw(canvas);

    return bitmap;
  }


  // 根据图片url获取图片对象
  public static Bitmap bitmap(String urlPath)
  {
    File cache = new File(Environment.getExternalStorageDirectory(), "cache");
    if (urlPath != null && (cache.exists() || cache.mkdirs())) {
      String name = MD5.md5(urlPath);
      File   file = new File(cache, name);
      if (file.exists()) {
        // 如果图片存在本地缓存目录，则不去服务器下载
        return BitmapFactory.decodeFile(file.getAbsolutePath());
      } else {
        // 从网络上获取图片
        try {
          URL               url  = new URL(urlPath);
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();
          conn.setConnectTimeout(5000);
          conn.setRequestMethod("GET");
          conn.setDoInput(true);
          if (conn.getResponseCode() == 200) {

            InputStream      is     = conn.getInputStream();
            FileOutputStream fos    = new FileOutputStream(file);
            byte[]           buffer = new byte[1024];
            int              len;
            while ((len = is.read(buffer)) != -1) {
              fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            // 返回一个URI对象
            return BitmapFactory.decodeFile(file.getAbsolutePath());
          }
        } catch (Exception e) {
          e.printStackTrace();
          Log.e(TAG, "download image failed", e);
        }
      }
      return null;
    } else {
      return null;
    }
  }

  /**
   * @param path 存储空间位置
   */
  public static Bitmap bitmap(String path, int w, int h)
  {
    Bitmap result = null;
    try {
      BitmapFactory.Options opts = new BitmapFactory.Options();
      opts.inJustDecodeBounds = true;
      opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
      BitmapFactory.decodeFile(path, opts);
      int   width      = opts.outWidth;
      int   height     = opts.outHeight;
      float scaleWidth = 0.f, scaleHeight = 0.f;
      if (width > w || height > h) {
        scaleWidth = ((float) width) / w;
        scaleHeight = ((float) height) / h;
      }
      opts.inJustDecodeBounds = false;
      float scale = Math.max(scaleWidth, scaleHeight);
      opts.inSampleSize = (int) scale;
      WeakReference<Bitmap> weak       = new WeakReference<>(BitmapFactory.decodeFile(path, opts));
      Bitmap                b          = weak.get();
      Bitmap                bMapRotate = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), null, true);

      if (bMapRotate != null) {
        result = bMapRotate;
      }
    } catch (Exception e) {
      Log.e("", "", e);
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 将方形bitmap转换为圆形bitmap
   *
   * @param bitmap 原图
   * @return 圆形bitmap
   */
  public static Bitmap circleBitmap(Bitmap bitmap)
  {
    Bitmap     output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas     canvas = new Canvas(output);
    final int  color  = 0xff424242;
    final Rect rect   = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    Paint      paint  = new Paint();
    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    int x = bitmap.getWidth();
    canvas.drawCircle(x / 2, x / 2, x / 2, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);
    return output;
  }

  //  转为灰阶图片
  public static Bitmap grayBitmap(Bitmap bmp)
  {
    int   width  = bmp.getWidth();
    int   height = bmp.getHeight();
    int[] pixels = new int[width * height];
    bmp.getPixels(pixels, 0, width, 0, 0, width, height);

    final int alpha = -0x1000000;
    int       r, g, b, grey;
    for (int i = 0; i < pixels.length; i++) {
      grey = pixels[i];
      r = (grey & 0xFF0000) >> 16;
      g = (grey & 0x00FF00) >> 8;
      b = grey & 0x0000FF;
      grey = (int) (r * 0.3D + g * 0.59D + b * 0.11D);
      grey |= alpha | grey << 16 | grey << 8;
      pixels[i] = grey;
    }

    Bitmap greyBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    greyBmp.setPixels(pixels, 0, width, 0, 0, width, height);
    return greyBmp;
  }

  /***
   * 图片的缩放方法
   *
   * @param bitmap ：源图片资源
   */
  public static Bitmap zoom(Bitmap bitmap, @FloatRange(from = 0F, to = 1F) float scale)
  {
    if (bitmap == null || scale < 0 || scale > 1) {
      return bitmap;
    }
    // 获取这个图片的宽和高
    float width  = bitmap.getWidth();
    float height = bitmap.getHeight();
    // 创建操作图片用的matrix对象
    Matrix matrix = new Matrix();
    // 缩放图片动作
    matrix.postScale(scale, scale);
    return Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
  }

  /***
   * 图片的缩放方法
   *
   * @param bgImage   ：源图片资源
   * @param newWidth  ：缩放后宽度
   * @param newHeight ：缩放后高度
   */
  public static Bitmap zoom(Bitmap bgImage, double newWidth, double newHeight)
  {
    // 获取这个图片的宽和高
    float width  = bgImage.getWidth();
    float height = bgImage.getHeight();
    // 创建操作图片用的matrix对象
    Matrix matrix = new Matrix();
    // 计算宽高缩放率
    float scaleWidth  = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // 缩放图片动作
    matrix.postScale(scaleWidth, scaleHeight);
    return Bitmap.createBitmap(bgImage, 0, 0, (int) width, (int) height, matrix, true);
  }

  /**
   * @param maxSize 图片允许最大空间   单位：KB
   */
  public static Bitmap zoom(Bitmap bigImage, int maxSize)
  {
    if (maxSize < 0) {
      maxSize = 400;
    }
    maxSize /= 3;
    //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bigImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] b = baos.toByteArray();
    //将字节换成KB
    double mid = b.length / 1024;
    //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
    if (mid > maxSize) {
      //获取bitmap大小 是允许最大大小的多少倍
      double i = mid / maxSize;
      Log.w("A", "" + i);
      //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
      bigImage = zoom(bigImage, bigImage.getWidth() / Math.sqrt(i), bigImage.getHeight() / Math.sqrt(i));
    }

    return bigImage;
  }

  public static void blur(Bitmap bmp, ImageView view)
  {
    float scaleFactor = 3;
    float radius      = 2;

    Bitmap overlay = Bitmap.createBitmap(
        (int) (bmp.getWidth() / scaleFactor),
        (int) (bmp.getHeight() / scaleFactor),
        Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(overlay);
    canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
        / scaleFactor);
    canvas.scale(1 / scaleFactor, 1 / scaleFactor);
    Paint paint = new Paint();
    paint.setFlags(Paint.FILTER_BITMAP_FLAG);
    canvas.drawBitmap(bmp, 0, 0, paint);

    overlay = doBlur(overlay, (int) radius, true);
    view.setImageBitmap(overlay);
  }

  public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap)
  {
    Bitmap bitmap;
    if (canReuseInBitmap && sentBitmap.isMutable()) {
      bitmap = sentBitmap;
    } else {
      bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
    }

    if (radius < 1) {
      return (null);
    }

    int w = bitmap.getWidth();
    int h = bitmap.getHeight();

    int[] pix = new int[w * h];
    bitmap.getPixels(pix, 0, w, 0, 0, w, h);

    int wm  = w - 1;
    int hm  = h - 1;
    int wh  = w * h;
    int div = radius + radius + 1;

    int r[]    = new int[wh];
    int g[]    = new int[wh];
    int b[]    = new int[wh];
    int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
    int vmin[] = new int[Math.max(w, h)];

    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    int dv[] = new int[256 * divsum];
    for (i = 0; i < 256 * divsum; i++) {
      dv[i] = (i / divsum);
    }

    yw = yi = 0;

    int[][] stack = new int[div][3];
    int     stackpointer;
    int     stackstart;
    int[]   sir;
    int     rbs;
    int     r1    = radius + 1;
    int     routsum, goutsum, boutsum;
    int     rinsum, ginsum, binsum;

    for (y = 0; y < h; y++) {
      rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
      for (i = -radius; i <= radius; i++) {
        p = pix[yi + Math.min(wm, Math.max(i, 0))];
        sir = stack[i + radius];
        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);
        rbs = r1 - Math.abs(i);
        rsum += sir[0] * rbs;
        gsum += sir[1] * rbs;
        bsum += sir[2] * rbs;
        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
        }
      }
      stackpointer = radius;

      for (x = 0; x < w; x++) {

        r[yi] = dv[rsum];
        g[yi] = dv[gsum];
        b[yi] = dv[bsum];

        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;

        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];

        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];

        if (y == 0) {
          vmin[x] = Math.min(x + radius + 1, wm);
        }
        p = pix[yw + vmin[x]];

        sir[0] = (p & 0xff0000) >> 16;
        sir[1] = (p & 0x00ff00) >> 8;
        sir[2] = (p & 0x0000ff);

        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];

        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;

        stackpointer = (stackpointer + 1) % div;
        sir = stack[(stackpointer) % div];

        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];

        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];

        yi++;
      }
      yw += w;
    }
    for (x = 0; x < w; x++) {
      rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
      yp = -radius * w;
      for (i = -radius; i <= radius; i++) {
        yi = Math.max(0, yp) + x;

        sir = stack[i + radius];

        sir[0] = r[yi];
        sir[1] = g[yi];
        sir[2] = b[yi];

        rbs = r1 - Math.abs(i);

        rsum += r[yi] * rbs;
        gsum += g[yi] * rbs;
        bsum += b[yi] * rbs;

        if (i > 0) {
          rinsum += sir[0];
          ginsum += sir[1];
          binsum += sir[2];
        } else {
          routsum += sir[0];
          goutsum += sir[1];
          boutsum += sir[2];
        }

        if (i < hm) {
          yp += w;
        }
      }
      yi = x;
      stackpointer = radius;
      for (y = 0; y < h; y++) {
        // Preserve alpha channel: ( 0xff000000 & pix[yi] )
        pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

        rsum -= routsum;
        gsum -= goutsum;
        bsum -= boutsum;

        stackstart = stackpointer - radius + div;
        sir = stack[stackstart % div];

        routsum -= sir[0];
        goutsum -= sir[1];
        boutsum -= sir[2];

        if (x == 0) {
          vmin[y] = Math.min(y + r1, hm) * w;
        }
        p = x + vmin[y];

        sir[0] = r[p];
        sir[1] = g[p];
        sir[2] = b[p];

        rinsum += sir[0];
        ginsum += sir[1];
        binsum += sir[2];

        rsum += rinsum;
        gsum += ginsum;
        bsum += binsum;

        stackpointer = (stackpointer + 1) % div;
        sir = stack[stackpointer];

        routsum += sir[0];
        goutsum += sir[1];
        boutsum += sir[2];

        rinsum -= sir[0];
        ginsum -= sir[1];
        binsum -= sir[2];

        yi += w;
      }
    }

    bitmap.setPixels(pix, 0, w, 0, 0, w, h);

    return bitmap;
  }

  /**
   * Save image to the SD card
   */
  public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName)
  {
    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      Log.e("ImageUtil", "SDCardDisabled");
      return;
    }

    File dir = new File(path);
    if (dir.exists() || dir.mkdirs()) {
      try {
        File             photoFile        = new File(path, photoName);
        FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
        if (photoBitmap != null && photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
          fileOutputStream.flush();
        }
        fileOutputStream.close();
      } catch (IOException e) {
        Log.e("ImageUtil", "io exception");
      }
    }
  }

  // 获取 Activity 截图
  public static Bitmap screenshot(Activity activity)
  {
    // 获取windows中最顶层的view
    View view = activity.getWindow().getDecorView();
    view.buildDrawingCache();

    // 获取状态栏高度
    Rect rect = new Rect();
    view.getWindowVisibleDisplayFrame(rect);
    int statusBarHeights = rect.top;

    // 获取屏幕宽和高
    Display display = activity.getWindowManager().getDefaultDisplay();
//    int widths = display.getWidth();
//    int heights = display.getHeight();
    Point size = new Point();
    display.getSize(size);

    // 允许当前窗口保存缓存信息
    view.setDrawingCacheEnabled(true);

    // 去掉状态栏
//    Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
//        statusBarHeights, widths, heights - statusBarHeights);
    Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
        statusBarHeights, size.x, size.y - statusBarHeights);

    // 销毁缓存信息
    view.destroyDrawingCache();

    return bmp;
  }

}
