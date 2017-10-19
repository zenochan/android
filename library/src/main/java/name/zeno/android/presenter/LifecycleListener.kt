package name.zeno.android.presenter

import android.content.Intent

/**
 * Create Date: 16/6/25
 *
 * @author 陈治谋 (513500085@qq.com)
 */
interface LifecycleListener {
  fun onCreate()

  fun onResume()

  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

  /** fragment  */
  fun onViewCreated()

  /** fragment  */
  fun onDestroyView()

  fun onDestroy()
}
