package com.kevin.cars.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author zhuangbinbin
 * Time 2016/1/16.
 */
public class SpHelper {


    private final String spFileName = "car";
    private final String spCarIp = "carIp";

    private SharedPreferences carShared;
    private SharedPreferences.Editor carEditor;

    public SpHelper(Context context) {
        carShared = context.getSharedPreferences(
                spFileName, Activity.MODE_PRIVATE);
    }


    /**
     * 取得carIp
     * @return 保存的carIp
     */
    public String getSpCarIp() {
        return carShared.getString(spCarIp, null);
    }

    /**
     * 保存IP
     * @param carIp carIp
     */
    public void saveSpCarIp(String carIp) {
        carEditor = carShared.edit();
        carEditor.putString(spCarIp, carIp);
        carEditor.apply();
    }




}
