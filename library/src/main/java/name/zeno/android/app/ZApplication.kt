@file:Suppress("unused")

package name.zeno.android.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.annotation.CallSuper
import androidx.multidex.MultiDexApplication
import com.igexin.sdk.PushManager
import name.zeno.android.third.getui.ZGetuiMessageService
import name.zeno.android.third.getui.ZGetuiService
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

  protected fun <T : ZGetuiService, I : ZGetuiMessageService> initGetui(tClazz: Class<T>, intentServiceClass: Class<I>) {
    PushManager.getInstance().initialize(this.applicationContext, tClazz)
    PushManager.getInstance().registerPushIntentService(this.applicationContext, intentServiceClass)
  }

  companion object {
    @JvmStatic
    var application: Application? = null
      private set
  }
}
