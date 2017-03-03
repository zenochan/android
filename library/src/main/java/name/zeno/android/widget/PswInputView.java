package name.zeno.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import lombok.Setter;
import name.zeno.android.listener.Action1;
import name.zeno.android.util.ZDimen;
import name.zeno.android.util.R;

/**
 * 仿照支付宝的密码输入控件,输入固定长度的数字密码或验证码
 * <p>
 * <p>
 * <h1>API</h1>
 * <ul>
 * <li>{@link #clearResult()}:清空输入结果</li>
 * <li>{@link #setOnDragListener(OnDragListener)}:设置输入完成监听</li>
 * </ul>
 *
 * @see <a href='https://github.com/huage2580/PswInputViewDemo'>PswInputViewDemo</a>
 */
public class PswInputView extends View
{
  private InputMethodManager input;       //输入法管理
  private ArrayList<Integer> result;      //输入结果保存
  private int                count;       //密码位数

  private boolean passwordModel;
  private int     size;        //默认每一格的大小
  private Paint   borderPaint; //边界画笔
  private Paint   contentPaint;//掩盖点的画笔
  @SuppressWarnings("FieldCanBeLocal")
  private int     contentColor;    //掩盖点的颜色
  private RectF   roundRect;   //外面的圆角矩形
  private int     roundRadius; //圆角矩形的圆角程度

  @Setter
  private Action1<String> onFinishListener;//输入完成的回调

  private int colorAccent;
  private int colorDefault;
  private int colorBorder;

  public PswInputView(Context context)
  {
    this(context, null);
  }

  public PswInputView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public PswInputView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @SuppressWarnings("unused")
  public void clearResult()
  {
    result.clear();
    invalidate();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {//点击控件弹出输入键盘
      requestFocus();
      input.showSoftInput(this, InputMethodManager.SHOW_FORCED);
      return true;
    }

    return super.onTouchEvent(event);
  }

  @Override
  public void onWindowFocusChanged(boolean hasWindowFocus)
  {
    super.onWindowFocusChanged(hasWindowFocus);
    if (!hasWindowFocus) {
      input.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }
  }


  @Override
  public boolean onCheckIsTextEditor()
  {
    return true;
  }

  @Override
  public InputConnection onCreateInputConnection(EditorInfo outAttrs)
  {
    outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;//输入类型为数字
    outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
    return new MyInputConnection(this, false);
  }

  @Override
  protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
  {
    super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    if (gainFocus) {
      input.showSoftInput(this, InputMethodManager.SHOW_FORCED);
      colorBorder = colorAccent;
    } else {
      input.hideSoftInputFromInputMethod(this.getWindowToken(), 0);
      colorBorder = colorDefault;
    }
  }

  @Override
  protected void onDraw(Canvas canvas)
  {
    super.onDraw(canvas);
    final int width = getWidth() - 2;
    final int height = getHeight() - 2;

    //先画个圆角矩形
    borderPaint.setColor(colorBorder);
    roundRect.set(0, 0, width, height);
    canvas.drawRoundRect(roundRect, roundRadius, roundRadius, borderPaint);

    //画分割线
    for (int i = 1; i < count; i++) {
      final int x = i * size;
      canvas.drawLine(x, 0, x, height, borderPaint);
    }

    //画掩盖点
    //这是前面定义的变量 private ArrayList<Integer> result;//输入结果保存
    int dotRadius = size / 6;     //圆圈占格子的三分之一
    for (int i = 0; i < result.size(); i++) {
      final float x = (float) (size * (i + 0.5));
      final float y = size / 2;
      if (passwordModel) {
        canvas.drawCircle(x, y, dotRadius, contentPaint);
      } else {
        contentPaint.setTextSize(dotRadius * 2);
        float txtW = contentPaint.measureText(result.get(i).toString());
        canvas.drawText(result.get(i).toString(), x - txtW / 2, y + dotRadius * 0.75f, contentPaint);
      }
    }
  }


  @Override protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (isFocused()) {
      input.showSoftInput(this, InputMethodManager.SHOW_FORCED);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    int w = measureWidth(widthMeasureSpec);
    int h = measureHeight(heightMeasureSpec);
    int wsize = MeasureSpec.getSize(widthMeasureSpec);
    int hsize = MeasureSpec.getSize(heightMeasureSpec);
    //宽度没指定,但高度指定
    if (w == -1) {
      if (h != -1) {
        w = h * count;//宽度=高*数量
        size = h;
      } else {//两个都不知道,默认宽高
        w = size * count;
        h = size;
      }
    } else {//宽度已知
      if (h == -1) {//高度不知道
        h = w / count;
        size = h;
      }
    }
    setMeasuredDimension(Math.min(w, wsize), Math.min(h, hsize));
  }

