package com.example.a003.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a003.myapplication.Adapter.VpProjectAdapter;
import com.example.a003.myapplication.Bean.ProjectTabBean;
import com.example.a003.myapplication.Fragment.ProjectArticleFragment;
import com.example.a003.myapplication.Util.StringUtil;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 003 on 2019/2/18.
 */

public class ProjectFragment extends Fragment {
    private static final String TAG = "ProjectFragment";
    private View view;
    private TabLayout mTabLayout;
    private ViewPager mVp;
    private XRecyclerView mXrl;

    private String mUrl = "http://www.wanandroid.com/project/tree/json";
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<ProjectTabBean.DataBean> obj = (ArrayList<ProjectTabBean.DataBean>) msg.obj;
            //创建碎片
            ArrayList<ProjectArticleFragment> list = new ArrayList<>();
            //有几个tab,新建几个碎片,网络上获取tab个数
            for (int i = 0; i < obj.size(); i++) {
                ProjectArticleFragment fragment = new ProjectArticleFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("cid", obj.get(i).getId());
                fragment.setArguments(bundle);
                list.add(fragment);
            }
            //适配器
            //VpProjectAdapter adatper = new VpProjectAdapter(getActivity().getSupportFragmentManager());
            //碎片嵌套碎片,使用的碎片管理器getChildFragmentManager(),要不然会有bug
            VpProjectAdapter adatper = new VpProjectAdapter(getChildFragmentManager(), list, obj);
            mVp.setAdapter(adatper);
            //关联tabLayout和viewPager
            mTabLayout.setupWithViewPager(mVp);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, null);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(mUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    int responseCode = con.getResponseCode();
                    if (responseCode == 200) {
                        InputStream inputStream = con.getInputStream();
                        String result = StringUtil.stream2String(inputStream);

                        Log.d(TAG, "tab: " + result);
                        Gson gson = new Gson();
                        ProjectTabBean bean = gson.fromJson(result, ProjectTabBean.class);
                        if (bean != null && bean.getData() != null &&
                                bean.getData().size() > 0) {
                            Message obtain = Message.obtain();
                            obtain.obj = bean.getData();
                            mHandler.sendMessage(obtain);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView(View view) {

        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mVp = (ViewPager) view.findViewById(R.id.vp);
    }
}
