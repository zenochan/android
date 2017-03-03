/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.core;

import android.content.Context;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.Map;

import name.zeno.android.presenter.bigbang.action.Action;
import name.zeno.android.presenter.bigbang.action.BaiduSearchAction;
import name.zeno.android.presenter.bigbang.action.CopyAction;
import name.zeno.android.presenter.bigbang.action.ShareAction;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class BigBang
{

  public static final String ACTION_SEARCH = "search";
  public static final String ACTION_SHARE  = "share";
  public static final String ACTION_COPY   = "copy";
  public static final String ACTION_BACK   = "back";
  private static int sItemSpace;
  private static int sLineSpace;
  private static int sItemTextSize;
  private static Map<String, Action> mActionMap = new HashMap<>();

  static {
    BigBang.registerAction(BigBang.ACTION_SEARCH, new BaiduSearchAction());
    BigBang.registerAction(BigBang.ACTION_SHARE, new ShareAction());
    BigBang.registerAction(BigBang.ACTION_COPY, new CopyAction());
  }

  public static void setStyle(int itemSpace, int lineSpace, int itemTextSize)
  {
    sItemSpace = itemSpace;
    sLineSpace = lineSpace;
    sItemTextSize = itemTextSize;
  }

  @StringDef({ACTION_SEARCH, ACTION_SHARE, ACTION_COPY, ACTION_BACK})
  @Retention(SOURCE)
  public @interface ActionType
  {

  }


  private BigBang()
  {
  }

  public static void registerAction(@ActionType String type, Action action)
  {
    mActionMap.put(type, action);
  }

  public static void unregisterAction(@ActionType String type)
  {
    mActionMap.remove(type);
  }

  public static Action getAction(@ActionType String type)
  {
    return mActionMap.get(type);
  }

  public static void startAction(Context context, @ActionType String type, String text)
  {
    Action action = BigBang.getAction(type);
    if (action != null) {
      action.start(context, text);
    }
  }

  public static int getItemSpace()
  {
    return sItemSpace;
  }

  public static int getLineSpace()
  {
    return sLineSpace;
  }

  public static int getItemTextSize()
  {
    return sItemTextSize;
  }
}
