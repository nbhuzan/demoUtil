package cn.trustway.nb.core.menu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.trustway.nb.core.R;
import cn.trustway.nb.core.base.listener.RecycleViewClickListener;
import cn.trustway.nb.core.menu.util.MenuUtil;
import cn.trustway.nb.core.menu.model.MenuModel;


/**
 * Created by huzan on 2017/8/11.
 * 描述：菜单dialog的adapter
 */

public class DialogMenuAdapter extends RecyclerView.Adapter<DialogMenuAdapter.DialogMenuHolder> {
    private LayoutInflater form;
    private Context context;
    @NonNull
    private List<MenuModel> list = new ArrayList<>(); //数据源
    private boolean canSelect = false;  //是否可以多选
    @NonNull
    private SparseBooleanArray selectArray = new SparseBooleanArray(); //项是否选中
    private RecycleViewClickListener onClickListener; //item点击事件


    public DialogMenuAdapter(Context context, boolean canSelect) {
        this.context = context;
        this.canSelect = canSelect;
        this.form = LayoutInflater.from(context);
    }

    public MenuModel getClickItem(int pos) {
        return list.get(pos);
    }



    /**
     * 创建时间：2017/8/11
     * 创建者：huzan
     * 描述：设置item点击事件
     */
    public void setOnClickListener(RecycleViewClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 创建时间：2017/8/11
     * 创建者：huzan
     * 描述：初始化数据
     */
    public void setList(@Nullable List<MenuModel> list, @Nullable List<MenuModel> selectList) {
        if (list != null) {
            this.list.clear();
            this.list.addAll(list);

            if (selectList != null) {//初始化选中的项
                initItemSelect(list, selectList);
            }
            notifyDataSetChanged();
        } else {
            Toast.makeText(context, "菜单数据获取失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建时间：2017/8/11
     * 创建者：huzan
     * 描述：初始化选中记录
     */
    private void initItemSelect(@NonNull List<MenuModel> list, @NonNull List<MenuModel> selectList) {
        int listSize = list.size();
        int selectListSize = selectList.size();
        for (int i = 0; i < listSize; i++) {
            MenuModel di = list.get(i);
            selectArray.put(i, false);
            for (int j = 0; j < selectListSize; j++) {
                MenuModel dj = selectList.get(j);
                if (dj.getKey().equals(di.getKey())) {
                    selectArray.put(i, true);
                }
            }
        }
    }

    /**
     * 创建时间：2017/8/11
     * 创建者：huzan
     * 描述：设置item是否被选中
     */
    private void setItemSelect(int position, boolean isCheck) {
        selectArray.put(position, isCheck);
        notifyItemChanged(position);
    }

    /**
     * Create by Zheming.xin on 2017/8/31 12:12
     * param:
     * description: 移除所有选择的Item
     */
    public void clearSelectedItems() {
        selectArray.clear();
        notifyDataSetChanged();
    }

    /**
     * 创建时间：2017/8/11
     * 创建者：huzan
     * 描述：判断item是否被选中
     */
    private boolean isItemSelect(int position) {
        return selectArray.get(position);
    }

    /**
     * 创建时间：2017/8/11
     * 创建者：huzan
     * 描述：获取被选中的集合
     */
    @NonNull
    public List<MenuModel> getSelectItems() {
        List<MenuModel> tempList = new ArrayList<>();
        for (int i = 0; i < selectArray.size(); i++) {
            if (selectArray.get(i)) {
                tempList.add(list.get(i));
            }
        }
        return tempList;
    }

    @NonNull
    @Override
    public DialogMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DialogMenuHolder(form.inflate(R.layout.item_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DialogMenuHolder holder, int position) {
        MenuModel dictionary = list.get(position);
        if (dictionary == null) {
            return;
        }
        holder.textView_msg.setText(MenuUtil.formatKeyValue(dictionary,MenuUtil.separator_line));
        holder.view.setOnClickListener(v -> {
            if (onClickListener != null && !canSelect) {
                onClickListener.OnItemClickListener(v, holder.getLayoutPosition());
            }
        });
        if (canSelect) {  //多选模式
            canSelectBindView(holder, position);
        }
    }

    /**
     * 创建时间：2017/8/11
     * 创建者：huzan
     * 描述：多选模式独有的bindview
     */
    private void canSelectBindView(@NonNull DialogMenuHolder holder, int position) {
        holder.checkBox_select.setChecked(isItemSelect(position));
        holder.view.setOnClickListener(
                v -> setItemSelect(holder.getLayoutPosition(), !isItemSelect(holder.getLayoutPosition())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DialogMenuHolder extends RecyclerView.ViewHolder {
        private TextView textView_msg;
        private CheckBox checkBox_select;
        private View view;

        DialogMenuHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            textView_msg = itemView.findViewById(R.id.textview_item_dialog_menu_msg);
            checkBox_select = itemView.findViewById(R.id.checkbox_item_dialog_menu_select);
            if (canSelect) {
                checkBox_select.setVisibility(View.VISIBLE);
            } else {
                checkBox_select.setVisibility(View.GONE);
            }
        }

    }
}
