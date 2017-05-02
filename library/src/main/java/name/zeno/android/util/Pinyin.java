package name.zeno.android.util;

import android.support.annotation.IntRange;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 使用：
 * <p>
 * 在依赖中添加 compile 'com.belerweb:pinyin4j:2.5.1'
 * <p>
 * <h4>API</h4>
 * <ul>
 * <li>{@link #cn2FirstSpell(String)} 汉语拼音 -> hypy</li>
 * <li>{@link #cn2FirstSpell(String, boolean)} 汉语拼音 -> {hypy|HYPY}</li>
 * <li>{@link #cn2Spell(String)} 汉语拼音 -> hanyupinyin</li>
 * <li>{@link #getRandomJianHan(int)} 获取指定长度随机中文</li>
 * <li>{@link #getJianHan(int, int)} 通过高位地位获取中文</li>
 * </ul>
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/15
 */
@SuppressWarnings("unused")
public class Pinyin
{
  public static String cn2FirstSpell(String chinese)
  {
    return cn2FirstSpell(chinese, false);
  }

  /**
   * 获取汉字串拼音首字母，英文及特殊字符字符不变
   *
   * @param chinese 汉字串
   * @return 汉语拼音首字母
   */
  public static String cn2FirstSpell(String chinese, boolean lowCase)
  {
    StringBuilder           pyBuilder     = new StringBuilder();
    char[]                  arr           = chinese.toCharArray();
    HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
    defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] > 128) {
        try {
          String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
          if (_t != null) {
            pyBuilder.append(_t[0].charAt(0));
          }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
          e.printStackTrace();
        }
      } else {
        pyBuilder.append(arr[i]);
      }
    }
    String r = pyBuilder.toString().trim();
    if (lowCase) {
      r = r.toLowerCase();
    } else {
      r = r.toUpperCase();
    }
    return r;
  }

  /**
   * 获取汉字串拼音，英文字符不变
   *
   * @param chinese 汉字串
   * @return 汉语拼音
   */
  public static String cn2Spell(String chinese)
  {
    StringBuilder           builder       = new StringBuilder();
    char[]                  arr           = chinese.toCharArray();
    HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
    defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] > 128) {
        try {
          builder.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
          e.printStackTrace();
        }
      } else {
        builder.append(arr[i]);
      }
    }
    return builder.toString();
  }

  /**
   * 获取指定长度随机简体中文
   *
   * @param len int
   * @return String
   */
  public static String getRandomJianHan(int len)
  {
    String ret = "";
    for (int i = 0; i < len; i++) {
      String str    = null;
      int    hightPos, lowPos; // 定义高低位
      Random random = new Random();
      hightPos = 176 + Math.abs(random.nextInt(39)); //获取高位值
      lowPos = 161 + Math.abs(random.nextInt(93)); //获取低位值
      byte[] b = new byte[2];
      b[0] = (Integer.valueOf(hightPos).byteValue());
      b[1] = (Integer.valueOf(lowPos).byteValue());
      try {
        str = new String(b, "UTF8"); //转成中文
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
      }
      ret += str;
    }
    return ret;
  }

  public static String getJianHan(@IntRange(from = 176, to = 215) int h, @IntRange(from = 161, to = 254) int l)
  {
    String str = null;
    byte[] b   = new byte[2];
    b[0] = Integer.valueOf(h).byteValue();
    b[1] = Integer.valueOf(l).byteValue();
    try {
      str = new String(b, "GBK"); //转成中文
    } catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }

    return str;
  }

}
