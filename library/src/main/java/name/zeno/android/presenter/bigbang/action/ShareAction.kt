/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action

import android.content.Context

import name.zeno.android.util.ZAction

class ShareAction : Action {
  override fun start(context: Context, text: String) {
    ZAction.sendText(context, text)
  }
}
