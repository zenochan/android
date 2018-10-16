/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.core

import android.content.Context
import androidx.annotation.StringDef
import name.zeno.android.presenter.bigbang.action.Action
import name.zeno.android.presenter.bigbang.action.BaiduSearchAction
import name.zeno.android.presenter.bigbang.action.CopyAction
import name.zeno.android.presenter.bigbang.action.ShareAction
import java.util.*

object BigBang {

  const val ACTION_SEARCH = "search"
  const val ACTION_SHARE = "share"
  const val ACTION_COPY = "copy"
  const val ACTION_BACK = "back"
  var itemSpace: Int = 0
    private set
  var lineSpace: Int = 0
    private set
  var itemTextSize: Int = 0
    private set
  private val mActionMap = HashMap<String, Action>()

  init {
    BigBang.registerAction(BigBang.ACTION_SEARCH, BaiduSearchAction())
    BigBang.registerAction(BigBang.ACTION_SHARE, ShareAction())
    BigBang.registerAction(BigBang.ACTION_COPY, CopyAction())
  }

  fun setStyle(itemSpace: Int, lineSpace: Int, itemTextSize: Int) {
    this.itemSpace = itemSpace
    this.lineSpace = lineSpace
    this.itemTextSize = itemTextSize
  }

  @StringDef(ACTION_SEARCH, ACTION_SHARE, ACTION_COPY, ACTION_BACK)
  @Retention(AnnotationRetention.SOURCE)
  annotation class ActionType()

  fun registerAction(@ActionType type: String, action: Action) {
    mActionMap.put(type, action)
  }

  fun unregisterAction(@ActionType type: String) {
    mActionMap.remove(type)
  }

  fun getAction(@ActionType type: String): Action? {
    return mActionMap[type]
  }

  fun startAction(context: Context, @ActionType type: String, text: String) {
    val action = BigBang.getAction(type)
    action?.start(context, text)
  }
}
