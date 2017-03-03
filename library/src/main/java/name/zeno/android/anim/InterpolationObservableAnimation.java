package name.zeno.android.anim;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 允许监听动画时间插值
 * <p>
 * Create Date: 15/10/22
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class InterpolationObservableAnimation extends Animation
{
  public interface OnInterpolatedListener
  {
    void onInterpolated(float interpolatedTime);
  }

  private OnInterpolatedListener onInterpolatedListener;

  public OnInterpolatedListener getOnInterpolatedListener()
  {
    return onInterpolatedListener;
  }

  public void setOnInterpolatedListener(OnInterpolatedListener onInterpolatedListener)
  {
    this.onInterpolatedListener = onInterpolatedListener;
  }

  @Override
  protected void applyTransformation(float interpolatedTime, Transformation t)
  {
    super.applyTransformation(interpolatedTime, t);
    if (onInterpolatedListener != null) {
      onInterpolatedListener.onInterpolated(interpolatedTime);
    }
  }
}
