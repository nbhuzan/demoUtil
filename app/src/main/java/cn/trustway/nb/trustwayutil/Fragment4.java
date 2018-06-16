package cn.trustway.nb.trustwayutil;

import android.view.View;
import android.widget.Toast;

import cn.trustway.nb.frame.view.LazyLoadFragment;
import cn.trustway.nb.rootview.RootView;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public class Fragment4 extends LazyLoadFragment {

    RootView rootView;
    @Override
    protected int setContentView() {
        return R.layout.activity_4;
    }

    @Override
    protected void initView(View v) {

    }

    @Override
    protected void startLoad() {
        Toast.makeText(getActivity(), "4开始加载", Toast.LENGTH_SHORT).show();
        rootView = getActivity().findViewById(R.id.view);
        rootView.showFail(() -> rootView.showLoading());
    }

    @Override
    protected void stopLoad() {
        Toast.makeText(getActivity(), "4停止加载，释放", Toast.LENGTH_SHORT).show();
    }
}
