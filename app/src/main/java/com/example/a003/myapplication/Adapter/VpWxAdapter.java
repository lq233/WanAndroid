package com.example.a003.myapplication.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.a003.myapplication.Bean.WxTabBean;
import com.example.a003.myapplication.Fragment.WxArticleFragment;

import java.util.ArrayList;

/**
 * Created by 003 on 2019/2/21.
 */

public class VpWxAdapter extends FragmentPagerAdapter {
    private final ArrayList<WxTabBean.DataBean> mList;
    private final ArrayList<WxArticleFragment> mList1;

    public VpWxAdapter(FragmentManager fm, ArrayList<WxTabBean.DataBean> list, ArrayList<WxArticleFragment> list1) {
        super(fm);
        mList = list;
        mList1 = list1;
    }


    @Override
    public Fragment getItem(int position) {
        return mList1.get(position);
    }

    @Override
    public int getCount() {
        return mList1.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getName();
    }
}
