package name.zeno.android.widget.recycler;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;

import name.zeno.android.listener.Action0;

/**
 * Create Date: 16/6/28
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public interface RecyclerLoadMoreAdapter
{
  int STATE_NONE         = 0;//啥也没有
  int STATE_LOADING      = 1;//第一次加载
  int STATE_EMPTY        = 2;//空
  int STATE_PULL_TO_LOAD = 3;//等待下拉加载
  int STATE_LOAD_MORE    = 4;//正在加载
  int STATE_LOAD_ALL     = 5;//已加载全部
  int STATE_LOAD_FAILED  = 6;//加载失败

  @IntDef({STATE_LOADING, STATE_EMPTY, STATE_PULL_TO_LOAD, STATE_LOAD_MORE, STATE_LOAD_ALL, STATE_NONE, STATE_LOAD_FAILED})
  @interface State {}

  /** 刷新时调用 */
  void setOnRefresh();

  /** 无状态 */
  void setNone();

  /** 正在加载中 */
  void setLoading();

  /** 切换为空数据状态 */
  void setEmpty();

  void setLoadMore();

  void setPullToLoad();

  /** 切换为没有更多数据状态 */
  void setLoadAll();

  /** 切换为加载失败 */
  void setLoadFailed();

  void setOnLoadMoreListener(Action0 onLoadMoreListener);

  void setupWithRcv(RecyclerView recyclerView);

  int getHeaderCount();

  int getFooterCount();
}
