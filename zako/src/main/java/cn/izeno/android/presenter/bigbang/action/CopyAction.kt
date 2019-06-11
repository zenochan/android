/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package cn.izeno.android.presenter.bigbang.action

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.widget.Toast

class CopyAction : Action {

  override fun start(context: Context, text: String) {
    if (!TextUtils.isEmpty(text)) {
      val service = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      service.primaryClip = ClipData.newPlainText("BigBang", text)
      copySuccess(context)
    }
  }

  fun copySuccess(context: Context) {
    Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
  }

  companion object {

    fun create(): CopyAction {
      return CopyAction()
    }
  }
}
