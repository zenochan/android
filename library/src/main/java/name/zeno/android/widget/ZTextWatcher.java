package name.zeno.android.widget;

import android.support.annotation.CallSuper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

import name.zeno.android.listener.Action2;

/**
 * Create Date: 16/5/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public abstract class ZTextWatcher implements TextWatcher
{

  public static TextWatcher watch(final TextView view, final Action2<TextView, String> watcher)
  {
    TextWatcher textWatcher = new TextWatcher()
    {
      private TextView textView = view;

      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

      @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
      {
        if (watcher != null) {
          watcher.call(textView, charSequence.toString());
        }
      }

      @Override public void afterTextChanged(Editable editable) { }
    };

    view.addTextChangedListener(textWatcher);
    return textWatcher;
  }

  /**
   * 通过 {@link TextWatcher} 纠正输入
   */
  public static TextWatcher pattern(final EditText view, String regex)
  {
    TextWatcher textWatcher = new TextWatcher()
    {
      Pattern pattern = Pattern.compile(regex);
      private EditText textView = view;
      CharSequence old = "";

      @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
      {
        if (old.length() == 0 || pattern.matcher(charSequence).matches()) {
          old = charSequence.toString();
        }
      }

      @Override public void onTextChanged(CharSequence s, int i, int i1, int i2)
      {
        if (s.length() > 0 && !old.equals(s.toString()) && !pattern.matcher(s).matches()) {
          int p = textView.getSelectionStart();
          p += s.length() > old.length() ? -1 : +1;
          textView.setText(old);
          textView.setSelection(Math.min(p, textView.length()));
        }
      }

      @Override public void afterTextChanged(Editable editable) { }
    };

    view.addTextChangedListener(textWatcher);
    return textWatcher;
  }

  @Override
  public void afterTextChanged(Editable s) { }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

  @CallSuper
  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count)
  {
    onTextChanged(s.toString());
  }

  abstract public void onTextChanged(String txt);

}
