package com.kevin.cars.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by kevin
 * on 2016/8/05.
 */
public class BroadcastUdp {

    private static final String TAG = "BroadcastUdp";
    private static final String BROADCAST_IP = "255.255.255.255";
    private static final int BROADCAST_PORT = 5678;
    private static final int HANDLER_MESSAGE = 0x123;
    private DatagramSocket udpSocket;
    private DatagramPacket udpSendPacket;
    private DatagramPacket udpReceivePacket;
    private byte[] receiveBytes = new byte[512];
    private InetAddress inetAddress;
    private MyHandler myHandler;
    private BroadcastListen broadcastListen;
    private Thread receiveThread;

    public BroadcastUdp() {

    }


    /**
     * 初始化
     */
    public void init() {
        try {
            udpSocket = new DatagramSocket();
            myHandler = new MyHandler(Looper.getMainLooper());
            receive();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送方法
     *
     * @param data 发送的数据
     */
    public void send(byte[] data) {
        try {
            if (inetAddress == null) {
                inetAddress = InetAddress.getByName(BROADCAST_IP);
            }

            if (udpSendPacket == null) {
                udpSendPacket = new DatagramPacket(data, data.length, inetAddress, BROADCAST_PORT);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        udpSocket.send(udpSendPacket);
                        Log.e(TAG,"发送成功");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e(TAG, "发送失败");

        }
    }

    /**
     * 接收方法
     */
    public void receive() {

        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!receiveThread.isInterrupted()) {
                    udpReceivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);
                    try {
                        udpSocket.receive(udpReceivePacket);
                        int len = udpReceivePacket.getLength();
                        if (len > 0) {
                            if (broadcastListen != null) {

                                Log.e(TAG, "ip=" + udpReceivePacket.getAddress().toString());
                                broadcastListen.onReceiveData(receiveBytes, len, udpReceivePacket.getAddress().toString().substring(1));
                                myHandler.sendEmptyMessage(HANDLER_MESSAGE);
                                String data = Util.bytes2HexString(receiveBytes, len);
                                Log.e(TAG,"ceshi datattttttttttttttttttttttt="+data);
                                if (Util.checkData(data) &&
                                        Constant.CMD_SEARCH_FINISH_RESPOND.equalsIgnoreCase(
                                                data.substring(Constant.DATA_CMD_START, Constant.DATA_CMD_END))) {
                                    Log.e(TAG,"ceshi break");
                                    break;
                                }

                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        receiveThread.start();

    }

    /**
     * 停止广播
     */
    public void stop() {

        if (receiveThread != null) {
            receiveThread.interrupt();
        }
        if (udpSocket != null) {
            udpSocket.close();
            Log.e(TAG, "broadcastClose");
        }
    }


    /**
     * 设置监听
     *
     * @param broadcastListen 监听接口
     */
    public void setReceiveListen(BroadcastListen broadcastListen) {
        this.broadcastListen = broadcastListen;
    }


    /**
     * Handler处理
     */
    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_MESSAGE) {
//                Arrays.fill(receiveBytes, (byte) 0x00);
            }
        }
    }


}
// FF AA
// 90 BF 69 09 00 4B 12 00
// 01 10 2A 00
// AB 8A
// 00 00 11 00 FF FC 00 00 00 00 00 00 00 00 00 00
// FF FF FF FF FF FF FF FF FF FF
// FF FF FF FF FF FF FF FF FF FF
// FF FF FF FF
// 00 FF 55

// FF AA 90 BF 69 09 00 4B 12 00 01 10 2A 00 AB 8A 00 00 11 00 FF FC 00 00 00 00 00 00 00 00 00 00 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 00 FF 55