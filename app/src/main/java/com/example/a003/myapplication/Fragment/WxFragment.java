package com.example.a003.myapplication.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a003.myapplication.Adapter.VpWxAdapter;
import com.example.a003.myapplication.Bean.WxTabBean;
import com.example.a003.myapplication.R;
import com.example.a003.myapplication.Util.StringUtil;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by 003 on 2019/2/18.
 */

public class WxFragment extends Fragment {
    private View view;
    private TabLayout mTabLayout;
    private ViewPager mVp;
    private String mUrl = "http://wanandroid.com/wxarticle/chapters/json ";
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayList<WxTabBean.DataBean> list = (ArrayList<WxTabBean.DataBean>) msg.obj;
            //创建碎片
            ArrayList<WxArticleFragment> list1 = new ArrayList<>();
            //有几个tab就有几个碎片
            for (int i = 0; i < list.size(); i++) {
                WxArticleFragment fragment = new WxArticleFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("cid", list.get(i).getId());
                fragment.setArguments(bundle);
                list1.add(fragment);
            }

            //适配器
            //VpProjectAdapter adatper = new VpProjectAdapter(getActivity().getSupportFragmentManager());
            //碎片嵌套碎片,使用的碎片管理器getChildFragmentManager(),要不然会有bug
            VpWxAdapter adapter = new VpWxAdapter(getChildFragmentManager(),list,list1);
             mVp.setAdapter(adapter);
            //关联tabLayout和viewPager
            mTabLayout.setupWithViewPager(mVp);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wx, null);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(mUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    int code = con.getResponseCode();
                    if (code == 200) {
                        String s = StringUtil.stream2String(con.getInputStream());
                        Gson gson = new Gson();
                        WxTabBean bean = gson.fromJson(s, WxTabBean.class);
                        if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
                            Message obtain = Message.obtain();
                            obtain.obj = bean.getData();
                            mHandler.sendMessage(obtain);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mVp = (ViewPager) view.findViewById(R.id.vp);
    }
}
