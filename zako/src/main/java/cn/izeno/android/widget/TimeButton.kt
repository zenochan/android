package cn.izeno.android.widget

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.widget.AppCompatButton
import java.util.*

/**
 * PS: 由于发现 timer 每次 cancel() 之后不能重新schedule方法,所以计时完毕置空timer.
 * 每次开始计时的时候重新设置timer, 没想到好办法初次下策
 */
class TimeButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatButton(context, attrs, defStyleAttr), OnClickListener, Handler.Callback {

  private val preferences: SharedPreferences

  private var length = (60 * 1000).toLong()// 倒计时长度,默认60s
  private val TIME = "time"
  private val C_TIME = "cTime"
  private var mOnclickListener: OnClickListener? = null
  private var timer: Timer? = null
  private var timerTask: TimerTask? = null
  private var time: Long = 0


  var textAfterSuffix = ")重新获取"
  var textAfterPrefix = "("
  var textNormal = "获取验证码"
    set(value) {
      field = value
      text = textNormal
    }

  internal var han = Handler(this)

  init {
    preferences = context.applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    setOnClickListener(this)
  }

  override fun handleMessage(msg: Message): Boolean {
    text = textAfterPrefix + (time / 1000).toString() + textAfterSuffix
    time -= 1000
    if (time < 0) {
      super.setEnabled(true)
      text = textNormal
      clearTimer()
    }
    return true
  }

  override fun onClick(v: View) {
    mOnclickListener?.onClick(v)
    startTime()
  }

  fun startTime() {
    initTimer()
    text = textAfterPrefix + (time / 1000).toString() + textAfterSuffix
    super.setEnabled(false)
    timer?.schedule(timerTask, 0, 1000)
  }

  override fun setEnabled(enabled: Boolean) {
    if (enabled && time > 0) {
      return
    }
    super.setEnabled(enabled)
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    val t = preferences.getLong(TIME, -1)
    val cT = preferences.getLong(C_TIME, -1)
    if (t == -1L || cT == -1L)
    // 这里表示没有上次未完成的计时
      return
    val time = System.currentTimeMillis() - cT - t
    if (time <= 0) {
      initTimer()
      this.time = Math.abs(time)
      timer!!.schedule(timerTask, 0, 1000)
      text = time.toString() + textAfterSuffix
      super.setEnabled(false)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()

    //记录剩余时间
    val editor = preferences.edit()
    editor.putLong(TIME, time)
    editor.putLong(C_TIME, System.currentTimeMillis())
    editor.apply()

    clearTimer()
  }

  /**
   * 清除倒计时
   */
  fun reset() {
    post { time = -1 }
  }

  private fun initTimer() {
    time = length
    timer = Timer()
    timerTask = object : TimerTask() {
      override fun run() {
        han.sendEmptyMessage(0x01)
      }
    }
  }

  private fun clearTimer() {
    if (timerTask != null) {
      timerTask?.cancel()
      timerTask = null
    }
    if (timer != null)
      timer?.cancel()
    timer = null
  }

  override fun setOnClickListener(l: OnClickListener) {
    if (l is TimeButton) {
      super.setOnClickListener(l)
    } else {
      mOnclickListener = l
    }
  }

  /**
   * 设置到计时长度
   *
   * @param length 时间 默认毫秒
   */
  fun setLength(length: Long): TimeButton {
    this.length = length
    return this
  }

  companion object {
    val TAG = TimeButton::class.java.simpleName
  }
}
