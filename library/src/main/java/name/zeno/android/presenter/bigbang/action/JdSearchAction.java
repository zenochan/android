/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import name.zeno.android.util.ZString;

public class JdSearchAction implements Action
{

  public static JdSearchAction create()
  {
    return new JdSearchAction();
  }

  @Override public void start(Context context, String text)
  {
    if (ZString.INSTANCE.isEmpty(text)) return;

    Intent intent = new Intent(Intent.ACTION_VIEW);
    String uri = "openapp.jdmobile://virtual?params=%7B%22des%22%3A%22productList%22%2C%22keyWord%22%3A%22"
        + Uri.encode(text)
        + "%22%2C%22from%22%3A%22search%22%2C%22category%22%3A%22jump%22%7D";
    intent.setData(Uri.parse(uri));

    if (intent.resolveActivity(context.getPackageManager()) != null) {
      context.startActivity(intent);
    } else {
      uri = "https://so.m.jd.com/ware/search.action?keyword=" + Uri.encode(text);
      intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
      context.startActivity(intent);
    }
  }
}
