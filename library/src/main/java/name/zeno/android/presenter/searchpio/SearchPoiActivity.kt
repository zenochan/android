package name.zeno.android.presenter.searchpio

import android.os.Bundle
import name.zeno.android.core.args
import name.zeno.android.presenter.ZActivity

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/12/14.
 */
class SearchPoiActivity : ZActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView { SearchPoiFragment().args(this) }
  }
}
