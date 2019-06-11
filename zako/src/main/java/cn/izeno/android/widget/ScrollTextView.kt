package cn.izeno.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import cn.izeno.android.anim.InterpolationObservableAnimation
import cn.izeno.android.util.R
import cn.izeno.android.util.ZDimen
import java.util.*

/**
 * 单行文字信息纵向滚动
 *
 *
 * Create Date: 16/5/29
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class ScrollTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), Handler.Callback {

  private var _context: Context? = null
  private var _width: Int = 0
  private var _height: Int = 0
  private var paint: Paint? = null

  private val scroll = true

  //字体大小，默认 12sp
  var textSize = ZDimen.sp2px(12f)
  //字体颜色，默认黑色
  var textColor = Color.BLACK

  private var anim: InterpolationObservableAnimation? = null
  private val animListener: Animation.AnimationListener? = null

  private var interTime = 0f
  var during: Long = 1000
    set(during) {
      field = during
      anim!!.duration = during
    }
  var interval: Long = 3000

  private val _handler = Handler(this)
  private var timer: Timer? = null
  private var timerTask: TimerTask? = null

  var stringList: List<String>? = null
    set(value) {
      clearAnimation()
      field = value
    }
  private var position = 0

  var onItemClickListener: ((position: Int) -> Unit)? = null

  init {
    initView(context, attrs)
  }

  override fun onAnimationEnd() {
    super.onAnimationEnd()
    if (this.stringList != null && !this.stringList!!.isEmpty()) {
      position = (position + 1) % this.stringList!!.size
    } else {
      position = 0
    }
    interTime = 0f
    invalidate()
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    initTimer()
    startAnimation(anim)
  }


  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    clearTimer()
  }

  override fun handleMessage(msg: Message): Boolean {
    //    timer.schedule(timerTask, 0, interval);
    startAnimation(anim)
    return true
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    val width = View.MeasureSpec.getSize(widthMeasureSpec)
    var height = View.MeasureSpec.getSize(heightMeasureSpec)
    val heightModel = View.MeasureSpec.getMode(heightMeasureSpec)
    if (heightModel == View.MeasureSpec.AT_MOST) {
      height = (textSize * 1.2).toInt() + paddingBottom + paddingTop
    }
    setMeasuredDimension(width, height)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    paint!!.color = textColor
    paint!!.textSize = textSize.toFloat()
    val currStr: String
    val nextStr: String

    if (this.stringList != null && !this.stringList!!.isEmpty()) {
      currStr = this.stringList!![position]
      nextStr = this.stringList!![(position + 1) % this.stringList!!.size]
    } else {
      nextStr = "nothing to show!"
      currStr = nextStr
    }
    canvas.drawText(currStr, paddingLeft.toFloat(), ((_height + textSize * 0.8) / 2 - interTime.toDouble() * _height.toDouble() * 1.2).toFloat(), paint!!)
    canvas.drawText(nextStr, paddingLeft.toFloat(), ((_height + textSize * 0.8) / 2 - (interTime - 1) * _height).toFloat(), paint!!)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    _width = w
    _height = h
  }

  private fun initView(context: Context, attrs: AttributeSet?) {
    this._context = context
    paint = Paint()
    paint!!.isAntiAlias = true
    initAttr(attrs)
    initAnim()
  }

  private fun initAttr(attrs: AttributeSet?) {
    val ta = _context!!.obtainStyledAttributes(attrs, R.styleable.ScrollTextView)

    for (i in 0 until ta.indexCount) {
      val index = ta.getIndex(i)
      if (index == R.styleable.ScrollTextView_textColor) {
        textColor = ta.getColor(index, Color.BLACK)
      } else if (index == R.styleable.ScrollTextView_textSize) {
        textSize = ta.getDimensionPixelSize(index, 48)
      }

    }
    ta.recycle()
  }

  private fun initTimer() {
    timer = Timer()
    timerTask = object : TimerTask() {
      override fun run() {
        _handler.sendEmptyMessage(0)
      }
    }

    timer!!.schedule(timerTask, 0, interval)
  }

  /**
   * 清除计时器
   */
  private fun clearTimer() {
    if (timerTask != null) {
      timerTask!!.cancel()
      timerTask = null
      timer = null
    }
  }

  /**
   * 初试化动画，通过插值动画，重绘 View
   */
  private fun initAnim() {
    anim = InterpolationObservableAnimation()
    anim?.duration = this.during
    anim?.interpolator = FastOutSlowInInterpolator()
    anim?.onInterpolatedListener = { interpolatedTime ->
      interTime = interpolatedTime
      invalidate()
    }
  }
}

