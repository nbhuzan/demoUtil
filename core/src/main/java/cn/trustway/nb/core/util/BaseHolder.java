package cn.trustway.nb.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by huzan  2017/3/17 18:03.
 * 描述：封装recycleviewadapter需要的viewholder
 */

public class BaseHolder extends RecyclerView.ViewHolder {
    View itemView;

    public BaseHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void setText(int viewId, String str) {
        ((TextView) itemView.findViewById(viewId)).setText(str);
    }

    public void setTextBuild(int viewId, SpannableStringBuilder str) {
        ((TextView) itemView.findViewById(viewId)).setText(str);
    }

    public void setTextColor(int viewId, int color) {
        ((TextView) itemView.findViewById(viewId)).setTextColor(color);
    }


    public void setDrawableLeft(int viewId, Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        ((TextView) itemView.findViewById(viewId)).setCompoundDrawables(drawable, null, null, null);
    }

    public void setViewVisiable(int viewId, int visiable) {
        itemView.findViewById(viewId).setVisibility(visiable);
    }




    public View getView() {
        return itemView;
    }

    public View getViewById(int viewId) {
        return itemView.findViewById(viewId);
    }

    public void setPicChoose(int viewId, Drawable img) {
        ((ImageView) itemView.findViewById(viewId)).setImageDrawable(img);
        ImageView imageView;
        imageView = ((ImageView) itemView.findViewById(viewId));

        imageView.setImageDrawable(img);

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int width = imageView.getMeasuredWidth();
                GridLayoutManager.LayoutParams para1;
                para1 = (GridLayoutManager.LayoutParams) imageView.getLayoutParams();
                para1.height = width;
                imageView.setLayoutParams(para1);
                return true;
            }
        });
    }

    public void setImg(int viewId, Drawable img) {
        ImageView imageView;
        imageView = ((ImageView) itemView.findViewById(viewId));

        imageView.setImageDrawable(img);

    }

    public void setBackground(int viewId, Drawable img) {
        ImageView imageView;
        imageView = ((ImageView) itemView.findViewById(viewId));

        imageView.setBackgroundDrawable(img);

    }



    public void setBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView;
        imageView = ((ImageView) itemView.findViewById(viewId));

        imageView.setImageBitmap(bitmap);
    }

    public void setDrawableId(int viewId, int drawableId, Context context) {
        if (drawableId != 0) {
            ImageView imageView;
            imageView = ((ImageView) itemView.findViewById(viewId));
            imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId));
        }
    }


    public void setLocalImg(int viewId, String path) {
        ImageView imageView;
        imageView = ((ImageView) itemView.findViewById(viewId));
        imageView.setAdjustViewBounds(true);
        if (new File(path).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
        }
//        ViewTreeObserver vto = imageView.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                LinearLayout.LayoutParams para1;
//                para1 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
//                para1.height = para1.width;
//                imageView.setLayoutParams(para1);
//                return true;
//            }
//        });
    }

    public void setLocalBackground(int viewId, String path) {
        ImageView imageView;
        imageView = ((ImageView) itemView.findViewById(viewId));
        imageView.setAdjustViewBounds(true);
        if (new File(path).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
//        ViewTreeObserver vto = imageView.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                LinearLayout.LayoutParams para1;
//                para1 = (LinearLayout.LayoutParams) imageView.getLayoutParams();
//                para1.height = para1.width;
//                imageView.setLayoutParams(para1);
//                return true;
//            }
//        });
    }




}
