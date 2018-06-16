package cn.trustway.nb.trustwayutil;

import android.view.View;
import android.widget.Toast;

import cn.trustway.nb.frame.view.LazyLoadFragment;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public class Fragment2 extends LazyLoadFragment {

    @Override
    protected int setContentView() {
        return R.layout.activity_2;
    }

    @Override
    protected void initView(View v) {

    }

    @Override
    protected void startLoad() {
        Toast.makeText(getActivity(), "2开始加载", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void stopLoad() {
        Toast.makeText(getActivity(), "2停止加载，释放", Toast.LENGTH_SHORT).show();
    }
}
