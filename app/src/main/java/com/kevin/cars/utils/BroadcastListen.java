package com.kevin.cars.utils;

import android.support.annotation.Nullable;

/**
 * Created by kevin
 * on 2016/8/5.
 */
public interface BroadcastListen {
        //广播接收接口
    void onReceiveData(byte[] data, int len, @Nullable String remoteIp);
}
