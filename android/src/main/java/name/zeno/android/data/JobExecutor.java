package name.zeno.android.data;


import android.support.annotation.NonNull;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create Date: 16/1/7
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class JobExecutor implements Executor
{
  private static JobExecutor instance;

  private static final int      INITIAL_POOL_SIZE    = 10;    //初始化线程数
  private static final int      MAX_POOL_SIZE        = 50;    //最大线程池数
  private static final int      KEEP_ALIVE_TIME      = 10;    //活跃线程保留时间
  private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

  private final BlockingDeque<Runnable> workQueue;
  private final ThreadPoolExecutor      threadPoolExecutor;
  private final ThreadFactory           threadFactory;

  public static JobExecutor instance()
  {
    if (instance == null) {
      synchronized (JobExecutor.class) {
        if (instance == null) {
          instance = new JobExecutor();
        }
      }
    }

    return instance;
  }

  private JobExecutor()
  {
    workQueue = new LinkedBlockingDeque<>();
    threadFactory = new JobThreadFactory();
    threadPoolExecutor = new ThreadPoolExecutor(INITIAL_POOL_SIZE, MAX_POOL_SIZE,
        KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, this.workQueue, this.threadFactory);
  }

  @Override
  public void execute(@NonNull Runnable runnable)
  {
    this.threadPoolExecutor.execute(runnable);
  }

  private static class JobThreadFactory implements ThreadFactory
  {
    private static final String THREAD_NAME = "android_";
    private              int    counter     = 0;

    @Override
    public Thread newThread(@NonNull Runnable r)
    {
      return new Thread(r, THREAD_NAME + counter++);
    }
  }
}
