package name.zeno.android.third.rxjava

import android.app.Activity
import android.content.Intent
import name.zeno.android.presenter.ActivityLauncher
import name.zeno.android.presenter.LifecycleListener

/**
 * # Rx封装 onActivityResult回掉
 *
 *
 * 同步  [onActivityResult] 方法的调用,占用[REQUEST_CODE]
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/9.
 */
class RxActivityResult(val launcher: ActivityLauncher) {
  private val REQUEST_CODE = 0xF001
  private var onResult: ((Boolean, Intent?) -> Unit)? = null

  init {
    launcher.registerLifecycleListener(object : LifecycleListener {
      override fun onCreate() {}
      override fun onResume() {}

      override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        this@RxActivityResult.onActivityResult(requestCode, resultCode, data)
      }

      override fun onViewCreated() {}
      override fun onDestroyView() {}
      override fun onDestroy() {}
    })

  }

  fun startActivityForResult(intent: Intent, onResult: (Boolean, Intent?) -> Unit) {
    this.onResult = onResult
    launcher.startActivityForResult(intent, REQUEST_CODE)
  }

  private fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == REQUEST_CODE) {
      onResult?.invoke(resultCode == Activity.RESULT_OK, data)
    }
    onResult = null
  }
}
