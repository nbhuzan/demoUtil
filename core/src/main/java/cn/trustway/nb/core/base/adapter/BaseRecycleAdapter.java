package cn.trustway.nb.core.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.base.listener.RecycleViewClickListener;
import cn.trustway.nb.core.base.util.BaseHolder;
import cn.trustway.nb.core.base.util.FootHolder;


/**
 * Created by huzan  2017/3/17 17:32.
 * 描述：封装recycleview adapter , 上拉加载和普通list
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseHolder> {

    protected RecycleViewClickListener listener;
    protected Context context;
    @NonNull
    protected List<T> itemList = new ArrayList<>();
    protected LayoutInflater inflater;
    protected boolean isPaging = false; //是否分页
    private boolean isPaged = false;//数据是否全部返回
    private boolean isRefreshing = false;//正在加载
    private boolean isCanRefreshing = false;//可进行刷新


    public void setListener(RecycleViewClickListener listener) {
        this.listener = listener;
    }

    public BaseRecycleAdapter(Context context, boolean isPaging) {
        this.context = context;
        this.isPaging = isPaging;
        this.inflater = LayoutInflater.from(context);
    }

    protected boolean isPaged() {
        return isPaged;
    }

    protected void setPaged(boolean paged) {
        isPaged = paged;
    }

    protected boolean isRefreshing() {
        return isRefreshing;
    }

    protected void setRefreshing(boolean refreshing) {
        isRefreshing = refreshing;
    }

    protected boolean isCanRefreshing() {
        return isCanRefreshing;
    }

    protected void setCanRefreshing(boolean canRefreshing) {
        isCanRefreshing = canRefreshing;
    }

    public RecycleViewClickListener getListener() {
        return listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == itemList.size()) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    public List<T> getItemList() {
        return itemList;
    }

    public void removeItem(int position) {
        this.itemList.remove(position);
        notifyItemRangeRemoved(position,this.itemList.size()+1);
    }

    public void setItemList(@NonNull List<T> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    public void addItemList(@NonNull List<T> itemList, int position) {
        this.itemList.addAll(position, itemList);
        if (position == itemList.size()) {
            this.isCanRefreshing = true;
            this.isPaged = false;
            this.isRefreshing = false;
        }
        notifyDataSetChanged();
    }

    public void addItem(T t, int position) {
        this.itemList.add(position, t);
        notifyItemRangeInserted(position,1);
//        notifyItemInserted(position);
    }

    @Nullable
    public T getItem(int position) {
        if (position < this.itemList.size()) {
            return this.itemList.get(position);
        } else {
            return null;
        }
    }

    public void updateItem(T t, int position) {
        this.itemList.set(position, t);
        notifyItemChanged(position);
    }


    public void clearItemList() {
        this.itemList.clear();
        notifyDataSetChanged();
    }

    protected abstract int setLayout();

    /**
     * foot布局，如果不需要分页可以不作处理
     *
     * @return
     */
    protected abstract int setFootLayout();

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = inflater.inflate(setLayout(), parent, false);
            return new BaseHolder(v);
        } else {
            v = inflater.inflate(setFootLayout(), parent, false);
            return new FootHolder(v);
        }

    }


    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (position == itemList.size()) {
            FootHolder footHolder = (FootHolder) holder;
            bindFootHoder(footHolder,position);
            if (isRefreshing) {
                footHolder.loading();
            }
            if (isPaged) {
                footHolder.loaded();
            }
            if (isCanRefreshing) {
                footHolder.load();
            }
        } else {
            bindYourViewHolder(holder, position);
        }
    }

    protected void bindFootHoder(FootHolder footHolder,int pos){

    }

    protected abstract void bindYourViewHolder(BaseHolder holder, int pos);

    @Override
    public int getItemCount() {
        if (isPaging) {
            return itemList.size() + 1;
        } else {
            return itemList.size();
        }

    }

    /**
     * 正在加载
     *
     * @param isRefreshing
     */
    public void isRefreshing(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        isCanRefreshing = false;
        isPaged = false;
        notifyDataSetChanged();
    }

    /**
     * 服务端数据已经全部返回
     *
     * @param isPaged
     */
    public void isRefreshed(boolean isPaged) {
        this.isPaged = isPaged;
        this.isCanRefreshing = false;
        this.isRefreshing = false;
        notifyDataSetChanged();
    }

    /**
     * 可刷新
     *
     * @param isCanRefreshing
     */
    public void canRefreshing(boolean isCanRefreshing) {
        this.isCanRefreshing = isCanRefreshing;
        this.isPaged = false;
        this.isRefreshing = false;
        notifyDataSetChanged();
    }


}

