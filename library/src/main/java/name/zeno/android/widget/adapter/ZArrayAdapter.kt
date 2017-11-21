package name.zeno.android.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

import java.util.ArrayList

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/5/19
 */
class ZArrayAdapter<T>(
    context: Context,
    textViewResouceId: Int,
    objects: List<T>)
  : BaseAdapter(), Filterable {
  private var mOriginalValues: List<T>? = null
  private var mObject: List<T>
  private val mLock = Any()
  private var mResouce: Int
  private var myFilter: MyFilter? = null
  private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

  init {
    mObject = objects
    mResouce = textViewResouceId
    myFilter = MyFilter()
  }

  override fun getCount(): Int {
    return mObject.size
  }

  override fun getItem(position: Int): T {
    return mObject[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
      getViewFromResouce(position, convertView, parent, mResouce)

  private fun getViewFromResouce(position: Int, convertView: View?, parent: ViewGroup, layoutRes: Int): View {
    val tv: TextView = (convertView ?: inflater.inflate(layoutRes, parent, false)) as TextView
    val item = getItem(position)
    if (item is CharSequence) {
      tv.text = item
    } else {
      tv.text = item.toString()
    }
    return tv
  }

  //返回过滤器
  override fun getFilter(): Filter? {
    return myFilter
  }

  //自定义过滤器
  private inner class MyFilter : Filter() {
    //得到过滤结果
    override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
      if (mOriginalValues == null) {
        synchronized(mLock) {
          mOriginalValues = ArrayList(mObject)
        }
      }

      val count = mOriginalValues!!.size
      val values = ArrayList<T>()
      for (i in 0 until count) {
        val value = mOriginalValues!![i]
        val valueText = value.toString()
        //自定义匹配规则
        if (constraint != null) {
          val words = constraint.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
          var containAll = true
          for (word in words) {
            if (!valueText.toLowerCase().contains(word.toLowerCase())) {
              containAll = false
              break
            }
          }
          if (containAll) {
            values.add(value)
          }
        }
      }

      val results = Filter.FilterResults()
      results.values = values
      results.count = values.size
      return results
    }

    //发布过滤结果
    override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
      //把搜索结果赋值给mObject这样每次输入字符串的时候就不必
      //从所有的字符串中查找，从而提高了效率
      mObject = results.values as List<T>
      if (results.count > 0) {
        notifyDataSetChanged()
      } else {
        notifyDataSetInvalidated()
      }
    }

  }

}

