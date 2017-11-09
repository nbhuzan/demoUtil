package cn.trustway.nb.core.vio_action.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.adapter.BaseRecycleAdapter;
import cn.trustway.nb.core.base.util.BaseHolder;
import cn.trustway.nb.core.base.util.StringUtil;
import cn.trustway.nb.core.vio_action.model.VioActionModel;


/**
 * Created by huzan  2017/4/5 14:40.
 * 描述：选择违法行为代码适配器
 */

public class VioActionAdapter extends BaseRecycleAdapter<VioActionModel> {
    private String key = "";
    private ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
    private OnCollectListener onCollectListener;


    private ArrayMap<String, Boolean> arrayMap = new ArrayMap<>();
    private String vioType;


    public VioActionAdapter(Context context, List<VioActionModel> preferenceList) {
        super(context, false);
        setPreferenceList(preferenceList);

    }

    public void setOnCollectListener(OnCollectListener onCollectListener) {
        this.onCollectListener = onCollectListener;
    }

    public void setPreferenceList(List<VioActionModel> preferenceList) {
        if (preferenceList != null) {
            for (VioActionModel pre : preferenceList) {
                arrayMap.put(pre.getWfxw(), true);
            }
        }
        notifyDataSetChanged();
    }


    private boolean isSelect(String key) {
        if (arrayMap.get(key) == null || (!arrayMap.get(key))) {
            return false;
        }
        return true;
    }

    private void setSelected(String key) {
        arrayMap.put(key, true);
    }

    @Override
    protected int setLayout() {
        return R.layout.item_vio_action;
    }

    @Override
    protected int setFootLayout() {
        return 0;
    }

    @Override
    public void bindYourViewHolder(final BaseHolder holder, int pos) {
        final VioActionModel vioWfdm = itemList.get(pos);
        SpannableStringBuilder strBuilder;
        int length;
        if (key.matches(StringUtil.regularNumber())) {
            strBuilder = new SpannableStringBuilder(vioWfdm.getWfxw());
            length = vioWfdm.getWfxw().indexOf(key);
            strBuilder.setSpan(redSpan, length, length + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.setTextBuild(R.id.textview_item_violation_conduct_wfdm, strBuilder);
            holder.setText(R.id.textview_item_violation_conduct_wfnr, vioWfdm.getWfnr());
        } else {
            strBuilder = new SpannableStringBuilder(vioWfdm.getWfnr());
            length = vioWfdm.getWfnr().indexOf(key);
            strBuilder.setSpan(redSpan, length, length + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.setText(R.id.textview_item_violation_conduct_wfdm, vioWfdm.getWfxw());
            holder.setTextBuild(R.id.textview_item_violation_conduct_wfnr, strBuilder);
        }
        if (arrayMap.get(vioWfdm.getWfxw()) == null || (!arrayMap.get(vioWfdm.getWfxw()))) {
            holder.setImg(R.id.imageview_item_violation_conduct_collect, ContextCompat.getDrawable(context, R.drawable.ic_collect_nor));
        } else {
            holder.setImg(R.id.imageview_item_violation_conduct_collect, ContextCompat.getDrawable(context, R.drawable.ic_collect_hl));
        }
        holder.getViewById(R.id.imageview_item_violation_conduct_collect)
                .setOnClickListener(view -> {
                    if (isSelect(vioWfdm.getWfxw())) {
                        //取消收藏
                        if (onCollectListener != null) {
                            onCollectListener.deleteCollect(holder.getLayoutPosition());
                            holder.setImg(R.id.imageview_item_violation_conduct_collect, ContextCompat.getDrawable(context, R.drawable.ic_collect_nor));
                        }

                    } else {
                        //收藏
                        if (onCollectListener != null) {
                            onCollectListener.addCollect(holder.getLayoutPosition());
                            setSelected(vioWfdm.getWfxw());
                            holder.setImg(R.id.imageview_item_violation_conduct_collect, ContextCompat.getDrawable(context, R.drawable.ic_collect_hl));
                        }

                    }
                });

        holder.getView().setOnClickListener(view -> {
            if (listener != null) {
                listener.OnItemClickListener(view, holder.getLayoutPosition());
            }
        });
    }

    public void setKey(String key) {
        this.key = key;
    }


    public interface OnCollectListener {
        void addCollect(int pos);

        void deleteCollect(int pos);
    }
}
