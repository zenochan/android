package name.zeno.zako

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouterX
import com.alibaba.android.arouter.launcher.nav
import name.zeno.android.presenter.ZFragment
import name.zeno.android.widget.StatusBarView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class BlankFragment : ZFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return UI {
      scrollView {

        verticalLayout {
          addView(StatusBarView(ctx))

          button {
            text = "Arouter Nav In Fragment"
            onClick {
              "/test/router".nav(this@BlankFragment) { _, _ ->
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


          button {
            text = "/app/shadow"
            onClick { nav(text) }
          }

          button {
            text = "/app/avatar"
            onClick { nav(text) }
          }


          button {
            text = "/app/glide"
            onClick { nav(text) }
          }
        }

      }
    }.view
  }

  fun nav(text: CharSequence) {
    ARouterX.build(text.toString()).navigation()
  }
}
