package com.kevin.cars.utils;

/**
 * Created by Administrator
 * on 2016/7/8.
 */
public class Constant {

    public static final String CMD_SEARCH_RESPOND = "0110";//获取设备
    public static final String CMD_SEARCH_FINISH_RESPOND = "0210";//获取设备完成
    public static final String CMD_SEND_RESPOND = "1110";//发送回馈
    public static final String CMD_SEND_RESPOND2 = "1120";//发送回馈

    public static final int REMOTE_PORT = 5678;//远程端口

    public static final int SEARCH_WAIT_DIALOG_TIME = 8000;//搜索设备最大等待时间

    /**
     * FFAA  0000000000000000  0100  0100      00    00   FF55
     * 头    MAC               CMD   内容长度   内容  校验  尾
     */
    public static final int DATA_CMD_START = 20; //CMD开始位
    public static final int DATA_CMD_END = 24; //CMD结束位

    public static final int DATA_MAC_START = 4; //MAC开始位
    public static final int DATA_MAC_END = 20; //MAC结束位

    public static final int DATA_CONTENT_START = 28;//内容开始位

    /**
     * 内容结束位
     */
    public static int DATA_CONTENT_END(int dataLength) {
        return DATA_CONTENT_START + dataLength;
    }


    public static final String SEND_DATA_SEARCH = "FFAA 0000000000000000 0100 0000 00 FF55";//广播数据


    public static String SEND_DATA_1(String mac,String shortAddr) {
        return "FFAA " + mac + " 1100 0800 "+shortAddr+"11 00 FF FC 01 31 00 FF55";
    }
    public static String SEND_DATA_2(String mac,String shortAddr) {
        return "FFAA " + mac + " 1100 0800 "+shortAddr+"11 00 FF FC 01 32 00 FF55";
    }
    public static String SEND_DATA_3(String mac,String shortAddr) {
        return "FFAA " + mac + " 1100 0800 "+shortAddr+"11 00 FF FC 01 33 00 FF55";
    }
    public static String SEND_DATA_4(String mac,String shortAddr) {
        return "FFAA " + mac + " 1100 0800 "+shortAddr+"11 00 FF FC 01 34 00 FF55";
    }
    public static String SEND_DATA_5(String mac,String shortAddr) {
        return "FFAA " + mac + " 1100 0800 "+shortAddr+"11 00 FF FC 01 35 00 FF55";
    }
    public static String SEND_DATA_6(String mac,String shortAddr) {
        return "FFAA " + mac + " 1100 0800 "+shortAddr+"11 00 FF FC 01 36 00 FF55";
    }


}
