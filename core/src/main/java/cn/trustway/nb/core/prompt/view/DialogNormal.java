package cn.trustway.nb.core.prompt.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.trustway.nb.core.R;


/**
 * Created by huzan  2017/3/13 22:28.
 * 描述：统一封装页面弹出框
 */

public class DialogNormal extends Dialog {


    public DialogNormal(@NonNull Context context) {
        super(context);
    }

    public DialogNormal(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }


    public static class Build {
        private Context context;
        private String title;
        private String message;
        private String positiveText;
        private String negativeText;
        private OnClickListener cancelClickListener;
//        private OnClickListener negativeClickListener;
        private OnSubmitClick onSubmitClick;

        public Build(Context context) {
            this.context = context;
        }

        public Build setTitle(String title) {
            this.title = title;
            return this;
        }

        public Build setMessage(String message) {
            this.message = message;
            return this;
        }



        TextView textView_title;
        TextView textView_message;
        Button button_ok;
        Button button_cancel;
        EditText editText;
        String messageText;
        boolean isEdit = false;

        public void isEdit(boolean is) {
            this.isEdit = is;
        }

        public Build setCancelClickListener(String positiveText, OnClickListener cancelClickListener) {
            this.positiveText = positiveText;
            this.cancelClickListener = cancelClickListener;
            return this;
        }

        public Build setOnSubmitClick(String negativeText, OnSubmitClick onSubmitClick) {
            this.negativeText = negativeText;
            this.onSubmitClick = onSubmitClick;
            return this;
        }


        public DialogNormal create() {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogNormal dialog = new DialogNormal(context, R.style.dialog);
            if(inflater==null){
                return dialog;
            }
            View layout = inflater.inflate(R.layout.dialog_normal, null);
            editText = layout.findViewById(R.id.edittext_widget_alertdialog_message);
            textView_title = layout.findViewById(R.id.textview_widget_alertdialog_title);
            textView_message = layout.findViewById(R.id.textview_widget_alertdialog_message);
            button_ok = layout.findViewById(R.id.button_widget_alertdialog_ok);
            button_cancel = layout.findViewById(R.id.button_widget_alertdialog_cancel);

            if (title == null) {
                title = context.getResources().getString(R.string.prompt);
                textView_title.setVisibility(View.VISIBLE);
                textView_title.setText(title);
            } else {
                textView_title.setVisibility(View.VISIBLE);
                textView_title.setText(title);
            }
            if (message == null) {
                textView_message.setVisibility(View.GONE);
            } else {
                textView_message.setVisibility(View.VISIBLE);
                textView_message.setText(message);
            }

            if (positiveText == null || positiveText.length() <= 0) {
                button_cancel.setVisibility(View.GONE);
            } else {
                button_cancel.setVisibility(View.VISIBLE);
                button_cancel.setText(positiveText);
                if (cancelClickListener != null) {
                    button_cancel.setOnClickListener(v -> {
                        hideInput(v);
                        cancelClickListener.onClick(dialog, BUTTON_POSITIVE);
                    });

                }
            }
            if (isEdit) {
                editText.setVisibility(View.VISIBLE);
                textView_message.setVisibility(View.GONE);
            } else {
                editText.setVisibility(View.GONE);
                textView_message.setVisibility(View.VISIBLE);
            }

            if (negativeText == null || negativeText.length() <= 0) {
                button_ok.setVisibility(View.GONE);
            } else {
                button_ok.setVisibility(View.VISIBLE);
                button_ok.setText(negativeText);
                if (onSubmitClick != null) {
                    button_ok.setOnClickListener(v -> {

                        if (isEdit) {
                            hideInput(v);
                            messageText = editText.getText().toString().length() == 0 ? "0" : editText.getText().toString();
                        } else {
                            messageText = textView_message.getText().toString().length() == 0 ? "0" : editText.getText().toString();
                        }
                        onSubmitClick.onSubmit(dialog, BUTTON_NEGATIVE, messageText);
                    });

                }
            }


            dialog.setContentView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setOnShowListener(dialogInterface -> {
                if (editText.getVisibility() == View.VISIBLE) {
                    editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            editText.setFocusableInTouchMode(true);
                            editText.setFocusable(true);
                            editText.requestFocus();
                            editText.requestFocusFromTouch();
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                            }
                        }
                    });
                }

            });
            dialog.setCancelable(false);
            return dialog;
        }

        private void hideInput(View v) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

    }


    public interface OnSubmitClick {
        void onSubmit(DialogInterface dialog, int var, String message);
    }

}
