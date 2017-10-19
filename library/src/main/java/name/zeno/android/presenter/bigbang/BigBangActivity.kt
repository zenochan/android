package name.zeno.android.presenter.bigbang

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_bigbang.*
import name.zeno.android.presenter.Extra
import name.zeno.android.presenter.ZActivity
import name.zeno.android.system.ZStatusBar
import name.zeno.android.util.R
import name.zeno.android.util.ZString
import java.util.*

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/3.
 */
class BigBangActivity : ZActivity(), BigBangView {
  private val presenter = BigBangPresenter(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ZStatusBar.setImage(this)
    setContentView(R.layout.activity_bigbang)
    ButterKnife.bind(this)
    segments(intent.getStringExtra(Extra.KEY))

  }

  private fun segments(text: String) {
    if (TextUtils.isEmpty(text)) {
      finish()
      return
    }

    val uglySegments = ArrayList<String>()
    for (i in 0 until (text.length + 1) / 4) {
      uglySegments.add(ZString.sub(text, 4 * i, 4 * i + 3))
    }

    layout_bigbang.setTextItems(uglySegments)
    presenter.segments(text, { layout_bigbang.setTextItems(it) })
  }

  companion object {
    fun callIntent(context: Context, text: String): Intent {
      val intent = Intent(context, BigBangActivity::class.java)
      intent.putExtra(Extra.KEY, text)
      return intent
    }
  }
}
