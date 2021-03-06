package com.kevin.cars.utils;

import android.util.Log;

/**
 * Created by kevin
 * on 2016/1/12.
 */
public class Util {


    /**
     * 16进制字符串转byte数组
     */
    public static byte[] HexString2Bytes(String hexString) {
        int stringLength = hexString.length();
        byte[] data = new byte[(stringLength / 2)];
        for (int i = 0, j = 0; i < data.length; i++, j = j + 2) {
            data[i] = (byte) Integer.parseInt(hexString.substring(j, (j + 2)), 16);
        }
        return data;
    }

    /**
     * byte数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b, int byteLength) {
        String ret = "";
        for (int i = 0; i < byteLength; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    //数据校验码
    public static byte CheckCode(String hexData) {
        byte reData;
        int sum = 0;
        int dataLength = hexData.length();
        for (int i = 0; i < (dataLength); i = i + 2) {
            sum = sum + Integer.parseInt(hexData.substring(i, 2 + i), 16);
        }
        String temp = "0" + Integer.toHexString(sum);
        reData = (byte) Integer.parseInt(temp.substring(temp.length() - 2, temp.length()).toUpperCase(), 16);
        return reData;
    }



    /**
     * 检验数据
     * @param data 需要检验的数据
     * @return 验证的结果
     */
    public static boolean checkData(String data) {
        if (data.length() >= 4) {
            // 数据头ffaa
            if ("FF".equalsIgnoreCase(data.substring(0, 2))
                    && "AA".equalsIgnoreCase(data.substring(2, 4))) {
                if (data.length() >= 26) {
                    //数据内容长度
                    int dataLength = Integer.parseInt(data.substring(24, 26), 16);
                    if (data.length() >= (dataLength * 2 + 34)) {
                        //数据尾ff55
                        if (data.substring(dataLength * 2 + 30,
                                dataLength * 2 + 32).equalsIgnoreCase("FF")
                                && data.substring(dataLength * 2 + 32,
                                dataLength * 2 + 34).equalsIgnoreCase("55")) {
                            if (data.substring(data.length() - 4, data.length() - 2).equalsIgnoreCase("FF")
                                    && data.substring(data.length() - 2, data.length()).equalsIgnoreCase("55")) {
                            //    int sum = 0;
                            //    for (int i = 0; i < (dataLength); i++) {
                            //        sum = sum + Integer.parseInt(data.substring(28 + i * 2, 30 + i * 2), 16);
                            //    }
                            //    String temp = "0" + Integer.toHexString(sum);
                            //    if (data.substring(data.length() - 6, data.length() - 4).equalsIgnoreCase(temp.substring(temp.length() - 2, temp.length()).toUpperCase())) {
                                    //数据验证通过
                                    Log.e("Util", "数据为真。命令=" + data.substring(20, 24));
                                    return true;
                           //     }
                            }
                        }
                    }

                }

            }
        }
        return false;
    }

}


