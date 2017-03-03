package name.zeno.android.third.rxjava;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import name.zeno.android.data.JobExecutor;

/**
 * Create Date: 16/6/20
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class RxUtils
{
  private static boolean UNIT_TEST = false;

  @SuppressWarnings("unchecked")
  public static <T> ObservableTransformer<T, T> applySchedulers()
  {
    return observable -> observable
        .subscribeOn(UNIT_TEST ? Schedulers.trampoline() : Schedulers.from(JobExecutor.instance()))
        .observeOn(UNIT_TEST ? Schedulers.trampoline() : AndroidSchedulers.mainThread());
  }

  /**
   * 把异步变成同步，方便测试
   */
  public static void openUnitTest()
  {
    if (UNIT_TEST) {
      return;
    }

    UNIT_TEST = true;

    RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
    RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
  }

}

