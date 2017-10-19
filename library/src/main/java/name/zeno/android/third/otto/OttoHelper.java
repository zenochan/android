package name.zeno.android.third.otto;

import android.os.Looper;

import com.squareup.otto.Bus;

import io.reactivex.Observable;
import name.zeno.android.third.rxjava.RxUtils;

/**
 * Create Date: 16/5/30
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class OttoHelper
{
  private static OttoHelper instance;

  private Bus bus;

  public OttoHelper()
  {
    this.bus = new Bus();
  }

  public static OttoHelper instance()
  {
    if (instance == null) {
      synchronized (OttoHelper.class) {
        if (instance == null) {
          instance = new OttoHelper();
        }
      }
    }
    return instance;
  }

  public void post(Object event)
  {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      bus.post(event);
    } else {
      // 切换到主线程发送事件
      // Event bus [Bus "default"] accessed from non-main thread Looper
      Observable.just(event).compose(RxUtils.INSTANCE.applySchedulers()).subscribe(bus::post);
    }
  }

  public void register(Object object)
  {
    bus.register(object);
  }

  public void unregister(Object object)
  {
    bus.unregister(object);
  }
}
