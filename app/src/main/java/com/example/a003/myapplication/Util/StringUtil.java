package com.example.a003.myapplication.Util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 003 on 2019/2/20.
 */

public class StringUtil {
    public static String stream2String(InputStream is) {
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        try {
            while ((len = is.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
