package cn.trustway.nb.trustwayutil;

import android.widget.Toast;

import cn.trustway.nb.frame.view.LazyLoadFragment;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public class Fragment3 extends LazyLoadFragment {

    @Override
    protected int setContentView() {
        return R.layout.activity_2;
    }

    @Override
    protected void startLoad() {
        Toast.makeText(getActivity(), "3开始加载", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void stopLoad() {
        Toast.makeText(getActivity(), "3停止加载，释放", Toast.LENGTH_SHORT).show();
    }
}
