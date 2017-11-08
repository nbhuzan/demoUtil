package cn.trustway.nb.core.base.listener;


import android.view.View;

/**
 * Created by huzan  2017/3/17 17:30.
 * 描述：暴露recycleview 点击item接口
 */

public interface RecycleViewClickListener {
    void OnItemClickListener(View v, int position);
}
