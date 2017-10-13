package name.zeno.android.util

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import name.zeno.android.app.AppInfo
import name.zeno.android.util.PictureUtils.getDataColumn
import name.zeno.android.util.PictureUtils.getPath
import name.zeno.android.util.PictureUtils.getUriType
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * - 获取图片 path  [getPath] , [getUriType] ,[getDataColumn]
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/3/1
 */
object PictureUtils {

  object UriType {
    val TYPES = arrayOf("com.android.externalstorage.documents", "com.android.providers.downloads.documents", "com.android.providers.media.documents", "com.google.android.apps.photos.content")

    val EXTERNAL_STORAGE_DOCUMENT = 0
    val DOWNLOADS_DOCUMENT = 1
    val MEDIA_DOCUMENT = 2
    val GOOGLE_PHOTOS_RUI = 3
    val UNKNOWN = 4
  }

  /**
   * @param uri 图片uri
   */
  fun getPath(context: Context, uri: Uri): String? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
      // DocumentProvider

      val docId: String
      val split: Array<String>
      val type: String
      var contentUri: Uri?

      when (getUriType(uri)) {
        UriType.EXTERNAL_STORAGE_DOCUMENT -> {
          // ExternalStorageProvider
          docId = DocumentsContract.getDocumentId(uri)
          split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
          type = split[0]

          if ("primary".equals(type, ignoreCase = true)) {
            return (Environment.getExternalStorageDirectory().toString() + "/"
                + split[1])
          }
        }
        UriType.DOWNLOADS_DOCUMENT -> {
          // DownloadsProvider
          val id = DocumentsContract.getDocumentId(uri)
          contentUri = ContentUris.withAppendedId(
              Uri.parse("content://downloads/public_downloads"),
              java.lang.Long.valueOf(id)!!)
          return getDataColumn(context, contentUri, null, null)
        }
        UriType.MEDIA_DOCUMENT -> {
          // MediaProvider
          docId = DocumentsContract.getDocumentId(uri)
          split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
          type = split[0]
          contentUri = null
          if ("image" == type) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
          } else if ("video" == type) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
          } else if ("audio" == type) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
          }
          val selection = "_id=?"
          val selectionArgs = arrayOf(split[1])
          return getDataColumn(context, contentUri, selection,
              selectionArgs)
        }
      }// TODO handle non-primary volumes
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
      // MediaStore (and general)

      return if (UriType.GOOGLE_PHOTOS_RUI == getUriType(uri)) {
        // Return the remote address
        uri.lastPathSegment
      } else {
        getDataColumn(context, uri, null, null)
      }
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
      // File
      return uri.path
    }

    return null
  }

  fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
    var cursor: Cursor? = null
    val column = "_data"
    val projection = arrayOf(column)
    try {
      cursor = context.contentResolver.query(uri!!, projection,
          selection, selectionArgs, null)
      if (cursor != null && cursor.moveToFirst()) {
        val index = cursor.getColumnIndexOrThrow(column)
        return cursor.getString(index)
      }
    } finally {
      if (cursor != null)
        cursor.close()
    }
    return null
  }

  fun getUriType(uri: Uri): Int {
    for (i in UriType.TYPES.indices) {
      val type = UriType.TYPES[i]
      if (type == uri.authority) {
        return i
      }
    }

    return UriType.UNKNOWN
  }

  /**
   * # 保存文件到指定路径
   * - [Android保存图片到系统相册](http://www.jianshu.com/p/8cede074ba5b)
   */
  fun saveImageToGallery(context: Context, bmp: Bitmap): Boolean {
    // 首先保存图片
    val storePath = Environment.getExternalStorageDirectory().absolutePath + File.separator + AppInfo.appName;
    val appDir = File(storePath);
    !appDir.exists() && appDir.mkdir()

    val fileName = "${System.currentTimeMillis()}.jpg";
    val file = File(appDir, fileName)
    try {
      val fos = FileOutputStream(file);
      //通过io流的方式来压缩保存图片
      val isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
      fos.flush();
      fos.close();

      //把文件插入到系统图库
      //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

      //保存图片后发送广播通知更新数据库
      val uri = Uri.fromFile(file);
      context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

      return isSuccess
    } catch (e: IOException) {
    }
    return false;
  }
}
