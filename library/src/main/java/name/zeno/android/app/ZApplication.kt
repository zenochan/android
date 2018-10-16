@file:Suppress("unused")

package name.zeno.android.app

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.CallSuper
import androidx.multidex.MultiDexApplication
import name.zeno.android.util.ZCookie
import name.zeno.android.util.ZLog

/**
 * - MultiDex
 * - [initBDMap]
 * - [initGetui]
 */
@SuppressLint("MissingSuperCall")
open class ZApplication : MultiDexApplication() {

  open val isDebug: Boolean = false

  @CallSuper
  override fun onCreate() {
    super.onCreate()
    application = this
    ZCookie.init(this)
    ZLog.DEBUG = isDebug
    AppInfo.init(this)
  }

  override fun onTerminate() {
    super.onTerminate()
    application = null
  }

  companion object {
    @JvmStatic
    var application: Application? = null
      private set
  }
}
