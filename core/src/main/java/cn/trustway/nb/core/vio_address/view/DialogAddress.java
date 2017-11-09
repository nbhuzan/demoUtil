package cn.trustway.nb.core.vio_address.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.util.HiddenSoftInputUtil;
import cn.trustway.nb.core.base.util.RecyclerViewDivider;
import cn.trustway.nb.core.base.util.StringUtil;
import cn.trustway.nb.core.base.view.TrustwayRecycleView;
import cn.trustway.nb.core.prompt.view.DialogNormal;
import cn.trustway.nb.core.search.view.ViewSearch;
import cn.trustway.nb.core.vio_address.adapter.AddressAdapter;
import cn.trustway.nb.core.vio_address.model.RoadModel;


/**
 * Created by huzan on 2017/8/28.
 * 描述：违法地址选择
 */

public class DialogAddress extends Dialog {
    public static final int KEY_TYPE_KEY = 0;
    public static final int KEY_TYPE_NAME = 1;

    public DialogAddress(@NonNull Context context) {
        this(context, 0);
    }

    public DialogAddress(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.dialogNormal);
    }

    public static class Build {
        private View view;
        private ImageView imageView_cancel;
        private TextView textView_title;
        private TextView textView_submit;
        private ViewSearch search_search;
        private TextView textView_location;
        private RadioGroup radioGroup_dm;
        private RoadRadioButton radioButton_dldm;
        private RoadRadioButton radioButton_lddm;
        private RoadRadioButton radioButton_ddms;
        private TextView textView_list_title;
        private TrustwayRecycleView recyclerView_list;

        private Context context;
        private DialogAddress dialogAddress;
        String hintRoad;
        String hintRoadSection;

        AddressAdapter adapter;
        List<RoadModel> list;
        String searchStr;
        RoadModel road; //临时存储road信息
        private String xzqh = "";
        DialogNormal.Build dialog;
        String roadStr;
        String roadSectionStr;

        private OnOperationListener onOperationListener;
        private RoadModel selectRoad;//存储道路信息，方便判断是否被收藏
        private boolean isExist = false;//当前选中道路是否已收藏

        private List<RoadModel> roadList = new ArrayList<>(); //道路列表
        private List<RoadModel> roadSectionList = new ArrayList<>();//路段列表
        private List<RoadModel> collectList = new ArrayList<>();//我收藏的道路列表

        public Build setOnOperationListener(OnOperationListener onOperationListener) {
            this.onOperationListener = onOperationListener;
            return this;
        }

        public Build(Context context) {
            this.context = context;
            dialog = new DialogNormal.Build(context);
            hintRoad = context.getResources().getString(R.string.input_road);
            hintRoadSection = context.getResources().getString(R.string.input_road_section);

            dialogAddress = new DialogAddress(context);
            initView();
        }

        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：设置自己管辖区域的道路信息
         */
        public Build setRoadList(List<RoadModel> roadList) {
            this.roadList = roadList;
            return this;
        }

        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：设置自己管辖区域的路段信息
         */
        public Build setRoadSectionList(List<RoadModel> roadSectionList) {
            this.roadSectionList = roadSectionList;
            return this;
        }

        public Build setCollectList(List<RoadModel> collectList) {
            this.collectList = collectList;
            list = collectList;
            adapter.setItemList(list);
            return this;
        }


        public void create() {
            dialogAddress.show();
            dialogAddress.setContentView(view);
            Window window = dialogAddress.getWindow();
            WindowManager.LayoutParams params;
            if (window != null) {
                window.setWindowAnimations(R.style.carNumAnim);
                params = window.getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.TOP;
                window.setAttributes(params);
            }
            dialogAddress.setOnDismissListener(dialog -> HiddenSoftInputUtil.hiddenSoftInput(context));
        }

        private void initView() {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_vio_address, null);
            imageView_cancel = view.findViewById(R.id.imageview_dialog_address_cancel);
            textView_title = view.findViewById(R.id.textview_dialog_address_title);
            textView_submit = view.findViewById(R.id.textview_dialog_address_submit);
            search_search = view.findViewById(R.id.search_dialog_address_search);
            textView_location = view.findViewById(R.id.textview_address_location);
            radioGroup_dm = view.findViewById(R.id.radiogroup_address_dm);
            radioButton_dldm = view.findViewById(R.id.radiobutton_address_dldm);
            radioButton_lddm = view.findViewById(R.id.radiobutton_address_lddm);
            radioButton_ddms = view.findViewById(R.id.radiobutton_address_dlms);
            textView_list_title = view.findViewById(R.id.textview_dialog_address_list_title);
            recyclerView_list = view.findViewById(R.id.recycleview_dialog_address_list);
            initRecycleView();
            radioGroup_dm.setOnCheckedChangeListener((radioGroup, i) -> {
                if (i == R.id.radiobutton_address_dldm) {
                    adapter.clearItemList();
                    search_search.setHintText(hintRoad);
                    search_search.setText(radioButton_dldm.getTextMessage());
                    search_search.setSection(radioButton_dldm.getTextMessage().length());

                } else if (i == R.id.radiobutton_address_lddm) {
                    if (radioButton_dldm.getTextMessage().length() <= 0) {
                        Toast.makeText(context, "请先选择道路代码", Toast.LENGTH_SHORT).show();
                        radioButton_dldm.setChecked(true);
                        return;
                    }
                    adapter.setKey("");
                    search_search.setHintText(hintRoadSection);
                    list = filterRoadSectionList(road.getDldm(), "", KEY_TYPE_KEY);
                    adapter.setItemList(list);
                    if (list.size() == 0) { //无路段
                        if (road.getQs() == null || road.getJs() == null) {
                            Toast.makeText(context, "道路有问题", Toast.LENGTH_SHORT).show();
                            radioButton_dldm.setChecked(true);
                            return;
                        } else {
                            showDialogGls(Integer.valueOf(road.getQs()), Integer.valueOf(road.getJs()));
                        }
                    } else {
                        search_search.setText(radioButton_lddm.getTextMessage());
                        search_search.setSection(radioButton_lddm.getTextMessage().length());
                        // list = DictionaryUtil.getRoadSectionListByLddm(radioButton_lddm.getTextMessage(),road.getDldm());
                    }

                } else if (i == R.id.radiobutton_address_dlms) {
                    if (radioButton_lddm.getTextMessage().length() <= 0) {
                        Toast.makeText(context, "请先选择路段代码/公里数", Toast.LENGTH_SHORT).show();
                        radioButton_lddm.setChecked(true);
                        return;
                    } else {
                        search_search.setHintText("");
                        search_search.setText("");
                        if (road.getLddm() != null && road.getQsms() != null && road.getJsms() != null) {
                            showDialogMs(road.getQsms(), road.getJsms());
                        } else {
                            showDialogMs(0, 999);
                        }
                    }

                } else {
                }
            });

            imageView_cancel.setOnClickListener(v -> {
                dialogAddress.dismiss();
                HiddenSoftInputUtil.hiddenSoftInput(context);
            });
            textView_submit.setOnClickListener(v -> {
                if (radioButton_dldm.getTextMessage().length() + radioButton_lddm.getTextMessage().length() + radioButton_ddms.getTextMessage().length() != 12) {
                    Toast.makeText(context, "选择的地址代码长度应为12位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onOperationListener != null) {
                    dialogAddress.dismiss();
                    onOperationListener.onSubmit(radioButton_dldm.getTextMessage(), radioButton_lddm.getTextMessage(), radioButton_ddms.getTextMessage(), textView_location.getText().toString());
                }
            });

            //是否收藏
            textView_location.setOnClickListener(v -> {
                boolean isSuccess = doPreference(selectRoad);  //操作是否成功
                if (isSuccess) {
                    makePreferenceIcon(!isExist);
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
                public void afterTextChanged(Editable editable) {
                    searchStr = editable.toString();
                    if (!searchStr.isEmpty()) {
                        textView_list_title.setVisibility(View.GONE);
                        if (search_search.getHintText().equals(hintRoad)) {
                            //道路查询
                            if (searchStr.matches(StringUtil.regularNumber())) {
                                list = filterRoadList(searchStr, KEY_TYPE_KEY);
                            } else {
                                list = filterRoadList(searchStr, KEY_TYPE_NAME);
                            }
                            adapter.setKey(searchStr);
                            adapter.setItemList(list);
                        } else if (searchStr != null && search_search.getHintText().equals(hintRoadSection)) {
                            //路段查询
                            if (searchStr.matches(StringUtil.regularNumber())) {
                                list = filterRoadSectionList(radioButton_dldm.getTextMessage(), searchStr, KEY_TYPE_KEY);
                            } else {
                                list = filterRoadSectionList(radioButton_dldm.getTextMessage(), searchStr, KEY_TYPE_NAME);
                            }
                            adapter.setKey(searchStr);
                            adapter.setItemList(list);
                        }
                    } else {
                        if (search_search.getHintText().equals(hintRoad)) {
                            textView_list_title.setVisibility(View.VISIBLE);
                            list = getPreference();
                            adapter.setKey("");
                            adapter.setItemList(list);
                        } else {
                            textView_list_title.setVisibility(View.GONE);
                        }
                    }
                }
            });
            adapter.setListener((v, position) -> {
                road = list.get(position);
                if (road.getLddm() == null) {  //道路
                    selectRoad = road; //选中的
                    isExist = inPreference(selectRoad);
                    makePreferenceIcon(isExist);

                    radioButton_dldm.setTextMessage(road.getDldm());
                    textView_location.setText(road.getDlmc());
                    roadStr = road.getDlmc();
                    radioButton_lddm.setTextMessage("");
                    radioButton_ddms.setTextMessage("");
                    radioButton_lddm.setChecked(true);

                } else { //路段
                    radioButton_lddm.setTextMessage(road.getLddm());
                    roadSectionStr = road.getLdmc();
                    if (!roadSectionStr.startsWith(roadStr)) {
                        textView_location.setText(String.format("%s%s", roadStr, roadSectionStr));
                    } else {
                        textView_location.setText(roadSectionStr);
                    }
                    radioButton_ddms.setChecked(true);
                }
            });
        }

        /**
         * 创建时间：2017/8/28
         * 创建者：huzan
         * 描述：判断是否已收藏
         */
        private boolean inPreference(RoadModel roadSearchBean) {
            boolean isExist = false;
            if (getPreference() == null || getPreference().isEmpty()) {
                return false;
            }
            for (RoadModel bean : getPreference()) {
                if (bean.getDldm().equals(roadSearchBean.getDldm())) {
                    isExist = true;
                    break;
                }
            }
            return isExist;

        }

        /**
         * 创建时间：2017/8/29
         * 创建者：huzan
         * 描述：(取消)收藏操作
         */
        private boolean doPreference(RoadModel roadSearchBean) {
            if (!inPreference(roadSearchBean)) {
                collectList.add(0, roadSearchBean);
                onOperationListener.onAddCollect(roadSearchBean);
            } else {
                collectList.remove(roadSearchBean);
                onOperationListener.onDeleteCollect(roadSearchBean);
            }

            return true;
        }

        /**
         * 创建时间：2017/8/29
         * 创建者：huzan
         * 描述：显示收藏的图标
         */
        private void makePreferenceIcon(boolean preferenced) {
            if (!preferenced) {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_collect_nor);
                if (drawable != null) {
                    drawable.setBounds(0,
                            0,
                            (int) Math.min(drawable.getMinimumWidth(), context.getResources().getDimension(R.dimen.x16)),
                            (int) Math.min(drawable.getMinimumHeight(), context.getResources().getDimension(R.dimen.y16)));
                    textView_location.setCompoundDrawables(textView_location.getCompoundDrawables()[0], null, drawable, null);
                }
            } else {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_collect_hl);
                if (drawable != null) {
                    drawable.setBounds(0,
                            0,
                            (int) Math.min(drawable.getMinimumWidth(), context.getResources().getDimension(R.dimen.x16)),
                            (int) Math.min(drawable.getMinimumHeight(), context.getResources().getDimension(R.dimen.y16)));
                    textView_location.setCompoundDrawables(textView_location.getCompoundDrawables()[0], null, drawable, null);
                }
            }
        }

        private void initRecycleView() {
            list = new ArrayList<>();
            list.addAll(getPreference());
            adapter = new AddressAdapter(context, false);
            adapter.setItemList(list);
            recyclerView_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            recyclerView_list.setAdapter(adapter);
            recyclerView_list.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.VERTICAL, 2, 0));
            recyclerView_list.setItemAnimator(new DefaultItemAnimator());
        }

        /**
         * 创建时间：2017/8/28
         * 创建者：huzan
         * 描述：获取个性化数据
         */
        private List<RoadModel> getPreference() {
            return collectList;
        }

        private void showDialogMs(final int qsms, final int jsms) {

            dialog.setTitle("道路米数：" + qsms + "~" + jsms);
            dialog.isEdit(true);
            dialog.setOnSubmitClick("确定", new DialogNormal.OnSubmitClick() {
                @Override
                public void onSubmit(DialogInterface dialog, int var, String message) {
                    try {
                        if (qsms <= Integer.valueOf(message) && Integer.valueOf(message) <= jsms) {
                            adapter.clearItemList();
                            message = message.replaceFirst("^0*", "");
                            if (message.length() <= 0) {
                                message = "0";
                            }
                            if (!message.equals("0")) {
                                if (!roadSectionStr.startsWith(roadStr)) {
                                    textView_location.setText(roadStr + roadSectionStr + message + "米");
                                } else {
                                    textView_location.setText(roadSectionStr + message + "米");
                                }
                            }
                            radioButton_ddms.setTextMessage(String.format("%03d", Integer.valueOf(message)));
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "范围超出", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "输入的道路米数不合法", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            dialog.setCancelClickListener("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.create().show();
        }

        private void showDialogGls(final int qs, final int js) {
            dialog.setTitle("公里数：" + road.getQs() + "~" + road.getJs());
            dialog.isEdit(true);
            dialog.setOnSubmitClick("确定", (dialog, var, message) -> {
                try {
                    if (qs <= Integer.valueOf(message) && Integer.valueOf(message) <= js) {
                        search_search.setText("");
                        message = message.replaceFirst("^0*", "");
                        if (message.length() <= 0) {
                            message = "0";
                        }
                        roadSectionStr = message + "公里";
                        textView_location.setText(String.format("%s%s", roadStr, roadSectionStr));
                        radioButton_lddm.setTextMessage(String.format("%04d", Integer.valueOf(message)));
                        radioButton_ddms.setChecked(true);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "范围超出", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "输入的公里数不合法", Toast.LENGTH_SHORT).show();
                }

            });
            dialog.setCancelClickListener("取消", (dialogInterface, i) -> dialogInterface.dismiss());
            dialog.create().show();
        }


        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：查询的道路列表过滤排序
         */
        private List<RoadModel> filterRoadList(String key, int keyType) {
            List<RoadModel> tempList = new ArrayList<>();
            for (RoadModel roadModel : roadList) {
                if (keyType == KEY_TYPE_KEY && roadModel.getDldm().contains(key)) {
                    tempList.add(roadModel);
                } else if (keyType == KEY_TYPE_NAME && roadModel.getDlmc().contains(key)) {
                    tempList.add(roadModel);
                }
            }

            Collections.sort(tempList, (road, road1) -> {
                Integer dldm = Integer.parseInt(road.getDldm());
                Integer dldm1 = Integer.parseInt(road1.getDldm());
                Integer index = 0;
                Integer index1 = 0;
                if (keyType == KEY_TYPE_NAME) {
                    index = road.getDlmc().indexOf(key);
                    index1 = road1.getDlmc().indexOf(key);
                } else if (keyType == KEY_TYPE_KEY) {
                    index = road.getDldm().indexOf(key);
                    index1 = road1.getDldm().indexOf(key);
                }
                if (index.equals(index1)) {
                    return dldm.compareTo(dldm1);
                } else {
                    return index.compareTo(index1);
                }
            });
            return tempList;
        }

        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：查询的路段列表过滤排序
         */
        private List<RoadModel> filterRoadSectionList(String dldm, String key, int keyType) {
            List<RoadModel> tempList = new ArrayList<>();
            for (RoadModel roadModel : roadSectionList) {
                if (keyType == KEY_TYPE_KEY && roadModel.getLddm().contains(key) && roadModel.getDldm().equals(dldm)) {
                    tempList.add(roadModel);
                } else if (keyType == KEY_TYPE_NAME && roadModel.getLdmc().contains(key) && roadModel.getDldm().equals(dldm)) {
                    tempList.add(roadModel);
                }
            }

            Collections.sort(tempList, (road, road1) -> {
                Integer lddm = Integer.parseInt(road.getDldm());
                Integer lddm1 = Integer.parseInt(road1.getDldm());
                Integer index = 0;
                Integer index1 = 0;
                if (keyType == KEY_TYPE_NAME) {
                    index = road.getLdmc().indexOf(key);
                    index1 = road1.getLdmc().indexOf(key);
                } else if (keyType == KEY_TYPE_KEY) {
                    index = road.getLddm().indexOf(key);
                    index1 = road1.getLddm().indexOf(key);
                }
                if (index.equals(index1)) {
                    return lddm.compareTo(lddm1);
                } else {
                    return index.compareTo(index1);
                }
            });
            return tempList;
        }
    }

    public interface OnOperationListener {
        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：选择道路信息完成
         *
         * @param dldm     道路代码
         * @param lddm     路段代码
         * @param dlms     地点米数
         * @param location 详细信息 dldm+lddm+dlms+"-"+道路信息
         */
        void onSubmit(String dldm, String lddm, String dlms, String location);

        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：添加收藏
         */
        void onAddCollect(RoadModel roadModel);

        /**
         * 创建时间：2017/11/9
         * 创建者：huzan
         * 描述：取消收藏
         */
        void onDeleteCollect(RoadModel roadModel);
    }
}
