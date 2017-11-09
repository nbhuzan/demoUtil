package cn.trustway.nb.core.vio_action.adapter;

import android.content.Context;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.adapter.BaseRecycleAdapter;
import cn.trustway.nb.core.base.util.BaseHolder;
import cn.trustway.nb.core.vio_action.model.VioActionModel;


/**
 * Created by huzan on 2017/8/25.
 * 描述：多选情况下违法行为选中的列表
 */

public class VioActionSelectedAdapter extends BaseRecycleAdapter<VioActionModel> {

    public VioActionSelectedAdapter(Context context) {
        this(context, false);
    }

    private VioActionSelectedAdapter(Context context, boolean isPaging) {
        super(context, isPaging);
    }

    @Override
    protected int setLayout() {
        return R.layout.item_vio_action_selected;
    }

    @Override
    protected int setFootLayout() {
        return 0;
    }

    @Override
    protected void bindYourViewHolder(BaseHolder holder, int pos) {
        VioActionModel vioWfdm = itemList.get(pos);
        holder.setText(R.id.textview_wfxw_select,vioWfdm.getWfxw()+"-"+vioWfdm.getWfnr());
        holder.getViewById(R.id.imageview_wfxw_select).setOnClickListener(v -> {
            if(listener!=null) {
                listener.OnItemClickListener(v, pos);
            }
        });
    }
}
