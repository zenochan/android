package name.zeno.zako

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.launcher.ARouterX
import com.alibaba.android.arouter.launcher.nav
import org.jetbrains.anko.button
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    frameLayout {
      id = R.id.actionbarLayoutId;
      fragmentManager.beginTransaction().apply {
        add(id, BlankFragment())
        commit()
      }
//      button("Router To Test") {
//        onClick {
//          ARouterX.getInstance().build("/test/router")
//              .nav(this@MainActivity)
//        }
//      }
    }
  }
}
