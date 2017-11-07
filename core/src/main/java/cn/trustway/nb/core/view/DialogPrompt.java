package cn.trustway.nb.core.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.trustway.nb.core.R;

/**
 * Created by huzan on 2017/8/10.
 * 描述：通用提示框
 */

public class DialogPrompt extends Dialog {

    public static final int STYLE_SUCCESS = 0;
    public static final int STYLE_WARNING = 1;
    public static final int STYLE_QUESTION = 2;

    private Context context;
    private ImageView imageView_icon;
    private TextView textView_title;
    private TextView textView_msg;
    private TextView textView_cancel;
    private CheckBox checkBox;

    private View.OnClickListener onClickListener;

    private int style;
    private String title;
    private String msg;

    private boolean isCheck = true;

    /**
     * 创建时间：2017/8/21
     * 创建者：huzan
     * 描述：提示框样式
     */
    public void setStyle(int style) {
        this.style = style;
        if (style == STYLE_SUCCESS) {
            imageView_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_wc));
        } else if (style == STYLE_WARNING) {
            imageView_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_jg));
        } else if (style == STYLE_QUESTION) {
            imageView_icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_tw));
        }
    }

    /**
     * 创建时间：2017/9/13
     * 创建者：huzan
     * 描述：是否需要先确认才能点击确认
     */
    public void setNeedCheck(boolean needCheck) {
        if (needCheck) {
            checkBox.setVisibility(View.VISIBLE);
        }
        this.isCheck = !needCheck;
        setCanceledOnTouchOutside(!needCheck);
    }

    /**
     * 创建时间：2017/8/21
     * 创建者：huzan
     * 描述：设置提示标题
     */
    public void setTitle(String title) {
        this.title = title;
        textView_title.setText(title);
    }

    /**
     * 创建时间：2017/8/21
     * 创建者：huzan
     * 描述：内容补充文本
     */
    public void setMsg(String msg) {
        this.msg = msg;
        textView_msg.setText(msg);
    }

    /**
     * 创建时间：2017/8/21
     * 创建者：huzan
     * 描述：确定回调
     */
    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
    }

    /**
     * 创建时间：2017/8/15
     * 创建者：huzan
     * 描述：是否有删除按钮
     */
    public void haveCancelButton(boolean haveCancelButton) {
        if (haveCancelButton) {
            textView_cancel.setVisibility(View.VISIBLE);
        } else {
            textView_cancel.setVisibility(View.GONE);
        }
    }


    public DialogPrompt(@NonNull Context context) {
        this(context, 0);
        this.context = context;
    }


    public DialogPrompt(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.Fingerprint);
        this.context = context;
        initDialog();
    }

    /**
     * 创建时间：2017/8/10
     * 创建者：huzan
     * 描述：初始化dialog
     */
    private void initDialog() {
        View inflate = initView();
        setContentView(inflate);
        setCancelable(true);
        Window window = getWindow();
        WindowManager.LayoutParams params;
        if (window != null) {
            window.setWindowAnimations(R.style.dialogPromptAnim);
            params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }

    @NonNull
    private View initView() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_prompt, null);
        imageView_icon = inflate.findViewById(R.id.imageview_prompt_icon);
        textView_title = inflate.findViewById(R.id.textview_prompt_title);
        textView_msg = inflate.findViewById(R.id.textview_prompt_msg);
        TextView textView_ok = inflate.findViewById(R.id.textview_prompt_ok);
        textView_cancel = inflate.findViewById(R.id.textview_prompt_cancel);
        textView_cancel.setOnClickListener(v -> dismiss());
        textView_cancel.setVisibility(View.GONE);
        checkBox = inflate.findViewById(R.id.checkout_prompt_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
            }
        });

        textView_ok.setOnClickListener(v -> {
            if(isCheck) {
                dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
        return inflate;
    }


}
