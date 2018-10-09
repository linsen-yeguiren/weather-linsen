package com.example.linsen.weather.netUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by linsen on 2018/9/28.
 */

public class NetUtil {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_MOBILE = 1;
    public static final int NETWORN_WIFI = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NETWORN_NONE;
        }
        int type = networkInfo.getType();
        if (type == connectivityManager.TYPE_MOBILE) {
            return NETWORN_MOBILE;
        } else if (type == connectivityManager.TYPE_WIFI) {
            return NETWORN_WIFI;
        }
        return NETWORN_NONE;
    }
}
