package name.zeno.android.third.rxjava

import io.reactivex.Emitter
import io.reactivex.ObservableTransformer
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import name.zeno.android.data.JobExecutor

/**
 * Create Date: 16/6/20
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object RxUtils {
  private var UNIT_TEST = false

  fun <T> applySchedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer {
      it.subscribeOn(if (UNIT_TEST) Schedulers.trampoline() else Schedulers.from(JobExecutor))
          .observeOn(if (UNIT_TEST) Schedulers.trampoline() else AndroidSchedulers.mainThread())
    }
  }

  /**
   * 把异步变成同步，方便测试
   */
  @Synchronized
  fun openUnitTest() {
    if (!UNIT_TEST) {
      UNIT_TEST = true
      RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
      RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
      RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
    }
  }

}

