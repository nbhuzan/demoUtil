package cn.trustway.nb.core.search.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;

import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.adapter.BaseRecycleAdapter;
import cn.trustway.nb.core.base.util.BaseHolder;
import cn.trustway.nb.core.search.model.SearchModel;

/**
 * Created by huzan on 2017/8/17.
 * 描述：搜索控件菜单
 */

public class SearchMenuAdapter extends BaseRecycleAdapter<SearchModel> {
    private SparseBooleanArray checkArray = new SparseBooleanArray();

    public SearchMenuAdapter(Context context) {
        this(context, false);
    }

    private SearchMenuAdapter(Context context, boolean isPaging) {
        super(context, isPaging);
    }

    @Override
    public void setItemList(List<SearchModel> itemList) {
        super.setItemList(itemList);
        initCheckArray();
    }



    public void initCheckArray() {
        for (int i = 0; i < itemList.size(); i++) {
            checkArray.put(i, false);
        }
    }

    public SearchModel getCheck() {
        for (SearchModel model : itemList) {
            for (int i = 0; i < checkArray.size(); i++) {
                if (checkArray.get(i)) {
                    return model;
                }
            }
        }
        return null;
    }

    private boolean getItemCheck(int pos) {
        return checkArray.get(pos);
    }

    public void setItemCheck(int pos) {
        initCheckArray();
        checkArray.put(pos, true);
        notifyDataSetChanged();
    }

    @Override
    protected int setLayout() {
        return R.layout.item_search_menu;
    }

    @Override
    protected int setFootLayout() {
        return 0;
    }

    @Override
    protected void bindYourViewHolder(BaseHolder holder, int pos) {
        SearchModel model = itemList.get(pos);
        if (model == null) {
            return;
        }
        holder.setText(R.id.textview_item_search_menu, model.getMenu());
        if (getItemCheck(pos)) {
            holder.getViewById(R.id.imageview_item_search_menu).setVisibility(View.VISIBLE);
        } else {
            holder.getViewById(R.id.imageview_item_search_menu).setVisibility(View.INVISIBLE);
        }

        holder.getView().setOnClickListener(v -> {
            setItemCheck(holder.getLayoutPosition());
            if (listener != null) {
                listener.OnItemClickListener(v, holder.getLayoutPosition());
            }
        });
    }
}
