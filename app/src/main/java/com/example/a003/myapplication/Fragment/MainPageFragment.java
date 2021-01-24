package com.example.a003.myapplication.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a003.myapplication.Bean.ArticleBean;
import com.example.a003.myapplication.Bean.BannerBean;
import com.example.a003.myapplication.ColorDividerItemDecoration;
import com.example.a003.myapplication.R;
import com.example.a003.myapplication.Adapter.RlvMainPageAdapter;
import com.example.a003.myapplication.WebViewActivity;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 003 on 2019/2/18.
 */

public class MainPageFragment extends Fragment {
    private static final String TAG = "MainPageFragment";
    private View view;
    private XRecyclerView mRlv;
    private String mBannerUrl = "http://www.wanandroid.com/banner/json";
    //http://www.wanandroid.com/article/list/0/json
    private String mArticleUrl = "http://www.wanandroid.com/article/list/";
    private RlvMainPageAdapter mAdapter;

    private int mPage = 0;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //banner
                    ArrayList<BannerBean.DataBean> list = (ArrayList<BannerBean.DataBean>) msg.obj;
                    mAdapter.addBannerData(list);
                    break;
                case 1:
                    //article
                    ArrayList<ArticleBean.DataBean.DatasBean> list1 = (ArrayList<ArticleBean.DataBean.DatasBean>) msg.obj;
                    mAdapter.addArticleData(list1);
                    break;
            }
            //解决下拉异常 和刷新异常
            mRlv.refreshComplete();
            mRlv.loadMoreComplete();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, null);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        //banner + 文章列表
        initBannerData();
        initArticleData();

    }

    //文章列表
    private void initArticleData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(mArticleUrl + mPage + "/json");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    int responseCode = con.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = con.getInputStream();
                        String result = stream2String(inputStream);

                        Gson gson = new Gson();
                        ArticleBean bean = gson.fromJson(result, ArticleBean.class);
                        if (bean != null && bean.getData() != null && bean.getData().getDatas() != null
                                && bean.getData().getDatas().size() > 0) {
                            Message obtain = Message.obtain();
                            obtain.what = 1;
                            obtain.obj = bean.getData().getDatas();
                            mHandler.sendMessage(obtain);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initBannerData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(mBannerUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    int responseCode = con.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = con.getInputStream();
                        String result = stream2String(inputStream);

                        Gson gson = new Gson();
                        BannerBean bannerBean = gson.fromJson(result, BannerBean.class);
                        if (bannerBean != null && bannerBean.getData() != null &&
                                bannerBean.getData().size() > 0) {
                            Message obtain = Message.obtain();
                            obtain.what = 0;
                            obtain.obj = bannerBean.getData();
                            mHandler.sendMessage(obtain);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
        }.start();
    }

    //进行解析
    private String stream2String(InputStream inputStream) {
        byte[] by = new byte[1024];

        int len;
        StringBuffer sb = new StringBuffer();
        try {
            while ((len = inputStream.read(by)) != -1) {
                sb.append(new String(by, 0, len));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initView(View view) {
        mRlv = (XRecyclerView) view.findViewById(R.id.rlv);
        mRlv.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<ArticleBean.DataBean.DatasBean> list = new ArrayList<>();
        ArrayList<BannerBean.DataBean> bannerList = new ArrayList<>();
        mAdapter = new RlvMainPageAdapter(getContext(), list, bannerList);
        mRlv.setAdapter(mAdapter);
        mRlv.addItemDecoration(new ColorDividerItemDecoration(Color.parseColor("#00ffffff"), 50));

        //下拉刷新，上拉加载更多
        mRlv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                mPage = 0;
                mAdapter.mList.clear();
                mAdapter.mBannerList.clear();
                initData();
            }

            @Override
            public void onLoadMore() {
                //上拉加载
                mPage++;
                initArticleData();

            }
        });

        mAdapter.setOnItemClickListener(new RlvMainPageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "" + position);
                go2WebView(position);
            }
        });
    }

    private void go2WebView(int position) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", mAdapter.mList.get(position - 1).getLink());
        startActivity(intent);
    }
}
