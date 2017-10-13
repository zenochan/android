/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public abstract class SearchAction implements Action
{

  @Override
  public void start(Context context, String text)
  {
    if (!TextUtils.isEmpty(text)) {
      Intent intent = new Intent(Intent.ACTION_VIEW, createSearchUri(text));
      context.startActivity(intent);
    }
  }

  public Uri createSearchUri(String text)
  {
    return createSearchUriWithEncodedText(Uri.encode(text));
  }

  public abstract Uri createSearchUriWithEncodedText(String encodedText);

}
