@file:Suppress("unused")

package name.zeno.android.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.annotation.CallSuper
import androidx.multidex.MultiDexApplication
import com.baidu.mapapi.SDKInitializer
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

    registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
      override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}
      override fun onActivityStarted(activity: Activity) {}
      override fun onActivityResumed(activity: Activity) {}
      override fun onActivityPaused(activity: Activity) {}
      override fun onActivityStopped(activity: Activity) {}
      override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
      override fun onActivityDestroyed(activity: Activity) {}
    })
  }

  override fun onTerminate() {
    super.onTerminate()
    application = null
  }

  // 初始化百度地图
  protected fun initBDMap() {
    SDKInitializer.initialize(this)
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
