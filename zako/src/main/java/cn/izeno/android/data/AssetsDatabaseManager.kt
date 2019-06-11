package cn.izeno.android.data


import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * This is a Assets Database Manager
 * Use it, you can use a assets database file in you application
 * It will copy the database file to "/setData/setData/[your application package name]/database" when you first time you use it
 * Then you can get a SQLiteDatabase object by the assets database file
 *
 *
 *
 *
 * How to use:
 * 1. Initialize AssetsDatabaseManager
 * 2. Get AssetsDatabaseManager
 * 3. Get a SQLiteDatabase object through database file
 * 4. Use this database object
 *
 *
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
class AssetsDatabaseManager private constructor(
    // Context of application
    val context: Context) {

  // A mapping from assets database file to SQLiteDatabase object
  private val databases = HashMap<String, SQLiteDatabase>()


  private val databaseFilepath: String
    get() = String.format(databasepath, context.applicationInfo.packageName)


  /**
   * # Get a assets database
   * > if this database is opened this method is only return a copy of the opened database
   *
   * @param dbFile the assets file which will be opened for a database
   * @return  [SQLiteDatabase] or [null]
   */
  @SuppressLint("CommitPrefEdits")
  fun getDatabase(dbFile: String): SQLiteDatabase? {
    if (databases[dbFile] != null) {
      Log.i(tag, String.format("Return a database copy of %s", dbFile))
      return databases[dbFile]
    }

    Log.i(tag, String.format("Create database %s", dbFile))
    val spath = databaseFilepath
    val sfile = getDatabaseFile(dbFile)
    var file = File(sfile)
    val dbs = context.getSharedPreferences(AssetsDatabaseManager::class.java.toString(), 0)

    val flag = dbs.getBoolean(dbFile, false) // Get Database file flag, if true means this database file was copied and valid
    if (!flag || !file.exists()) {
      file = File(spath)
      if (!file.exists() && !file.mkdirs()) {
        Log.i(tag, "Create \"$spath\" fail!")
        return null
      }
      if (!copyAssetsToFilesystem(dbFile, sfile)) {
        Log.i(tag, String.format("Copy %s to %s fail!", dbFile, sfile))
        return null
      }
      dbs.edit().putBoolean(dbFile, true).commit()
    }

    val db = SQLiteDatabase.openDatabase(sfile, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS)
    if (db != null) {
      databases.put(dbFile, db)
    }
    return db
  }


  private fun getDatabaseFile(dbfile: String): String {
    return databaseFilepath + "/" + dbfile
  }


  /** 复制文件  */
  private fun copyAssetsToFilesystem(assetsSrc: String, des: String): Boolean {
    Log.i(tag, "Copy $assetsSrc to $des")
    var istream: InputStream? = null
    var ostream: OutputStream? = null
    try {
      istream = context.assets.open(assetsSrc)
      ostream = FileOutputStream(des)
      val buffer = ByteArray(1024)
      var length: Int
      do {
        length = istream!!.read(buffer)
        if (length < 0) break
        ostream.write(buffer, 0, length)

      } while (length > 0)

      istream.close()
      ostream.close()
    } catch (e: Exception) {
      e.printStackTrace()
      try {
        if (istream != null)
          istream.close()
        if (ostream != null)
          ostream.close()
      } catch (ee: Exception) {
        ee.printStackTrace()
      }

      return false
    }

    return true
  }

  /**
   * Close assets database
   *
   * @param dbFile, the assets file which will be closed soon
   * @return the status of this operating
   */

  fun closeDatabase(dbFile: String): Boolean {
    val db = databases[dbFile] ?: return false

    db.close()
    databases.remove(dbFile)
    return true
  }

  companion object {
    private val tag = "AssetsDatabase" // for LogCat
    private val databasepath = "/setData/setData/%s/database" // %s is packageName

    /**
     * Get a AssetsDatabaseManager object
     *
     * @return if success return a AssetsDatabaseManager object, else return null
     */
    var manager: AssetsDatabaseManager? = null
      private set

    /**
     * Initialize AssetsDatabaseManager
     * @param context, context of application
     */
    fun init(context: Context) {
      if (manager == null) {
        manager = AssetsDatabaseManager(context)
      }
    }

    /** Close all assets database    */
    fun closeAllDatabase() {
      Log.i(tag, "closeAllDatabase")
      manager?.databases?.values?.forEach { it.close() }
      manager?.databases?.clear()
    }
  }
}
