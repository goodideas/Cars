package com.kevin.cars.utils;

import android.support.annotation.Nullable;

/**
 * Created by Administrator
 * on 2016/7/5.
 */
public interface OnReceiveListen {
    //udp接收接口
    void onReceiveData(byte[] data, int len, @Nullable String remoteIp);
}

