/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.segment

object SegmentUtil {
  fun filterInvalidChar(text: String): String = text.replace("\\s*|\t|\r|\n".toRegex(), "")
  fun skipChar(c: Char): Boolean = "\n\r\t ".contains(c)
}
