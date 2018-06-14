package name.zeno.android.system

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import name.zeno.android.system.ZStatusBar.lightMode
import org.jetbrains.anko.contentView
import java.lang.reflect.Field

/**
 * - [lightMode]
 *
 *
 * - [透明状态栏](http://blog.csdn.net/lisdye2/article/details/51331602)
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/10/11.
 */
object ZStatusBar {


  /**
   * 将 activity 中的状态栏设置为一个纯色
   *
   * @param activity 需要设置的activity
   * @param color    设置的颜色（一般是titlebar的颜色）
   */
  fun setColor(activity: Activity, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //5.0及以上，不设置透明状态栏，设置会有半透明阴影
      activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      //设置statusBar的背景色
      activity.window.statusBarColor = color
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      // 生成一个状态栏大小的矩形
      val statusView = createStatusBarView(activity, color)
      // 添加 statusView 到布局中
      val decorView = activity.window.decorView as ViewGroup
      decorView.addView(statusView)
      //让我们的activity_main。xml中的布局适应屏幕
      setRootView(activity)
    }
  }

  /** 填充状态栏 */
  fun transparentAndFit(activity: Activity) = transparentAndFit(activity.window)

  /**  填充状态栏 */
  fun transparentAndFit(window: Window) {
    //window.requestFeature(Window.FEATURE_NO_TITLE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
      window.decorView.systemUiVisibility = (
          View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      window.statusBarColor = Color.TRANSPARENT
      window.navigationBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      window.decorView.fitsSystemWindows = true
    }
  }

  /**
   * 生成一个和状态栏大小相同的矩形条
   *
   * @param activity 需要设置的activity
   * @param color    状态栏颜色值
   * @return 状态栏矩形条
   */
  fun createStatusBarView(activity: Activity, color: Int): View {
    // 绘制一个和状态栏一样高的矩形
    val statusBarView = View(activity)
    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity))
    statusBarView.layoutParams = params
    statusBarView.setBackgroundColor(color)
    return statusBarView
  }


  /**
   * 当顶部是图片时，是图片显示到状态栏上
   */
  fun setImage(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      //5.0及以上，不设置透明状态栏，设置会有半透明阴影
      activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
      //是activity_main。xml中的图片可以沉浸到状态栏上
      activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
      //设置状态栏颜色透明。
      activity.window.statusBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      //。。。。
      activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
  }


  /**
   * 设置根布局参数，让跟布局参数适应透明状态栏
   */
  private fun setRootView(activity: Activity) {
    //如果不是设置参数，会使内容显示到状态栏上
    activity.contentView?.fitsSystemWindows = true
  }

  /**
   * 获取状态栏的高度
   */
  fun getStatusBarHeight(context: Context): Int {
//    val oppoOutCut = context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
//    ZLog.e(oppoOutCut.toString())

    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return context.resources.getDimensionPixelOffset(resourceId)
  }

  @SuppressLint("PrivateApi")
  fun isSupportLightMode(window: Window): Boolean {
    var lpClass: Class<*>? = null
    var darkFlag: Field? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      try {
        lpClass = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
      } catch (ignore: Throwable) {
      }

    }
    return lpClass != null || darkFlag != null || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
  }

  /**
   * 状态栏亮色模式，设置状态栏黑色文字、图标，
   * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
   *
   * [白底黑字！Android浅色状态栏黑色字体模式 ](http://www.jianshu.com/p/7f5a9969be53)
   *
   * @return -1: not support， 1:MIUUI 2:Flyme 3:android6.0
   */
  fun lightMode(activity: Activity, dark: Boolean = true): Int = lightMode(activity.window, dark)

  fun lightMode(window: Window, dark: Boolean = true): Int {
    var result = 0

    //  < 4.4
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return result

    if (miuiLightMode(window, dark)) {
      result = 1
    } else if (flymeLightMode(window, dark)) {
      result = 2
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      window.decorView.systemUiVisibility = when {
        dark -> View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else -> View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
      }
      result = 3
    }
    return result
  }

  /**
   * 需要 MIUI V6 以上
   *
   * - [「状态栏黑色字符」实现方法变更通知 - 2017.7.10](https://dev.mi.com/doc/p=10416/index.html)
   *
   * @param dark 是否把状态栏文字及图标颜色设置为深色
   * @return boolean 成功执行返回true
   */
  private fun miuiLightMode(window: Window, dark: Boolean): Boolean {
    var result = false

    try {
      val clazz = window.javaClass
      val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
      val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
      val darkModeFlag = field.getInt(layoutParams)
      val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
      if (dark) {
        extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
      } else {
        extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
      }
      result = true

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
        if (dark) {
          window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
          window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
      }
    } catch (ignore: Exception) {
    }

    return result
  }


  /**
   * 设置状态栏图标为深色和魅族特定的文字风格
   * 可以用来判断是否为Flyme用户
   *
   * @param dark 是否把状态栏文字及图标颜色设置为深色
   * @return boolean 成功执行返回true
   */
  private fun flymeLightMode(activity: Activity, dark: Boolean): Boolean = flymeLightMode(activity.window, dark)

  private fun flymeLightMode(window: Window, dark: Boolean): Boolean {
    var result = false
    try {
      val lp = window.attributes
      val darkFlag = WindowManager.LayoutParams::class.java
          .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
      val meizuFlags = WindowManager.LayoutParams::class.java
          .getDeclaredField("meizuFlags")
      darkFlag.isAccessible = true
      meizuFlags.isAccessible = true
      val bit = darkFlag.getInt(null)
      var value = meizuFlags.getInt(lp)
      if (dark) {
        value = value or bit
      } else {
        value = value and bit.inv()
      }
      meizuFlags.setInt(lp, value)
      window.attributes = lp
      result = true
    } catch (ignore: Exception) {
    }

    return result
  }

  /**
   * 系统 bar 高度
   *
   * @return {statusBarHeight,navigationBarHeight}
   */
  private fun barSize(): IntArray {
    //{statusBarHeight,navigationBarHeight}
    val size = intArrayOf(0, 0)

    val resources = Resources.getSystem()
    val resIdStatusbarHeight = resources.getIdentifier("status_bar_height", "dimen", "Android")

    if (resIdStatusbarHeight > 0) {
      size[0] = resources.getDimensionPixelSize(resIdStatusbarHeight)//状态栏高度
    }

    val resIdShow = resources.getIdentifier("config_showNavigationBar", "bool", "android")
    var hasNavigationBar = false
    if (resIdShow > 0) {
      hasNavigationBar = resources.getBoolean(resIdShow)//是否显示底部navigationBar
    }
    if (hasNavigationBar) {
      val resIdNavigationBar = resources.getIdentifier("navigation_bar_height", "dimen", "android")
      if (resIdNavigationBar > 0) {
        size[1] = resources.getDimensionPixelSize(resIdNavigationBar)//navigationBar高度
      }
    }

    return size
  }
}
