package cn.trustway.nb.core.search.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.search.model.SearchModel;


/**
 * Created by huzan on 2017/8/17.
 * 描述：
 */

public class Search extends LinearLayout {

    public static final int 键盘_普通 = 0;
    public static final int 键盘_号牌 = 1;
    public static final int 键盘_菜单 = 2;
    public static final int 键盘_字母数字 = 3;
    public static final int 键盘_数字 = 4;
    public static final int 键盘_身份证 = 5;

    private Context context;
    private LayoutInflater layoutInflater;
    private ImageView imageView_menu;
    private TextView textView_search;
    private OnSearchListener onSearchListener;
    private List<SearchModel> menuList = new ArrayList<>();
    private boolean isScaled = false;
    private String strDefaultMenu = "";


    public Search(Context context) {
        this(context, null);
    }

    public Search(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Search(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        initView();
    }

    /**
     * 创建时间：2017/8/18
     * 创建者：huzan
     * 描述：设置搜索选中
     */
    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    /**
     * 创建时间：2017/8/18
     * 创建者：huzan
     * 描述：设置菜单列表
     */
    public void setMenuList(List<SearchModel> menuList) {
        this.menuList = menuList;
    }
    public void setDefaultMenu(String str){
        this.strDefaultMenu = str!=null?str:"";
    }
    /**
     * 创建时间：2017/8/21
     * 创建者：huzan
     * 描述：收起收缩框
     */
    public void setSearchScale(boolean isScale) {
        if (isScale && !this.isScaled) {
            this.isScaled = true;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.search_menu_edit_out);
            textView_search.startAnimation(animation);
            imageView_menu.setVisibility(View.VISIBLE);
            textView_search.setVisibility(View.GONE);
        } else {
            this.isScaled = false;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.search_menu_edit_in);
            textView_search.startAnimation(animation);
            imageView_menu.setVisibility(View.GONE);
            textView_search.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 创建时间：2017/8/21
     * 创建者：huzan
     * 描述：设置hint
     */
    public void setSearchHint(String hint) {
        textView_search.setText(hint);
    }

    /**
     * 创建时间：2017/8/17
     * 创建者：huzan
     * 描述：初始化页面
     */
    private void initView() {
        View view = layoutInflater.inflate(R.layout.view_search_origin, this);
        imageView_menu = view.findViewById(R.id.imageview_search);
        textView_search = view.findViewById(R.id.textview_search);
        textView_search.setOnClickListener(v -> startSearch());
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_search_search);
        drawable.setBounds(0,
                0,
                (int) Math.min(drawable.getMinimumWidth(), context.getResources().getDimension(R.dimen.x14)),
                (int) Math.min(drawable.getMinimumHeight(), context.getResources().getDimension(R.dimen.y14)));
        textView_search.setCompoundDrawables(drawable, null, null, null);
        imageView_menu.setOnClickListener(v -> {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.search_menu_edit_in);
            textView_search.startAnimation(animation);
            imageView_menu.setVisibility(View.GONE);
            textView_search.setVisibility(View.VISIBLE);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    textView_search.setVisibility(GONE);
                    imageView_menu.setVisibility(VISIBLE);
                    startSearch();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        });

    }

    private void startSearch() {
        new DialogSearch.Build(context, menuList)
                .setOnSearch((menu, searchForm) -> {
                    if (onSearchListener != null) {
                        textView_search.setText(searchForm);
                        onSearchListener.onSearch(menu, searchForm);
                    }
                })
                .setDefaultMenu(strDefaultMenu)
                .create();
    }

    public interface OnSearchListener {
        void onSearch(String menu, String searchForm);
    }


}
