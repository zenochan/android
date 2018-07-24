package name.zeno.android.widget.sort

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import name.zeno.android.core.drawable
import name.zeno.android.util.R
import java.util.*

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/11.
 */
class SortHeaderView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : LinearLayout(context, attrs, defStyleAttr) {
  private var sortFiledList = emptyList<SortField>()
  private lateinit var onCLickListener: View.OnClickListener
  private var onSort: ((SortField) -> Unit)? = null

  private val drawableRes = intArrayOf(R.mipmap.ic_sortable, R.mipmap.ic_sorted_asc, R.mipmap.ic_sorted_desc)
  private var drawables = arrayOfNulls<Drawable>(3)

  private val sortItems = ArrayList<RelativeLayout>()

  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {
    orientation = LinearLayout.HORIZONTAL
    for (i in drawableRes.indices) {
      drawables[i] = getDrawable(drawableRes[i])
    }
    onCLickListener = View.OnClickListener { view ->
      val filed = view.tag as? SortField
      if (filed != null) {
        onClickSort(filed)
      }
    }
  }

  fun setDrawables(ascDrawable: Drawable, descDrawable: Drawable, noneDrawable: Drawable) {
    drawables = arrayOf(noneDrawable, ascDrawable, descDrawable)
  }

  fun setDrawables(@DrawableRes ascRes: Int, @DrawableRes descRes: Int, @DrawableRes noneRes: Int) {
    drawables = arrayOf(noneRes, ascRes, descRes).map { getDrawable(it) }.toTypedArray()
  }

  fun setSortField(vararg filed: SortField) {
    sortFiledList = Arrays.asList(*filed)
    invalidate()
  }

  override fun invalidate() {
    super.invalidate()
    val context = context

    if (sortItems.size < sortFiledList.size) {
      for (i in sortItems.size until sortFiledList.size) {
        val relativeLayout = RelativeLayout(context)

        val view = TextView(getContext())
        view.gravity = Gravity.CENTER
        val rp = RelativeLayout.LayoutParams(-2, -1)
        rp.addRule(RelativeLayout.CENTER_IN_PARENT)
        relativeLayout.addView(view, rp)

        relativeLayout.setOnClickListener(onCLickListener)
        sortItems.add(relativeLayout)
      }
    }

    for (sortFiled in sortFiledList) {
      val index = sortFiledList.indexOf(sortFiled)
      val rl = sortItems[index]
      rl.tag = sortFiled
      val view = rl.getChildAt(0) as TextView

      val cusDrawable = sortFiled.drawableRes(sortFiled.sort)
      val drawableRight = when (cusDrawable) {
        null -> drawables[sortFiled.sort]
        else -> getDrawable(cusDrawable)
      }

      view.setCompoundDrawables(null, null, drawableRight, null)
      view.gravity = Gravity.CENTER
      view.text = sortFiled.name
    }

    if (childCount != sortItems.size) {
      removeAllViews()
      for (i in sortFiledList.indices) {
        val rl = sortItems[i]
        val params = LinearLayout.LayoutParams(0, -1, sortFiledList[i].weight)
        addView(rl, params)
      }
    }
  }

  private fun onClickSort(sortFiled: SortField) {
    for (filed in sortFiledList) {
      if (filed !== sortFiled) {
        filed.sort = SortField.NONE
      } else {
        sortFiled.sort = when {
          sortFiled.sort != SortField.ASC && sortFiled.ascEnable -> SortField.ASC
          sortFiled.sort != SortField.DESC && sortFiled.descEnable -> SortField.DESC
          else -> SortField.NONE
        }
      }
    }

    invalidate()
    onSort?.invoke(sortFiled)
  }

  private fun getDrawable(@DrawableRes res: Int): Drawable {
    val drawable = context.drawable(res)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    return drawable
  }

  fun setOnSort(onSort: (SortField) -> Unit) {
    this.onSort = onSort
  }

}
