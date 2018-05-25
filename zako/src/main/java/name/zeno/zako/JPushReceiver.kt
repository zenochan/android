package name.zeno.zako

import android.util.Log
import name.zeno.android.jiguang.AbsJPushReceiver
import name.zeno.android.jiguang.JPushMsg

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/24
 */
class JPushReceiver : AbsJPushReceiver() {
  override fun onReceivedRegistrationId(id: String) {
    Log.e("jpush_id", id)
  }

  override fun onReceivedMessage(msg: JPushMsg) {
    Log.e("jpush_msg", msg.toString())
  }
}