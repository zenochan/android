package name.zeno.android.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import name.zeno.android.listener.Action2;
import name.zeno.android.third.rxjava.RxActivityResult;
import name.zeno.android.util.ZLog;

/**
 * Create Date: 16/6/9
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings({"unused", "Convert2streamapi"})
public abstract class ZFragment extends ToastFragment implements LifeCycleObservable
{
  protected final String TAG;
  protected final int RESULT_OK = Activity.RESULT_OK;

  private List<LifecycleListener> listenerList   = new ArrayList<>();
  private RxActivityResult        activityResult = new RxActivityResult(this);

  public static ZFragment newInstance()
  {
    return null;
  }

  public ZFragment()
  {
    TAG = getClass().getSimpleName();
  }


  public void registerLifecycleListener(LifecycleListener listener)
  {
    if (!listenerList.contains(listener)) {
      listenerList.add(listener);
    }
  }

  @CallSuper
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ZLog.v(TAG, "onCreate()");
    for (LifecycleListener l : listenerList) {
      l.onCreate();
    }
  }

  @CallSuper
  @Override
  public void onResume()
  {
    ZLog.v(TAG, "onResume()");
    super.onResume();
    for (LifecycleListener l : listenerList) {
      l.onResume();
    }
  }

  @CallSuper
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    ZLog.v(TAG, "onActivityResult()");
    super.onActivityResult(requestCode, resultCode, data);
    for (LifecycleListener l : listenerList) {
      l.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
  {
    ZLog.v(TAG, "onCreateView()");
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @CallSuper
  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
  {
    ZLog.v(TAG, "onViewCreated()");
    super.onViewCreated(view, savedInstanceState);
    for (LifecycleListener l : listenerList) {
      l.onViewCreated();
    }
  }

  @CallSuper
  @Override
  public void onDestroyView()
  {
    super.onDestroyView();
    ZLog.v(TAG, "onDestroyView()");
    for (LifecycleListener l : listenerList) {
      l.onDestroyView();
    }
  }

  @CallSuper
  @Override
  public void onStop()
  {
    super.onStop();
    ZLog.v(TAG, "onStop()");
  }

  @CallSuper
  @Override
  public void onDestroy()
  {
    super.onDestroy();
    ZLog.v(TAG, "onDestroy()");
    for (LifecycleListener l : listenerList) {
      l.onDestroy();
    }
  }

  public void setActivityResultOk(Intent data)
  {
    getActivity().setResult(Activity.RESULT_OK, data);
  }

  public void setActivityResult(int resultCode)
  {
    getActivity().setResult(resultCode);
  }

  public void setActivityResult(int resultCode, Intent data)
  {
    getActivity().setResult(resultCode, data);
  }

  public void setActivityResult(int resultCode, Parcelable data)
  {
    getActivity().setResult(resultCode, Extra.setData(data));
  }

  public void finish()
  {
    getActivity().finish();
  }

  public void startActivityForResult(Intent intent, Action2<Boolean, Intent> onResult)
  {
    activityResult.startActivityForResult(intent, onResult);
  }
}