package name.zeno.android.third.wxapi;

import android.os.Parcelable;

import com.tencent.mm.opensdk.modelbase.BaseReq;

import io.reactivex.Observable;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/4/27
 */
public abstract class AbsReq implements Parcelable
{
  public abstract Observable<BaseReq> build();
}
