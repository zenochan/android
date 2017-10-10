/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action;

import android.content.Context;

import name.zeno.android.util.ZAction;

public class ShareAction implements Action
{
  @Override
  public void start(Context context, String text)
  {
    ZAction.sendText(context, text);
  }
}
