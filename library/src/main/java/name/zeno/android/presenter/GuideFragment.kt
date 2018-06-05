package name.zeno.android.presenter


import android.graphics.Color
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import kale.adapter.BasePagerAdapter
import kotlinx.android.synthetic.main.fragment_guide.view.*
import name.zeno.android.util.R
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * 简单的引导页, 使用 [.newInstance] 新建 fragment
 */
class GuideFragment : ZFragment() {
  var onStart: (() -> Unit)? = null

  private var resIds: IntArray? = null
  private lateinit var fragmentView: View

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    fragmentView = inflater.inflate(R.layout.fragment_guide, container, false)
    return fragmentView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(fragmentView) {
    super.onViewCreated(view, savedInstanceState)


    btn_start.onClick { onStart?.invoke() }
    btn_skip.onClick { onStart?.invoke() }

    indicator.fillColor = Color.parseColor("#d82c3b")
    indicator.strokeColor = Color.parseColor("#d82c3b")
    indicator.pageColor = Color.TRANSPARENT

    resIds = arguments?.getIntArray(PARAM_RES_IDS)

    pager_guide.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (position == resIds!!.size - 1) {
          btn_start.visibility = View.VISIBLE
          btn_start.alpha = 1f
        } else if (position == resIds!!.size - 2) {
          btn_start.visibility = View.VISIBLE
          btn_start.alpha = positionOffset
        } else {
          btn_start.visibility = View.GONE
        }
      }

      override fun onPageSelected(position: Int) {}

      override fun onPageScrollStateChanged(state: Int) {}
    })
    pager_guide.adapter = object : BasePagerAdapter<AppCompatImageView>() {
      override fun getCount() = resIds?.size ?: 0

      override fun isViewFromObject(view: View, any: Any) = view === any

      override fun createItem(viewPager: ViewGroup, position: Int): AppCompatImageView {
        val v = AppCompatImageView(view.context)
        v.layoutParams = LinearLayout.LayoutParams(-1, -1)
        v.scaleType = ImageView.ScaleType.CENTER_CROP
        return v
      }

      override fun getViewFromItem(item: AppCompatImageView, position: Int): View {
        (item as ImageView).setImageDrawable(ContextCompat.getDrawable(view.context, resIds!![position]))
        return item
      }
    }

    indicator.setViewPager(pager_guide)
    indicator.isSnap = true
  }


  companion object {
    val PARAM_RES_IDS = "param_res_ids"

    fun newInstance(@DrawableRes vararg resIds: Int): GuideFragment {
      val args = Bundle()
      args.putIntArray(PARAM_RES_IDS, resIds)
      val fragment = GuideFragment()
      fragment.arguments = args
      return fragment
    }
  }
}
