package name.zeno.android.third.rxjava;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import name.zeno.android.exception.ZException;
import name.zeno.android.listener.Action1;

/**
 * 统一处理Exception
 * Create Date: 16/6/3
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ZObserver<T> implements Observer<T>
{
  private static final String TAG = "ZObserver";

  public final void onError(Throwable e)
  {
    ZException be;
    if (e instanceof NetworkOnMainThreadException) {
      be = new ZException(ZException.ERR_NETWORK_ON_UI, "UI线程执行网络请求", e);
    } else if (e instanceof UnknownHostException) {
      be = new ZException(ZException.ERR_UNKNOWN_HOST, "无法连接服务器", e);
    } else if (e instanceof JSONException) {
      be = new ZException(ZException.ERR_DATA_PARSE, "数据解析异常", e);
    } else if (e instanceof SocketTimeoutException) {
      be = new ZException(ZException.ERR_TIMEOUT, "连接超时", e);
    } else if (e instanceof ConnectException) {
      be = new ZException(ZException.ERR_CONNECT_FAILED, "网络连接失败", e);
    } else if (e instanceof HttpException) {
      be = new ZException(ZException.ERR_NOT_FOUND, "未找到网页", e);
    } else if (e instanceof ZException) {
      be = (ZException) e;
    } else {
      be = new ZException(ZException.ERR_DEFAULT, e.getMessage(), e);
    }
    Log.w(TAG, e.getMessage(), e);

    onBmError(be);
    onComplete();
  }

  public void onBmError(ZException e) {}

  @Override public void onSubscribe(Disposable d) { }

  @Override public void onNext(T value) { }

  @Override public void onComplete() { }

  public static <T> ZObserver<T> next(Action1<T> next)
  {
    return new ZObserver<T>()
    {
      @Override public void onNext(T value)
      {
        super.onNext(value);
        next.call(value);
      }
    };
  }
}
