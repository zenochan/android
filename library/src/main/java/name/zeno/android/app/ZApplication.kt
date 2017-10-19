package name.zeno.android.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.baidu.mapapi.SDKInitializer
import com.igexin.sdk.PushManager
import com.orhanobut.logger.Logger
import com.taobao.sophix.PatchStatus
import com.taobao.sophix.SophixManager
import name.zeno.android.third.getui.ZGetuiMessageService
import name.zeno.android.third.getui.ZGetuiService
import name.zeno.android.util.ZCookie
import name.zeno.android.util.ZLog
import java.util.*

abstract class ZApplication : MultiDexApplication() {

  protected abstract val isDebug: Boolean

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

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    MultiDex.install(this)
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

  protected fun initHotFix(appVersion: String) {
    SophixManager.getInstance()
        .setContext(this)
        .setAppVersion(appVersion)
        .setEnableDebug(isDebug)
        .setPatchLoadStatusStub { mode, code, info, handlePatchVersion ->
          val tag = "HOT_FIX[$handlePatchVersion]"
          Logger.t(tag).e(String.format(Locale.CHINA, "[%d]%s", code, info))
          // 补丁加载回调通知
          when (code) {
            PatchStatus.CODE_LOAD_SUCCESS -> {
            }
            PatchStatus.CODE_ERR_NOTINIT -> Logger.t(tag).v(PatchStatus.INFO_ERR_NOTINIT)
            PatchStatus.CODE_LOAD_RELAUNCH -> {
            }
            PatchStatus.CODE_LOAD_FAIL ->
              // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
              SophixManager.getInstance().cleanPatches()
            PatchStatus.CODE_REQ_NOUPDATE -> {
            }
            else -> {
            }
          }// 表明补丁加载成功
          // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
          // 建议: 用户可以监听进入后台事件, 然后应用自杀
          // 其它错误信息, 查看PatchStatus类说明
        }.initialize()
    SophixManager.getInstance().queryAndLoadNewPatch()
  }

  companion object {

    @JvmStatic
    var application: Application? = null
      private set
  }
}
