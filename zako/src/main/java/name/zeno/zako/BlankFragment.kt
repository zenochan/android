package name.zeno.zako

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouterX
import com.alibaba.android.arouter.launcher.nav
import name.zeno.android.presenter.ZFragment
import name.zeno.android.util.dp
import name.zeno.android.widget.StatusBarView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class BlankFragment : ZFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return UI {
      verticalLayout {
        addView(StatusBarView(ctx))

        button {
          text = "Arouter Nav In Fragment"
          onClick {
            ARouterX.build("/test/router").nav<Parcelable>(this@BlankFragment) { ok, data ->
              toast("23424")
            }
          }
        }

        button {
          text = "/sub/test"
          onClick { ARouterX.build(text.toString()).navigation() }
        }

        button {
          text = "/app/shadow"
          onClick { ARouterX.build(text.toString()).navigation() }
        }
      }
    }.view
  }
}
