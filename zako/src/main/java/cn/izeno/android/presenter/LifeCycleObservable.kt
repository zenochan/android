package cn.izeno.android.presenter

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/13.
 */
interface LifeCycleObservable {
  fun registerLifecycleListener(listener: LifecycleListener)
  fun unregisterLifecycleListener(listener: LifecycleListener)
}
