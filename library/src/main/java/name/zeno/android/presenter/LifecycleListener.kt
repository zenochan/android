package name.zeno.android.presenter

import android.content.Intent

/**
 * Create Date: 16/6/25
 *
 * @author 陈治谋 (513500085@qq.com)
 */
interface LifecycleListener {
  fun onStart() {}

  fun onCreate() {}

  fun onResume() {}

  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}

  /** fragment  */
  fun onViewCreated() {}

  fun onPause() {}

  /** fragment  */
  fun onDestroyView() {}

  fun onDestroy() {}
}
