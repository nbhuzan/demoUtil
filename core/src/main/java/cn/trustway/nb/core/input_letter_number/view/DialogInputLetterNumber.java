package cn.trustway.nb.core.input_letter_number.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

public class DialogInputLetterNumber extends Dialog {



    public DialogInputLetterNumber(@NonNull Context context, String carNum) {
        this(context, 0, carNum);
    }

    private DialogInputLetterNumber(@NonNull Context context, @StyleRes int themeResId, String carNum) {
        super(context, R.style.Fingerprint);

    }

    public static class Build {
        private DialogInputLetterNumber dialogInputLetterNumber;
        private RecyclerView recyclerView;
        private LinearLayout linearLayout_carnum; //车牌父view
        private EditText editText;//当前获取焦点的edittext
        private TextView textView_ok; //确定
        private ImageView imageView_cancel;//取消
        private LayoutInflater layoutInflater;
        private RelativeLayout relativelayout_prompt;
        private TextView textView_prompt;
        private RelativeLayout.LayoutParams lpPrompt;
        private TextView textView_change;
        private TextView textView_empty;

        private static String letter1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        private KeyBoardAdapter adapterKey;
        private Context context;
        private List<String> listKey; //第一页键盘数据源
        private boolean isProvinceKeyBoard = true;//当前键盘为省份键盘

        private OnSubmitListener onSubmitListener;//录入完获取车牌号的监听
        private View view;

        public Build(Context context, String carNum) {
            this.context = context;
            dialogInputLetterNumber = new DialogInputLetterNumber(context, carNum);
            view = initView(context, carNum);
        }

        public Build setOnSubmitListener(OnSubmitListener onSubmitListener) {
            this.onSubmitListener = onSubmitListener;
            return this;
        }

        public void create(){
            dialogInputLetterNumber.show();
            dialogInputLetterNumber.setContentView(view);
            dialogInputLetterNumber.setCancelable(true);
            Window window = dialogInputLetterNumber.getWindow();
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


        private View  initView(@NonNull Context context, String carNum) {
            layoutInflater = LayoutInflater.from(context);
            View inflate = layoutInflater.inflate(R.layout.dialog_input_letter_number, null);
            textView_ok = inflate.findViewById(R.id.textview_letter_number_ok);
            textView_empty = inflate.findViewById(R.id.textView_letter_number_empty);
            textView_change = inflate.findViewById(R.id.textView_letter_number_change);
            imageView_cancel = inflate.findViewById(R.id.imageview_letter_number_cancel);
            editText = inflate.findViewById(R.id.edittext_letter_number_input);
            recyclerView = inflate.findViewById(R.id.recycleview_letter_number_keyboard);
            relativelayout_prompt = inflate.findViewById(R.id.relativelayout_letter_number_prompt);
            HiddenSoftInputUtil.hiddenSoftInput(editText);
            initPrompt(context);
            if (carNum != null) {
                char[] chars = carNum.toCharArray();
                int charsLength = chars.length;
                for (int i = 0; i < charsLength; i++) {
                    if (editText != null)
                        editText.setText(String.valueOf(chars[i]));
                }
            }
            textView_empty.setOnClickListener(v -> editText.setText(""));
            textView_change.setOnClickListener(v -> adapterKey.setUpperCase(!adapterKey.isUpperCase()));


            textView_ok.setOnClickListener(v -> {
                if (onSubmitListener != null) {

                    dialogInputLetterNumber.dismiss();
                    onSubmitListener.onClick(editText.getText().toString());
                }
            });
            imageView_cancel.setOnClickListener(v -> dialogInputLetterNumber.dismiss());
            initRecycleViewFirst();
            dialogInputLetterNumber.setOnShowListener(dialog -> editText.requestFocus());
            return inflate;
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
        private void setKeyBoardType() {

            listKey = initLetterData();

            adapterKey.setItemList(listKey);
        }


        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：初始化数字字母键盘数据
         */
        private List<String> initLetterData() {

            return initLetterList1();

        }


        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：初始化键盘
         */
        private int positon;

        private void initRecycleViewFirst() {

            adapterKey = new KeyBoardAdapter(context);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
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
                String edit = editText.getText().toString();
                if (!edit.isEmpty()) {
                    editText.setText(edit.substring(0, edit.length() - 1));
                    editText.setSelection(edit.length() - 1);
                }
            });

            recyclerView.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //防止按下后滑动item的up事件被recycle接管，无法执行adapterKey.setOnClickListener和adapterKey.setOnKeyTouchListener
                        if (textView_prompt != null) {
                            relativelayout_prompt.removeView(textView_prompt);
                            textView_prompt = null;
                            if (adapterKey.isUpperCase()) {
                                setInput(listKey.get(positon));
                            } else {
                                setInput(listKey.get(positon).toLowerCase());
                            }
                        }
                        break;
                }
                return false;
            });

            adapterKey.setOnKeyTouchListener(new KeyBoardAdapter.OnKeyTouchListener() {
                @Override
                public void onDown(View v, int pos) {
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
            setKeyBoardType();

        }


        /**
         * 创建时间：2017/8/2
         * 创建者：huzan
         * 描述：设置选择的键盘数据
         */
        @SuppressLint("SetTextI18n")
        private void setInput(String str) {
            editText.setText(editText.getText().toString() + str);
            editText.setSelection(editText.getText().length());
        }

        private static List<String> initLetterList1() {
            String x = letter1;
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
