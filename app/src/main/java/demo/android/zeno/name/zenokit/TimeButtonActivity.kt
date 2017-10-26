package demo.android.zeno.name.zenokit

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_time_button.*
import name.zeno.android.presenter.ZActivity
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author 陈治谋 (微信: puppet2436)
 * @since 2017/4/9
 */
class TimeButtonActivity : ZActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_time_button)
    btn_sms.setLength(1000)
    btn_reset.onClick { btn_sms.reset() }
  }
}
