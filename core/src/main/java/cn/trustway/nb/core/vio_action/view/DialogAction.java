package cn.trustway.nb.core.vio_action.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.util.HiddenSoftInputUtil;
import cn.trustway.nb.core.base.util.RecyclerViewDivider;
import cn.trustway.nb.core.base.util.StringUtil;
import cn.trustway.nb.core.search.view.ViewSearch;
import cn.trustway.nb.core.vio_action.adapter.VioActionAdapter;
import cn.trustway.nb.core.vio_action.adapter.VioActionSelectedAdapter;
import cn.trustway.nb.core.vio_action.model.VioActionModel;
import cn.trustway.nb.core.vio_action.util.VioActionUtil;

/**
 * Created by huzan on 2017/8/23.
 * 描述：违法行为选择
 */

public class DialogAction extends Dialog {
    public DialogAction(@NonNull Context context) {
        this(context, 0);
    }

    public DialogAction(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.dialogNormal);
    }

    public static class Build {
        private Context context;
        private View view;
        private TextView textView_title;
        private TextView textView_submit;
        private ImageView imageView_cancel;
        private ViewSearch search;
        private RecyclerView recyclerView_select; //多选情况下选中的表
        private RecyclerView recyclerView_list; //搜索总表
        private TextView textView_list;//个人搜查标题
        private TextView textView_selected;
        private DialogAction dialogWfxw;

        private VioActionAdapter adapter;
        private List<VioActionModel> list; //筛选出来供选中的列表
        private List<VioActionModel> collectList; //收藏过的列表

        private VioActionSelectedAdapter selectedAdapter;
        private List<VioActionModel> selectList; //多选情况下选中的列表

        private boolean isSelectMore = false;//是否多选
        private int selectMax = 5; //多选情况下最大数量

        private OnSelectOne onSelectOne;
        private OnSelectMore onSelectMore;

        private List<VioActionModel> vioActionList = new ArrayList<>(); //原始数据
        private OnOperationListener onOperationListener;

        public Build(Context context) {
            this.context = context;
            dialogWfxw = new DialogAction(context);
            view = LayoutInflater.from(context).inflate(R.layout.dialog_vio_action, null);
            initView();
        }

        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：设置原始违法行为数据
         */
        public Build setVioActionList(List<VioActionModel> vioActionList) {
            this.vioActionList = vioActionList;
            return this;
        }

        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：设置收藏的违法行为数据
         */
        public Build setCollectVioActionList(List<VioActionModel> collectList) {
            this.collectList = collectList;
            return this;
        }

        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：设置操作回调
         */
        public Build setOnOperationListener(OnOperationListener onOperationListener) {
            this.onOperationListener = onOperationListener;
            return this;
        }


        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：多选情况下设置已选择的违法行为列表
         */
        public Build setSelectList(List<VioActionModel> selectList) {
            if (selectList != null && !selectList.isEmpty()) {
                this.selectList = selectList;
                showSelectedWidget(true);
                selectedAdapter.setItemList(selectList);
            }
            return this;
        }


        /**
         * 创建时间：2017/8/25
         * 创建者：huzan
         * 描述：是否多选
         */
        public Build isSelectMore(boolean isSelectMore) {
            this.isSelectMore = isSelectMore;
            if (isSelectMore) {
                textView_title.setText("违法行为(多选)");
            }
            return this;
        }

        /**
         * 创建时间：2017/8/25
         * 创建者：huzan
         * 描述：单选监听
         */
        public Build setOnSelectOne(OnSelectOne onSelectOne) {
            this.onSelectOne = onSelectOne;
            return this;
        }

        /**
         * 创建时间：2017/8/25
         * 创建者：huzan
         * 描述：多选监听
         */
        public Build setOnSelectMore(OnSelectMore onSelectMore) {
            this.onSelectMore = onSelectMore;
            return this;
        }

        /**
         * 创建时间：2017/8/25
         * 创建者：huzan
         * 描述：设置多选最大条数
         */
        public Build setSelectMax(int count) {
            this.selectMax = count;
            return this;
        }

        /**
         * 创建时间：2017/8/25
         * 创建者：huzan
         * 描述：创建显示dialog
         */
        public void create() {
            dialogWfxw.show();
            dialogWfxw.setContentView(view);
            Window window = dialogWfxw.getWindow();
            WindowManager.LayoutParams params;
            if (window != null) {
                window.setWindowAnimations(R.style.carNumAnim);
                params = window.getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.TOP;
                window.setAttributes(params);
            }
            dialogWfxw.setOnDismissListener(dialog -> HiddenSoftInputUtil.hiddenSoftInput(context));
            getPreferenceList();
        }

        /**
         * 创建时间：2017/8/25
         * 创建者：huzan
         * 描述：设置多选的选中列表是否显示
         */
        @SuppressLint("DefaultLocale")
        private void showSelectedWidget(boolean isShow) {
            if (isShow) {
                textView_selected.setVisibility(View.VISIBLE);
                if (recyclerView_select.getVisibility() == View.VISIBLE) {
                    textView_selected.setText(String.format("已选：%d条(点击展开详情)", selectList.size()));
                } else {
                    textView_selected.setText(String.format("已选：%d条(点击关闭详情)", selectList.size()));
                }
            } else {
                textView_selected.setVisibility(View.GONE);
            }
        }


        /**
         * 创建时间：2017/8/24
         * 创建者：huzan
         * 描述：初始化页面
         */
        @SuppressLint("DefaultLocale")
        private void initView() {
            textView_title = view.findViewById(R.id.textview_dialog_wfxw_title);
            textView_submit = view.findViewById(R.id.textview_dialog_wfxw_submit);
            imageView_cancel = view.findViewById(R.id.imageview_dialog_wfxw_cancel);
            search = view.findViewById(R.id.search_dialog_wfxw_search);
            recyclerView_list = view.findViewById(R.id.recycleview_dialog_wfxw_list);
            recyclerView_select = view.findViewById(R.id.recycleview_dialog_wfxw_select);
            textView_list = view.findViewById(R.id.textview_dialog_wfxw_list_title);
            imageView_cancel.setOnClickListener(v -> {
                HiddenSoftInputUtil.hiddenSoftInput(context);
                dialogWfxw.dismiss();
            });
            textView_selected = view.findViewById(R.id.textview_dialog_wfxw_select_title);
            textView_selected.setOnClickListener(v -> {
                if (recyclerView_select.getVisibility() == View.VISIBLE) {
                    textView_selected.setText(String.format("已选：%d条(点击展开详情)", selectList.size()));
                    recyclerView_select.setVisibility(View.GONE);
                } else {
                    textView_selected.setText(String.format("已选：%d条(点击关闭详情)", selectList.size()));
                    recyclerView_select.setVisibility(View.VISIBLE);
                }
            });
            textView_submit.setOnClickListener(v -> {
                HiddenSoftInputUtil.hiddenSoftInput(context);
                dialogWfxw.dismiss();

                if (onSelectMore != null) {
                    onSelectMore.onSelect(selectList);
                }
            });
            search.setOnTextWatcher(new ViewSearch.TextWatcherListener() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //搜索过滤
                    String searchStr = editable.toString();
                    adapter.setKey(searchStr);
                    if (searchStr.matches(StringUtil.regularNumber())) {
                        list = VioActionUtil.filterVioActionList(vioActionList, searchStr, VioActionUtil.KEY_TYPE_KEY);
                        adapter.setItemList(list);
                        textView_list.setText("违法行为");
                    } else if (searchStr.equals("")) {
                        getPreferenceList();

                    } else {
                        list = VioActionUtil.filterVioActionList(vioActionList, searchStr, VioActionUtil.KEY_TYPE_NAME);
                        adapter.setItemList(list);
                        textView_list.setText("违法行为");
                    }
                }
            });
            initRecycleView();
            initRecycleViewSelected();

        }

        private void initRecycleView() {
            list = new ArrayList<>();
            adapter = new VioActionAdapter(context, collectList);
            adapter.setItemList(list);
            recyclerView_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView_list.setAdapter(adapter);
            recyclerView_list.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 2, 0));
            recyclerView_list.setItemAnimator(new DefaultItemAnimator());
            adapter.setListener((v, position) -> {
                if (!isSelectMore && onSelectOne != null) {
                    onSelectOne.onSelect(list.get(position));
                    dialogWfxw.dismiss();
                    return;
                }
                if (isSelectMore) {
                    VioActionModel wfdm = list.get(position);
                    boolean isHave = false;
                    for (VioActionModel w : selectList) {
                        if (w.getWfnr().equals(wfdm.getWfnr())) {
                            isHave = true;
                            break;
                        }
                    }
                    if (!isHave) {
                        if (selectList.size() >= selectMax) {
                            Toast.makeText(context, "最多只能选择" + selectMax + "条违法行为", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selectList.add(list.get(position));
                        selectedAdapter.addItem(list.get(position), 0);
                        recyclerView_select.scrollToPosition(0);
                        showSelectedWidget(true);
                    } else {
                        Toast.makeText(context, "请勿重复选择", Toast.LENGTH_SHORT).show();
                    }
                }

            });
            adapter.setOnCollectListener(new VioActionAdapter.OnCollectListener() {
                @Override
                public void addCollect(int pos) {
                    if (onOperationListener != null) {
                        onOperationListener.onAddCollect(list.get(pos));
                        collectList.add(0, list.get(pos));
                    }
                }

                @Override
                public void deleteCollect(int pos) {
                    if (onOperationListener != null) {
                        onOperationListener.onDeleteCollect(list.get(pos));
                        for (VioActionModel vioActionModel : collectList) {
                            if (vioActionModel.getWfxw().equals(list.get(pos).getWfxw())) {
                                collectList.remove(list.get(pos));
                                break;
                            }
                        }
                    }
                }
            });

        }

        private void initRecycleViewSelected() {
            selectList = new ArrayList<>();
            selectedAdapter = new VioActionSelectedAdapter(context);
            selectedAdapter.setItemList(selectList);
            recyclerView_select.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView_select.setAdapter(selectedAdapter);
            recyclerView_select.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 2, 0));
            recyclerView_select.setItemAnimator(new DefaultItemAnimator());
            selectedAdapter.setListener((v, position) -> {
                selectedAdapter.removeItem(position);
                selectList.remove(position);
                showSelectedWidget(!selectList.isEmpty());
            });
        }


        private void getPreferenceList() {
            //获取常用违法行为
            list = collectList;
            adapter.setItemList(list);
            adapter.setPreferenceList(collectList);
            textView_list.setText("个人收藏");
        }
    }

    /**
     * 创建时间：2017/8/25
     * 创建者：huzan
     * 描述：单选回调
     */
    public interface OnSelectOne {
        void onSelect(VioActionModel vioWfdm);
    }

    /**
     * 创建时间：2017/8/25
     * 创建者：huzan
     * 描述：多选回调
     */
    public interface OnSelectMore {
        void onSelect(List<VioActionModel> vioWfdms);
    }

    public interface OnOperationListener {
        void onAddCollect(VioActionModel vioActionModel);

        void onDeleteCollect(VioActionModel vioActionModel);
    }

}
