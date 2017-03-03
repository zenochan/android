package name.zeno.android.presenter;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.Setter;
import name.zeno.android.listener.Action0;
import name.zeno.android.util.R;
import name.zeno.android.util.R2;
import kale.adapter.BasePagerAdapter;
import name.zeno.android.widget.viewpagerindicator.CirclePageIndicator;

/**
 * 简单的引导页, 使用 {@link #newInstance(int...)} 新建 fragment
 */
public class GuideFragment extends ZFragment
{
  public static final String PARAM_RES_IDS = "param_res_ids";

  @BindView(R2.id.pager_guide) ViewPager           pagerGuide;
  @BindView(R2.id.btn_start)   AppCompatButton     btnStart;
  @BindView(R2.id.indicator)   CirclePageIndicator indicator;

  @Setter
  private Action0 onStartListener;

  private int[] resIds;

  public static GuideFragment newInstance(@DrawableRes int... resIds)
  {
    Bundle args = new Bundle();
    args.putIntArray(PARAM_RES_IDS, resIds);
    GuideFragment fragment = new GuideFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View view = inflater.inflate(R.layout.fragment_guide, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
  {
    super.onViewCreated(view, savedInstanceState);

    indicator.setFillColor(Color.parseColor("#d82c3b"));
    indicator.setStrokeColor(Color.parseColor("#d82c3b"));
    indicator.setPageColor(Color.TRANSPARENT);

    resIds = getArguments().getIntArray(PARAM_RES_IDS);

    pagerGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
    {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
      {
        if (position == resIds.length - 1) {
          btnStart.setVisibility(View.VISIBLE);
          btnStart.setAlpha(1);
        } else if (position == resIds.length - 2) {
          btnStart.setVisibility(View.VISIBLE);
          btnStart.setAlpha(positionOffset);
        } else {
          btnStart.setVisibility(View.GONE);
        }
      }

      @Override public void onPageSelected(int position) { }

      @Override public void onPageScrollStateChanged(int state) { }
    });
    pagerGuide.setAdapter(new BasePagerAdapter()
    {
      @Override public int getCount()
      {
        return resIds == null ? 0 : resIds.length;
      }

      @Override public boolean isViewFromObject(View view, Object object)
      {
        return view == object;
      }

      @Override protected Object createItem(ViewGroup viewPager, int position)
      {
        ImageView v = new ImageView(view.getContext());
        v.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        v.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return v;
      }

      @NonNull @Override protected View getViewFromItem(Object item, int position)
      {
        ((ImageView) item).setImageDrawable(ContextCompat.getDrawable(view.getContext(), resIds[position]));
        return (ImageView) item;
      }
    });

    indicator.setViewPager(pagerGuide);
    indicator.setSnap(true);
  }

  @OnClick({R2.id.btn_start, R2.id.btn_skip}) void start(View v)
  {
    if (onStartListener != null) {
      onStartListener.call();
    }
  }

}
