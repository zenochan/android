package name.zeno.android.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a Assets Database Manager
 * Use it, you can use a assets database file in you application
 * It will copy the database file to "/setData/setData/[your application package name]/database" when you first time you use it
 * Then you can get a SQLiteDatabase object by the assets database file
 * <p>
 * <p>
 * How to use:
 * 1. Initialize AssetsDatabaseManager
 * 2. Get AssetsDatabaseManager
 * 3. Get a SQLiteDatabase object through database file
 * 4. Use this database object
 * <p>
 * Using example:
 * AssetsDatabaseManager.init(getApplication()); // this method is only need call one time
 * AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  // get a AssetsDatabaseManager object
 * SQLiteDatabase db1 = mg.getDatabase("db1.db");  // get SQLiteDatabase object, db1.db is a file in assets folder
 * db1.??? // every operate by you want
 * Of cause, you can use AssetsDatabaseManager.getManager().getDatabase("xx") to get a database when you need use a database
 *
 * @author RobinTang
 * @since 2012-09-20
 */
public class AssetsDatabaseManager
{
  private static String tag          = "AssetsDatabase"; // for LogCat
  private static String databasepath = "/setData/setData/%s/database"; // %s is packageName

  // A mapping from assets database file to SQLiteDatabase object
  private Map<String, SQLiteDatabase> databases = new HashMap<>();

  // Context of application
  private Context context = null;

  // Singleton Pattern
  private static AssetsDatabaseManager mInstance = null;

  /**
   *    * Initialize AssetsDatabaseManager
   *    * @param context, context of application
   *    
   */


  public static void init(Context context)
  {
    if (mInstance == null) {
      mInstance = new AssetsDatabaseManager(context);
    }
  }

  /**
   * Get a AssetsDatabaseManager object
   *
   * @return if success return a AssetsDatabaseManager object, else return null
   */

  public static AssetsDatabaseManager getManager()
  {
    return mInstance;
  }


  private AssetsDatabaseManager(Context context)
  {
    this.context = context;
  }

  /**
   * Get a assets database, if this database is opened this method is only return a copy of the opened database
   *
   * @param dbFile, the assets file which will be opened for a database
   * @return if success it return a SQLiteDatabase object else return null
   */

  @SuppressLint("CommitPrefEdits")
  public SQLiteDatabase getDatabase(String dbFile)
  {
    if (databases.get(dbFile) != null) {
      Log.i(tag, String.format("Return a database copy of %s", dbFile));
      return databases.get(dbFile);
    }
    if (context == null) {
      return null;
    }
    Log.i(tag, String.format("Create database %s", dbFile));
    String            spath = getDatabaseFilepath();
    String            sfile = getDatabaseFile(dbFile);
    File              file  = new File(sfile);
    SharedPreferences dbs   = context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);

    boolean flag = dbs.getBoolean(dbFile, false); // Get Database file flag, if true means this database file was copied and valid
    if (!flag || !file.exists()) {
      file = new File(spath);
      if (!file.exists() && !file.mkdirs()) {
        Log.i(tag, "Create \"" + spath + "\" fail!");
        return null;
      }
      if (!copyAssetsToFilesystem(dbFile, sfile)) {
        Log.i(tag, String.format("Copy %s to %s fail!", dbFile, sfile));
        return null;
      }
      dbs.edit().putBoolean(dbFile, true).commit();
    }

    SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    if (db != null) {
      databases.put(dbFile, db);
    }
    return db;
  }


  private String getDatabaseFilepath()
  {
    return String.format(databasepath, context.getApplicationInfo().packageName);
  }


  private String getDatabaseFile(String dbfile)
  {
    return getDatabaseFilepath() + "/" + dbfile;
  }


  /** 复制文件 */
  private boolean copyAssetsToFilesystem(String assetsSrc, String des)
  {
    Log.i(tag, "Copy " + assetsSrc + " to " + des);
    InputStream  istream = null;
    OutputStream ostream = null;
    try {
      AssetManager am = context.getAssets();
      istream = am.open(assetsSrc);
      ostream = new FileOutputStream(des);
      byte[] buffer = new byte[1024];
      int    length;
      while ((length = istream.read(buffer)) > 0) {
        ostream.write(buffer, 0, length);
      }
      istream.close();
      ostream.close();
    } catch (Exception e) {
      e.printStackTrace();
      try {
        if (istream != null)
          istream.close();
        if (ostream != null)
          ostream.close();
      } catch (Exception ee) {
        ee.printStackTrace();
      }
      return false;
    }
    return true;
  }

  /**
   * Close assets database
   *
   * @param dbFile, the assets file which will be closed soon
   * @return the status of this operating
   */

  public boolean closeDatabase(String dbFile)
  {
    if (databases.get(dbFile) != null) {
      SQLiteDatabase db = databases.get(dbFile);
      db.close();
      databases.remove(dbFile);
      return true;
    }
    return false;
  }

  /** Close all assets database   */
  static public void closeAllDatabase()
  {
    Log.i(tag, "closeAllDatabase");
    if (mInstance != null) {
      for (int i = 0; i < mInstance.databases.size(); ++i) {
        if (mInstance.databases.get(i) != null) {
          mInstance.databases.get(i).close();
        }
      }
      mInstance.databases.clear();
    }
  }
}
