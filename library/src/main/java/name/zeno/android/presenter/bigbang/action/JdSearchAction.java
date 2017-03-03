/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action;

import android.net.Uri;

public class JdSearchAction extends SearchAction
{

  public static JdSearchAction create()
  {
    return new JdSearchAction();
  }

  @Override
  public Uri createSearchUriWithEncodedText(String encodedText)
  {
    return Uri.parse("https://so.m.jd.com/ware/search.action?keyword=" + encodedText);
  }
}
