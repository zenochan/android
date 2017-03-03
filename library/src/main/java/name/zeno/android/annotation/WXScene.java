package name.zeno.android.annotation;

import android.support.annotation.IntDef;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/23
 */
@IntDef({
    WXScene.FAVORITE,
    WXScene.SESSION,
    WXScene.TIMELINE
})
@Retention(RetentionPolicy.SOURCE)
public @interface WXScene
{
  int FAVORITE = SendMessageToWX.Req.WXSceneFavorite;//搜藏
  int SESSION  = SendMessageToWX.Req.WXSceneSession;//好友
  int TIMELINE = SendMessageToWX.Req.WXSceneTimeline;//朋友圈
}
