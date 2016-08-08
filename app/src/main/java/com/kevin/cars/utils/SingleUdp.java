package com.kevin.cars.utils;

import android.os.Handler;
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
 * on 2016/7/5.
 */
public class SingleUdp {

    private static final String TAG = "SingleUdp";
    public static final int ONE_KB = 1024;
    public static final int HALF_KB = 512;
    public static final int RECEIVED_DATA = 0x123;
    private String ipAddress;
    private int udpLocalPort = -1;
    private int udpRemotePort = -1;
    private DatagramSocket udpSocket;
    private DatagramPacket udpReceivePacket;
    private DatagramPacket udpSendPacket;
    private InetAddress inetAddress;
    private static byte[] udpReceiveBytes;
    private OnReceiveListen onReceiveListen;//接收监听
    private Thread udpReceiveThread;
    private static SingleUdp UdpInstance;//单例
    private Myhandler myhandler;

    //私有构造器
    private SingleUdp() {
        init();
    }

    //单例
    public static SingleUdp getUdpInstance() {
        if (UdpInstance == null) {
            UdpInstance = new SingleUdp();
        }
        return UdpInstance;
    }

    //设置监听
    public void setOnReceiveListen(OnReceiveListen receiveListen) {
        this.onReceiveListen = receiveListen;
    }

    //设置Udp的IP
    public void setUdpIp(String ip) {
        this.ipAddress = ip;
    }

    //设置Udp的本地端口
    public void setUdpLocalPort(int port) {
        this.udpLocalPort = port;
    }

    //设置Udp的远程端口
    public void setUdpRemotePort(int port) {
        this.udpRemotePort = port;
    }

    //初始化
    private void init() {
        udpReceiveBytes = new byte[HALF_KB];
        udpReceivePacket = new DatagramPacket(udpReceiveBytes, HALF_KB);
        Arrays.fill(udpReceiveBytes, (byte) 0x00);//初始化赋值 (byte)0x00
        myhandler = new Myhandler();
    }

    //启动udp
    public void start() {

        try {
            inetAddress = InetAddress.getByName(ipAddress);
//            udpSocket = udpSocket == null ? new DatagramSocket() : udpSocket;
            udpSocket = new DatagramSocket();//每次都new
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    //关闭udp
    public void stop() {
        if (udpReceiveThread != null)
            udpReceiveThread.interrupt();
        if (udpSocket != null) {
            udpSocket.close();
        }
        ipAddress = null;
        UdpInstance = null;
    }

    //发送
    public void send(byte[] data) {

        if (udpSendPacket == null) {
            udpSendPacket = new DatagramPacket(data, data.length, inetAddress, udpRemotePort);
        } else {
            udpSendPacket.setData(data);
            udpSendPacket.setLength(data.length);
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    if (udpSocket != null) {
                        udpSocket.send(udpSendPacket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //接收线程
    public void receiveUdp() {

        udpReceiveThread = new Thread() {
            public void run() {
                while (!udpReceiveThread.isInterrupted()) {
                    try {
                        //会阻塞
                        udpSocket.receive(udpReceivePacket);
                        int len = udpReceivePacket.getLength();
                        if (len > 0) {
                            if (onReceiveListen != null) {
                                onReceiveListen.onReceiveData(udpReceiveBytes, len, null);
                                myhandler.sendEmptyMessage(RECEIVED_DATA);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        udpReceiveThread.start();

    }

    /**
     * 根据有没有输入ip来判断udp与没有开启，一般认为有ip就可以启动udp
     */
    public String getIpAddress(){
        return ipAddress;
    }


    /**
     * Handler
     */
    static class Myhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RECEIVED_DATA) {
                //初始化
                Arrays.fill(udpReceiveBytes, (byte) 0x00);
            }
        }
    }

}
