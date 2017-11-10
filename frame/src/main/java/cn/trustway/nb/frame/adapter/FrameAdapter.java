package cn.trustway.nb.frame.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.trustway.nb.frame.view.LazyLoadFragment;

/**
 * Created by huzan on 2017/11/10.
 * 描述：
 */

public class FrameAdapter extends FragmentPagerAdapter {
    private List<LazyLoadFragment> fragmentList;

    public FrameAdapter(FragmentManager fm, List<LazyLoadFragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
