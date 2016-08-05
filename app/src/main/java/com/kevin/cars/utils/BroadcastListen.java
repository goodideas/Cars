package com.kevin.cars.utils;

import android.support.annotation.Nullable;

/**
 * Created by Administrator
 * on 2016/8/5.
 */
public interface BroadcastListen {
    void onReceiveData(byte[] data, int len, @Nullable String remoteIp);
}
