package name.zeno.android.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.FloatRange
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections

import name.zeno.android.listener.Action1
import name.zeno.android.util.R

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/11.
 */
class SortHeaderView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {
  private var sortFiledList = emptyList<SortFiled>()
  private lateinit var onCLickListener: View.OnClickListener
  private var onSort: ((SortFiled) -> Unit)? = null

  private val drawableRes = intArrayOf(R.mipmap.ic_sortable, R.mipmap.ic_sorted_asc, R.mipmap.ic_sorted_desc)


  private val drawables = arrayOfNulls<Drawable>(3)

  private val sortItems = ArrayList<RelativeLayout>()

  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {
    orientation = LinearLayout.HORIZONTAL
    for (i in drawableRes.indices) {
      drawables[i] = getDrawable(drawableRes[i])
    }
    onCLickListener = View.OnClickListener { view ->
      val filed = view.tag as? SortFiled
      if (filed != null) {
        onClickSort(filed)
      }
    }
  }

  fun setSortFiled(vararg filed: SortFiled) {
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
      view.setCompoundDrawables(null, null, drawables[sortFiled.sort], null)
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

  private fun onClickSort(sortFiled: SortFiled) {
    for (filed in sortFiledList) {
      if (filed !== sortFiled) {
        filed.sort = SortFiled.NONE
      } else {
        sortFiled.sort = if (sortFiled.sort != SortFiled.ASC)
          SortFiled.ASC
        else
          SortFiled.DESC
      }
    }

    invalidate()
    onSort?.invoke(sortFiled)
  }

  private fun getDrawable(@DrawableRes res: Int): Drawable {
    val drawable = ContextCompat.getDrawable(context, res)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    return drawable
  }

  fun setOnSort(onSort: (SortFiled) -> Unit) {
    this.onSort = onSort
  }

  class SortFiled {
    var name: String? = null
    var filed: String? = null
    @SortInt
    var sort: Int = 0
    var weight = 1f

    @IntDef(NONE.toLong(), ASC.toLong(), DESC.toLong())
    annotation class SortInt

    companion object {

      const val NONE = 0
      const val ASC = 1
      const val DESC = 2


      @JvmOverloads
      fun newInstance(name: String, filed: String, @FloatRange(from = 1.0) weight: Float = 1f): SortFiled {
        val f = SortFiled()
        f.name = name
        f.filed = filed
        f.weight = weight
        return f
      }
    }
  }

}
