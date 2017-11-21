package name.zeno.android.presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import name.zeno.android.core.sdkInt
import name.zeno.android.third.otto.registerOtto
import name.zeno.android.third.otto.unregisterOtto
import name.zeno.android.third.rxjava.RxActivityResult
import name.zeno.android.third.umeng.ZUmeng
import name.zeno.android.util.ZLog
import name.zeno.android.util.ZString
import java.util.*

/**
 * - auto register and unregister Otto
 * - implement [ActivityLauncher],[LoadDataView]
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/9
 */
open class ZFragment : ToastFragment(), ActivityLauncher, LoadDataView {
  protected val TAG: String = javaClass.simpleName
  protected val RESULT_OK = Activity.RESULT_OK
  @Suppress("unused")
  val inflater by lazy { LayoutInflater.from(activity) }

  private val listenerList = ArrayList<LifecycleListener>()
  private val activityResult = RxActivityResult(this)

  override fun registerLifecycleListener(listener: LifecycleListener) {
    if (!listenerList.contains(listener)) {
      listenerList.add(listener)
    }
  }

  override fun unregisterLifecycleListener(listener: LifecycleListener) {
    if (listenerList.contains(listener)) {
      listenerList.remove(listener)
    }
  }

  @SuppressLint("NewApi")
  override fun getContext(): Context? = if (sdkInt >= 23) super.getContext() else activity

  override val ctx: Context
    @SuppressLint("NewApi")
    get() = if (sdkInt >= 23) super.getContext() else activity

  override fun showLoading() = super.showLoading("加载中...")

  /** 友盟统计页面名称，如果集成了友盟，应当重写此方法  */
  fun pageName(): String = ""

  //<editor-fold desc="life circle">
  @CallSuper
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ZLog.v(TAG, "onCreate()")
    listenerList.forEach { it.onCreate() }
    registerOtto(this)
  }

  override fun onStart() {
    super.onStart()
    listenerList.forEach { it.onStart() }
  }

  @CallSuper
  override fun onResume() {
    ZLog.v(TAG, "onResume()")
    super.onResume()
    listenerList.forEach { it.onResume() }
    var pageName = TAG
    if (ZString.notEmpty(pageName())) pageName += " | " + pageName()
    ZUmeng.onPageStart(pageName)
  }

  @CallSuper
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    ZLog.v(TAG, "onActivityResult()")
    super.onActivityResult(requestCode, resultCode, data)
    listenerList.forEach { it.onActivityResult(requestCode, resultCode, data) }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    ZLog.v(TAG, "onCreateView()")
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  @CallSuper
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    ZLog.v(TAG, "onViewCreated()")
    listenerList.forEach { it.onViewCreated() }
  }

  @CallSuper
  override fun onDestroyView() {
    super.onDestroyView()
    ZLog.v(TAG, "onDestroyView()")
    listenerList.forEach { it.onDestroyView() }
  }

  @CallSuper
  override fun onStop() {
    super.onStop()
    ZLog.v(TAG, "onStop()")
  }

  override fun onPause() {
    super.onPause()
    listenerList.forEach { it.onPause() }
  }

  @CallSuper
  override fun onDestroy() {
    super.onDestroy()
    ZLog.v(TAG, "onDestroy()")
    listenerList.forEach { it.onDestroy() }
    unregisterOtto(this)
  }
  //</editor-fold>

  override fun finish() = activity?.finish() ?: Unit

  fun showCalender(min: Calendar? = null, max: Calendar? = null, next: (Calendar) -> Unit) {
    val today = Calendar.getInstance()
    val y = today.get(Calendar.YEAR)
    val m = today.get(Calendar.MONTH)
    val d = today.get(Calendar.DAY_OF_MONTH)

    val dialog = DatePickerDialog.newInstance({ _, _y, _m, _d ->
      val calendar = Calendar.getInstance()
      calendar.set(_y, _m, _d)
      next(calendar)
    }, y, m, d)
    if (min != null) dialog.minDate = min
    if (max != null) dialog.maxDate = max
    dialog.show(fragmentManager, "date_picker_dialog")
  }

  fun startActivityForResult(intent: Intent, next: (Boolean, Intent?) -> Unit) {
    activityResult.startActivityForResult(intent, next)
  }
}
