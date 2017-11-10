package cn.trustway.nb.trustwayutil;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.frame.model.TabModel;
import cn.trustway.nb.frame.view.BaseFrameActivity;
import cn.trustway.nb.frame.view.LazyLoadFragment;

/**
 * Created by huzan on 2017/11/9.
 * 描述：
 */

public class FrameActivity extends BaseFrameActivity {
    @Override
    protected int setLayout() {
        return R.layout.activity_frame;
    }

    @Override
    protected List<LazyLoadFragment> setFragmentList() {
        List<LazyLoadFragment> list = new ArrayList<>();
        list.add(new Fragment1());
        list.add(new Fragment2());
        list.add(new Fragment3());
        list.add(new Fragment4());
        return list;
    }

    @Override
    protected List<TabModel> setTabList() {
        List<TabModel> list = new ArrayList<>();
        TabModel model = new TabModel();
        model.setText("首页");
        model.setIcon(R.drawable.home_bottom_home);
        list.add(model);
         model = new TabModel();
        model.setText("业务");
        model.setIcon(R.drawable.home_bottom_function);
        list.add(model);
         model = new TabModel();
        model.setText("我的");
        model.setIcon(R.drawable.home_bottom_my);
        list.add(model);
 model = new TabModel();
        model.setText("消息");
        model.setIcon(R.drawable.home_bottom_message);
        list.add(model);

        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(findViewById(R.id.tablayout),findViewById(R.id.viewpager));
    }

    @Override
    protected void initView(@NonNull TabLayout tabLayout, @NonNull ViewPager viewPager) {
        super.initView(tabLayout, viewPager);

    }
}
