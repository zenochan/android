package name.zeno.zako

import android.os.Bundle
import name.zeno.android.presenter.ZActivity
import org.jetbrains.anko.frameLayout

class MainActivity : ZActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    frameLayout {
      id = R.id.actionbarLayoutId;
      fragmentManager.beginTransaction().add(id, BlankFragment()).commit()
    }
  }
}
