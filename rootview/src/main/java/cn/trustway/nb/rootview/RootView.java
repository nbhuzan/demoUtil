package cn.trustway.nb.rootview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by huzan on 2018/1/16.
 * 描述：
 */

public class RootView extends RelativeLayout {
    private Context context;
    private RelativeLayout relativeLayout;
    private TextView textView;
    private ImageView imageView;

    private AnimationDrawable spinner;

    public RootView(Context context) {
        this(context, null);
    }

    public RootView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    /**
     * 创建时间：2018/1/25
     * 创建者：huzan
     * 描述：加载失败系列
     */
    public void showFail() {
        showFail(null);
    }

    public void showFail(final Runnable runnable) {
        showFail("加载失败", ContextCompat.getDrawable(context, R.drawable.loading_1), runnable);

    }

    public void showFail(String text, Drawable drawable) {
        showFail(text, drawable, null);

    }


    public void showFail(String text, Drawable drawable, final Runnable runnable) {
        if(relativeLayout==null){
            init();
        }
        relativeLayout.setVisibility(VISIBLE);
        initData(text, drawable);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runnable != null)
                    runnable.run();
            }
        });
    }


    /**
     * 创建时间：2018/1/25
     * 创建者：huzan
     * 描述：加载中
     */
    public void showLoading(String text, Drawable drawable) {
        if(relativeLayout==null){
            init();
        }
        relativeLayout.setVisibility(VISIBLE);
        initData(text, drawable);
        textView.setOnClickListener(null);
        if (spinner != null && spinner.isRunning()) {
            spinner.stop();
        }
        spinner = (AnimationDrawable) imageView.getDrawable();
        // 开始动画
        spinner.start();
    }

    public void showLoading() {
        showLoading("加载中...", ContextCompat.getDrawable(context, R.drawable.loading_anim));
    }


    /**
     * 创建时间：2018/1/25
     * 创建者：huzan
     * 描述：加载成功
     */
    public void success() {
        if (spinner != null && spinner.isRunning()) {
            spinner.stop();
        }
        if (relativeLayout != null) {
            relativeLayout.setVisibility(GONE);
        }
    }

    public void initData(String text, Drawable drawable) {
        textView.setText(text);
        imageView.setImageDrawable(drawable);
    }


    private void init() {
        relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackgroundColor(Color.WHITE);
        addView(relativeLayout);

        LinearLayout linearLayout = new LinearLayout(context);
        LayoutParams lLayoutLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lLayoutLP.addRule(CENTER_IN_PARENT);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(lLayoutLP);
        relativeLayout.addView(linearLayout);

        imageView = new ImageView(context);
        LayoutParams ivLP = new LayoutParams(196, 224);
        imageView.setLayoutParams(ivLP);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        linearLayout.addView(imageView);

        textView = new TextView(context);
        LayoutParams textViewLP = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLP.setMargins(0, 80, 0, 0);
        textView.setLayoutParams(textViewLP);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(14);
        linearLayout.addView(textView);
        relativeLayout.setVisibility(VISIBLE);


    }


}
