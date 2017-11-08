package cn.trustway.nb.trustwayutil;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.base.util.RecyclerViewDivider;
import cn.trustway.nb.core.base.view.TrustwayRecycleView;
import cn.trustway.nb.core.menu.adapter.DialogMenuAdapter;
import cn.trustway.nb.core.menu.model.MenuModel;
import cn.trustway.nb.core.search.model.SearchModel;
import cn.trustway.nb.core.search.view.Search;

/**
 * Created by huzan on 2017/11/8.
 * 描述：
 */

public class SearchActivity extends Activity {
    TrustwayRecycleView recycleView;
    Search search;
    List<MenuModel> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = findViewById(R.id.search);
        recycleView = findViewById(R.id.recycle);
        for (int i = 0; i < 100; i++) {
            MenuModel menuModel = new MenuModel();
            menuModel.setKey(i + "");
            menuModel.setValue("测试" + i);
            list.add(menuModel);
        }
        DialogMenuAdapter adapter = new DialogMenuAdapter(this,false);
        adapter.setList(list,null);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false){
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        recycleView.addItemDecoration(new RecyclerViewDivider(this,
                LinearLayoutManager.VERTICAL,
                2,
                ContextCompat.getColor(this, cn.trustway.nb.core.R.color.grey)));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setScrollHeaderFooterStateListener(new TrustwayRecycleView.ScrollHeaderFooterStateListener() {
            @Override
            public void scrollToHeader() {
                search.setSearchScale(false);
            }

            @Override
            public void scrollToFooter() {

            }

            @Override
            public void scrollFromHeader() {
                search.setSearchScale(true);
            }

            @Override
            public void scrollFromFooter() {

            }
        });
        initSearch();


    }

    private void initSearch() {
        List<SearchModel> searchList = new ArrayList<>();
        SearchModel searchModel = new SearchModel();
        searchModel.setMenu("号牌号码");
        searchModel.setKeyboardType(Search.键盘_号牌);
        searchList.add(searchModel);
        searchModel = new SearchModel();
        searchModel.setMenu("驾驶证号");
        searchModel.setKeyboardType(Search.键盘_身份证);
        searchList.add(searchModel);

        search.setDefaultMenu("号牌号码");
        search.setMenuList(searchList);
        search.setSearchHint("查询了，可以点我");

        search.setOnSearchListener(new Search.OnSearchListener() {
            @Override
            public void onSearch(String menu, String searchForm) {
                //搜索结果，可做后续操作
                Toast.makeText(SearchActivity.this, menu + "-" + searchForm, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
