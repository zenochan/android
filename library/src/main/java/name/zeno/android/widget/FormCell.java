package name.zeno.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import name.zeno.android.listener.Action0;
import name.zeno.android.util.R;
import name.zeno.android.util.R2;


/**
 * 封装 label ，content， next箭头
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class FormCell extends LinearLayout
{
  private View root;

  @Getter @BindView(R2.id.tv_label)   ZTextView         labelTv;
  @Getter @BindView(R2.id.et_content) AppCompatTextView contentTv;
  @Getter @BindView(R2.id.iv_next)    ZImageView        nextIv;

  public FormCell(Context context)
  {
    this(context, null);
  }

  public FormCell(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public FormCell(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    root = LayoutInflater.from(context).inflate(R.layout.view_form_cell, this);
    ButterKnife.bind(this, root);
    initAttr(context, attrs);
  }

  public void setText(String content)
  {
    contentTv.setText(content);
  }

  public void setText(@StringRes int res)
  {
    contentTv.setText(res);
  }

  public void setTextLabel(String label)
  {
    labelTv.setText(label);
  }

  //设置 label 文字
  public void setTextLabel(@StringRes int res)
  {
    labelTv.setText(res);
  }

  public void setTextSizeLabel(float sp)
  {
    labelTv.setTextSize(Dimension.SP, sp);
  }

  public String getText()
  {
    return contentTv.getText().toString();
  }

  public String getTextLabel()
  {
    return labelTv.getText().toString();
  }

  public void setHint(String hint)
  {
    contentTv.setHint(hint);
  }

  public void setNextIvRes(@DrawableRes int resId)
  {
    nextIv.setImageResource(resId);
  }

  public void setNextEnable(boolean enable)
  {
    nextIv.setVisibility(enable ? VISIBLE : INVISIBLE);
  }

  public void setOnClickNextListener(Action0 onClick)
  {
    if (onClick == null) {
      nextIv.setOnClickListener(null);
    } else {
      nextIv.setOnClickListener(view -> {
        onClick.call();
      });
    }
  }

  private void initAttr(Context context, AttributeSet attrs)
  {
    setBackgroundResource(R.drawable.z_bg_white_selector);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FormCell);
    if (ta.hasValue(R.styleable.FormCell_labelDrawableLeft)) {
      Drawable drawable = ta.getDrawable(R.styleable.FormCell_labelDrawableLeft);
      assert drawable != null;
      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
      labelTv.setCompoundDrawables(drawable, null, null, null);
    }

    if (ta.hasValue(R.styleable.FormCell_labelDrawablePadding)) {
      int padding = ta.getDimensionPixelSize(R.styleable.FormCell_labelDrawablePadding, 0);
      labelTv.setCompoundDrawablePadding(padding);
    }

    if (ta.hasValue(R.styleable.FormCell_labelDrawableTint)) {
      ColorStateList c = ta.getColorStateList(R.styleable.FormCell_labelDrawableTint);
      labelTv.setSupportCompoundDrawableTintList(c);
    }

    if (ta.hasValue(R.styleable.FormCell_labelText)) {
      labelTv.setText(ta.getString(R.styleable.FormCell_labelText));
    }
    if (ta.hasValue(R.styleable.FormCell_contentText)) {
      contentTv.setText(ta.getString(R.styleable.FormCell_contentText));
    }
    if (ta.hasValue(R.styleable.FormCell_contentTextColor)) {
      contentTv.setTextColor(ta.getColorStateList(R.styleable.FormCell_contentTextColor));
    }
    if (ta.hasValue(R.styleable.FormCell_contentHint)) {
      contentTv.setHint(ta.getString(R.styleable.FormCell_contentHint));
    }

    if (ta.hasValue(R.styleable.FormCell_srcNext)) {
      nextIv.setImageDrawable(ta.getDrawable(R.styleable.FormCell_srcNext));
    }
    if (ta.hasValue(R.styleable.FormCell_nextTint)) {
      nextIv.setSupportDrawableTintList(ta.getColorStateList(R.styleable.FormCell_nextTint));
    } else {
      nextIv.setSupportDrawableTintList(new ColorStateList(new int[][]{{}},
          new int[]{Color.parseColor("#9e9e9e")}));
    }

    nextIv.setVisibility(ta.getBoolean(R.styleable.FormCell_nextEnable, true) ? VISIBLE : INVISIBLE);
    ta.recycle();
  }

}
