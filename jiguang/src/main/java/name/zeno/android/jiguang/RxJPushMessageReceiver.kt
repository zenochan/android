package name.zeno.android.jiguang

import android.content.Context
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.service.JPushMessageReceiver
import io.reactivex.subjects.PublishSubject

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/25
 */
class RxJPushMessageReceiver : JPushMessageReceiver() {

  override fun onAliasOperatorResult(p0: Context?, p1: JPushMessage?) {
    if (p1 != null) ALIAS_OPERATOR_RESULT.onNext(p1)
  }

  override fun onMobileNumberOperatorResult(p0: Context?, p1: JPushMessage?) {
    if (p1 != null) MOBILE_NUMBER_OPERATOR_RESULT.onNext(p1)
  }

  override fun onCheckTagOperatorResult(p0: Context?, p1: JPushMessage?) {
    if (p1 != null) CHECK_TAG_OPERATOR_RESULT.onNext(p1)
  }

  override fun onTagOperatorResult(p0: Context?, p1: JPushMessage?) {
    if (p1 != null) TAG_OPERATOR_RESULT.onNext(p1)
  }

  companion object {
    internal val ALIAS_OPERATOR_RESULT = PublishSubject.create<JPushMessage>()
    internal val MOBILE_NUMBER_OPERATOR_RESULT = PublishSubject.create<JPushMessage>()
    internal val CHECK_TAG_OPERATOR_RESULT = PublishSubject.create<JPushMessage>()
    internal val TAG_OPERATOR_RESULT = PublishSubject.create<JPushMessage>()
  }
}
