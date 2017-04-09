package name.zeno.android.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.igexin.sdk.PushManager;
import com.orhanobut.logger.Logger;
import com.taobao.hotfix.HotFixManager;
import com.taobao.hotfix.util.PatchStatusCode;

import name.zeno.android.third.getui.GetuiService;
import name.zeno.android.util.ZCookie;
import name.zeno.android.util.ZLog;

public abstract class ZApplication extends MultiDexApplication {

  @SuppressLint("StaticFieldLeak")
  private static Application instance;

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    ZCookie.init(this);
    ZLog.debug(isDebug());
    AppInfo.init(this);
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    instance = null;
  }

  // 初始化百度地图
  protected void initBDMap() {
    SDKInitializer.initialize(this);
  }

  public static Application getApplication() {
    return instance;
  }

  protected abstract boolean isDebug();

  protected <T extends GetuiService> void initGetui(Class<T> tClazz) {
    PushManager.getInstance().initialize(this.getApplicationContext(), tClazz);
  }

  protected void initHotFix(String appId, String appVersion) {
    String tag = "HOT_FIX";
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      // 只支持到 安卓 6.0
      return;
    }

    HotFixManager.getInstance().setContext(this)
        .setAppVersion(appVersion)
        .setAppId(appId)
        .setAesKey(null)
        .setSupportHotpatch(true)
        .setEnableDebug(true)
        .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
          switch (code) {
            case PatchStatusCode.CODE_SUCCESS_LOAD:
              Logger.t(tag).v("补丁加载回调通知");
              break;
            case PatchStatusCode.CODE_ERROR_NEEDRESTART:
              Logger.t(tag).v("新补丁生效需要重启");
              break;
            case PatchStatusCode.CODE_ERROR_INNERENGINEFAIL:
              HotFixManager.getInstance().cleanPatches(false);
              Logger.t(tag).v("内部引擎加载异常");
              break;
            case PatchStatusCode.CODE_ERROR_PATCHNOUPDATE:
              Logger.t(tag).v("没有新的 patch");
              break;
            default:
              Logger.t(tag).v("其他错误-------> " + code);
              break;
          }
        }).initialize();
  }

}
