package cn.trustway.nb.core.search.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.util.HiddenSoftInputUtil;
import cn.trustway.nb.core.base.util.RecyclerViewDivider;
import cn.trustway.nb.core.input_cardid.view.DialogIdCardIdInput;
import cn.trustway.nb.core.input_carnum.view.DialogCarNumInput;
import cn.trustway.nb.core.search.adapter.SearchHistoryAdapter;
import cn.trustway.nb.core.search.adapter.SearchMenuAdapter;
import cn.trustway.nb.core.search.model.SearchModel;


/**
 * Created by huzan on 2017/8/17.
 * 描述：搜索弹出页面
 */

class DialogSearch extends Dialog {


    public DialogSearch(@NonNull Context context) {
        this(context, 0);
    }

    public DialogSearch(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.dialogNormal);
    }

    public static class Build {
        private Context context;
        private DialogSearch dialogSearch;
        private View view;
        private EditText editText_form;
        private RecyclerView recyclerView_menu;
        private RecyclerView recyclerView_history;
        private List<String> historyList;
        private SearchHistoryAdapter historyAdapter;
        @Nullable
        private List<SearchModel> menuList;
        private SearchMenuAdapter menuAdapter;
        private int checkPos; //选中的pos
        private Search.OnSearchListener onSearch;
        private TextView textView_cancel;
        private LinearLayout linearlayout_parent;

        @NonNull
        public Build setOnSearch(Search.OnSearchListener onSearch) {
            this.onSearch = onSearch;
            return this;
        }

        public Build(@NonNull Context context, @Nullable List<SearchModel> menuList) {
            this.context = context;
            dialogSearch = new DialogSearch(context);
            historyList = new ArrayList<>();
            if (menuList == null || menuList.isEmpty()) {
                menuList = new ArrayList<>();
            }
            this.menuList = menuList;
            initView();
        }

        /**
         * 创建时间：2017/8/18
         * 创建者：huzan
         * 描述：设置默认键盘
         */
        @NonNull
        public Build setDefaultMenu(@NonNull String menu) {
            for (int i = 0; i < menuList.size(); i++) {
                SearchModel model = menuList.get(i);
                if (model.getMenu().equals(menu)) {
                    checkPos = i;
                }
            }
            return this;
        }


        public void create() {
            menuAdapter.setItemCheck(checkPos);
            dialogSearch.setOnShowListener(dialog -> selectKeyBoard(getKeyType(checkPos)));
            dialogSearch.setOnDismissListener(dialog -> {
//                    HiddenSoftInputUtil.hiddenSoftInput(context);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(editText_form.getWindowToken(), 0);
                }
            });
            dialogSearch.show();
            dialogSearch.setContentView(view);
            Window window = dialogSearch.getWindow();
            WindowManager.LayoutParams params;
            if (window != null) {
                window.setWindowAnimations(R.style.searchAnim);
                params = window.getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.BOTTOM;
                window.setAttributes(params);
            }
        }

        private void initView() {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_search, null);
            editText_form = view.findViewById(R.id.edittext_search_form);
            editText_form.setOnClickListener(v -> {
                //选中默认的键盘
                selectKeyBoard(getKeyType(checkPos));
            });
            linearlayout_parent = view.findViewById(R.id.linearlayout_search_parent);
            recyclerView_menu = view.findViewById(R.id.recycle_search_menu);
            recyclerView_history = view.findViewById(R.id.recycle_search_history);
            Drawable drawableLeft = ContextCompat.getDrawable(context, R.drawable.ic_search_search);
            drawableLeft.setBounds(0, 0, (int) context.getResources().getDimension(R.dimen.x16), (int) context.getResources().getDimension(R.dimen.y16));
            editText_form.setCompoundDrawables(drawableLeft, null, null, null);
            textView_cancel = view.findViewById(R.id.textview_search_cancel);
            textView_cancel.setOnClickListener(v -> {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null) {
                    imm.hideSoftInputFromWindow(editText_form.getWindowToken(), 0);
                }
                dialogSearch.dismiss();
            });

            historyAdapter = new SearchHistoryAdapter(context);
            recyclerView_history.setAdapter(historyAdapter);
            recyclerView_history.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView_history.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 2, 0));
            recyclerView_history.setItemAnimator(new DefaultItemAnimator());
            historyAdapter.setItemList(historyList);

            menuAdapter = new SearchMenuAdapter(context);
            recyclerView_menu.setAdapter(menuAdapter);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            recyclerView_menu.setLayoutManager(gridLayoutManager);
            recyclerView_menu.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                    int childCount = parent.getChildCount();
                    Paint paint = new Paint();
                    for (int i = 0; i < childCount - 1; i++) {
                        paint.setColor(ContextCompat.getColor(context, R.color.grey));
                        View view = parent.getChildAt(i);
                        float top = view.getTop() + context.getResources().getDimension(R.dimen.y15);
                        float bottom = view.getTop() + view.getHeight() - context.getResources().getDimension(R.dimen.y15);
                        float left = view.getRight();
                        int right = view.getRight() + 2;
                        c.drawRect(left, top, right, bottom, paint);  //画右侧边界线
                    }
                }


                @Override
                public void getItemOffsets(@NonNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    GridLayoutManager.LayoutParams gl = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                    int spanSize = gl.getSpanSize();
                    int spanIndex = gl.getSpanIndex();
                    if (spanSize != gridLayoutManager.getSpanCount()) {
                        if (spanIndex < 2) {
                            outRect.right = 2;
                        }
                    }

                }
            });
            recyclerView_menu.setItemAnimator(new DefaultItemAnimator());
            menuAdapter.setItemList(menuList);

            //选择搜索条件及输入法，如果重复选则不做操作
            menuAdapter.setListener((v, position) -> {
                if (position != checkPos) {
                    selectKeyBoard(getKeyType(position));
                }
            });

            editText_form.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    HiddenSoftInputUtil.hiddenSoftInput(editText_form);
                    makeSearchForm(editText_form.getText().toString());
                    return true;
                }
                return false;
            });

        }

        /**
         * 创建时间：2017/8/18
         * 创建者：huzan
         * 描述：生成搜索表单回调
         */
        private void makeSearchForm(String str) {
            if (onSearch != null) {
                dialogSearch.dismiss();
                onSearch.onSearch(menuList.get(checkPos).getMenu(), str);
            }
        }

        /**
         * 创建时间：2017/8/18
         * 创建者：huzan
         * 描述：根据pos获取keytype
         */
        private int getKeyType(int pos) {
            checkPos = pos;
            return menuList.get(pos).getKeyboardType();
        }

        /**
         * 创建时间：2017/8/18
         * 创建者：huzan
         * 描述：选择键盘
         */

        private void selectKeyBoard(int type) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm!=null) {
                imm.hideSoftInputFromWindow(editText_form.getWindowToken(), 0);
            }
            editText_form.setInputType(InputType.TYPE_CLASS_TEXT);
            switch (type) {
                case Search.键盘_号牌:
                    new DialogCarNumInput.Build(context,"")
                            .setOnSubmitListener(this::makeSearchForm)
                            .create();
                    break;
                case Search.键盘_字母数字:

//                    DialogSafeKeyBoard dialogSafeKeyBoard = new DialogSafeKeyBoard(context, "");
//                    dialogSafeKeyBoard.setOnSubmitListener(this::makeSearchForm);
//                    dialogSafeKeyBoard.show();
                    break;
                case Search.键盘_数字:
                    editText_form.setInputType(InputType.TYPE_CLASS_NUMBER);
                    if(imm!=null) {
                        imm.showSoftInput(editText_form, InputMethodManager.SHOW_FORCED);
                    }
                    break;
                case Search.键盘_菜单:

                    break;
                case Search.键盘_身份证:
                    new DialogIdCardIdInput.Build(context)
                            .setOnSubmitListener(this::makeSearchForm)
                            .create();
                    break;
                default:
                    //合普通
                    if(imm!=null) {
                        imm.showSoftInput(editText_form, InputMethodManager.SHOW_FORCED);
                    }
                    break;
            }
        }


    }


}