  private void init(AttributeSet attrs)
  {
    //<editor-fold desc="初始化默认颜色">
    TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{
        R.attr.colorAccent,
        android.R.attr.textColor
    });
    colorAccent = array.getColor(0, Color.BLACK);
    //noinspection ResourceType
    colorDefault = array.getColor(1, Color.GRAY);
    contentColor = colorDefault;
    array.recycle();
    //</editor-fold>

    if (attrs != null) {
      TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PswInputView);
      if (ta.hasValue(R.styleable.PswInputView_colorBorder)) {
        colorBorder = ta.getColor(R.styleable.PswInputView_colorBorder, Color.LTGRAY);
      }
      contentColor = ta.getColor(R.styleable.PswInputView_colorContent, Color.GRAY);
      count = ta.getInt(R.styleable.PswInputView_count, 6);
      passwordModel = ta.getBoolean(R.styleable.PswInputView_passwordModel, false);
      ta.recycle();
    } else {
      contentColor = Color.GRAY;
      count = 6;//默认6位密码
    }

    this.setFocusable(true);
    this.setFocusableInTouchMode(true);
    result = new ArrayList<>();


    size = ZDimen.dp2px(30);//默认30dp一格

    //color
    borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    borderPaint.setStrokeWidth(2);
    borderPaint.setStyle(Paint.Style.STROKE);
    borderPaint.setColor(colorBorder);
    contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    contentPaint.setStrokeWidth(3);
    contentPaint.setStyle(Paint.Style.FILL);

    contentPaint.setColor(contentColor);

    roundRect = new RectF();
    roundRadius = ZDimen.dp2px(5);

    initOnKeyListener();

    input = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
  }

  private void initOnKeyListener()
  {
    super.setOnKeyListener((view, keyCode, event) -> {
      if (event.getAction() != KeyEvent.ACTION_DOWN) {
        return false;
      }

      if (event.isShiftPressed()) {//处理*#等键
        return false;
      }

      //键入数字
      if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
        if (result.size() < count) {
          result.add(keyCode - 7);
          invalidate();
          ensureFinishInput();
        }
        return true;
      }

      //键入删除
      if (keyCode == KeyEvent.KEYCODE_DEL && !result.isEmpty()) {
        result.remove(result.size() - 1);
        invalidate();
        return true;
      }


      if (keyCode == KeyEvent.KEYCODE_ENTER) {
        ensureFinishInput();
        return true;
      }
      return false;
    });
  }

  /**
   * 判断是否输入完成，输入完成后调用callback
   */
  private void ensureFinishInput()
  {
    //输入完成
    if (result.size() == count && onFinishListener != null) {
      StringBuilder sb = new StringBuilder();
      for (int i : result) {
        sb.append(i);
      }
      onFinishListener.call(sb.toString());
    }
  }

  private int measureWidth(int widthMeasureSpec)
  {
    //宽度
    int wmode = MeasureSpec.getMode(widthMeasureSpec);
    int wsize = MeasureSpec.getSize(widthMeasureSpec);
    if (wmode == MeasureSpec.AT_MOST) {//wrap_content
      return -1;
    }
    return wsize;
  }

  private int measureHeight(int heightMeasureSpec)
  {
    //高度
    int hmode = MeasureSpec.getMode(heightMeasureSpec);
    int hsize = MeasureSpec.getSize(heightMeasureSpec);
    if (hmode == MeasureSpec.AT_MOST) {//wrap_content
      return -1;
    }
    return hsize;
  }

  class MyInputConnection extends BaseInputConnection
  {
    public MyInputConnection(View targetView, boolean fullEditor)
    {
      super(targetView, fullEditor);
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition)
    {
      //这里是接受输入法的文本的，我们只处理数字，所以什么操作都不做
      return super.commitText(text, newCursorPosition);
    }

    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength)
    {
      //软键盘的删除键 DEL 无法直接监听，自己发送del事件
      if (beforeLength == 1 && afterLength == 0) {
        return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
      }
      return super.deleteSurroundingText(beforeLength, afterLength);
    }
  }
}

