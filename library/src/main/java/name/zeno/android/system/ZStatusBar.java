package name.zeno.android.system;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @see <a href="http://blog.csdn.net/lisdye2/article/details/51331602">透明状态栏(StatusBar)的全适配</a>
 * @since 2016/10/11.
 */
public class ZStatusBar
{


  /**
   * 将acitivity中的activity中的状态栏设置为一个纯色
   *
   * @param activity 需要设置的activity
   * @param color    设置的颜色（一般是titlebar的颜色）
   */
  public static void setColor(Activity activity, int color)
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //5.0及以上，不设置透明状态栏，设置会有半透明阴影
      activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      //设置statusBar的背景色
      activity.getWindow().setStatusBarColor(color);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      // 生成一个状态栏大小的矩形
      View statusView = createStatusBarView(activity, color);
      // 添加 statusView 到布局中
      ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
      decorView.addView(statusView);
      //让我们的activity_main。xml中的布局适应屏幕
      setRootView(activity);
    }
  }

  /**
   * 生成一个和状态栏大小相同的矩形条
   *
   * @param activity 需要设置的activity
   * @param color    状态栏颜色值
   * @return 状态栏矩形条
   */
  public static View createStatusBarView(Activity activity, int color)
  {
    // 绘制一个和状态栏一样高的矩形
    View                      statusBarView = new View(activity);
    LinearLayout.LayoutParams params        = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
    statusBarView.setLayoutParams(params);
    statusBarView.setBackgroundColor(color);
    return statusBarView;
  }


  /**
   * 当顶部是图片时，是图片显示到状态栏上
   */
  public static void setImage(Activity activity)
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //5.0及以上，不设置透明状态栏，设置会有半透明阴影
      activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      //是activity_main。xml中的图片可以沉浸到状态栏上
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
      //设置状态栏颜色透明。
      activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      //。。。。
      activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
  }


  /**
   * 设置根布局参数，让跟布局参数适应透明状态栏
   */
  private static void setRootView(Activity activity)
  {
    //获取到activity_main.xml文件
    ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    //如果不是设置参数，会使内容显示到状态栏上
    rootView.setFitsSystemWindows(true);
  }

  /**
   * 获取状态栏的高度
   */
  public static int getStatusBarHeight(Context context)
  {
    int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    return context.getResources().getDimensionPixelOffset(resourceId);
  }

  /**
   * 状态栏亮色模式，设置状态栏黑色文字、图标，
   * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
   *
   * @return 1:MIUUI 2:Flyme 3:android6.0
   * @see <a href="http://www.jianshu.com/p/7f5a9969be53">白底黑字！Android浅色状态栏黑色字体模式 </a>
   */
  public static int lightMode(Activity activity)
  {
    int result = 0;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      if (miuiSetStatusBarLightMode(activity, true)) {
        result = 1;
      } else if (flymeSetStatusBarLightMode(activity, true)) {
        result = 2;
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        result = 3;
      }
    }
    return result;
  }

  /**
   * 需要 MIUI V6 以上
   *
   * @param dark 是否把状态栏文字及图标颜色设置为深色
   * @return boolean 成功执行返回true
   */
  private static boolean miuiSetStatusBarLightMode(@NonNull Activity activity, boolean dark)
  {
    boolean result = false;

    try {
      Window window       = activity.getWindow();
      Class  clazz        = window.getClass();
      Class  layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
      Field  field        = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
      int    darkModeFlag = field.getInt(layoutParams);
      @SuppressWarnings("unchecked")
      Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
      if (dark) {
        extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
      } else {
        extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
      }
      result = true;

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
        if (dark) {
          activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
          activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
      }
    } catch (Exception ignore) { }

    return result;
  }


  /**
   * 设置状态栏图标为深色和魅族特定的文字风格
   * 可以用来判断是否为Flyme用户
   *
   * @param dark 是否把状态栏文字及图标颜色设置为深色
   * @return boolean 成功执行返回true
   */
  private static boolean flymeSetStatusBarLightMode(@NonNull Activity activity, boolean dark)
  {
    boolean result = false;
    try {
      Window                     window = activity.getWindow();
      WindowManager.LayoutParams lp     = window.getAttributes();
      Field darkFlag = WindowManager.LayoutParams.class
          .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
      Field meizuFlags = WindowManager.LayoutParams.class
          .getDeclaredField("meizuFlags");
      darkFlag.setAccessible(true);
      meizuFlags.setAccessible(true);
      int bit   = darkFlag.getInt(null);
      int value = meizuFlags.getInt(lp);
      if (dark) {
        value |= bit;
      } else {
        value &= ~bit;
      }
      meizuFlags.setInt(lp, value);
      window.setAttributes(lp);
      result = true;
    } catch (Exception ignore) { }

    return result;
  }

  /**
   * 系统 bar 高度
   *
   * @return {statusBarHeight,navigationBarHeight}
   */
  private int[] barSize()
  {
    //{statusBarHeight,navigationBarHeight}
    int[] size = {0, 0};

    Resources resources            = Resources.getSystem();
    int       resIdStatusbarHeight = resources.getIdentifier("status_bar_height", "dimen", "Android");

    if (resIdStatusbarHeight > 0) {
      size[0] = resources.getDimensionPixelSize(resIdStatusbarHeight);//状态栏高度
    }

    int     resIdShow        = resources.getIdentifier("config_showNavigationBar", "bool", "android");
    boolean hasNavigationBar = false;
    if (resIdShow > 0) {
      hasNavigationBar = resources.getBoolean(resIdShow);//是否显示底部navigationBar
    }
    if (hasNavigationBar) {
      int resIdNavigationBar = resources.getIdentifier("navigation_bar_height", "dimen", "android");
      if (resIdNavigationBar > 0) {
        size[1] = resources.getDimensionPixelSize(resIdNavigationBar);//navigationBar高度
      }
    }

    return size;
  }
}
