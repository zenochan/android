package name.zeno.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import name.zeno.android.util.R;

/**
 * Create Date: 16/7/1
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class BadgeView extends LinearLayout
{
  View              v;
  ZTextView         labelTv;
  AppCompatTextView badgeTv;

  public BadgeView(Context context)
  {
    this(context, null);
  }

  public BadgeView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public BadgeView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    v = inflate(context, R.layout.view_badge, this);
    labelTv = (ZTextView) v.findViewById(R.id.tv_label);
    badgeTv = (AppCompatTextView) findViewById(R.id.tv_badge);

    initAttr(context, attrs);
  }

  public void setBadge(@IntRange(from = 0) int count)
  {
    if (count < 99) {
      badgeTv.setText(String.valueOf(count));
    } else {
      badgeTv.setText("99+");
    }
    badgeTv.setVisibility(count > 0 ? VISIBLE : GONE);
  }

  private void initAttr(Context context, AttributeSet attributeSet)
  {

    TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BadgeView);
    if (ta.hasValue(R.styleable.BadgeView_drawableTop)) {
      Drawable drawable = ta.getDrawable(R.styleable.BadgeView_drawableTop);
      assert drawable != null;
      drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
      labelTv.setCompoundDrawables(null, drawable, null, null);
    }

    if (ta.hasValue(R.styleable.BadgeView_compoundDrawableTint)) {
      ColorStateList tint = ta.getColorStateList(R.styleable.BadgeView_compoundDrawableTint);
      labelTv.setSupportCompoundDrawableTintList(tint);
    }

    if (ta.hasValue(R.styleable.BadgeView_textLabel)) {
      String s = ta.getString(R.styleable.BadgeView_textLabel);
      labelTv.setText(s);
    }

    ta.recycle();
  }
}
