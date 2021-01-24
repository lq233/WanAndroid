package com.example.a003.myapplication.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.a003.myapplication.Bean.ProjectTabBean;
import com.example.a003.myapplication.Fragment.ProjectArticleFragment;

import java.util.ArrayList;

/**
 * Created by 003 on 2019/2/20.
 */

public class VpProjectAdapter extends FragmentPagerAdapter {


    private final ArrayList<ProjectArticleFragment> mList;
    private final ArrayList<ProjectTabBean.DataBean> mObj;

    public VpProjectAdapter(FragmentManager fm, ArrayList<ProjectArticleFragment> list, ArrayList<ProjectTabBean.DataBean> obj) {
        super(fm);

        mList = list;
        mObj = obj;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mObj.get(position).getName();
    }
}
