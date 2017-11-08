package cn.trustway.nb.core.base.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import cn.trustway.nb.core.base.listener.RecycleViewRefreshListener;


/**
 * Created by huzan  2017/3/17 19:49.
 * 描述：
 */

public class TrustwayRecycleView extends RecyclerView {

    boolean isSliding = false;
    RecycleViewRefreshListener listener;
    ScrollHeaderFooterStateListener scrollHeaderFooterStateListener;

    protected boolean inHeader = true;
    protected boolean inFooter = false;

    public TrustwayRecycleView(Context context) {
        super(context);
    }

    public TrustwayRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TrustwayRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setListener(RecycleViewRefreshListener listener) {
        this.listener = listener;
    }

    public void setScrollHeaderFooterStateListener(ScrollHeaderFooterStateListener scrollHeaderFooterStateListener) {
        this.scrollHeaderFooterStateListener = scrollHeaderFooterStateListener;
    }

    private boolean getReverseLayout(){
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        return layoutManager.getReverseLayout();
    }

    @Override
    public void setOverScrollMode(int overScrollMode) {

        super.setOverScrollMode(OVER_SCROLL_NEVER);
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        RecyclerView.LayoutManager manager = getLayoutManager();
        int totalItemCount = manager.getItemCount();
        int firstActualVisbleItem = ((LinearLayoutManager)manager).findFirstCompletelyVisibleItemPosition(); //第一个完全可见
        int lastActualVisbleItem = ((LinearLayoutManager)manager).findLastCompletelyVisibleItemPosition(); //最后一个完全可见
        // 当不滚动时
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            int lastVisibleItem = ((LinearLayoutManager)manager).findLastVisibleItemPosition();  //最后一个可见
            if (lastVisibleItem == (totalItemCount - 1) && listener!=null) {
               listener.refresh();
            }

            if (scrollHeaderFooterStateListener != null) {
                if (firstActualVisbleItem == 0 && !isSliding && !inHeader) {
                    scrollHeaderFooterStateListener.scrollToHeader();
                    inHeader = true;
                } else if (lastActualVisbleItem == (totalItemCount - 1) && isSliding && !inFooter) {
                    scrollHeaderFooterStateListener.scrollToFooter();
                    inFooter = true;
                }
            }
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if(dy>0){
            isSliding = true;
        }else{
            isSliding = false;
        }
        if (scrollHeaderFooterStateListener != null) {
            RecyclerView.LayoutManager manager = getLayoutManager();
            int totalItemCount = manager.getItemCount();
            int firstActualVisbleItem = ((LinearLayoutManager)manager).findFirstCompletelyVisibleItemPosition(); //第一个完全可见
            int lastActualVisbleItem = ((LinearLayoutManager)manager).findLastCompletelyVisibleItemPosition(); //最后一个完全可见
            if (firstActualVisbleItem >= 0 && isSliding && inHeader) {
                scrollHeaderFooterStateListener.scrollFromHeader();
                inHeader = false;
                inFooter = false;
            } else if (lastActualVisbleItem <= (totalItemCount - 1) && !isSliding && inFooter) {
                scrollHeaderFooterStateListener.scrollFromFooter();
                inFooter = false;
                inHeader = false;
            }
        }
    }

    /**
     * Create by Zheming.xin on 2017/8/18 17:09
     * param:
     * description: 用于判断view 滑动到头，滑动到尾，从头部向下滑动，从尾部向上滑动
     */
    public interface ScrollHeaderFooterStateListener {
        void scrollToHeader();
        void scrollToFooter();
        void scrollFromHeader();
        void scrollFromFooter();
    }
}
