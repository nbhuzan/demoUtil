package cn.trustway.nb.frame.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public abstract class LazyLoadFragment extends Fragment {

    private boolean isLoad = false;
    private boolean isInit = false;

    /**
     * 创建时间：2017/11/10
     * 创建者：huzan
     * 描述：设置页面
     */
    protected abstract int setContentView();

    /**
     * 创建时间：2017/11/10
     * 创建者：huzan
     * 描述：加载数据
     */
    protected abstract  void startLoad();

    /**
     * 创建时间：2017/11/10
     * 创建者：huzan
     * 描述：停止加载
     */
    protected abstract void stopLoad();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isInit = true;
        isLoad();
        return inflater.inflate(setContentView(), container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isLoad();
    }

    private void isLoad() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            isLoad = true;
            startLoad();
        } else {
            if (isLoad) {
                isLoad = false;
                stopLoad();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoad = false;
    }
}
