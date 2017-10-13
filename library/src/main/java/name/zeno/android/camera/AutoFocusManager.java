package name.zeno.android.camera;

import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.RejectedExecutionException;

import name.zeno.android.util.ZLog;

/**
 * 该类提取自 Zxing, 功能是每隔 2s 进行自动对焦
 * Create Date: 16/6/18
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class AutoFocusManager implements Camera.AutoFocusCallback
{
  private static final String TAG = "AutoFocusManager";


  //自动对焦周期 2s
  private static final long AUTO_FOCUS_INTERVAL_MS = 2000L;

  //对焦模式
  private static final Collection<String> FOCUS_MODES_CALLING_AF =
      Arrays.asList(Camera.Parameters.FOCUS_MODE_AUTO, Camera.Parameters.FOCUS_MODE_MACRO);
  private       boolean            stopped;
  private       boolean            focusing;
  private final boolean            useAutoFucus;
  private       Camera             camera;
  private       AsyncTask<?, ?, ?> outstandingTask;


  public AutoFocusManager(Camera camera)
  {
    this.camera = camera;
    String currentFocusMode = camera.getParameters().getFocusMode();
    useAutoFucus = FOCUS_MODES_CALLING_AF.contains(currentFocusMode);
    start();
  }

  /** 停止自动对焦 */
  public synchronized void stop()
  {
    stopped = true;
    if (useAutoFucus) {
      cancelOutstandingTask();
      // Doesn't hurt to call this even if not focusing
      try {
        camera.cancelAutoFocus();
      } catch (RuntimeException e) {
        // Have heard RuntimeException reported in Android 4.0.x+; continue?
        Log.e(TAG, "Unexpected exception while cancelling focusing", e);
      }
    }

  }


  @Override public void onAutoFocus(boolean b, Camera camera)
  {
    focusing = false;
    autoFocusAgainLater();
  }

  /** 延迟自动对焦 */
  private synchronized void autoFocusAgainLater()
  {
    if (!stopped && outstandingTask == null) {
      AutoFocusTask task = new AutoFocusTask();
      try {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        outstandingTask = task;
      } catch (RejectedExecutionException e) {
        ZLog.e(TAG, "请求自动对焦失败");
      }
    }
  }

  /** 开始自动对焦 */
  private synchronized void start()
  {
    if (useAutoFucus) {
      outstandingTask = null;
      if (!stopped && !focusing) {
        try {
          camera.autoFocus(this);
          focusing = true;
        } catch (RuntimeException e) {
          // Have heard RuntimeException reported in Android 4.0.x+; continue?
          ZLog.e(TAG, "Unexpected exception while focusing", e);
          // Try again later to keep cycle going
          autoFocusAgainLater();
        }
      }
    }
  }

  private synchronized void cancelOutstandingTask()
  {
    if (outstandingTask != null) {
      if (outstandingTask.getStatus() != AsyncTask.Status.FINISHED) {
        outstandingTask.cancel(true);
      }
      outstandingTask = null;
    }
  }

  private final class AutoFocusTask extends AsyncTask<Object, Object, Object>
  {
    @Override protected Object doInBackground(Object... objects)
    {
      try {
        Thread.sleep(AUTO_FOCUS_INTERVAL_MS);
      } catch (InterruptedException ignore) {
        //继续
      }
      start();
      return null;
    }
  }
}
