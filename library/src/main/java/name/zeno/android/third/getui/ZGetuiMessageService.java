package name.zeno.android.third.getui;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import name.zeno.android.data.models.GetuiMessage;

/**
 * <li>{@link #onReceiveClientId(Context, String)} 接受 cid</li>
 * <li>{@link #onReceiveMessageData(Context, GetuiMessage)} 接收透传消息</li>
 * <li>{@link #onReceiveOnlineState(Context, boolean)} 离线上线通知</li>
 * <li>{@link #getCid(Context)} 获取缓存中的 cid</li>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/21
 */
abstract public class ZGetuiMessageService extends GTIntentService
{
  private static final String TAG = "ZGetuiMessageService";

  @Override public void onReceiveServicePid(Context context, int i) { }

  /**
   * <h1>当获取到 client id 时调用</h1>
   * 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
   */
  @CallSuper @Override
  public void onReceiveClientId(Context context, String cid)
  {
    Log.e(TAG, String.format("ClientId: %s", cid));
    saveCid(context, cid);
  }

  @Override
  public final void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage)
  {
    onReceiveMessageData(context, new GetuiMessage(gtTransmitMessage));
  }

  @Override public void onReceiveOnlineState(Context context, boolean b) { }

  abstract public void onReceiveMessageData(Context context, GetuiMessage message);

  @Override public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) { }

  /** 保存cid */
  private void saveCid(Context context, String cid)
  {
    context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().putString(TAG, cid).apply();
  }

  /** 获取保存的cid */
  @Nullable
  public static String getCid(Context context)
  {
    return context.getSharedPreferences(TAG, Context.MODE_PRIVATE).getString(TAG, null);
  }
}
