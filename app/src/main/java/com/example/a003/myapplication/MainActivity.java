package com.example.a003.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a003.myapplication.Fragment.KnowledgeFragment;
import com.example.a003.myapplication.Fragment.MainPageFragment;
import com.example.a003.myapplication.Fragment.NavigationFragment;
import com.example.a003.myapplication.Fragment.ProjectFragment;
import com.example.a003.myapplication.Fragment.WxFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * My Application
     */
    private TextView mTvTitle;
    private Toolbar mToolBar;
    private FrameLayout mContainer;
    private LinearLayout mLl;
    private NavigationView mNv;
    private DrawerLayout mDl;
    private TabLayout mTabLayout;
    private ArrayList<Fragment> mFragments;
    private FragmentManager mManager;
    private int mLastposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mManager = getSupportFragmentManager();
        initView();
        initFragments();
        addMainPageFragment();
    }

    /**
     * 打开软软件默认显示的
     */
    private void addMainPageFragment() {
        FragmentTransaction transaction = mManager.beginTransaction();//开启事务
        transaction.add(R.id.container, mFragments.get(0));
        transaction.commit();//提交事务

        mTvTitle.setText(R.string.mainpage);
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new MainPageFragment());
        mFragments.add(new KnowledgeFragment());
        mFragments.add(new WxFragment());
        mFragments.add(new NavigationFragment());
        mFragments.add(new ProjectFragment());
    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mLl = (LinearLayout) findViewById(R.id.ll);
        mNv = (NavigationView) findViewById(R.id.nv);
        mDl = (DrawerLayout) findViewById(R.id.dl);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);

        mToolBar.setTitle("");
        //新官上任
        setSupportActionBar(mToolBar);
        //解决抽屉菜单图片的问题
        mNv.setItemIconTintList(null);

        mNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.music:
                        startActivity(new Intent(MainActivity.this,MusicListActivity.class));
                        break;
                }
                return false;
            }
        });
        //旋转按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDl, mToolBar, R.string.app_name, R.string.app_name);
        mDl.addDrawerListener(toggle);
        toggle.syncState();

        mDl.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //slideOffset滑动的百分比
                //抽屉部分滑出多少,主界面向右移动多少
                //抽屉菜单滑动距离
                float x = mNv.getWidth() * slideOffset;
                //设置x方向的位置
                mLl.setX(x);
                super.onDrawerSlide(drawerView, slideOffset);
            }
        });

        //tablayout
        for (int i = 0; i < 5; i++) {
            //自己顶一个方法来实现tablayout
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTab(i)));
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab选中后切换对应碎片
                int position = tab.getPosition();
                switchFragment(position);
                //一旦完成切换,这个碎片就是下次切换tab要隐藏的对象
                mLastposition = position;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void switchFragment(int position) {
        //要切换到的fragment
        Fragment fragment = mFragments.get(position);
        FragmentTransaction transaction = mManager.beginTransaction();
        //除了把tab对应位置的Fragment添加,隐藏上一个显示的Fragment
        if (!fragment.isAdded()) {
            //同一个fragment添加第二次会造成软件崩溃
            transaction.add(R.id.container, fragment);
        }
        transaction.show(fragment);
        transaction.hide(mFragments.get(mLastposition));
        transaction.commit();
        //切换toolbar标题
        if (position == 0) {
            mTvTitle.setText(R.string.mainpage);
        } else if (position == 1) {
            mTvTitle.setText(R.string.knowledge);
        } else if (position == 2) {
            mTvTitle.setText(R.string.wx);
        } else if (position == 3) {
            mTvTitle.setText(R.string.navigation);
        } else if (position == 4) {
            mTvTitle.setText(R.string.project);
        }
    }

    private View getTab(int i) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.tab, null, false);
        ImageView ivTab = inflate.findViewById(R.id.iv_tab);
        TextView tvTab = inflate.findViewById(R.id.tv_tab);
        if (i == 0) {
            ivTab.setImageResource(R.drawable.home);
            tvTab.setText(R.string.mainpage);
        } else if (i == 1) {
            ivTab.setImageResource(R.drawable.knowledge);
            tvTab.setText(R.string.knowledge);
        } else if (i == 2) {
            ivTab.setImageResource(R.drawable.wx);
            tvTab.setText(R.string.wx);
        } else if (i == 3) {
            ivTab.setImageResource(R.drawable.navigation);
            tvTab.setText(R.string.navigation);
        } else if (i == 4) {
            ivTab.setImageResource(R.drawable.project);
            tvTab.setText(R.string.project);
        }
        return inflate;
    }

    /**
     * 选项菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
