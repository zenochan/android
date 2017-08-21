package name.zeno.android.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.igexin.sdk.PushManager;
import com.orhanobut.logger.Logger;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;

import java.util.Locale;

import name.zeno.android.third.getui.ZGetuiService;
import name.zeno.android.third.getui.ZGTIntentService;
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

  @Override
  protected void attachBaseContext(Context base)
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

  /**
   * @deprecated 个推官方推荐使用 GTIntentService 代替 BroadcastReceiver 来接受消息， 使用 {@link #initGetui(Class, Class)} 注册个推
   */
  protected <T extends ZGetuiService> void initGetui(Class<T> tClazz)
  {
    PushManager.getInstance().initialize(this.getApplicationContext(), tClazz);
  }

  protected <T extends ZGetuiService, I extends ZGTIntentService> void initGetui(Class<T> tClazz, Class<I> intentServiceClass)
  {
    PushManager.getInstance().initialize(this.getApplicationContext(), tClazz);
    PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), intentServiceClass);
  }

  protected void initHotFix(String appVersion)
  {
    SophixManager.getInstance()
        .setContext(this)
        .setAppVersion(appVersion)
        .setEnableDebug(isDebug())
        .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
          String tag = "HOT_FIX[" + handlePatchVersion + "]";
          Logger.t(tag).e(String.format(Locale.CHINA, "[%d]%s", code, info));
          // 补丁加载回调通知
          switch (code) {
            case PatchStatus.CODE_LOAD_SUCCESS:
              // 表明补丁加载成功
              break;
            case PatchStatus.CODE_ERR_NOTINIT:
              Logger.t(tag).v(PatchStatus.INFO_ERR_NOTINIT);
              break;
            case PatchStatus.CODE_LOAD_RELAUNCH:
              // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
              // 建议: 用户可以监听进入后台事件, 然后应用自杀
              break;
            case PatchStatus.CODE_LOAD_FAIL:
              // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
              SophixManager.getInstance().cleanPatches();
              break;
            case PatchStatus.CODE_REQ_NOUPDATE:
              break;
            default:
              // 其它错误信息, 查看PatchStatus类说明
              break;
          }
        }).initialize();
    SophixManager.getInstance().queryAndLoadNewPatch();
  }

}
