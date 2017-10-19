package name.zeno.android.presenter

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/18
 */
interface PageDataView : LoadDataView {
  // 获取数据失败
  fun err()

  // 没有数据
  fun empty()

  // 下拉加载
  fun pullToLoad()

  // 已加载全部
  fun loadAll()
}
