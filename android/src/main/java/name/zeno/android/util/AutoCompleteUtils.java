package name.zeno.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.widget.ArrayAdapter;

import com.alibaba.fastjson.JSON;

import java.util.List;

import name.zeno.android.widget.adapter.ZArrayAdapter;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/1.
 */
public class AutoCompleteUtils
{
  private static final String TAG = "AutoCompleteUtils";

  private SharedPreferences preferences;
  private String            key;

  public AutoCompleteUtils(Context context, String key)
  {
    preferences = context.getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    this.key = key;
  }

  public void add(String item)
  {
    String s = preferences.getString(key, "[]");
    if (!s.contains(item)) {
      List<String> items = JSON.parseArray(s, String.class);
      items.add(item);
      SharedPreferences.Editor editor = preferences.edit();
      s = JSON.toJSONString(items);
      editor.putString(key, s);
      editor.apply();
    }
  }

  public List<String> get()
  {
    return JSON.parseArray(preferences.getString(key, "[]"), String.class);
  }

  public void setup(AppCompatAutoCompleteTextView view)
  {
    view.setAdapter(new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, get()));
  }


  public static void with(AppCompatAutoCompleteTextView view, List<String> data)
  {
    with(view, data, android.R.layout.simple_list_item_1);
  }

  public static void with(AppCompatAutoCompleteTextView view, List<String> data, @LayoutRes int resource)
  {
    if (!ZList.isEmpty(data)) {
      view.setAdapter(new ZArrayAdapter<>(view.getContext(), resource, data));
    }
  }

}
