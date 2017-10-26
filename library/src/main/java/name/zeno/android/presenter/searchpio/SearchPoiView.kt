package name.zeno.android.presenter.searchpio

import name.zeno.android.presenter.LoadDataView

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
interface SearchPoiView : LoadDataView {
  fun requestLocationPermission(next: () -> Unit)
  fun empty()
}
