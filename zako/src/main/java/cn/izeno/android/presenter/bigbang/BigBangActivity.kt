package cn.izeno.android.presenter.bigbang

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_bigbang.*
import cn.izeno.android.presenter.ZActivity
import cn.izeno.android.system.ZStatusBar
import cn.izeno.android.util.R
import cn.izeno.android.util.ZString
import java.util.*

/**
 * @param data [String] 需要分词的字符串
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/3.
 */
class BigBangActivity : ZActivity(), BigBangView {
  private val presenter = BigBangPresenter(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ZStatusBar.setImage(this)
    setContentView(R.layout.activity_bigbang)
    segments(intent.getStringExtra("data"))

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
      intent.putExtra("data", text)
      return intent
    }
  }
}
