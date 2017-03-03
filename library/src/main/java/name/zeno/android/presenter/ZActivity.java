package name.zeno.android.presenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import name.zeno.android.listener.Action0;
import name.zeno.android.listener.Action2;
import name.zeno.android.presenter.activities.AutoHideIMActivity;
import name.zeno.android.third.rxjava.RxActivityResult;

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
//public abstract class ZActivity extends AutoHideIMActivity
public abstract class ZActivity extends AutoHideIMActivity implements LifeCycleObservable
{
  protected final String TAG;
  private boolean isDestroyed = false;

  private Dialog loadingDialog;

  @Getter private boolean afterSaveInstanceState = false;

  private final List<LifecycleListener> listeners = new ArrayList<>();

  private RxActivityResult rxActivityResult = new RxActivityResult(this);

  public ZActivity()
  {
    TAG = getClass().getSimpleName();
  }

  @Override public boolean isDestroyed()
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return super.isDestroyed();
    } else {
      return isDestroyed;
    }
  }

  public Context getContext()
  {
    return this;
  }

  public void showMessage(String msg)
  {
    snack(msg);
  }

  public void showMessage(@StringRes int resId)
  {
    snack(resId);
  }

  public void registerLifecycleListener(LifecycleListener listener)
  {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  @Override public void setContentView(@LayoutRes int layoutResID)
  {
    super.setContentView(layoutResID);
    onViewCreated();
  }

  @Override public void setContentView(View view)
  {
    super.setContentView(view);
    onViewCreated();
  }

  @Override public void setContentView(View view, ViewGroup.LayoutParams params)
  {
    super.setContentView(view, params);
    onViewCreated();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    for (LifecycleListener listener : listeners) {
      listener.onCreate();
    }
  }

  @Override protected void onResume()
  {
    super.onResume();
    for (LifecycleListener listener : listeners) {
      listener.onResume();
    }
    afterSaveInstanceState = false;
  }

  @Override protected void onPause()
  {
    super.onPause();
    if (loadingDialog != null) {
      loadingDialog.dismiss();
      loadingDialog = null;
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    afterSaveInstanceState = true;
  }

  @Override protected void onDestroy()
  {
    super.onDestroy();
    isDestroyed = true;
    for (LifecycleListener listener : listeners) {
      listener.onDestroyView();
      listener.onDestroy();
    }
  }

  public void showLoading()
  {
    if (loadingDialog == null) {
      loadingDialog = new MaterialDialog.Builder(this)
          .progress(true, 10000, true)
          .content("加载中...")
          .cancelable(false)
          .build();
    }
    loadingDialog.show();
  }

  public void hideLoading()
  {
    if (loadingDialog != null && loadingDialog.isShowing()) {
      loadingDialog.dismiss();
    }
  }

  protected void toast(String text)
  {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
  }

  protected void toast(@StringRes int resId)
  {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
  }

  public void showMessageAndFinish(String confirm)
  {
    new MaterialDialog.Builder(getContext())
        .title("提示")
        .content(confirm)
        .neutralText("好")
        .onNeutral((dialog, which) -> {
          finish();
        })
        .cancelable(false)
        .show();
  }

  public void startActivityForResult(Intent intent, Action2<Boolean, Intent> onResult)
  {
    rxActivityResult.startActivityForResult(intent, onResult);
  }

  protected void fullScreen()
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
  }

  protected void addFragment(@IdRes int container, Fragment fragment)
  {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.add(container, fragment);
    transaction.commit();
  }

  protected void addFragment(@IdRes int container, android.app.Fragment fragment)
  {
    android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(container, fragment);
    transaction.commit();
  }

  protected void snack(String text)
  {
    Snackbar.make(getRootView(), text, Snackbar.LENGTH_SHORT).show();
  }

  protected void snack(@StringRes int resId)
  {
    Snackbar.make(getRootView(), resId, Snackbar.LENGTH_SHORT).show();
  }


  protected void snack(String msg, String button)
  {
    snack(msg, button, null);
  }

  protected void snack(String msg, String button, Action0 onNext)
  {
    Snackbar.make(getRootView(), msg, Snackbar.LENGTH_LONG)
        .setAction(button, view -> {
          if (onNext != null) {
            onNext.call();
          }
        })
        .show();
  }

  protected View getRootView()
  {
    return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
  }

  private void onViewCreated()
  {
    for (LifecycleListener listener : listeners) {
      listener.onViewCreated();
    }
  }

}
