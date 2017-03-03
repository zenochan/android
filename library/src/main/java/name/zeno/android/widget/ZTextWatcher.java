package name.zeno.android.widget;

import android.support.annotation.CallSuper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import name.zeno.android.listener.Action2;

/**
 * Create Date: 16/5/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public abstract class ZTextWatcher implements TextWatcher
{
  public static TextWatcher watch(final TextView view, Action2<TextView, String> watcher)
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
