package cn.trustway.nb.core.vio_address.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.adapter.BaseRecycleAdapter;
import cn.trustway.nb.core.base.util.BaseHolder;
import cn.trustway.nb.core.base.util.StringUtil;
import cn.trustway.nb.core.vio_address.model.RoadModel;


/**
 * Created by huzan  2017/3/31 13:36.
 * 描述：违法地点adapter
 */

public class AddressAdapter extends BaseRecycleAdapter<RoadModel> {
    private String key = "";
    private ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);

    public AddressAdapter(Context context, boolean isPaging) {
        super(context, isPaging);
    }

    @Override
    protected int setLayout() {
        return R.layout.item_address;
    }

    @Override
    protected int setFootLayout() {
        return 0;
    }

    @Override
    protected void bindYourViewHolder(final BaseHolder holder, int pos) {
        RoadModel roadSearchBean = itemList.get(pos);
        SpannableStringBuilder strBuilder;
        int length;
        if (roadSearchBean.getLddm() == null) {  //显示道路信息
            if (key.matches(StringUtil.regularNumber())) {
                strBuilder = new SpannableStringBuilder(roadSearchBean.getDldm());
                length = roadSearchBean.getDldm().indexOf(key);
                strBuilder.setSpan(redSpan, length, length + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.setTextBuild(R.id.textview_item_address_dldm, strBuilder);
                holder.setText(R.id.textview_item_address_dlmc, roadSearchBean.getDlmc());
            }
//            else if (key.matches(StringUtil.regularLetter())) {
//
//            }
            else {
                strBuilder = new SpannableStringBuilder(roadSearchBean.getDlmc());
                length = roadSearchBean.getDlmc().indexOf(key);
                strBuilder.setSpan(redSpan, length, length + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.setTextBuild(R.id.textview_item_address_dlmc, strBuilder);
                holder.setText(R.id.textview_item_address_dldm, roadSearchBean.getDldm());
            }
        } else {//显示路段信息
            if (key.matches(StringUtil.regularNumber())) {
                strBuilder = new SpannableStringBuilder(roadSearchBean.getLddm());
                length = roadSearchBean.getLddm().indexOf(key);
                if(length <0){
                    holder.setText(R.id.textview_item_address_dlmc, roadSearchBean.getLdmc());
                    holder.setText(R.id.textview_item_address_dldm, roadSearchBean.getLddm());
                }else {
                    strBuilder.setSpan(redSpan, length, length + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.setTextBuild(R.id.textview_item_address_dldm, strBuilder);
                    holder.setText(R.id.textview_item_address_dlmc, roadSearchBean.getLdmc());
                }
            } else if (key.matches(StringUtil.regularLetter())) {

            } else {
                strBuilder = new SpannableStringBuilder(roadSearchBean.getLdmc());
                length = roadSearchBean.getLdmc().indexOf(key);
                if(length <0){
                    holder.setText(R.id.textview_item_address_dlmc, roadSearchBean.getLdmc());
                    holder.setText(R.id.textview_item_address_dldm, roadSearchBean.getLddm());
                }else {
                    strBuilder.setSpan(redSpan, length, length + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.setTextBuild(R.id.textview_item_address_dlmc, strBuilder);
                    holder.setText(R.id.textview_item_address_dldm, roadSearchBean.getLddm());
                }
            }

        }


        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.OnItemClickListener(view, holder.getLayoutPosition());
                }
            }
        });
    }

    public void setKey(String key) {
        this.key = key;
    }


}
