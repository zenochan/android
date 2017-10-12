package name.zeno.android.third.rxjava;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.alibaba.fastjson.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import name.zeno.android.exception.ZException;
import name.zeno.android.listener.Action1;
import retrofit2.HttpException;

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
      int code = ((HttpException) e).code();
      if (code == 500) {
        be = new ZException(ZException.ERR_SERVICE, "服务器繁忙", e);
      } else {
        be = new ZException(ZException.ERR_SERVICE, "服务器繁忙[" + code + "]", e);
      }
    } else if (e instanceof ZException) {
      be = (ZException) e;
    } else {
      be = new ZException(ZException.ERR_DEFAULT, e.getMessage(), e);
    }
    Log.w(TAG, e.getMessage(), e);

    handleError(be);
  }

  public void handleError(ZException e) {}

  @Override
  public void onSubscribe(Disposable d) { }

  @Override
  public void onNext(T value) { }

  @Override
  public void onComplete() { }

  public static <T> ZObserver<T> next(Action1<T> next)
  {
    return new ZObserver<T>()
    {
      @Override
      public void onNext(T value)
      {
        super.onNext(value);
        next.call(value);
      }
    };
  }
}
