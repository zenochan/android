/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package cn.izeno.android.presenter.bigbang.action

import android.net.Uri

/**
 * @author baoyongzhang
 * @since 2016/10/26
 */
class GoogleSearchAction : SearchAction() {
  override fun createSearchUriWithEncodedText(encodedText: String): Uri {
    return Uri.parse("https://www.google.com/search?q=" + encodedText)
  }

  companion object {
    @JvmStatic
    fun create(): GoogleSearchAction {
      return GoogleSearchAction()
    }
  }
}
