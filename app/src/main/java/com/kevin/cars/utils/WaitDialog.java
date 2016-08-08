package com.kevin.cars.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by kevin
 * on 2016/6/23.
 */
public class WaitDialog {

    private static final String TAG = "WaitDialog";
    private static ProgressDialog progressDialog;
    private static Runnable runnable;
    private static Handler handler = new Handler();

    /**
     * 显示等到对话框
     * @param context context
     * @param message 显示的信息
     * @param disappearTime 消失时间
     * @param broadcastUdp udp实例
     */
    public static void showDialog(Context context, String message, int disappearTime,@Nullable final BroadcastUdp broadcastUdp) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.show();
        runnable = new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if(broadcastUdp!=null){
                    broadcastUdp.stop();
                }
            }
        };

        handler.postDelayed(runnable, disappearTime);
    }

    /**
     * 等待对话框立即消失
     */
    public static void immediatelyDismiss() {
        if (progressDialog != null && handler != null && runnable != null) {
            progressDialog.dismiss();
            handler.removeCallbacks(runnable);
        }
    }


}
