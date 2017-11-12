package name.zeno.android.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import name.zeno.android.util.R
import name.zeno.android.util.ZDimen
import java.util.*

/**
 * # 仿照支付宝的密码输入控件,输入固定长度的数字密码或验证码
 *
 * * [clearResult] 清空输入结果
 * * [setOnDragListener] 设置输入完成监听
 *
 * @see [PswInputViewDemo](https://github.com/huage2580/PswInputViewDemo)
 */
class PswInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  private var input: InputMethodManager? = null       //输入法管理
  private var result: ArrayList<Int> = ArrayList()   //输入结果保存
  private var count: Int = 0       //密码位数

  private var passwordModel: Boolean = false
  private var size: Int = 0        //默认每一格的大小
  private var borderPaint: Paint? = null //边界画笔
  private var contentPaint: Paint? = null//掩盖点的画笔
  private var contentColor: Int = 0    //掩盖点的颜色
  private var roundRect: RectF? = null   //外面的圆角矩形
  private var roundRadius: Int = 0 //圆角矩形的圆角程度

  private var onFinishListener: ((String) -> Unit)? = null//输入完成的回调

  private var colorAccent: Int = 0
  private var colorDefault: Int = 0
  private var colorBorder: Int = 0

  init {
    init(attrs)
  }

  fun clearResult() {
    result.clear()
    invalidate()
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {//点击控件弹出输入键盘
      requestFocus()
      input!!.showSoftInput(this, InputMethodManager.SHOW_FORCED)
      return true
    }

    return super.onTouchEvent(event)
  }

  override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
    super.onWindowFocusChanged(hasWindowFocus)
    if (!hasWindowFocus) {
      input!!.hideSoftInputFromWindow(this.windowToken, 0)
    }
  }


  override fun onCheckIsTextEditor(): Boolean {
    return true
  }

  override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
    outAttrs.inputType = InputType.TYPE_CLASS_NUMBER//输入类型为数字
    outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE
    return MyInputConnection(this, false)
  }

  override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
    super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
    if (gainFocus) {
      input!!.showSoftInput(this, InputMethodManager.SHOW_FORCED)
      colorBorder = colorAccent
    } else {
      input!!.hideSoftInputFromInputMethod(this.windowToken, 0)
      colorBorder = colorDefault
    }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    val width = width - 2
    val height = height - 2

    //先画个圆角矩形
    borderPaint!!.color = colorBorder
    roundRect!!.set(0f, 0f, width.toFloat(), height.toFloat())
    canvas.drawRoundRect(roundRect!!, roundRadius.toFloat(), roundRadius.toFloat(), borderPaint!!)

    //画分割线
    for (i in 1 until count) {
      val x = i * size
      canvas.drawLine(x.toFloat(), 0f, x.toFloat(), height.toFloat(), borderPaint!!)
    }

    //画掩盖点
    //这是前面定义的变量 private ArrayList<Integer> result;//输入结果保存
    val dotRadius = size / 6     //圆圈占格子的三分之一
    result.forEachIndexed { i, value ->
      val x = (size * (i + 0.5)).toFloat()
      val y = (size / 2).toFloat()

      if (passwordModel) {
        canvas.drawCircle(x, y, dotRadius.toFloat(), contentPaint!!)
      } else {
        contentPaint!!.textSize = (dotRadius * 2).toFloat()
        val txtW = contentPaint!!.measureText(value.toString())
        canvas.drawText(value.toString(), x - txtW / 2, y + dotRadius * 0.75f, contentPaint!!)
      }
    }
  }


  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (isFocused) {
      input?.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    var w = measureWidth(widthMeasureSpec)
    var h = measureHeight(heightMeasureSpec)
    val wsize = View.MeasureSpec.getSize(widthMeasureSpec)
    val hsize = View.MeasureSpec.getSize(heightMeasureSpec)
    //宽度没指定,但高度指定
    if (w == -1) {
      if (h != -1) {
        w = h * count//宽度=高*数量
        size = h
      } else {//两个都不知道,默认宽高
        w = size * count
        h = size
      }
    } else {//宽度已知
      if (h == -1) {//高度不知道
        h = w / count
        size = h
      }
    }
    setMeasuredDimension(Math.min(w, wsize), Math.min(h, hsize))
  }

  @SuppressLint("ResourceType")
  private fun init(attrs: AttributeSet?) {
    //<editor-fold desc="初始化默认颜色">
    val array = context.theme.obtainStyledAttributes(intArrayOf(R.attr.colorAccent, android.R.attr.textColor))

    colorAccent = array.getColor(0, Color.BLACK)
    colorDefault = array.getColor(1, Color.GRAY)
    contentColor = colorDefault
    array.recycle()
    //</editor-fold>

    if (attrs != null) {
      val ta = context.obtainStyledAttributes(attrs, R.styleable.PswInputView)
      if (ta.hasValue(R.styleable.PswInputView_colorBorder)) {
        colorBorder = ta.getColor(R.styleable.PswInputView_colorBorder, Color.LTGRAY)
      }
      contentColor = ta.getColor(R.styleable.PswInputView_colorContent, Color.GRAY)
      count = ta.getInt(R.styleable.PswInputView_count, 6)
      passwordModel = ta.getBoolean(R.styleable.PswInputView_passwordModel, false)
      ta.recycle()
    } else {
      contentColor = Color.GRAY
      count = 6//默认6位密码
    }

    this.isFocusable = true
    this.isFocusableInTouchMode = true
    result


    size = ZDimen.dp2px(30f)//默认30dp一格

    //color
    borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    borderPaint!!.strokeWidth = 2f
    borderPaint!!.style = Paint.Style.STROKE
    borderPaint!!.color = colorBorder
    contentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    contentPaint!!.strokeWidth = 3f
    contentPaint!!.style = Paint.Style.FILL

    contentPaint!!.color = contentColor

    roundRect = RectF()
    roundRadius = ZDimen.dp2px(5f)

    initOnKeyListener()

    input = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  }

  private fun initOnKeyListener() {
    super.setOnKeyListener listener@ { view, keyCode, event ->
      if (event.action != KeyEvent.ACTION_DOWN) {
        return@listener false
      }

      if (event.isShiftPressed) {//处理*#等键
        return@listener false
      }

      //键入数字
      if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
        if (result.size < count) {
          result.add(keyCode - 7)
          invalidate()
          ensureFinishInput()
        }
        return@listener true
      }

      //键入删除
      if (keyCode == KeyEvent.KEYCODE_DEL && !result!!.isEmpty()) {
        result.removeAt(result.size - 1)
        invalidate()
        return@listener true
      }


      if (keyCode == KeyEvent.KEYCODE_ENTER) {
        ensureFinishInput()
        return@listener true
      }
      false
    }
  }

  /**
   * 判断是否输入完成，输入完成后调用callback
   */
  private fun ensureFinishInput() {
    //输入完成
    if (result.size == count && onFinishListener != null) {
      onFinishListener?.invoke(result.joinToString(""))
    }
  }

  private fun measureWidth(widthMeasureSpec: Int): Int {
    //宽度
    val wmode = View.MeasureSpec.getMode(widthMeasureSpec)
    val wsize = View.MeasureSpec.getSize(widthMeasureSpec)
    return if (wmode == View.MeasureSpec.AT_MOST) {//wrap_content
      -1
    } else wsize
  }

  private fun measureHeight(heightMeasureSpec: Int): Int {
    //高度
    val hmode = View.MeasureSpec.getMode(heightMeasureSpec)
    val hsize = View.MeasureSpec.getSize(heightMeasureSpec)
    return if (hmode == View.MeasureSpec.AT_MOST) {//wrap_content
      -1
    } else hsize
  }

  fun setOnFinishListener(onFinishListener: (String) -> Unit) {
    this.onFinishListener = onFinishListener
  }

  internal inner class MyInputConnection(targetView: View, fullEditor: Boolean) : BaseInputConnection(targetView, fullEditor) {

    override fun commitText(text: CharSequence, newCursorPosition: Int): Boolean {
      //这里是接受输入法的文本的，我们只处理数字，所以什么操作都不做
      return super.commitText(text, newCursorPosition)
    }

    override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
      //软键盘的删除键 DEL 无法直接监听，自己发送del事件
      return if (beforeLength == 1 && afterLength == 0) {
        super.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) && super.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL))
      } else super.deleteSurroundingText(beforeLength, afterLength)
    }
  }
}

