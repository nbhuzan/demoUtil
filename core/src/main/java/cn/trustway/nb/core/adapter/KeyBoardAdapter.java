package cn.trustway.nb.core.adapter;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.listener.RecycleViewClickListener;
import cn.trustway.nb.core.util.BaseHolder;


/**
 * Created by huzan on 2017/8/2.
 * 描述：录入车牌界面的键盘adapter
 */

public class KeyBoardAdapter extends BaseRecycleAdapter<String> {
    private RecycleViewClickListener onClickListener;
    private OnBeforeInputClickListener onOtherKeyBoard;
    private OnKeyTouchListener onKeyTouchListener;
    private boolean isUpperCase = true;
    private int cancelStyle = 2; //删除键大小（默认占两行）

    public boolean isUpperCase() {
        return isUpperCase;
    }

    public void setUpperCase(boolean upperCase) {
        isUpperCase = upperCase;
        notifyDataSetChanged();
    }

    public void setOnKeyTouchListener(OnKeyTouchListener onKeyTouchListener) {
        this.onKeyTouchListener = onKeyTouchListener;
    }

    public void setOnCancelKeyBoard(OnBeforeInputClickListener onOtherKeyBoard) {
        this.onOtherKeyBoard = onOtherKeyBoard;
    }

    public void setOnClickListener(RecycleViewClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public KeyBoardAdapter(Context context){
        this(context,false);
    }

    public void setCancelStyle(int style){
        this.cancelStyle = style;
    }

    private KeyBoardAdapter(Context context, boolean isPaging) {
        super(context, isPaging);
    }

    @Override
    protected int setLayout() {
        return R.layout.item_keyboard;
    }

    @Override
    protected int setFootLayout() {
        return 0;
    }

    @Override
    protected void bindYourViewHolder(BaseHolder holder, int pos) {
        String str = itemList.get(pos);
        if(str.equals("移动警务")) {
            ImageView imageView_cancel = (ImageView) holder.getViewById(R.id.imageview_keyboard_next);
            holder.setText(R.id.textview_keyboard_key, null);
            holder.getViewById(R.id.textview_keyboard_key).setVisibility(View.GONE);
            imageView_cancel.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView_cancel.getLayoutParams();
            layoutParams.height = (int) (context.getResources().getDimension(R.dimen.y55)*cancelStyle);
            imageView_cancel.setLayoutParams(layoutParams);
            holder.getView().setOnClickListener(v -> {
                if (onOtherKeyBoard != null) {
                    onOtherKeyBoard.onClick();
                }
            });
        }else{
            holder.getView().setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.OnItemClickListener(v, holder.getLayoutPosition());
                }
            });
            holder.getView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){

                        case MotionEvent.ACTION_DOWN:
                            if(onKeyTouchListener!=null){
                                onKeyTouchListener.onDown(v,holder.getLayoutPosition());
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(onKeyTouchListener!=null){
                                onKeyTouchListener.onUp();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            if(onKeyTouchListener!=null){
                                onKeyTouchListener.onUp();
                            }
                            break;
                    }
                    return false;
                }
            });
            holder.getViewById(R.id.imageview_keyboard_next).setVisibility(View.GONE);
            holder.getViewById(R.id.textview_keyboard_key).setVisibility(View.VISIBLE);
            if(isUpperCase) {
                holder.setText(R.id.textview_keyboard_key, str);
            }else{
                holder.setText(R.id.textview_keyboard_key, str.toLowerCase());
            }
        }



    }

    public interface OnBeforeInputClickListener{
        void onClick();
    }

    public interface OnKeyTouchListener{
        void onDown(View v, int pos);
        void onUp();
    }
}
