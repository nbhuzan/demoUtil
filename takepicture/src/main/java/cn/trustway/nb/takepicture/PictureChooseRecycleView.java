package cn.trustway.nb.takepicture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzan on 2018/2/27.
 * 描述：
 */

public class PictureChooseRecycleView extends RecyclerView {
    private Context context;
    private List<PictureBean> pictureList = new ArrayList<>();
    private PictureAdapter pictureAdapter;
    private int count = 4;
    private Action<String> actionDeletePicture;

    public PictureChooseRecycleView(Context context) {
        this(context, null);
    }

    public PictureChooseRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureChooseRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }



    private void init() {
        setBackgroundColor(Color.WHITE);
        setPadding(0,20,20,20);
        pictureAdapter = new PictureAdapter();
        final GridLayoutManager manager = new GridLayoutManager(context, count) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        this.setLayoutManager(manager);
        this.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                GridLayoutManager.LayoutParams gl = (GridLayoutManager.LayoutParams) view.getLayoutParams();
                int spanSize = gl.getSpanSize();
                int spanIndex = gl.getSpanIndex();
                outRect.top = 10;
                outRect.bottom = 10;
                outRect.left = 20;

            }
        });
        this.setAdapter(pictureAdapter);
        pictureAdapter.setOnClick(new OnClick() {
            @Override
            public void onInsert() {
                WidgetCamera widgetCamera = new WidgetCamera();
                widgetCamera.init(context);
                widgetCamera.setPicPath(Environment.getExternalStorageDirectory().getPath());
                widgetCamera.setOnToolListener(new WidgetCamera.OnToolListener() {
                    @Override
                    public void onExit() {

                    }

                    @Override
                    public void onSubmit(String path, Bitmap bitmap) {
                        pictureList.add(new PictureBean(path,true));
//                        pictureAdapter.notifyItemRangeInserted(pictureList.size()-1,1);
                        pictureAdapter.notifyDataSetChanged();
                    }
                });
                widgetCamera.show(((Activity) context).getFragmentManager(), "camera");
//                widgetCamera.setPreview();
            }

            @Override
            public void onClick(int pos, View v) {

            }


            @Override
            public void onDelete(int pos) {
                if(actionDeletePicture!=null){
                    actionDeletePicture.call(pictureList.get(pos).getPath());
                }
                pictureList.remove(pos);

                    pictureAdapter.notifyItemRangeRemoved(pos, 1);

            }
        });
    }

    public void setActionDeletePicture(Action<String> actionDeletePicture) {
        this.actionDeletePicture = actionDeletePicture;
    }

    public List<PictureBean> getPicture(){
        return pictureList;
    }


    /**
     *创建时间：2018/6/16
     *创建者：huzan
     *描述：设置图片张数
     */
    public void setCount(int count){
        this.count = count;
    }

    /**
     *创建时间：2018/6/16
     *创建者：huzan
     *描述：添加图片列表，islocalPic 是否是本地图片
     */
    public void setPictureList(List<PictureBean> pictureList){
        this.pictureList.addAll(pictureList);
        pictureAdapter.notifyDataSetChanged();
    }



    private class PictureAdapter extends RecyclerView.Adapter<PictureHolder> {
        private OnClick onClick;
        private boolean canDelete = false;

        public void setOnClick(OnClick onClick) {
            this.onClick = onClick;
        }

        public void setCanDelete() {
            canDelete = !canDelete;
            notifyDataSetChanged();
        }



        @Override
        public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_takepicture_pic, parent, false);
            return new PictureHolder(view);
        }

        @Override
        public void onBindViewHolder(final PictureHolder holder, int position) {
            ImageView imageView = holder.getIv();
            TextView tvDelete = holder.getTvDelete();
            RequestOptions cropOptions = new RequestOptions()
                    .override((int)context.getResources().getDimension(R.dimen.x90),(int)context.getResources().getDimension(R.dimen.y90))
                    .centerCrop();
            if (position == pictureList.size()) {
                Glide.with(context).applyDefaultRequestOptions(cropOptions).load(R.drawable.icon_default_photo).into(imageView);
                tvDelete.setVisibility(GONE);
            } else {
                if(pictureList.get(position).isLocal()) {
                    Glide.with(context).applyDefaultRequestOptions(cropOptions).load(new File(pictureList.get(position).getPath())).into(imageView);
                }else{
                    Glide.with(context).applyDefaultRequestOptions(cropOptions).load(pictureList.get(position).getPath()).into(imageView);
                }
                if(canDelete) {
                    tvDelete.setVisibility(VISIBLE);
                }else{
                    tvDelete.setVisibility(GONE);
                }
                tvDelete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClick != null) {
                            System.out.println("删除图片"+holder.getLayoutPosition());
                            onClick.onDelete(holder.getLayoutPosition());
                        }
                    }
                });
            }

            holder.getItemView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick != null) {
                        if (holder.getLayoutPosition() == pictureList.size()) {
                            onClick.onInsert();
                        } else {
                            onClick.onClick(holder.getLayoutPosition(), v);
                        }
                    }
                }
            });
            holder.getItemView().setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setCanDelete();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            if (count <= pictureList.size()) {
                return pictureList.size();
            } else {
                return pictureList.size() + 1;
            }
        }
    }

    interface OnClick {
        void onInsert();

        void onClick(int pos, View v);


        void onDelete(int pos);
    }

    private class PictureHolder extends ViewHolder {
        private ImageView iv;
        private TextView tvDelete;
        private View itemView;

        PictureHolder(View itemView) {
            super(itemView);
//            Resources resources = context.getResources();
//            DisplayMetrics dm = resources.getDisplayMetrics();
//            float density = dm.density;
//            int width = dm.widthPixels;
//            int height = dm.heightPixels;
//

            this.itemView = itemView;
            iv = itemView.findViewById(R.id.iv_picture_content);
            tvDelete = itemView.findViewById(R.id.tv_picture_delete);


        }

        public ImageView getIv() {
            return iv;
        }

        public TextView getTvDelete() {
            return tvDelete;
        }

        public View getItemView() {
            return itemView;
        }
    }

    public interface Action<T>{
        void call(T t);
    }

}
