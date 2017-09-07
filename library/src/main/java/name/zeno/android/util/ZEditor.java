package name.zeno.android.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import name.zeno.android.listener.Action0;

/**
 * 输入控件常用方法工具类
 * <ul>
 * <li>{@link #actionDone(TextView, Action0)} 完成Action</li>
 * <li>{@link #number(EditText)} 仅输入数字</li>
 * <li>{@link #decimal(EditText)} 输入无符号小数</li>
 * <li>{@link #signDecimal(EditText)} 输入有符号小数</li>
 * </ul>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/5
 */
public abstract class ZEditor
{
  public static void actionDone(@NonNull TextView textView, @NonNull Action0 action)
  {
    textView.setOnEditorActionListener((textView1, i, keyEvent) -> {
      // TODO: 2017/9/5 仅在 action done时 处理，现在找不到常量了
      action.call();
      return true;
    });
  }


  /** [0-9] */
  public static void number(EditText view)
  {
    view.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
  }

  /** 无符号小数 */
  public static void decimal(EditText view)
  {
    KeyListener listener;
    if (Build.VERSION.SDK_INT < 26) {
      //noinspection deprecation
      listener = DigitsKeyListener.getInstance(false, true);
    } else {
      listener = DigitsKeyListener.getInstance(Locale.CHINESE, false, true);
    }

    view.setKeyListener(listener);
  }

  /** 有符号小数 */
  public static void signDecimal(EditText view)
  {
    KeyListener listener;
    if (Build.VERSION.SDK_INT < 26) {
      //noinspection deprecation
      listener = DigitsKeyListener.getInstance(true, true);
    } else {
      listener = DigitsKeyListener.getInstance(Locale.CHINESE, true, true);
    }

    view.setKeyListener(listener);
  }


  /** 显示输入法 */
  public static void showInputMethod(EditText view)
  {
    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
  }

  /** 隐藏输入法 */
  public static void hideInputMethod(View view)
  {
    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  /** 切换输入法显示 */
  public static void toggleInputMethod(View view)
  {
    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
  }

}
