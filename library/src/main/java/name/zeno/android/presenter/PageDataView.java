package name.zeno.android.presenter;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/9/18
 */
public interface PageDataView extends LoadDataView
{
  // 获取数据失败
  void err();

  // 没有数据
  void empty();

  // 下拉加载
  void pullToLoad();

  // 已加载全部
  void loadAll();
}
