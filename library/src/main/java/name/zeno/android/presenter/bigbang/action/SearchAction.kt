/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils

/**
 * @author baoyongzhang
 * @since 2016/10/26.
 */
abstract class SearchAction : Action {

  override fun start(context: Context, text: String) {
    if (!TextUtils.isEmpty(text)) {
      val intent = Intent(Intent.ACTION_VIEW, createSearchUri(text))
      context.startActivity(intent)
    }
  }

  fun createSearchUri(text: String): Uri {
    return createSearchUriWithEncodedText(Uri.encode(text))
  }

  abstract fun createSearchUriWithEncodedText(encodedText: String): Uri

}
