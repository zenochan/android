package name.zeno.android.jiguang

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cn.jpush.android.api.JPushInterface

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/24
 */
abstract class AbsJPushReceiver : BroadcastReceiver() {


  final override fun onReceive(context: Context?, intent: Intent?) {
    when (intent?.action) {
      JPushInterface.ACTION_REGISTRATION_ID -> {
        // SDK 向 JPush Server 注册所得到的注册 ID 。
        val id = intent.getStringExtra(JPushInterface.EXTRA_REGISTRATION_ID)
        onReceivedRegistrationId(id)
      }

      JPushInterface.ACTION_MESSAGE_RECEIVED -> {
        // 收到了自定义消息 Push
        val msg = JPushMsg(
            id = intent.getStringExtra(JPushInterface.EXTRA_MSG_ID),
            title = intent.getStringExtra(JPushInterface.EXTRA_TITLE),
            msg = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE),
            extra = intent.getStringExtra(JPushInterface.EXTRA_EXTRA)
        )
        onReceivedMessage(msg)
      }

      JPushInterface.ACTION_NOTIFICATION_RECEIVED,
      JPushInterface.ACTION_NOTIFICATION_OPENED,
      JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION,
      JPushInterface.ACTION_CONNECTION_CHANGE,
      JPushInterface.ACTION_RICHPUSH_CALLBACK,
      JPushInterface.ACTION_NOTIFICATION_RECEIVED_PROXY -> Unit
    }
  }

  abstract fun onReceivedRegistrationId(id: String)

  abstract fun onReceivedMessage(msg: JPushMsg)
}