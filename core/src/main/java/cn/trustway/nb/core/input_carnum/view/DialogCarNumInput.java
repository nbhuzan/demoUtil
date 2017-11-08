package cn.trustway.nb.core.input_carnum.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.adapter.KeyBoardAdapter;
import cn.trustway.nb.core.base.util.HiddenSoftInputUtil;
import cn.trustway.nb.core.base.util.StringUtil;

/**
 * Created by huzan on 2017/8/2.
 * 描述：号牌录入组件
 * 传入传出
 */

public class DialogCarNumInput extends Dialog {


    @NonNull
    private static String province = "京津冀鲁晋蒙辽吉黑沪苏浙皖闽赣豫鄂湘粤桂渝川贵云藏陕甘青琼新港澳台宁";
    @NonNull
    private static String letter = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";


    public DialogCarNumInput(@NonNull Context context) {
        this(context, 0);
    }

    private DialogCarNumInput(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.Fingerprint);
    }


    public static class Build {
        private DialogCarNumInput dialogCarNumInput;
        private View view;
        private RecyclerView recyclerView;
        private LinearLayout linearLayout_carnum; //车牌父view
        private EditText editText_now;//当前获取焦点的edittext
        private TextView textView_ok; //确定
        private TextView textView_empty; //清空
        private ImageView imageView_cancel;//取消
        private RelativeLayout relativelayout_prompt;
        @Nullable
        private TextView textView_prompt;
        private RelativeLayout.LayoutParams lpPrompt;

        private String carNum;

        private KeyBoardAdapter adapterKey;
        private Context context;
        private List<String> listKey; //第一页键盘数据源
        private boolean isProvinceKeyBoard = true;//当前键盘为省份键盘
        private String fzjg;

        private OnSubmitListener onSubmitListener;//录入完获取车牌号的监听

        public Build(@NonNull Context context, String carNum) {
            this.context = context;
            this.carNum = carNum;
            dialogCarNumInput = new DialogCarNumInput(context);
            view = initView();
        }


        @NonNull
        public Build setOnSubmitListener(OnSubmitListener onSubmitListener) {
            this.onSubmitListener = onSubmitListener;
            return this;
        }

        @NonNull
        public Build setFzjg(String fzjg) {
            this.fzjg = fzjg;
            if (carNum == null || carNum.isEmpty()) {
                this.carNum = fzjg;
                fillCarNum(carNum);
            } else {
                fillCarNum(carNum);
            }
            return this;
        }

        public void create() {
            dialogCarNumInput.show();
            dialogCarNumInput.setContentView(view);
            dialogCarNumInput.setCancelable(true);
            Window window = dialogCarNumInput.getWindow();
            WindowManager.LayoutParams params;
            if (window != null) {
                window.setWindowAnimations(R.style.carNumAnim);
                params = window.getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.CENTER;
                window.setAttributes(params);
            }
        }

        private View initView() {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View inflate = layoutInflater.inflate(R.layout.dialog_input_carnum, null);
            textView_ok = inflate.findViewById(R.id.textview_input_carnum_ok);
            textView_empty = inflate.findViewById(R.id.textview_input_carnum_empty);
            imageView_cancel = inflate.findViewById(R.id.imageview_input_carnum_cancel);
            linearLayout_carnum = inflate.findViewById(R.id.linearlayout_input_carnum);
            editText_now = (EditText) linearLayout_carnum.getChildAt(0);
            recyclerView = inflate.findViewById(R.id.recycleview_input_carnum_keyboard);
            relativelayout_prompt = inflate.findViewById(R.id.relativelayout_input_carnum_prompt);
            HiddenSoftInputUtil.hiddenSoftInput(editText_now);
            initPrompt(context);
            for (int i = 0; i < linearLayout_carnum.getChildCount(); i++) {
                final EditText e = (EditText) linearLayout_carnum.getChildAt(i);
                HiddenSoftInputUtil.hiddenSoftInput(e);
                int finalI = i;
                e.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            editText_now = (EditText) linearLayout_carnum.getChildAt(finalI);
                            edittextWhichOne();
                            return false;
                        default:
                            break;
                    }
                    return false;
                });
            }
            initRecycleViewFirst();
            setKeyBoardType(true);
            if (carNum == null || carNum.isEmpty()) {

            } else {
                fillCarNum(carNum);
            }


            textView_ok.setOnClickListener(v -> {
                if (onSubmitListener != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < linearLayout_carnum.getChildCount(); i++) {
                        sb.append(((EditText) linearLayout_carnum.getChildAt(i)).getText().toString().trim());
                    }
                    dialogCarNumInput.dismiss();
                    if (sb.toString().equals(fzjg)) {
                        onSubmitListener.onClick("");
                    } else {
                        onSubmitListener.onClick(sb.toString());
                    }
                }
            });

            textView_empty.setOnClickListener(v -> {
                String carNum1 = fzjg;
                char[] chars1 = carNum1.toCharArray();
                for (int i = 0; i < linearLayout_carnum.getChildCount(); i++) {
                    EditText editText = ((EditText) linearLayout_carnum.getChildAt(i));
                    boolean needGetFoucus = false;
                    if (linearLayout_carnum.getChildCount() == chars1.length && i == linearLayout_carnum.getChildCount() - 1) {
                        needGetFoucus = true;
                    } else if (linearLayout_carnum.getChildCount() > chars1.length && i == chars1.length) {
                        needGetFoucus = true;
                    }
                    if (editText != null) {
                        if (i < chars1.length) {
                            editText.setText(String.valueOf(chars1[i]));
                        } else {
                            if (needGetFoucus) {
                                editText_now = editText;
                                edittextWhichOne();
                            }
                            editText.setText(null);
                        }
                    }
                }
            });

            imageView_cancel.setOnClickListener(v -> dialogCarNumInput.dismiss());

            dialogCarNumInput.setOnShowListener(dialog -> editText_now.requestFocus());
            return inflate;
        }

        private void fillCarNum(@NonNull String carNum) {
            char[] chars = carNum.toCharArray();
            int charsLength = chars.length;
            for (int i = 0; i < charsLength; i++) {
                EditText editText = ((EditText) linearLayout_carnum.getChildAt(i));
                if (editText != null) {
                    editText.setText(String.valueOf(chars[i]));
                }
            }
            if (charsLength > 0 && charsLength < 7) {
                editText_now = (EditText) linearLayout_carnum.getChildAt(charsLength);
                edittextWhichOne();
            } else if (charsLength >= 7) {
                editText_now = (EditText) linearLayout_carnum.getChildAt(charsLength - 1);
                edittextWhichOne();
            }
        }

        private void initPrompt(@NonNull Context context) {
            textView_prompt = new TextView(context);
            textView_prompt.setTextColor(Color.BLACK);
            textView_prompt.setGravity(Gravity.CENTER);
            textView_prompt.setTextSize(context.getResources().getDimension(R.dimen.sp14));
            textView_prompt.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_border_circle_blue_30));
            lpPrompt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpPrompt.width = (int) context.getResources().getDimension(R.dimen.x55);
            lpPrompt.height = (int) context.getResources().getDimension(R.dimen.x55);
            lpPrompt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }


        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：设置键盘数据
         */
        private void setKeyBoardType(boolean isProvince) {
            if (isProvince) {
                listKey = initProvinceData();
            } else {
                listKey = initLetterData();
            }
            adapterKey.setItemList(listKey);
        }

        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：初始化省份键盘数据
         */
        @NonNull
        private List<String> initProvinceData() {
            List<String> tempList = initProvinceList();
            if (tempList == null) {
                tempList = new ArrayList<>();
            } else {
                tempList.add(29, "trustway");
            }
            return tempList;

        }

        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：初始化数字字母键盘数据
         */
        @NonNull
        private List<String> initLetterData() {
            List<String> tempList = initLetterList();
            if (tempList == null) {
                tempList = new ArrayList<>();
            } else {
                tempList.add(29, "trustway");
            }

            return tempList;

        }


        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：初始化第一页的键盘
         */
        int positon;

        private void initRecycleViewFirst() {

            adapterKey = new KeyBoardAdapter(context);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.top = 1;
                    outRect.bottom = 1;
                    outRect.left = 1;
                    outRect.right = 1;
                }
            });
            recyclerView.setAdapter(adapterKey);
            adapterKey.setOnClickListener((v, position) -> {

                setInput(listKey.get(position));


            });
            adapterKey.setOnCancelKeyBoard(() -> {
                if (editText_now != null) {
                    EditText editText_next = (EditText) FocusFinder.getInstance()
                            .findNextFocus((ViewGroup) editText_now.getParent(), editText_now, View.FOCUS_LEFT);
                    editText_now.setText("");
                    if (editText_next != null) {
                        editText_now = editText_next;
                        edittextWhichOne();
                    }
                }
            });

            recyclerView.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //防止按下后滑动item的up事件被recycle接管，无法执行adapterKey.setOnClickListener和adapterKey.setOnKeyTouchListener
                        if (textView_prompt != null) {
                            relativelayout_prompt.removeView(textView_prompt);
                            textView_prompt = null;
                            setInput(listKey.get(positon));
                        }
                        break;
                }
                return false;
            });

            adapterKey.setOnKeyTouchListener(new KeyBoardAdapter.OnKeyTouchListener() {
                @Override
                public void onDown(@NonNull View v, int pos) {
                    //key提示
                    positon = pos;
                    if (textView_prompt != null) {
                        relativelayout_prompt.removeView(textView_prompt);
                        textView_prompt = null;
                    }
                    initPrompt(context);
                    textView_prompt.setText(listKey.get(pos));
                    lpPrompt.bottomMargin = v.getHeight() * (6 - pos / 6) + v.getHeight() / 2;
                    lpPrompt.leftMargin = pos % 6 * v.getWidth();
                    relativelayout_prompt.addView(textView_prompt, lpPrompt);
                }

                @Override
                public void onUp() {
                    //关闭key提示
                    relativelayout_prompt.removeView(textView_prompt);
                    textView_prompt = null;
                }
            });

        }


        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：设置选择的键盘数据
         */
        private void setInput(String str) {
            if (editText_now != null) {
                editText_now.setText(str);
                editText_now = (EditText) FocusFinder.getInstance().findNextFocus((ViewGroup) editText_now.getParent(), editText_now, View.FOCUS_RIGHT);
                edittextWhichOne();
            }
        }

        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：选择的键盘
         */
        private void edittextWhichOne() {
            if (editText_now != null) {
                if (editText_now.getId() == linearLayout_carnum.getChildAt(0).getId()) {
                    //省份键盘
                    if (!isProvinceKeyBoard) {
                        setKeyBoardType(true);
                    }
                    isProvinceKeyBoard = true;
                } else {
                    //数字字母键盘
                    if (isProvinceKeyBoard) {
                        setKeyBoardType(false);
                    }
                    isProvinceKeyBoard = false;
                }
                editText_now.requestFocus();
                editText_now.requestFocusFromTouch();
            } else {
                textView_ok.performClick();
//            textView_ok.setFocusable(true);
//            textView_ok.setFocusableInTouchMode(true);
//            textView_ok.requestFocus();
//            textView_ok.requestFocusFromTouch();
            }
        }

        @NonNull
        private static List<String> initProvinceList() {
            String x = "鲁";
            x += province.replace("鲁", "");
            return StringUtil.str2List(x);

        }

        @NonNull
        private static List<String> initLetterList() {
            String x = letter;
            return StringUtil.str2List(x);
        }
    }


    /**
     * 创建时间：2017/8/2
     * 创建者：huzan
     * 描述：外部调用的carnum录入完成接口
     */
    public interface OnSubmitListener {
        void onClick(String carNum);
    }

}
