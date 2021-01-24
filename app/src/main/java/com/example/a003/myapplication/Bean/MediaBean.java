package com.example.a003.myapplication.Bean;

import java.io.Serializable;

/**
 * Created by 003 on 2019/2/22.
 */

public class MediaBean implements Serializable{
    public String path;
    public String title;

    public MediaBean(String path, String title) {
        this.path = path;
        this.title = title;
    }

    @Override
    public String toString() {
        return "MediaBean{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
