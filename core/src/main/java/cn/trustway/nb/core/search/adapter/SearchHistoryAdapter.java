package cn.trustway.nb.core.search.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.adapter.BaseRecycleAdapter;
import cn.trustway.nb.core.base.util.BaseHolder;
import cn.trustway.nb.core.search.view.ClickTextview;


/**
 * Created by huzan on 2017/8/17.
 * 描述：
 */

public class SearchHistoryAdapter extends BaseRecycleAdapter<String> {

    public SearchHistoryAdapter(Context context){
        this(context,false);
    }

    private SearchHistoryAdapter(Context context, boolean isPaging) {
        super(context, isPaging);
    }

    @Override
    protected int setLayout() {
        return R.layout.item_search_history;
    }

    @Override
    protected int setFootLayout() {
        return 0;
    }

    @Override
    protected void bindYourViewHolder(BaseHolder holder, int pos) {
        ClickTextview textview = (ClickTextview) holder.getViewById(R.id.textview_item_search_history);
        textview.setText(itemList.get(pos));
        Drawable drawableLeft= ContextCompat.getDrawable(context,R.drawable.ic_search_history);
        drawableLeft.setBounds(0, 0, (int)context.getResources().getDimension(R.dimen.x14), (int)context.getResources().getDimension(R.dimen.y14));
        Drawable drawableRight= ContextCompat.getDrawable(context,R.drawable.ic_search_menu_cancel);
        drawableRight.setBounds(0, 0, (int)context.getResources().getDimension(R.dimen.x12), (int)context.getResources().getDimension(R.dimen.y12));
        textview.setCompoundDrawables(drawableLeft,null,drawableRight,null);

    }
}
