package com.example.a003.myapplication.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a003.myapplication.Bean.MediaBean;
import com.example.a003.myapplication.R;

import java.util.ArrayList;

/**
 * Created by 003 on 2019/2/22.
 */

public class RlvMediaAdapter extends RecyclerView.Adapter {
    public ArrayList<MediaBean> mList;
    private OnItemClickListener mListener;

    public RlvMediaAdapter(ArrayList<MediaBean> list) {

        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, null, false);
        return new MediaViewHolder(inflate);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
       MediaViewHolder holder1= (MediaViewHolder) holder;
       holder1.mTvTitle.setText(mList.get(position).title);
       holder1.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            if (mListener!=null){
                mListener.OnItemClick(v,position);
            }
           }
       });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addData(ArrayList<MediaBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvTitle;

        public MediaViewHolder(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
    public interface OnItemClickListener{
        void OnItemClick(View v,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
}
