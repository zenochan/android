package name.zeno.android.third.otto;

import com.squareup.otto.Bus;

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
    bus.post(event);
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
