package name.zeno.android.presenter.input

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_input.*
import name.zeno.android.data.models.TextData
import name.zeno.android.presenter.ZActivity
import name.zeno.android.system.ZStatusBar
import name.zeno.android.util.R
import name.zeno.android.util.ZDimen
import name.zeno.android.util.ZEditor
import name.zeno.android.util.ZRex
import name.zeno.android.widget.ZTextWatcher
import java.util.regex.Pattern

/**
 * @param data [TextData]
 * @return data [TextData]
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/14
 */
class InputActivity : ZActivity() {

  private var pattern: Pattern? = null
  private lateinit var textData: TextData

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_input)

    ZTextWatcher.watch(et_content, { _, s -> textData.result = s })
    btn_submit.setOnClickListener { submit() }

    textData = intent.getParcelableExtra("data")

    if (textData.isWhiteStatusBar) {
      ZStatusBar.lightMode(this)
      actionbar.setBackgroundColor(Color.WHITE)
      actionbar.setStatusBarViewBackgroundColor(Color.WHITE)
      val color = Color.parseColor("#333333")
      actionbar.setTitleColor(color)
      actionbar.setPreTint(color)
    }

    if (textData.btnBackground != 0) {
      btn_submit.setBackgroundResource(textData.btnBackground)
    }

    textData.result = ""
    if (textData.regex.isNullOrEmpty().not()) {
      pattern = Pattern.compile(textData.regex)
    }

    actionbar.setTitleText(textData.title)

    when (textData.type) {
      TextData.TYPE_TEXT -> {
      }
      TextData.TYPE_PHONE, TextData.TYPE_NUMBER -> et_content.inputType = InputType.TYPE_CLASS_NUMBER
      TextData.TYPE_CONTENT -> {
        et_content.setSingleLine(false)
        et_content.height = ZDimen.dp2px(120f)
        et_content.gravity = Gravity.NO_GRAVITY
      }
    }
    et_content.hint = textData.hint
    et_content.setText(textData.preFill)
    et_content.setSelection(et_content.text?.length ?: 0)
    ZEditor.actionDone(et_content) { submit() }
  }


  private fun submit() {
    if (textData.type == TextData.TYPE_PHONE && !ZRex.validPhone(textData.result)) {
      snack("手机号码格式不正确")
      return
    }

    if (pattern != null && !pattern!!.matcher(textData.result).matches()) {
      snack(textData.regexHint ?: "")
      return
    }

    setResult(RESULT_OK, Intent().putExtra("data", textData))
    finish()
  }
}
