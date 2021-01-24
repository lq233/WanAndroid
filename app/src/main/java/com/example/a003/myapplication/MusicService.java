package com.example.a003.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.a003.myapplication.Bean.MediaBean;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 003 on 2019/2/22.
 * 拿着个服务播放音乐
 */

public class MusicService extends Service {

    private int mPosition;
    private MediaPlayer mMediaPlayer;
    private ArrayList<MediaBean> mList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        //1.创建mediaplayer对象
        mMediaPlayer = new MediaPlayer();
        //4.加载完成监听
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //资源加载ok了,就可以播放了
                //5.播放
                mMediaPlayer.start();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPosition = intent.getIntExtra("position", 0);
        mList = (ArrayList<MediaBean>) intent.getSerializableExtra("data");

        //2.设置资源
        try {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mList.get(mPosition).path);
            //3.加载资源,异步加载
            mMediaPlayer.prepareAsync();
            //异步加载,不能在这里直接播放
            //mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
