package name.zeno.zako

import android.app.Fragment
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouterX
import com.alibaba.android.arouter.launcher.nav
import kotlinx.android.synthetic.main.fragment_blank.*
import name.zeno.android.util.ZLog
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class BlankFragment : Fragment() {

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_blank, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btn_nav_test.onClick {
      ARouterX.build("/test/router").nav<Parcelable>(this@BlankFragment) { ok, data ->
        toast("23424")
      }
    }

    btn_nav_sub_test.onClick { ARouterX.build("/sub/test").nav(this@BlankFragment) }
  }

}
