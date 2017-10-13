package name.zeno.android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import name.zeno.android.listener.Action1;
import name.zeno.android.util.R;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/11.
 */
public class SortHeaderView extends LinearLayout
{
  private List<SortFiled> sortFiledList = Collections.emptyList();
  private OnClickListener    onCLickListener;
  private Action1<SortFiled> onSort;

  private int drawableRes[] = {
      R.mipmap.ic_sortable,
      R.mipmap.ic_sorted_asc,
      R.mipmap.ic_sorted_desc
  };


  private Drawable[] drawables = new Drawable[3];

  private List<RelativeLayout> sortItems = new ArrayList<>();


  public SortHeaderView(Context context)
  {
    this(context, null);
  }

  public SortHeaderView(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
    setOrientation(HORIZONTAL);
    for (int i = 0; i < drawableRes.length; i++) {
      drawables[i] = getDrawable(drawableRes[i]);
    }
    onCLickListener = view -> {
      SortFiled filed = (SortFiled) view.getTag();
      if (filed != null) {
        onClickSort(filed);
      }
    };
  }

  public SortHeaderView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
  }

  public void setSortFiled(SortFiled... filed)
  {
    sortFiledList = Arrays.asList(filed);
    invalidate();
  }

  @Override public void invalidate()
  {
    super.invalidate();
    Context context = getContext();

    if (sortItems.size() < sortFiledList.size()) {
      for (int i = sortItems.size(); i < sortFiledList.size(); i++) {
        RelativeLayout relativeLayout = new RelativeLayout(context);

        TextView view = new TextView(getContext());
        view.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2, -1);
        rp.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(view, rp);

        relativeLayout.setOnClickListener(onCLickListener);
        sortItems.add(relativeLayout);
      }
    }

    for (SortFiled sortFiled : sortFiledList) {
      int            index = sortFiledList.indexOf(sortFiled);
      RelativeLayout rl    = sortItems.get(index);
      rl.setTag(sortFiled);
      TextView view = (TextView) rl.getChildAt(0);
      view.setCompoundDrawables(null, null, drawables[sortFiled.sort], null);
      view.setGravity(Gravity.CENTER);
      view.setText(sortFiled.name);
    }

    if (getChildCount() != sortItems.size()) {
      removeAllViews();
      for (int i = 0; i < sortFiledList.size(); i++) {
        RelativeLayout rl     = sortItems.get(i);
        LayoutParams   params = new LayoutParams(0, -1, sortFiledList.get(i).weight);
        addView(rl, params);
      }
    }
  }

  private void onClickSort(SortFiled sortFiled)
  {
    for (SortFiled filed : sortFiledList) {
      if (filed != sortFiled) {
        filed.sort = SortFiled.SortInt.NONE;
      } else {
        sortFiled.sort = sortFiled.sort != SortFiled.SortInt.ASC
            ? SortFiled.SortInt.ASC
            : SortFiled.SortInt.DESC;
      }
    }

    invalidate();
    if (onSort != null) {
      onSort.call(sortFiled);
    }
  }

  private Drawable getDrawable(@DrawableRes int res)
  {
    Drawable drawable = ContextCompat.getDrawable(getContext(), res);
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    return drawable;
  }

  public void setOnSort(Action1<SortFiled> onSort)
  {this.onSort = onSort; }

  public static class SortFiled
  {
    private String name;
    private String filed;
    @SortInt
    private int    sort;
    private float weight = 1;

    public SortFiled() {}

    public String getName()
    {return this.name;}

    public String getFiled()
    {return this.filed;}

    public int getSort()
    {return this.sort;}

    public float getWeight()
    {return this.weight;}

    public void setName(String name)
    {this.name = name; }

    public void setFiled(String filed)
    {this.filed = filed; }

    public void setSort(int sort)
    {this.sort = sort; }

    public void setWeight(float weight)
    {this.weight = weight; }

    public boolean equals(Object o)
    {
      if (o == this) return true;
      if (!(o instanceof SortFiled)) return false;
      final SortFiled other = (SortFiled) o;
      if (!other.canEqual((Object) this)) return false;
      final Object this$name  = this.getName();
      final Object other$name = other.getName();
      if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
      final Object this$filed  = this.getFiled();
      final Object other$filed = other.getFiled();
      if (this$filed == null ? other$filed != null : !this$filed.equals(other$filed)) return false;
      if (this.getSort() != other.getSort()) return false;
      if (Float.compare(this.getWeight(), other.getWeight()) != 0) return false;
      return true;
    }

    public int hashCode()
    {
      final int    PRIME  = 59;
      int          result = 1;
      final Object $name  = this.getName();
      result = result * PRIME + ($name == null ? 43 : $name.hashCode());
      final Object $filed = this.getFiled();
      result = result * PRIME + ($filed == null ? 43 : $filed.hashCode());
      result = result * PRIME + this.getSort();
      result = result * PRIME + Float.floatToIntBits(this.getWeight());
      return result;
    }

    protected boolean canEqual(Object other)
    {return other instanceof SortFiled;}

    public String toString()
    {return "SortHeaderView.SortFiled(name=" + this.getName() + ", filed=" + this.getFiled() + ", sort=" + this.getSort() + ", weight=" + this.getWeight() + ")";}

    @IntDef({SortInt.NONE, SortInt.ASC, SortInt.DESC})
    public @interface SortInt
    {
      int NONE = 0;
      int ASC  = 1;
      int DESC = 2;
    }

    public static SortFiled newInstance(String name, String filed)
    {
      return newInstance(name, filed, 1);
    }

    public static SortFiled newInstance(String name, String filed, @FloatRange(from = 1) float weight)
    {
      SortFiled f = new SortFiled();
      f.name = name;
      f.filed = filed;
      f.weight = weight;
      return f;
    }
  }

}
