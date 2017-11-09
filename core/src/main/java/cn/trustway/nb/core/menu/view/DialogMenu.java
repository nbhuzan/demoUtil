package cn.trustway.nb.core.menu.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.listener.RecycleViewClickListener;
import cn.trustway.nb.core.base.util.RecyclerViewDivider;
import cn.trustway.nb.core.base.view.TrustwayRecycleView;
import cn.trustway.nb.core.menu.adapter.DialogMenuAdapter;
import cn.trustway.nb.core.menu.model.MenuModel;
import cn.trustway.nb.core.search.view.ViewSearch;


/**
 * Created by huzan on 2017/8/10.
 * 描述：通用功能菜单选择
 */

public class DialogMenu extends Dialog {

    public static final int SORT_KEY = 0; //根据key排序
    public static final int SORT_VALUE = 1; //根据value排序


    public DialogMenu(@NonNull Context context) {
        this(context, 0);
    }

    private DialogMenu(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.Fingerprint);
    }

    public static class Build {
        private Context context;
        private ImageView imageView_cancel;
        private TextView textView_title;
        private TextView textView_submit;
        private TextView textView_empty;
        private TrustwayRecycleView recycleView;
        private ViewSearch search_search;

        private OnClickSubmit onClickSubmit;
        private OnSelectSubmit onSelectSubmit;

        private DialogMenu dialogMenu;

        private boolean canSelect = false;//是否可多选
        @NonNull
        private List<MenuModel> list = new ArrayList<>(); //数据源
        @Nullable
        private List<MenuModel> selectList = new ArrayList<>();//多选情况下的选中数据
        private boolean canSort = false; //是否可排序
        private DialogMenuAdapter adapter;

        public Build(@NonNull Context context){
            this(context,false);
        }
        public Build(@NonNull Context context, boolean canSelect) {
            this.context = context;
            this.canSelect = canSelect;
            dialogMenu = new DialogMenu(context,R.style.Fingerprint);
            initView(dialogMenu);
        }


        @NonNull
        public Build setTitle(String title){
            if(canSelect){
                textView_title.setText(title+"(多选)");
            }else{
                textView_title.setText(title);
            }
            return this;
        }

        /**
         * 创建时间：2017/8/11
         * 创建者：huzan
         * 描述：设置数据源
         */
        @NonNull
        public Build setList(@Nullable List<MenuModel> list) {
            if (list != null) {
                this.list.clear();
                this.list.addAll(list);
            }
            return this;
        }

        /**
         * 创建时间：2017/8/11
         * 创建者：huzan
         * 描述：设置是否可分组排序
         */
        @NonNull
        public Build setCanSort(boolean canSort) {
            this.canSort = canSort;
            return this;
        }

        @NonNull
        public Build setOnClickSubmit(OnClickSubmit onClickSubmit) {
            this.onClickSubmit = onClickSubmit;
            return this;
        }

        @NonNull
        public Build setOnSelectSubmit(OnSelectSubmit onSelectSubmit) {
            this.onSelectSubmit = onSelectSubmit;
            return this;
        }

        /**
         * 创建时间：2017/8/11
         * 创建者：huzan
         * 描述：多选情况下设置选中的数据
         */
        @NonNull
        public Build setSelectList(@Nullable List<MenuModel> selectList) {
            if (selectList != null) {
                this.selectList = selectList;
            }else{
                this.selectList = new ArrayList<>();
            }
            return this;
        }


        public void create() {

            dialogMenu.show();
            Window window = dialogMenu.getWindow();
            WindowManager.LayoutParams params;
            if (window != null) {
                window.setWindowAnimations(R.style.carNumAnim);
                params = window.getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                params.height = (int) context.getResources().getDimension(R.dimen.y480);
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.BOTTOM;
                window.setAttributes(params);
            }

            adapter.setOnClickListener(new RecycleViewClickListener() {
                @Override
                public void OnItemClickListener(View v, int position) {
                    if(onClickSubmit!=null){
                        dialogMenu.dismiss();
                        onClickSubmit.onClick(adapter.getClickItem(position));
                    }
                }
            });
            adapter.setList(list, selectList);

        }

        private void initView(@NonNull DialogMenu dialogMenu) {
            dialogMenu.setContentView(R.layout.dialog_menu);
            imageView_cancel = (ImageView) dialogMenu.findViewById(R.id.imageview_menu_cancel);
            textView_title = (TextView) dialogMenu.findViewById(R.id.textview_menu_title);
            textView_submit = (TextView) dialogMenu.findViewById(R.id.textview_menu_submit);
            textView_empty = (TextView) dialogMenu.findViewById(R.id.textview_menu_empty);
            recycleView = dialogMenu.findViewById(R.id.recycleview_menu_content);
            search_search = dialogMenu.findViewById(R.id.search_menu_search);
            textView_title.setText("菜单");
            adapter = new DialogMenuAdapter(context, canSelect);

            recycleView.setAdapter(adapter);
            recycleView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false){
                @Override
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            });
            recycleView.addItemDecoration(new RecyclerViewDivider(context,
                    LinearLayoutManager.VERTICAL,
                    2,
                    ContextCompat.getColor(context, R.color.gray)));
            recycleView.setItemAnimator(new DefaultItemAnimator());
            imageView_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMenu.dismiss();
                }
            });
            textView_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMenu.dismiss();
                    if(onSelectSubmit!=null){
                        onSelectSubmit.onSelect(adapter.getSelectItems());
                    }
                }
            });
            textView_empty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canSelect) {
                        adapter.clearSelectedItems();
                    } else {
                        if(onClickSubmit!=null){
                            dialogMenu.dismiss();
                            onClickSubmit.onClick(null);
                        }
                    }
                }
            });
            search_search.setOnTextWatcher(new ViewSearch.TextWatcherListener() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(@NonNull Editable editable) {

                    adapter.setList(filterList(editable.toString()), selectList);
                }
            });
        }

        /**
         *创建时间：2017/8/14
         *创建者：huzan
         *描述：过滤
         */
        @NonNull
        private List<MenuModel> filterList(@Nullable String str){
            if(str==null||str.length()==0){
                return list;
            }else{
                List<MenuModel> tempList = new ArrayList<>();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    MenuModel dictionary = list.get(i);
                    if(dictionary.getKey().contains(str.toUpperCase())||dictionary.getValue().contains(str.toUpperCase())){
                        tempList.add(dictionary);
                    }
                }
                return tempList;
            }
        }
    }

    public interface OnClickSubmit{
        void onClick(MenuModel dictionary);
    }

    public interface OnSelectSubmit{
        void onSelect(List<MenuModel> list);
    }

}
