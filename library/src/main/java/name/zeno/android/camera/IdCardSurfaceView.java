package name.zeno.android.camera;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceView;

import name.zeno.android.util.MeasureUtils;

/**
 * 重写 onMeasure 方法，使高宽比为 4:3
 * Create Date: 16/6/19
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class IdCardSurfaceView extends SurfaceView
{
  public IdCardSurfaceView(Context context)
  {
    this(context, null);
  }

  public IdCardSurfaceView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public IdCardSurfaceView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    Point size = MeasureUtils.as4Rate3(widthMeasureSpec, heightMeasureSpec);
    setMeasuredDimension(size.x, size.y);
  }
}
