package cn.trustway.nb.core.search.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.trustway.nb.core.R;


/**
 * Created by huzan  2017/3/31 11:32.
 * 描述：组合控件：搜索框
 */

public class ViewSearch extends LinearLayout {
    private static final String TAG = "ViewSearch";
    EditText editText;
    RelativeLayout relativeLayout;
    ImageButton imageButton_clear;
    TextWatcherListener onTextWatcher;

    public ViewSearch(@NonNull Context context) {
        this(context, null);
    }

    public ViewSearch(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewSearch(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = LayoutInflater.from(context).inflate(R.layout.view_search, this,true);
        editText = v.findViewById(R.id.edittext_widget_search);
        relativeLayout = v.findViewById(R.id.relativelayout_widget_search_parent);
        imageButton_clear = v.findViewById(R.id.imagebutton_widget_search_right);
        imageButton_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });


        editText.setOnEditorActionListener((v1, actionId, event) -> event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewSearch);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int index = array.getIndex(i);
            if (index == R.styleable.ViewSearch_msgColor) {
                editText.setTextColor(array.getColor(index, ContextCompat.getColor(context, R.color.grey)));

            } else if (index == R.styleable.ViewSearch_msgText) {
                editText.setText(array.getString(index));

            } else if (index == R.styleable.ViewSearch_msgHint) {
                editText.setHint(array.getString(index));

            } else if (index == R.styleable.ViewSearch_msgHintColor) {
                editText.setHintTextColor(array.getColor(index, ContextCompat.getColor(context, R.color.grey)));

            } else if (index == R.styleable.ViewSearch_viewHeight) {
                ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
                layoutParams.height = (int) array.getDimension(index, getResources().getDimension(R.dimen.y48));
                relativeLayout.setLayoutParams(layoutParams);

            } else if (index == R.styleable.ViewSearch_bg) {
                relativeLayout.setBackgroundDrawable(array.getDrawable(index));

            } else{
                Log.d(TAG, "ViewSearch: no this attr "+index);
            }
        }
        array.recycle();
    }

    public void setText(String str) {
        editText.setText(str);
    }

    public void setHintText(String str) {
        editText.setHint(str);
    }

    public void setTextColor(int color) {
        editText.setTextColor(color);
    }

    public void setHintColor(int color) {
        editText.setHintTextColor(color);
    }

    @NonNull
    public String getHintText(){
        return editText.getHint().toString();
    }

    public void setSection(int length){
        editText.setSelection(length);
    }


    public void setOnTextWatcher(@NonNull final TextWatcherListener onTextWatcher){
        this.onTextWatcher = onTextWatcher;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onTextWatcher.beforeTextChanged(charSequence,i,i1,i2);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onTextWatcher.onTextChanged(charSequence,i,i1,i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                onTextWatcher.afterTextChanged(editable);
            }
        });
    }

    public interface TextWatcherListener {
        void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2);

        void onTextChanged(CharSequence charSequence, int i, int i1, int i2);

        void afterTextChanged(Editable editable);
    }

}
