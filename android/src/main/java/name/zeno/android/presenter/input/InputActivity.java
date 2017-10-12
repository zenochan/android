package name.zeno.android.presenter.input;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.Gravity;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import name.zeno.android.data.models.TextData;
import name.zeno.android.presenter.Extra;
import name.zeno.android.presenter.ZActivity;
import name.zeno.android.system.ZStatusBar;
import name.zeno.android.util.R;
import name.zeno.android.util.R2;
import name.zeno.android.util.ZDimen;
import name.zeno.android.util.ZRex;
import name.zeno.android.util.ZString;
import name.zeno.android.widget.SimpleActionbar;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/8/14
 */
public class InputActivity extends ZActivity
{

  @BindView(R2.id.et_content) TextInputEditText etContent;
  @BindView(R2.id.actionbar)  SimpleActionbar   actionbar;
  @BindView(R2.id.btn_submit) AppCompatButton   submitBtn;

  private Pattern  pattern;
  private TextData textData;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_input);
    ButterKnife.bind(this);

    textData = Extra.getData(getIntent());

    if (textData.isWhiteStatusBar()) {
      ZStatusBar.INSTANCE.lightMode(this);
      actionbar.setBackgroundColor(Color.WHITE);
      actionbar.setStatusBarViewBackgroundColor(Color.WHITE);
      @ColorInt int color = Color.parseColor("#333333");
      actionbar.setTitleColor(color);
      actionbar.setPreTint(color);
    }

    if (textData.getBtnBackground() != 0) {
      submitBtn.setBackgroundResource(textData.getBtnBackground());
    }

    textData.setResult("");
    if (ZString.notEmpty(textData.getRegex())) {
      pattern = Pattern.compile(textData.getRegex());
    }

    actionbar.setTitleText(textData.getTitle());

    switch (textData.getType()) {
      case TextData.TYPE_TEXT:
        break;
      case TextData.TYPE_PHONE:
      case TextData.TYPE_NUMBER:
        etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        break;
      case TextData.TYPE_CONTENT:
        etContent.setSingleLine(false);
        etContent.setHeight(ZDimen.dp2px(120));
        etContent.setGravity(Gravity.NO_GRAVITY);
        break;
    }
    etContent.setHint(textData.getHint());
    etContent.setText(textData.getPreFill());
    etContent.setSelection(etContent.getText().length());
    etContent.setOnEditorActionListener((textView, i, keyEvent) -> {
      onClickSubmit();
      return true;
    });
  }

  @OnTextChanged(R2.id.et_content) void onContentChange(CharSequence s)
  {
    textData.setResult(s.toString());
  }

  @OnClick(R2.id.btn_submit) void onClickSubmit()
  {
    if (textData.getType() == TextData.TYPE_PHONE && !ZRex.validPhone(textData.getResult())) {
      snack("手机号码格式不正确");
      return;
    }

    if (pattern != null && !pattern.matcher(textData.getResult()).matches()) {
      snack(textData.getRegexHint());
      return;
    }

    setResult(RESULT_OK, Extra.setData(textData));
    finish();
  }

}
