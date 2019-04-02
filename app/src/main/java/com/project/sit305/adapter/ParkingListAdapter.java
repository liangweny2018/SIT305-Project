package com.project.sit305.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.sit305.R;
import com.project.sit305.bean.ParkingDataBean;
import com.project.sit305.utils.ImageLoaderUtils;

import java.util.List;

/**
 * DESC:
 * Date: 2019/4/2  10:39 AM
 * Time: 下午4:29
 * author: liang
 */
public class ParkingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM   = 0;
    private static final int TYPE_FOOTER = 1;

    private List<ParkingDataBean> mData;
    private boolean               mShowFooter = true;
    private Context               mContext;

    private OnItemClickListener mOnItemClickListener;

    public ParkingListAdapter(Context context) {
        this.mContext = context;
    }

    public void setmDate(List<ParkingDataBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView

        return TYPE_ITEM;
//        if (!mShowFooter) {
//            return TYPE_ITEM;
//        }
//        if (position + 1 == getItemCount()) {
//            return TYPE_FOOTER;
//        } else {
//            return TYPE_ITEM;
//        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
//        if (viewType == TYPE_ITEM) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_parking, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
//        }
//        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            ParkingDataBean news = mData.get(position);
            if (news == null) {
                return;
            }
            ((ItemViewHolder) holder).mTitle.setText(news.getName());
            ((ItemViewHolder) holder).mDesc.setText("$ " + news.getPrice());
            ((ItemViewHolder) holder).mDistance.setText(news.getDistance() + " areas left");
//            Uri uri = Uri.parse(news.getImgsrc());
//            ((ItemViewHolder) holder).mNewsImg.setImageURI(uri);
            ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).mNewsImg, "http://cdn-img.q-media.cn/tes/whkm/i/5c7f3b329972f40cec698637.jpg?auth_key=4636195200-0-0-e637817a9531bbbcbe4bc074f3e5681c&x-oss-process=image/resize,m_lfit,h_400,w_400");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();

//        int begin = mShowFooter ? 1 : 0;
//        if (mData == null) {
//            return begin;
//        }
//        return mData.size() + begin;
    }

    public ParkingDataBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView  mTitle;
        public TextView  mDesc;
        public TextView  mDistance;
        public ImageView mNewsImg;

        public ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tvTitle);
            mDesc = (TextView) v.findViewById(R.id.tvDesc);
            mDistance = (TextView) v.findViewById(R.id.tvDistance);
            mNewsImg = (ImageView) v.findViewById(R.id.ivNews);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getPosition());
            }
        }
    }

}
