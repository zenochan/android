package name.zeno.android.presenter

import android.content.Intent
import android.os.Bundle

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/18
 */
interface ActivityLauncher : LifeCycleObservable {
  fun startActivity(intent: Intent)
  fun startActivityForResult(intent: Intent, requestCode: Int)
  fun startActivityForResult(intent: Intent, requestCode: Int, optionsBundle: Bundle?)
}