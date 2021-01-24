package com.example.a003.myapplication;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 003 on 2019/2/22.
 */

class ToastUtil {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
