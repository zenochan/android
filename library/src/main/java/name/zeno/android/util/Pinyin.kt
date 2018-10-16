package name.zeno.android.util

import androidx.annotation.IntRange
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

/**
 * 使用：
 *
 *
 * 在依赖中添加 compile 'com.belerweb:pinyin4j:2.5.1'
 *
 *
 * <h4>API</h4>
 *
 *  * [.cn2FirstSpell] 汉语拼音 -> hypy
 *  * [.cn2FirstSpell] 汉语拼音 -> {hypy|HYPY}
 *  * [.cn2Spell] 汉语拼音 -> hanyupinyin
 *  * [.getRandomJianHan] 获取指定长度随机中文
 *  * [.getJianHan] 通过高位地位获取中文
 *
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/15
 */
object Pinyin {

  /**
   * 获取汉字串拼音首字母，英文及特殊字符字符不变
   *
   * @param chinese 汉字串
   * @return 汉语拼音首字母
   */
  @JvmOverloads
  fun cn2FirstSpell(chinese: String, lowCase: Boolean = false): String {
    val pyBuilder = StringBuilder()
    val arr = chinese.toCharArray()
    val defaultFormat = HanyuPinyinOutputFormat()
    defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
    defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
    for (i in arr.indices) {
      if (arr[i].toInt() > 128) {
        try {
          val _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)
          if (_t != null) {
            pyBuilder.append(_t[0][0])
          }
        } catch (e: BadHanyuPinyinOutputFormatCombination) {
          e.printStackTrace()
        }

      } else {
        pyBuilder.append(arr[i])
      }
    }
    var r = pyBuilder.toString().trim { it <= ' ' }
    if (lowCase) {
      r = r.toLowerCase()
    } else {
      r = r.toUpperCase()
    }
    return r
  }

  /**
   * 获取汉字串拼音，英文字符不变
   *
   * @param chinese 汉字串
   * @return 汉语拼音
   */
  fun cn2Spell(chinese: String): String {
    val builder = StringBuilder()
    val arr = chinese.toCharArray()
    val defaultFormat = HanyuPinyinOutputFormat()
    defaultFormat.caseType = HanyuPinyinCaseType.LOWERCASE
    defaultFormat.toneType = HanyuPinyinToneType.WITHOUT_TONE
    for (i in arr.indices) {
      if (arr[i].toInt() > 128) {
        try {
          builder.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0])
        } catch (e: BadHanyuPinyinOutputFormatCombination) {
          e.printStackTrace()
        }

      } else {
        builder.append(arr[i])
      }
    }
    return builder.toString()
  }

  /**
   * 获取指定长度随机简体中文
   *
   * @param len int
   * @return String
   */
  fun getRandomJianHan(len: Int): String {
    var ret = ""
    for (i in 0 until len) {
      var str: String? = null
      val hightPos: Int
      val lowPos: Int // 定义高低位
      val random = Random()
      hightPos = 176 + Math.abs(random.nextInt(39)) //获取高位值
      lowPos = 161 + Math.abs(random.nextInt(93)) //获取低位值
      val b = ByteArray(2)
      b[0] = Integer.valueOf(hightPos).toByte()
      b[1] = Integer.valueOf(lowPos).toByte()
      try {
        str = String(b, "UTF8" as Charset) //转成中文
      } catch (ex: UnsupportedEncodingException) {
        ex.printStackTrace()
      }

      ret += str
    }
    return ret
  }

  fun getJianHan(@IntRange(from = 176, to = 215) h: Int, @IntRange(from = 161, to = 254) l: Int): String? {
    var str: String? = null
    val b = ByteArray(2)
    b[0] = Integer.valueOf(h).toByte()
    b[1] = Integer.valueOf(l).toByte()
    try {
      str = String(b, "GBK" as Charset) //转成中文
    } catch (ex: UnsupportedEncodingException) {
      ex.printStackTrace()
    }

    return str
  }

}
