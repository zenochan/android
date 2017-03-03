package kale.adapter;

import android.content.Context;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import kale.adapter.item.AdapterItem;
import kale.adapter.util.DataBindingJudgement;
import kale.adapter.util.IAdapter;
import kale.adapter.util.ItemTypeUtil;

/**
 * @author Jack Tony
 * @since 2015/5/17
 */
@SuppressWarnings("ConstantConditions")
public abstract class CommonRcvAdapter<T> extends RecyclerView.Adapter implements IAdapter<T>
{

  private List<T> mDataList;

  protected Object mType;

  protected ItemTypeUtil mUtil;


  public CommonRcvAdapter()
  {
    this(null);
  }

  public CommonRcvAdapter(@Nullable List<T> data)
  {
    mUtil = new ItemTypeUtil();
    setData(data);

  }

  @Override
  public int getItemCount()
  {
    return mDataList == null ? 0 : mDataList.size();
  }

  @Override
  public void setData(List<T> data)
  {
    if (data == null) {
      mDataList = Collections.emptyList();
    } else if (data != mDataList) {
      mDataList = data;
      observeData(mDataList);
    }
  }

  @Override
  public List<T> getData()
  {
    return mDataList;
  }

  public T getItem(int position)
  {
    T t = null;
    if (mDataList != null && mDataList.size() > position) {
      t = getData().get(position);
    }
    return t;
  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  /**
   * instead by{@link #getItemType(Object)}
   * <p>
   * 通过数据得到obj的类型的type
   * 然后，通过{@link ItemTypeUtil}来转换位int类型的type
   */
  @Deprecated
  @Override
  public int getItemViewType(int position)
  {
    mType = getItemType(mDataList == null ? null : mDataList.get(position));
    return mUtil.getIntType(mType);
  }

  @Override
  public Object getItemType(T t)
  {
    return -1; // default
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
  {
    return new RcvAdapterItem(parent.getContext(), parent, createItem(mType));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
  {
    debug((RcvAdapterItem) holder);
    //noinspection unchecked
    ((RcvAdapterItem) holder).item.handleData(getConvertedData(mDataList.get(position), mType), position);
  }

  @NonNull
  @Override
  public Object getConvertedData(T data, Object type)
  {
    return data;
  }


  ///////////////////////////////////////////////////////////////////////////
  // 内部用到的viewHold
  ///////////////////////////////////////////////////////////////////////////
  protected static class RcvAdapterItem extends RecyclerView.ViewHolder
  {

    public AdapterItem item;

    boolean isNew = true; // debug中才用到

    public RcvAdapterItem(Context context, ViewGroup parent, AdapterItem item)
    {
      super(LayoutInflater.from(context).inflate(item.getLayoutResId(), parent, false));
      this.item = item;
      this.item.bindViews(itemView);
      this.item.setViews();
    }
  }


  private void observeData(List<T> data)
  {
    if (DataBindingJudgement.SUPPORT_DATABINDING && data instanceof ObservableList) {
      ((ObservableList<T>) data).addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>()
      {
        @Override
        public void onChanged(ObservableList<T> sender)
        {
          notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount)
        {
          notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount)
        {
          notifyItemRangeInserted(positionStart, itemCount);
          notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount)
        {
          notifyItemRangeRemoved(positionStart, itemCount);
          notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount)
        {
          // Note:不支持一次性移动"多个"item的情况！！！！
          notifyItemMoved(fromPosition, toPosition);
          notifyDataSetChanged();
        }
      });
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // For debug
  ///////////////////////////////////////////////////////////////////////////
  private void debug(RcvAdapterItem holder)
  {
    boolean debug = false;
    if (debug) {
      holder.itemView.setBackgroundColor(holder.isNew ? 0xffff0000 : 0xff00ff00);
      holder.isNew = false;
    }
  }

}
