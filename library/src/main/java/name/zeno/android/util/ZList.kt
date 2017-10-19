package name.zeno.android.util

/**
 * 集合工具类
 * Create Date: 16/7/2
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object ZList {
  //集合是否空的
  fun isEmpty(list: List<*>?): Boolean {
    return list == null || list.isEmpty()
  }

  //安全的获取数据
  operator fun <T> get(list: List<T>?, index: Int): T? {
    return if (list != null && index >= 0 && list.size > index) list[index] else null
  }

  fun size(list: List<*>?): Int {
    return list?.size ?: 0
  }

  //第一条数据
  fun <T> first(list: List<T>): T? {
    return if (isEmpty(list)) null else list[0]
  }


  //最后一条数据
  fun <T> last(list: List<T>): T? {
    return if (isEmpty(list)) null else list[list.size - 1]
  }

}
