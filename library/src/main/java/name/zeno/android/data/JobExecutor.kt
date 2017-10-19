package name.zeno.android.data


import java.util.concurrent.*

/**
 * Create Date: 16/1/7
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object JobExecutor : Executor {
  private val INITIAL_POOL_SIZE = 10        //初始化线程数
  private val MAX_POOL_SIZE = 50            //最大线程池数
  private val KEEP_ALIVE_TIME = 10L         //活跃线程保留时间
  private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS

  private val workQueue: BlockingDeque<Runnable>
  private val threadPoolExecutor: ThreadPoolExecutor
  private val threadFactory: ThreadFactory

  init {
    workQueue = LinkedBlockingDeque()
    threadFactory = JobThreadFactory()
    threadPoolExecutor = ThreadPoolExecutor(
        INITIAL_POOL_SIZE,
        MAX_POOL_SIZE,
        KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT,
        this.workQueue,
        this.threadFactory
    )
  }

  override fun execute(runnable: Runnable) {
    this.threadPoolExecutor.execute(runnable)
  }

  private class JobThreadFactory : ThreadFactory {
    private var counter = 0

    override fun newThread(r: Runnable): Thread {
      return Thread(r, "android_${counter++}")
    }
  }
}
