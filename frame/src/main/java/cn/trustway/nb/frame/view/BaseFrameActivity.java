package cn.trustway.nb.frame.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.trustway.nb.frame.R;
import cn.trustway.nb.frame.adapter.FrameAdapter;
import cn.trustway.nb.frame.model.TabModel;


public abstract class BaseFrameActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        context = this;
    }


    protected abstract int setLayout();

    protected abstract List<LazyLoadFragment> setFragmentList();

    protected abstract List<TabModel> setTabList();

    protected void initView(@NonNull TabLayout tabLayout, @NonNull final ViewPager viewPager) {

        List<LazyLoadFragment> fragments = setFragmentList();
        List<TabModel> tabs = setTabList();

        final FrameAdapter frameAdapter = new FrameAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(frameAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.tab, null);
            TextView tv = view.findViewById(R.id.tv);
            tv.setText(tabs.get(i).getText());
            ImageView img = view.findViewById(R.id.iv);
            img.setImageResource(tabs.get(i).getIcon());
            tabLayout.getTabAt(i).setCustomView(view);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                clear();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void clear() {

    }


}

