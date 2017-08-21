package name.zeno.android.third.getui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import name.zeno.android.util.ZLog;


/**
 * 个推 Receiver 封装
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/18
 * @deprecated 使用 ZGTIntentService
 */
public abstract class AbsGetuiReceiver extends BroadcastReceiver
{
  private static final String TAG = "AbsGetuiReceiver";

  @Override
  public void onReceive(Context context, Intent intent)
  {
    Bundle bundle = intent.getExtras();
    int    action = bundle.getInt(PushConsts.CMD_ACTION);

    switch (action) {
      case PushConsts.GET_MSG_DATA:
        ZLog.v(TAG, "action: GET_MSG_DATA（获取透传数据）");

        //回调确认
        String taskid = bundle.getString("taskid");
        String messageid = bundle.getString("messageid");
        // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        ZLog.v(TAG, String.format("第三方回执接口调用%s%n", result ? "成功" : "失败"));

        //String appid = bundle.getString("appid");
        // 获取透传数据
        byte[] payload = bundle.getByteArray("payload");
        if (payload != null) {
          String data = new String(payload);
          onGetMsgData(context, messageid, data);
        }
        break;

      case PushConsts.GET_CLIENTID:
        ZLog.v(TAG, "action: GET_CLIENT_ID（获取CID）");
        // 获取ClientID(CID)
        String cid = bundle.getString("clientid");
        onGetClientId(context, cid);
        break;

      case PushConsts.THIRDPART_FEEDBACK:
        ZLog.v(TAG, "action: THIRD_PART_FEEDBACK(第三方回调)");
                /*
                 * String appid = bundle.getString("appid");
                 * String taskid = bundle.getString("taskid");
                 * String actionid = bundle.getString("actionid");
                 * String result = bundle.getString("result");
                 * long timestamp = bundle.getLong("timestamp");
                 *
                 * Log.d("GetuiSdkDemo", "appid = " + appid);
                 * Log.d("GetuiSdkDemo", "taskid = " + taskid);
                 * Log.d("GetuiSdkDemo", "actionid = " + actionid);
                 * Log.d("GetuiSdkDemo", "result = " + result);
                 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
                 */
        break;

      default:
        ZLog.v(TAG, String.format("action: %d(其他)", action));
        break;
    }
  }

  /**
   * <h1>当获取到 client id 时调用</h1>
   * 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
   */
  protected void onGetClientId(Context context, String cid)
  {
    Log.e(TAG, String.format("ClientId: %s", cid));
    saveCid(context, cid);
  }

  /**
   * 接受到透传消息数据时调用
   *
   * @param msgData 透传数据
   */
  protected void onGetMsgData(Context context, String messageId, String msgData)
  {
    ZLog.w(TAG, "receiver payload : " + msgData);
  }

  private void saveCid(Context context, String cid)
  {
    context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().putString(TAG, cid).apply();
  }

  @NonNull
  public static String getCid(Context context)
  {
    return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(TAG, "");
  }
}
