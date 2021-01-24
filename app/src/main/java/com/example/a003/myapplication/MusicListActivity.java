package com.example.a003.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.a003.myapplication.Adapter.RlvMediaAdapter;
import com.example.a003.myapplication.Bean.MediaBean;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {

    private RecyclerView mRlv;
    private RlvMediaAdapter mAdapter;
    private ArrayList<MediaBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        initView();
        initData();
    }

    private void initData() {
       //查询音频系统
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED){
            executeReadAudio();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
    }

    /**
     * 执行读取操作
     * 1.遍历sd卡
     * 2.内容提供者
     */
    private void executeReadAudio() {
        ContentResolver resolver = getContentResolver();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor query = resolver.query(uri, null, null, null, null);

        ArrayList<MediaBean> list = new ArrayList<>();
        if (query!=null){
            while (query.moveToNext()){
                String path = query.getString(query.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = query.getString(query.getColumnIndex(MediaStore.Audio.Media.TITLE));
                MediaBean mediaBean = new MediaBean(path, title);
                list.add(mediaBean);
            }
            query.close();
        }
        //
        if (list.size()>0){
            mAdapter.addData(list);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                executeReadAudio();
            }else {
                ToastUtil.showToast(this,"你拒绝了权限");
            }
        }
    }

    private void initView() {
        mRlv = (RecyclerView) findViewById(R.id.rlv);
        mRlv.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        mAdapter = new RlvMediaAdapter(mList);
        mRlv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRlv.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new RlvMediaAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                //跳转到音乐播放界面
                Intent intent = new Intent(MusicListActivity.this, MusicPlayActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("data",mAdapter.mList);
                startActivity(intent);
            }
        });
    }
}
