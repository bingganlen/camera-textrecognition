package com.qgqaqgqa.deviceinfomanager.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qgqaqgqa.deviceinfomanager.R;
import com.qgqaqgqa.deviceinfomanager.intf.OnRecyclerViewItemClickListener;
import com.qgqaqgqa.deviceinfomanager.model.DeviceInfoModel;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目详情
 * User: Created by 钱昱凯
 * Date: 2019/11/14
 * Time: 21:36
 * EMail: 342744291@qq.com
 */
public class DeviceInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mContext;
    private List<DeviceInfoModel> mItems;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public DeviceInfoAdapter(Activity mContext, List<DeviceInfoModel> items) {
        this.mContext = mContext;
        this.mItems = items;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_info, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).init(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();// == null || mItems.size() == 0 ? 0 : ((mItems.size() - 1) / 5 + 1) * 5;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_device_id)
        TextView tvDeviceId;
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;

        private int mPosition = 0;

        public MyViewHolder(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
            itemView.setOnClickListener(this);


        }

        public void init(int position) {
            mPosition = position;
            tvDeviceId.setText(String.format("%d、设备ID：%s", position+1,mItems.get(position).getId()));
            tvDeviceName.setText(String.format("设备详情：%s", mItems.get(position).getName()));
        }

        @Override
        public void onClick(View v) {
            if (mOnRecyclerViewItemClickListener != null) {
                mOnRecyclerViewItemClickListener.onRecyclerViewItemClick(v, mPosition);
            }
        }
    }
}
