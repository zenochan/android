package name.zeno.android.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.logger.Logger;
import com.taobao.hotfix.HotFixManager;
import com.taobao.hotfix.util.PatchStatusCode;

import name.zeno.android.util.ZCookie;
import name.zeno.android.util.ZLog;

public abstract class ZApplication extends MultiDexApplication
{

  @SuppressLint("StaticFieldLeak")
  private static Application instance;

  @Override
  public void onCreate()
  {
    super.onCreate();
    instance = this;
    ZCookie.init(this);
    ZLog.debug(isDebug());
    AppInfo.init(this);
  }

  @Override protected void attachBaseContext(Context base)
  {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Override
  public void onTerminate()
  {
    super.onTerminate();
    instance = null;
  }

  // 初始化百度地图
  protected void initBDMap()
  {
    SDKInitializer.initialize(this);
  }

  public static Application getApplication()
  {
    return instance;
  }

  protected abstract boolean isDebug();

  protected void initHotFix(String appId, String appVersion)
  {
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
              // TODO: 10/24/16 表明补丁加载成功
              Logger.t(tag).v("补丁加载回调通知");
              break;
            case PatchStatusCode.CODE_ERROR_NEEDRESTART:
              // TODO: 10/24/16 表明新补丁生效需要重启. 业务方可自行实现逻辑, 提示用户或者强制重启, 建议: 用户可以监听进入后台事件, 然后应用自杀
              Logger.t(tag).v("新补丁生效需要重启");
              break;
            case PatchStatusCode.CODE_ERROR_INNERENGINEFAIL:
              // 内部引擎加载异常, 推荐此时清空本地补丁, 但是不清空本地版本号, 防止失败补丁重复加载
              //HotFixManager.getInstance().cleanPatches(false);
              Logger.t(tag).v("内部引擎加载异常");
              break;
            default:
              // TODO: 10/25/16 其它错误信息, 查看PatchStatusCode类说明
              Logger.t(tag).v("其他-------> " + code);
              break;
          }
        }).initialize();
  }

}
