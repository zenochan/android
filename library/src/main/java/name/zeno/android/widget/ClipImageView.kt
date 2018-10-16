package name.zeno.android.widget


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class ClipImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs),
    OnScaleGestureListener,
    ViewTreeObserver.OnGlobalLayoutListener {

  /** 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于0  */
  private var initScale = 1.0f
  private var once = true

  /** 缩放的手势检测  */
  private var mScaleGestureDetector = ScaleGestureDetector(context, this)
  /** 用于双击检测  */
  private val mGestureDetector: GestureDetector

  /** 用于存放矩阵的9个值  */
  private val matrixValues = FloatArray(9)
  private val mScaleMatrix = Matrix()

  private var isAutoScale: Boolean = false

  private val mTouchSlop: Int = 0

  private var mLastX: Float = 0F
  private var mLastY: Float = 0F

  /** 水平方向与View的边距  */
  private var paddingHorizontal: Int = 0//——屏幕边缘离截图区的宽度
  /** 垂直方向与View的边距  */
  private var paddingVertical: Int = 0//——屏幕顶部离截图区的高度


  private var dragEnable: Boolean = false
  private var lastPointerCount: Int = 0

  /** 根据当前图片的Matrix获得图片的范围  */
  private val matrixRectF: RectF
    get() {
      val matrix = mScaleMatrix
      val rect = RectF()
      val d = drawable
      if (null != d) {
        rect.set(0f, 0f, d.intrinsicWidth.toFloat(), d.intrinsicHeight.toFloat())
        matrix.mapRect(rect)
      }
      return rect
    }

  /**
   * 获得当前的缩放比例
   */
  val scale: Float
    get() {
      mScaleMatrix.getValues(matrixValues)
      return matrixValues[Matrix.MSCALE_X]
    }

  init {

    scaleType = ImageView.ScaleType.MATRIX
    mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
      override fun onDoubleTap(e: MotionEvent): Boolean {
        if (isAutoScale == true)
          return true

        val x = e.x
        val y = e.y
        if (scale < SCALE_MID) {
          this@ClipImageView.postDelayed(
              AutoScaleRunnable(SCALE_MID, x, y), 16)
          isAutoScale = true
        } else {
          this@ClipImageView.postDelayed(
              AutoScaleRunnable(initScale, x, y), 16)
          isAutoScale = true
        }

        return true
      }
    })
  }

  /**
   * 自动缩放的任务
   *
   * @author zhy
   */
  private inner class AutoScaleRunnable
  /** 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小  */
  (private val mTargetScale: Float,
   /** 缩放的中心  */
   private val x: Float, private val y: Float) : Runnable {
    private var tmpScale: Float = 0.toFloat()

    init {
      if (scale < mTargetScale) {
        tmpScale = BIGGER
      } else {
        tmpScale = SMALLER
      }

    }

    override fun run() {
      // 进行缩放
      mScaleMatrix.postScale(tmpScale, tmpScale, x, y)
      checkBorder()
      imageMatrix = mScaleMatrix

      val currentScale = scale
      // 如果值在合法范围内，继续缩放
      if (tmpScale > 1f && currentScale < mTargetScale || tmpScale < 1f && mTargetScale < currentScale) {
        this@ClipImageView.postDelayed(this, 16)
      } else
      // 设置为目标的缩放比例
      {
        val deltaScale = mTargetScale / currentScale
        mScaleMatrix.postScale(deltaScale, deltaScale, x, y)
        checkBorder()
        imageMatrix = mScaleMatrix
        isAutoScale = false
      }

    }

  }

  override fun onScale(detector: ScaleGestureDetector): Boolean {
    val scale = scale
    var scaleFactor = detector.scaleFactor

    if (drawable == null)
      return true

    // 缩放的范围控制
    if (scale < SCALE_MAX && scaleFactor > 1.0f || scale > initScale && scaleFactor < 1.0f) {
      // 最大值最小值判断
      if (scaleFactor * scale < initScale) {
        scaleFactor = initScale / scale
      }
      if (scaleFactor * scale > SCALE_MAX) {
        scaleFactor = SCALE_MAX / scale
      }
      // 设置缩放比例
      mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.focusX, detector.focusY)
      checkBorder()
      imageMatrix = mScaleMatrix
    }
    return true

  }

  override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
    return true
  }

  override fun onScaleEnd(detector: ScaleGestureDetector) {}

  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (mGestureDetector.onTouchEvent(event))
      return true
    mScaleGestureDetector!!.onTouchEvent(event)

    var x = 0f
    var y = 0f
    // 拿到触摸点的个数
    val pointerCount = event.pointerCount
    // 得到多个触摸点的x与y均值
    for (i in 0 until pointerCount) {
      x += event.getX(i)
      y += event.getY(i)
    }
    x = x / pointerCount
    y = y / pointerCount

    // 每当触摸点发生变化时，重置mLasX , mLastY
    if (pointerCount != lastPointerCount) {
      dragEnable = false
      mLastX = x
      mLastY = y
    }

    lastPointerCount = pointerCount
    when (event.action) {
      MotionEvent.ACTION_MOVE -> {
        var dx = x - mLastX
        var dy = y - mLastY

        if (!dragEnable) {
          dragEnable = isCanDrag(dx, dy)
        }
        if (dragEnable) {
          if (drawable != null) {

            val rectF = matrixRectF
            // 如果宽度小于屏幕宽度，则禁止左右移动
            if (rectF.width() <= width - paddingHorizontal * 2) {
              dx = 0f
            }
            // 如果高度小雨屏幕高度，则禁止上下移动
            if (rectF.height() <= height - paddingVertical * 2) {
              dy = 0f
            }
            mScaleMatrix.postTranslate(dx, dy)
            checkBorder()
            imageMatrix = mScaleMatrix
          }
        }
        mLastX = x
        mLastY = y
      }

      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> lastPointerCount = 0
    }

    return true
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    viewTreeObserver.addOnGlobalLayoutListener(this)
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    viewTreeObserver.removeGlobalOnLayoutListener(this)
  }

  override fun onGlobalLayout() {
    if (once) {
      val d = drawable ?: return

      val w = width
      val h = height

      // 垂直方向的边距
      paddingVertical = (h - (w - 2 * paddingHorizontal)) / 2

      // 拿到图片的宽和高
      val dw = d.intrinsicWidth
      val dh = d.intrinsicHeight
      val scaleW = (w - paddingHorizontal * 2).toFloat() / dw
      val scaleH = (h - paddingVertical * 2).toFloat() / dh
      initScale = Math.max(scaleW, scaleH)

      SCALE_MID = initScale * 2
      SCALE_MAX = initScale * 4
      mScaleMatrix.postTranslate(((w - dw) / 2).toFloat(), ((h - dh) / 2).toFloat())//平移至屏幕中心
      mScaleMatrix.postScale(initScale, initScale, (w / 2).toFloat(), (h / 2).toFloat())//设置缩放比例
      // 图片移动至屏幕中心
      imageMatrix = mScaleMatrix
      once = false
    }

  }

  /**
   * 剪切图片，返回剪切后的bitmap对象
   */
  fun clip(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return Bitmap.createBitmap(bitmap, paddingHorizontal,
        paddingVertical, width - 2 * paddingHorizontal,
        width - 2 * paddingHorizontal)
  }

  /**
   * 边界检测
   */
  private fun checkBorder() {
    val rect = matrixRectF
    var deltaX = 0f
    var deltaY = 0f

    val width = width
    val height = height
    // 如果宽或高大于屏幕，则控制范围 ; 这里的0.001是因为精度丢失会产生问题，但是误差一般很小，所以我们直接加了一个0.01
    if (rect.width() + 0.01 >= width - 2 * paddingHorizontal) {
      if (rect.left > paddingHorizontal) {
        deltaX = -rect.left + paddingHorizontal
      }
      if (rect.right < width - paddingHorizontal) {
        deltaX = width.toFloat() - paddingHorizontal.toFloat() - rect.right
      }
    }
    if (rect.height() + 0.01 >= height - 2 * paddingVertical) {
      if (rect.top > paddingVertical) {
        deltaY = -rect.top + paddingVertical
      }
      if (rect.bottom < height - paddingVertical) {
        deltaY = height.toFloat() - paddingVertical.toFloat() - rect.bottom
      }
    }
    mScaleMatrix.postTranslate(deltaX, deltaY)

  }

  /**
   * 是否是拖动行为
   */
  private fun isCanDrag(dx: Float, dy: Float): Boolean {
    return Math.sqrt((dx * dx + dy * dy).toDouble()) >= mTouchSlop
  }

  fun setHorizontalPadding(mHorizontalPadding: Int) {
    this.paddingHorizontal = mHorizontalPadding
  }

  companion object {

    var SCALE_MAX = 4f
    private var SCALE_MID = 2f

    internal val BIGGER = 1.07f
    internal val SMALLER = 0.93f

  }

}
