package com.example.a003.myapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.a003.myapplication.Bean.MediaBean;

import java.util.ArrayList;

public class MusicPlayActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MusicPlayActivity";
    private Object mData;
    private int mPosition;
    private ArrayList<MediaBean> mList;
    /**
     * 登录
     */
    private TextView mTvTitle;
    private SeekBar mSeekBar;
    /**
     * 01:20
     */
    private TextView mTvTime;
    /**
     * 03:20
     */
    private TextView mTvDuration;
    private ImageView mIvLast;
    private ImageView mIvPlay;
    private ImageView mIvNext;
    private ServiceConnection mConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        initView();
        getData();
        //开启之后播放音乐
        startSer();
        //绑定服务,Activiyt控制服务中mediaPlayer的播放
        bindSer();
    }

    private void bindSer() {
        Intent intent = new Intent(this, MusicService.class);
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, mConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConn != null) {
            unbindService(mConn);
            mConn = null;
        }
    }

    private void startSer() {
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("position", mPosition);
        intent.putExtra("data", mList);
        startService(intent);
    }

    public void getData() {
        Intent intent = getIntent();
        mPosition = intent.getIntExtra("position", 0);
        mList = (ArrayList<MediaBean>) intent.getSerializableExtra("data");
        Log.d(TAG, "position: " + mPosition + ",list:" + mList.toString());
    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);
        mIvLast = (ImageView) findViewById(R.id.iv_last);
        mIvPlay = (ImageView) findViewById(R.id.iv_play);
        mIvNext = (ImageView) findViewById(R.id.iv_next);
        mIvLast.setOnClickListener(this);
        mIvPlay.setOnClickListener(this);
        mIvNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv_last:
                break;
            case R.id.iv_play:
                break;
            case R.id.iv_next:
                break;
        }
    }
}
