package name.zeno.android.presenter

import android.content.Context
import android.support.annotation.StringRes

/**
 * Create Date: 16/6/18
 *
 * @author 陈治谋 (513500085@qq.com)
 */
interface View : LifeCycleObservable {
  val ctx: Context

  fun getString(@StringRes res: Int): String

  fun toast(msg: String?)

  fun toast(@StringRes resId: Int)

  fun showMessage(msg: String?)

  fun showMessage(@StringRes resId: Int)

  fun showMessageAndFinish(message: String?)

  fun finish()
}
