/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action

import android.net.Uri

/**
 * @author baoyongzhang
 * @since  2016/10/26.
 */
class BaiduSearchAction : SearchAction() {

  override fun createSearchUriWithEncodedText(encodedText: String): Uri {
    return Uri.parse("https://www.baidu.com/s?wd=" + encodedText)
  }

  companion object {

    fun create(): BaiduSearchAction {
      return BaiduSearchAction()
    }
  }
}
