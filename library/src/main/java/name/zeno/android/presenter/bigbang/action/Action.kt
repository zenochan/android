/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action

import android.content.Context

interface Action {
  fun start(context: Context, text: String)
}
