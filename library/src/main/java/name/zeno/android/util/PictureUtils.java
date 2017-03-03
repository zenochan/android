package name.zeno.android.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Create Date: 16/3/1
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class PictureUtils
{
  interface UriType
  {
    String[] TYPES = {
        "com.android.externalstorage.documents",
        "com.android.providers.downloads.documents",
        "com.android.providers.media.documents",
        "com.google.android.apps.photos.content"
    };

    int EXTERNAL_STORAGE_DOCUMENT = 0;
    int DOWNLOADS_DOCUMENT        = 1;
    int MEDIA_DOCUMENT            = 2;
    int GOOGLE_PHOTOS_RUI         = 3;
    int UNKNOWN                   = 4;
  }

  /**
   * @param uri 图片uri
   */
  public static String getPath(final Context context, final Uri uri)
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        && DocumentsContract.isDocumentUri(context, uri)) {
      // DocumentProvider

      String docId;
      String[] split;
      String type;
      switch (getUriType(uri)) {
        case UriType.EXTERNAL_STORAGE_DOCUMENT:
          // ExternalStorageProvider
          docId = DocumentsContract.getDocumentId(uri);
          split = docId.split(":");
          type = split[0];
          Uri contentUri;

          if ("primary".equalsIgnoreCase(type)) {
            return Environment.getExternalStorageDirectory() + "/"
                + split[1];
          }
          // TODO handle non-primary volumes
          break;
        case UriType.DOWNLOADS_DOCUMENT:
          // DownloadsProvider
          final String id = DocumentsContract.getDocumentId(uri);
          contentUri = ContentUris.withAppendedId(
              Uri.parse("content://downloads/public_downloads"),
              Long.valueOf(id));
          return getDataColumn(context, contentUri, null, null);
        case UriType.MEDIA_DOCUMENT:
          // MediaProvider
          docId = DocumentsContract.getDocumentId(uri);
          split = docId.split(":");
          type = split[0];
          contentUri = null;
          if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
          } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
          } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
          }
          final String selection = "_id=?";
          final String[] selectionArgs = new String[]{split[1]};
          return getDataColumn(context, contentUri, selection,
              selectionArgs);
      }
    } else if ("content".equalsIgnoreCase(uri.getScheme())) {
      // MediaStore (and general)

      if (UriType.GOOGLE_PHOTOS_RUI == getUriType(uri)) {
        // Return the remote address
        return uri.getLastPathSegment();
      } else {
        return getDataColumn(context, uri, null, null);
      }
    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
      // File
      return uri.getPath();
    }

    return null;
  }

  public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
  {
    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {column};
    try {
      cursor = context.getContentResolver().query(uri, projection,
          selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        final int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } finally {
      if (cursor != null)
        cursor.close();
    }
    return null;
  }

  public static int getUriType(Uri uri)
  {
    for (int i = 0; i < UriType.TYPES.length; i++) {
      String type = UriType.TYPES[i];
      if (type.equals(uri.getAuthority())) {
        return i;
      }
    }

    return UriType.UNKNOWN;
  }
}
