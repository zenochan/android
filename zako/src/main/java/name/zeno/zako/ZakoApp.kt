package name.zeno.zako

import android.app.Application
import name.zeno.android.jiguang.jPushInit

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/24
 */
class ZakoApp : Application() {
  override fun onCreate() {
    super.onCreate()
    jPushInit()
  }
}