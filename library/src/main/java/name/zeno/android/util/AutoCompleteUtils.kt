package name.zeno.android.util

import android.content.Context
import android.content.SharedPreferences
import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatAutoCompleteTextView
import android.widget.ArrayAdapter

import com.alibaba.fastjson.JSON

import name.zeno.android.widget.adapter.ZArrayAdapter

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/1.
 */
class AutoCompleteUtils(context: Context, private val key: String) {

  private val preferences: SharedPreferences

  init {
    preferences = context.applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
  }

  fun add(item: String) {
    var s = preferences.getString(key, "[]")
    if (!s!!.contains(item)) {
      val items = JSON.parseArray(s, String::class.java)
      items.add(item)
      val editor = preferences.edit()
      s = JSON.toJSONString(items)
      editor.putString(key, s)
      editor.apply()
    }
  }

  fun get(): List<String> {
    return JSON.parseArray(preferences.getString(key, "[]"), String::class.java)
  }

  fun setup(view: AppCompatAutoCompleteTextView) {
    view.setAdapter(ArrayAdapter(view.context, android.R.layout.simple_list_item_1, get()))
  }

  companion object {
    private val TAG = "AutoCompleteUtils"

    @JvmOverloads
    fun with(view: AppCompatAutoCompleteTextView, data: List<String>, @LayoutRes resource: Int = android.R.layout.simple_list_item_1) {
      if (!ZList.isEmpty(data)) {
        view.setAdapter(ZArrayAdapter(view.context, resource, data))
      }
    }
  }

}