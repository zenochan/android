package cn.izeno.android.camera

import android.hardware.Camera
import android.os.AsyncTask
import android.util.Log

import java.util.Arrays
import java.util.concurrent.RejectedExecutionException

import cn.izeno.android.util.ZLog

/**
 * 该类提取自 Zxing, 功能是每隔 2s 进行自动对焦
 * Create Date: 16/6/18
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class AutoFocusManager(private val camera: Camera) : Camera.AutoFocusCallback {
  private var stopped: Boolean = false
  private var focusing: Boolean = false
  private val useAutoFucus: Boolean
  private var outstandingTask: AsyncTask<*, *, *>? = null


  init {
    val currentFocusMode = camera.parameters.focusMode
    useAutoFucus = FOCUS_MODES_CALLING_AF.contains(currentFocusMode)
    start()
  }

  /** 停止自动对焦  */
  @Synchronized
  fun stop() {
    stopped = true
    if (useAutoFucus) {
      cancelOutstandingTask()
      // Doesn't hurt to call this even if not focusing
      try {
        camera.cancelAutoFocus()
      } catch (e: RuntimeException) {
        // Have heard RuntimeException reported in Android 4.0.x+; continue?
        Log.e(TAG, "Unexpected exception while cancelling focusing", e)
      }

    }

  }


  override fun onAutoFocus(b: Boolean, camera: Camera) {
    focusing = false
    autoFocusAgainLater()
  }

  /** 延迟自动对焦  */
  @Synchronized private fun autoFocusAgainLater() {
    if (!stopped && outstandingTask == null) {
      val task = AutoFocusTask()
      try {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        outstandingTask = task
      } catch (e: RejectedExecutionException) {
        ZLog.e(TAG, "请求自动对焦失败")
      }

    }
  }

  /** 开始自动对焦  */
  @Synchronized private fun start() {
    if (useAutoFucus) {
      outstandingTask = null
      if (!stopped && !focusing) {
        try {
          camera.autoFocus(this)
          focusing = true
        } catch (e: RuntimeException) {
          // Have heard RuntimeException reported in Android 4.0.x+; continue?
          ZLog.e(TAG, "Unexpected exception while focusing", e)
          // Try again later to keep cycle going
          autoFocusAgainLater()
        }

      }
    }
  }

  @Synchronized private fun cancelOutstandingTask() {
    if (outstandingTask != null) {
      if (outstandingTask!!.status != AsyncTask.Status.FINISHED) {
        outstandingTask!!.cancel(true)
      }
      outstandingTask = null
    }
  }

  private inner class AutoFocusTask : AsyncTask<Any, Any, Any>() {
    override fun doInBackground(vararg objects: Any): Any? {
      try {
        Thread.sleep(AUTO_FOCUS_INTERVAL_MS)
      } catch (ignore: InterruptedException) {
        //继续
      }

      start()
      return null
    }
  }

  companion object {
    private val TAG = "AutoFocusManager"


    //自动对焦周期 2s
    private val AUTO_FOCUS_INTERVAL_MS = 2000L

    //对焦模式
    private val FOCUS_MODES_CALLING_AF = Arrays.asList(Camera.Parameters.FOCUS_MODE_AUTO, Camera.Parameters.FOCUS_MODE_MACRO)
  }
}
