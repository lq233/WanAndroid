package com.example.a003.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a003.myapplication.Bean.ArticleBean;
import com.example.a003.myapplication.Bean.BannerBean;
import com.example.a003.myapplication.R;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 * Created by 003 on 2019/2/18.
 */

public class RlvMainPageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    public ArrayList<ArticleBean.DataBean.DatasBean> mList;
    public ArrayList<BannerBean.DataBean> mBannerList;
    private OnItemClickListener mListener;
    private int mNewPosition;

    public RlvMainPageAdapter(Context context, ArrayList<ArticleBean.DataBean.DatasBean> list, ArrayList<BannerBean.DataBean> bannerList) {

        mContext = context;
        mList = list;
        mBannerList = bannerList;
    }

    @Override
    public int getItemViewType(int position) {
//如果有一天,老板说banner不要了,你这样写,写死了
        if (mBannerList.size() > 0 && position == 0) {
            //banner
            return 0;
        } else {
            //文章列表
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_banner, null);
            return new BannerViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_main_page_article, null);
            return new ArticleViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == 0) {
            BannerViewHolder holder1 = (BannerViewHolder) holder;
            holder1.mBanner.setImages(mBannerList).setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    //path,是setImages()方法里面集合的泛型,BannerBean.DataBean
                    BannerBean.DataBean bean = (BannerBean.DataBean) path;
                    Glide.with(context).load(bean.getImagePath()).into(imageView);
                }
            }).start();
        } else {
            //有banner的情况下,position 从1开始,最后20,
            mNewPosition = position;
            if (mBannerList.size() > 0) {
                mNewPosition = position - 1;
            }

            ArticleViewHolder holder1 = (ArticleViewHolder) holder;
            ArticleBean.DataBean.DatasBean bean = mList.get(mNewPosition);
            holder1.mTvAuthor.setText(bean.getAuthor());
            holder1.mTvChapterName.setText(bean.getChapterName());
            holder1.mTvTitle.setText(bean.getTitle());
            holder1.mTvTime.setText(bean.getNiceDate());

            if (bean.isFresh()) {
                holder1.mTvNew.setVisibility(View.VISIBLE);
            } else {
                holder1.mTvNew.setVisibility(View.INVISIBLE);
            }
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener!=null){
                     mListener.onItemClick(v, position);
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (mBannerList.size() > 0) {
            //文章的数量+banner的位置
            return mList.size() + 1;
        } else {
            return mList.size();
        }
    }

    public void addBannerData(ArrayList<BannerBean.DataBean> list) {
        mBannerList.addAll(list);
        notifyDataSetChanged();
    }

    public void addArticleData(ArrayList<ArticleBean.DataBean.DatasBean> list1) {
        mList.addAll(list1);
        notifyDataSetChanged();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        public Banner mBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mBanner = itemView.findViewById(R.id.banner);
        }
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {


        ImageView mIvWan;
        TextView mTvAuthor;
        TextView mTvChapterName;
        TextView mTvTitle;
        ImageView mIvHeart;
        ImageView mIvClock;
        TextView mTvTime;
        TextView mTvNew;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            this.mIvWan = (ImageView) itemView.findViewById(R.id.iv_wan);
            this.mTvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            this.mTvChapterName = (TextView) itemView.findViewById(R.id.tv_chapter_name);
            this.mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            this.mIvHeart = (ImageView) itemView.findViewById(R.id.iv_heart);
            this.mIvClock = (ImageView) itemView.findViewById(R.id.iv_clock);
            this.mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            this.mTvNew = (TextView) itemView.findViewById(R.id.tv_new);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener=listener;
    }
}
