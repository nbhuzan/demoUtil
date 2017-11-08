package cn.trustway.nb.core.input_cardid.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.adapter.KeyBoardAdapter;
import cn.trustway.nb.core.base.listener.RecycleViewClickListener;
import cn.trustway.nb.core.base.util.HiddenSoftInputUtil;
import cn.trustway.nb.core.base.util.StringUtil;


/**
 * Created by huzan on 2017/8/21.
 * 描述：
 */

public class DialogIdCardIdInput extends Dialog {
    private static final String keyDate = "123456789X0";

    public DialogIdCardIdInput(@NonNull Context context) {
        this(context, 0);
    }

    private DialogIdCardIdInput(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.Fingerprint);
    }

    public static class Build {
        private DialogIdCardIdInput dialogIdCardKeyBoard;
        private Context context;
        private View view;
        private TextView textView_title;
        private LinearLayout linearLayout_cancel;
        private TextView textView_submit;
        private EditText editText_msg;
        private RecyclerView recyclerView_key;
        private TextView textView_empty;
        private KeyBoardAdapter adapter;
        private List<String> keyList;
        private String idcard = "";
        private DialogIdCardIdInput.onSubmitListener onSubmitListener;
        private boolean isMustAll = true;//是否需要输入全18位

        private int editTextMaxLength = 18; //设置证件最大输入值

        public Build(@NonNull Context context) {
            this.context = context;
            dialogIdCardKeyBoard = new DialogIdCardIdInput(context);
            initView();
        }

        @NonNull
        public Build setMustAll(boolean mustAll) {
            isMustAll = mustAll;
            return this;
        }

        @NonNull
        public Build setOnSubmitListener(DialogIdCardIdInput.onSubmitListener onSubmitListener) {
            this.onSubmitListener = onSubmitListener;
            return this;
        }

        public void create() {
            dialogIdCardKeyBoard.show();
            dialogIdCardKeyBoard.setContentView(view);
            Window window = dialogIdCardKeyBoard.getWindow();
            WindowManager.LayoutParams params;
            if (window != null) {
                window.setWindowAnimations(R.style.carNumAnim);
                params = window.getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.BOTTOM;
                window.setAttributes(params);
            }
            adapter.setOnClickListener(new RecycleViewClickListener() {
                @Override
                public void OnItemClickListener(View v, int position) {
                    int start = editText_msg.getSelectionStart();
                    int end = editText_msg.getSelectionEnd();

                    String partone = "";
                    String parttwo = "";

                    if (start == end && editText_msg.getText().length() < editTextMaxLength) {
                        partone = idcard.substring(0, start);
                        parttwo = idcard.substring(end, idcard.length());
                        idcard = partone + keyList.get(position) + parttwo;
                        editText_msg.setText(idcard);
                        editText_msg.setSelection(start + 1);
                    } else if (start != end) {
                        partone = idcard.substring(0, start);
                        parttwo = idcard.substring(end, idcard.length());
                        idcard = partone + keyList.get(position) + parttwo;
                        editText_msg.setText(idcard);
                        editText_msg.setSelection(start + 1);
                    }
                }
            });
            adapter.setOnCancelKeyBoard(new KeyBoardAdapter.OnBeforeInputClickListener() {
                @Override
                public void onClick() {
                    if (!idcard.isEmpty()) {
                        int start = editText_msg.getSelectionStart();
                        int end = editText_msg.getSelectionEnd();

                        if (start == end && start == 0) {
                            return;
                        }
                        String partone = "";
                        String parttwo = "";

                        if (start == end) {
                            partone = idcard.substring(0, start - 1);
                            parttwo = idcard.substring(end, idcard.length());
                            idcard = partone + parttwo;
                            editText_msg.setText(idcard);
                            editText_msg.setSelection(start - 1);
                        } else {
                            partone = idcard.substring(0, start);
                            parttwo = idcard.substring(end, idcard.length());
                            idcard = partone + parttwo;
                            editText_msg.setText(idcard);
                            editText_msg.setSelection(start);
                        }

                    }
                }
            });

        }

        @NonNull
        public Build setLastIdcard(String str) {

            this.idcard = str;
            setIdcard(idcard);
            return this;
        }

        private void setIdcard(String str) {
            str = str != null ? str : "";
            editText_msg.setText(str);
            editText_msg.setSelection(str.length());
        }

        private void initView() {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_input_idcard, null);
            textView_title = view.findViewById(R.id.textview_idcard_title);
            textView_submit = view.findViewById(R.id.textview_idcard_ok);
            editText_msg = view.findViewById(R.id.edittext_idcard);
            linearLayout_cancel = view.findViewById(R.id.linearlayout_idcard_cancel);
            recyclerView_key = view.findViewById(R.id.recycleview_idcard_key);
            textView_empty = view.findViewById(R.id.textview_idcard_empty);
            adapter = new KeyBoardAdapter(context);
            adapter.setCancelStyle(1);
            HiddenSoftInputUtil.hiddenSoftInput(editText_msg);
            recyclerView_key.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            recyclerView_key.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.top = 1;
                    outRect.bottom = 1;
                    outRect.left = 1;
                    outRect.right = 1;
                }
            });
            recyclerView_key.setAdapter(adapter);
            keyList = initKeyList();
            adapter.setItemList(keyList);
            textView_empty.setOnClickListener(v -> {
                idcard = "";
                setIdcard(idcard);
            });
            linearLayout_cancel.setOnClickListener(v -> dialogIdCardKeyBoard.dismiss());
            textView_submit.setOnClickListener(v -> {
                if (onSubmitListener != null) {
                    if(isMustAll&&idcard.length()!=editTextMaxLength){
                        Toast.makeText(context, "身份证号码必须为18位", Toast.LENGTH_SHORT).show();
                    }else {
                        dialogIdCardKeyBoard.dismiss();
                        onSubmitListener.onSubmit(idcard);
                    }
                }
            });
            editText_msg.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(@NonNull Editable s) {
                    idcard = s.toString();
                    if(s.length()==18){
                        textView_submit.performClick();
                    }
                }
            });
        }

        /**
         * 创建时间：2017/8/21
         * 创建者：huzan
         * 描述：初始化键盘列表
         */
        @NonNull
        private List<String> initKeyList() {
            List<String> keyList = StringUtil.str2List(keyDate);
            keyList.add("trustway");
            return keyList;
        }


    }

    public interface onSubmitListener {
        void onSubmit(String idcard);
    }

}
