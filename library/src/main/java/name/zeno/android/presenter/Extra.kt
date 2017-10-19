package name.zeno.android.presenter

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/9.
 */
object Extra {
  val KEY = "intent_data"

  fun setData(data: Parcelable): Intent {
    return setData(Intent(), data)
  }

  fun setData(intent: Intent, data: Parcelable?): Intent {
    intent.putExtra(KEY, data)
    return intent
  }

  fun setData(bundle: Bundle, data: Parcelable?): Bundle {
    bundle.putParcelable(KEY, data)
    return bundle
  }

  fun <T : Parcelable> getData(intent: Intent?): T? {
    return intent?.getParcelableExtra(KEY)
  }

  fun <T : Parcelable> getData(bundle: Bundle?): T? {
    return bundle?.getParcelable(KEY)
  }
}
