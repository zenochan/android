package name.zeno.android.widget

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

import name.zeno.android.tint.TintHelper
import name.zeno.android.tint.TintInfo
import name.zeno.android.tint.TintableCompoundDrawableView
import name.zeno.android.util.R

/**
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class ZTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr),
    TintableCompoundDrawableView {
  private val compoundDrawableTint: TintInfo

  internal var drawableGravity = -1
  internal var drawableWidth = -1
  internal var drawableHeight = -1
  internal var LeftDrawableWidth = -1
  internal var LeftDrawableHeight = -1
  internal var TopDrawableWidth = -1
  internal var TopDrawableHeight = -1
  internal var RightDrawableWidth = -1
  internal var RightDrawableHeight = -1
  internal var BottomDrawableWidth = -1
  internal var BottomDrawableHeight = -1

  override var supportCompoundDrawableTintList: ColorStateList?
    get() = TintHelper.getSupportTintList(compoundDrawableTint)
    set(tint) = TintHelper.setSupportCompoundDrawableTintList(this, compoundDrawableTint, tint!!)

  override var supportCompoundDrawableTintMode: PorterDuff.Mode?
    get() = TintHelper.getSupportTintMode(compoundDrawableTint)
    set(tintMode) = TintHelper.setSupportTintMode(this, compoundDrawableTint, tintMode!!)

  init {
    compoundDrawableTint = TintInfo()
    compoundDrawableTint.hasTintList = true

    TintHelper.loadFromAttributes(this, attrs!!, defStyleAttr,
        R.styleable.ZTextView,
        R.styleable.ZTextView_backgroundTint,
        R.styleable.ZTextView_backgroundTintMode
    )

    val ta = context.obtainStyledAttributes(attrs, R.styleable.ZTextView, defStyleAttr, 0)
    if (ta.hasValue(R.styleable.ZTextView_compoundDrawableTint)) {
      val c = ta.getColorStateList(R.styleable.ZTextView_compoundDrawableTint)
      compoundDrawableTint.tintList = c
      TintHelper.setSupportCompoundDrawableTintList(this, compoundDrawableTint, c!!)
    }
    initCompoundDrawableSize(ta)
    ta.recycle()
  }

  override fun setSelected(selected: Boolean) {
    super.setSelected(selected)
    supportCompoundDrawableTintList = supportCompoundDrawableTintList
  }

  private fun getBitmap(drawable: Drawable): Bitmap {
    val bd = drawable as BitmapDrawable
    return bd.bitmap
  }

  private fun initCompoundDrawableSize(tArray: TypedArray) {
    //遍历参数
    drawableGravity = tArray.getInt(R.styleable.ZTextView_drawableGravity, -1)
    drawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_drawableWidth, -1)
    drawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_drawableHeight, -1)
    LeftDrawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_LeftDrawableWidth, -1)
    LeftDrawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_LeftDrawableHeight, -1)
    TopDrawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_TopDrawableWidth, -1)
    TopDrawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_TopDrawableHeight, -1)
    RightDrawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_RightDrawableWidth, -1)
    RightDrawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_RightDrawableHeight, -1)
    BottomDrawableWidth = tArray.getDimensionPixelSize(R.styleable.ZTextView_BottomDrawableWidth, -1)
    BottomDrawableHeight = tArray.getDimensionPixelSize(R.styleable.ZTextView_BottomDrawableHeight, -1)


    // 获取各个方向的图片，按照：l-t-r-b(左-上-右-下) 的顺序存于数组中
    val drawables = compoundDrawables
    for (i in drawables.indices) {
      setImageSize(drawables[i], i)
    }
    // 将图片放回到TextView中
    setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3])
  }

  private fun setImageSize(drawable: Drawable?, position: Int) {
    if (null != drawable) {
      var width = -1
      var height = -1
      // 0-left; 1-top; 2-right; 3-bottom;
      when (position) {
        0 -> {
          width = LeftDrawableWidth
          height = LeftDrawableHeight
        }
        1 -> {
          width = RightDrawableWidth
          height = RightDrawableHeight
        }
        2 -> {
          width = TopDrawableWidth
          height = TopDrawableHeight
        }
        3 -> {
          width = BottomDrawableWidth
          height = BottomDrawableHeight
        }
      }
      if (width == -1) {
        width = drawableWidth
      }
      if (height == -1) {
        height = drawableHeight
      }
      if (width != -1 && height != -1) {
        val offset = 20
        drawable.setBounds(0, 0, width, height)
      }
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }

  override fun onDraw(canvas: Canvas) {
    val drawables = compoundDrawables
    when (drawableGravity) {
      0//l
      -> {
        drawableAtLeft(drawables[1])
        drawableAtLeft(drawables[3])
      }
      1//t
      -> {
        drawableAtTop(drawables[0])
        drawableAtTop(drawables[2])
      }
      2//r
      -> {
        drawableAtRight(drawables[1])
        drawableAtRight(drawables[3])
      }
      3//b
      -> {
        drawableAtBottom(drawables[0])
        drawableAtBottom(drawables[2])
      }
    }
    super.onDraw(canvas)
  }

  private fun drawableAtTop(drawable: Drawable?) {
    if (drawable != null) {
      val bounds = drawable.bounds
      val height = bounds.bottom - bounds.top
      bounds.top = (height - getHeight()) / 2
      bounds.bottom = bounds.top + height
    }
  }

  private fun drawableAtBottom(drawable: Drawable?) {
    if (drawable != null) {
      val bounds = drawable.bounds
      val height = bounds.bottom - bounds.top
      bounds.top = (-height + getHeight()) / 2
      bounds.bottom = bounds.top + height
    }
  }


  private fun drawableAtLeft(drawable: Drawable?) {
    if (drawable != null) {
      val bounds = drawable.bounds
      val width = bounds.right - bounds.left
      bounds.left = (width - getWidth()) / 2
      bounds.right = bounds.left + width
    }
  }


  private fun drawableAtRight(drawable: Drawable?) {
    if (drawable != null) {
      val bounds = drawable.bounds
      val width = bounds.right - bounds.left
      bounds.left = (-width + getWidth()) / 2
      bounds.right = bounds.left + width
    }
  }
}
