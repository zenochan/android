package name.zeno.android.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import name.zeno.android.listener.Action0;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/9/27.
 */
@SuppressWarnings("unused")
public class ToastFragment extends Fragment
{
  private android.view.View fragmentView;
  private MaterialDialog    progressDialog;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);
    fragmentView = view;
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    if (progressDialog != null) {
      progressDialog.dismiss();
      progressDialog = null;
    }
    fragmentView = null;
  }

  //<editor-fold desc="snack & toast">
  protected void snack(String msg)
  {
    Snackbar.make(fragmentView, msg, Snackbar.LENGTH_LONG).show();
  }

  protected void snack(String msg, String button)
  {
    snack(msg, button, null);
  }

  protected void snack(String msg, String button, Action0 onNext)
  {
    Snackbar.make(fragmentView, msg, Snackbar.LENGTH_LONG)
        .setAction(button, view -> {
          if (onNext != null) {
            onNext.call();
          }
        })
        .show();
  }

  protected void snack(@StringRes int resId)
  {
    Snackbar.make(fragmentView, resId, Snackbar.LENGTH_SHORT).show();
  }

  protected void snack(@StringRes int resId, String button)
  {
    snack(resId, button, null);
  }

  protected void snack(@StringRes int resId, String button, Action0 onNext)
  {
    Snackbar.make(fragmentView, resId, Snackbar.LENGTH_LONG)
        .setAction(button, view -> {
          if (onNext != null) {
            onNext.call();
          }
        })
        .show();
  }

  protected void longSnack(String msg)
  {
    Snackbar.make(fragmentView, msg, Snackbar.LENGTH_LONG).show();
  }

  protected void longSnack(@StringRes int resId)
  {
    Snackbar.make(fragmentView, resId, Snackbar.LENGTH_LONG).show();
  }

  protected void toast(String msg)
  {
    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
  }

  protected void toast(@StringRes int resId)
  {
    Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
  }

  public void showLoading()
  {
    showLoading("加载中...");
  }

  public void showLoading(String content)
  {
    if (progressDialog == null) {
      progressDialog = new MaterialDialog.Builder(getContext())
          .progress(true, 10000, true)
          .cancelable(false)
          .build();
    }
    progressDialog.setContent(content);
    progressDialog.show();
  }

  public void hideLoading()
  {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  public void confirm(String confirm, String ok, String cancel, Action0 onOk)
  {
    new MaterialDialog.Builder(getContext())
        .content(confirm)
        .positiveText(ok)
        .neutralText(cancel)
        .onPositive((dialog, which) -> onOk.call())
        .show();
  }

  public void showMessageAndFinish(String confirm)
  {
    new MaterialDialog.Builder(getContext())
        .title("提示")
        .content(confirm)
        .neutralText("好")
        .onNeutral((dialog, which) -> {
          getActivity().finish();
        })
        .cancelable(false)
        .show();
  }

  public void showMessage(String msg)
  {
    snack(msg);
  }

  public void showMessage(@StringRes int resId)
  {
    snack(resId);
  }
  //</editor-fold>

}
