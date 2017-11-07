package cn.trustway.nb.core.util;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by huzan  2017/3/20 14:28.
 * 描述：recycleview 分页的foot viewholder
 */

public class FootHolder extends BaseHolder {

    private RelativeLayout relativeLayout_foot_parent;
    private LinearLayout linearLayout_foot_loading; //正在加载
    private TextView textView_foot_load;  //上拉加载
    private TextView textView_foot_loaded;//到底了

    public FootHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
//        relativeLayout_foot_parent = (RelativeLayout) itemView.findViewById(R.id.relativelayout_recycleview_foot_parent);
//        textView_foot_load = (TextView) itemView.findViewById(R.id.textview_recycleview_foot_load);
//        linearLayout_foot_loading = (LinearLayout) itemView.findViewById(R.id.linearlayout_recycleview_foot_loading);
//        textView_foot_loaded = (TextView) itemView.findViewById(R.id.textview_recycleview_foot_loaded);
    }


    public void setText(int viewId, String str) {
        ((TextView) itemView.findViewById(viewId)).setText(str);
    }

    public View getView() {
        return itemView;
    }

    public void setAllGone() {
        relativeLayout_foot_parent.setVisibility(View.GONE);
    }

    public void loading() {
        linearLayout_foot_loading.setVisibility(View.VISIBLE);
        textView_foot_load.setVisibility(View.GONE);
        textView_foot_loaded.setVisibility(View.GONE);
    }

    public void loaded() {
        linearLayout_foot_loading.setVisibility(View.GONE);
        textView_foot_load.setVisibility(View.GONE);
        textView_foot_loaded.setVisibility(View.VISIBLE);
    }

    public void load() {
        linearLayout_foot_loading.setVisibility(View.GONE);
        textView_foot_load.setVisibility(View.VISIBLE);
        textView_foot_loaded.setVisibility(View.GONE);
    }

    public View getLoading(){
        return textView_foot_load;
    }


}
