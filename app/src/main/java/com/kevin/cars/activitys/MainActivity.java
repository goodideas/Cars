package com.kevin.cars.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kevin.cars.R;
import com.kevin.cars.utils.BroadcastListen;
import com.kevin.cars.utils.BroadcastUdp;
import com.kevin.cars.utils.CarAdapter;
import com.kevin.cars.utils.CarItem;
import com.kevin.cars.utils.Constant;
import com.kevin.cars.utils.OnReceiveListen;
import com.kevin.cars.utils.SingleUdp;
import com.kevin.cars.utils.SpHelper;
import com.kevin.cars.utils.Util;
import com.kevin.cars.utils.WaitDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 小车
 * 其中smartLink的广播数据中的命令类型为8F01
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,OnReceiveListen{

    private static final String TAG = "MainActivity";
    private ListView listCar;//小车列表
    private Button btnSmartLink;//smartLink
    private Button btnSearch;//搜索按钮
    private Button btn1;//按钮1
    private Button btn2;//按钮2
    private Button btn3;//按钮3
    private Button btn4;//按钮4
    private Button btn5;//按钮5
    private Button btn6;//按钮6
    private BroadcastUdp broadcastUdp;//广播
    private List<CarItem> cList = new ArrayList<>();//list
    private SingleUdp singleUdp;//udp
    private SpHelper spHelper;//sp
    private CarAdapter carAdapter;//适配器
    private int lastSelect = -1;//记录最后的选择
    private int selected = -1;//选择item
    private boolean isSelect = false;//是否选择
    private TextView tvShowInfo;//显示信息
    private long exitTime;//退出时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        finds();
        btnClicks();
        //广播实例
        broadcastUdp = new BroadcastUdp();
        //广播初始化
        broadcastUdp.init();
        //udp单例
        singleUdp = SingleUdp.getUdpInstance();
        //SharedPreferences
        spHelper = new SpHelper(this);
        //设置滚动
        tvShowInfo.setMovementMethod(new ScrollingMovementMethod());
        listCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (lastSelect == position) {//再次点击item
                    isSelect = !isSelect;
                    carAdapter.setSelected(position, isSelect);
                    carAdapter.notifyDataSetChanged();
                } else {//点击其他item
                    isSelect = true;
                    carAdapter.setSelected(position, true);
                    carAdapter.notifyDataSetChanged();
                }

                selected = isSelect ? position : -1;
                lastSelect = position;
            }
        });
    }

    /**
     * 按钮监听
     */
    private void btnClicks() {
        btnSmartLink.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void finds() {
        listCar = (ListView)findViewById(R.id.listCar);
        btnSmartLink  = (Button)findViewById(R.id.btnSmartLink);
        btnSearch  = (Button)findViewById(R.id.btnSearch);
        tvShowInfo = (TextView)findViewById(R.id.tvShowInfo);
        btn1  = (Button)findViewById(R.id.btn1);
        btn2  = (Button)findViewById(R.id.btn2);
        btn3  = (Button)findViewById(R.id.btn3);
        btn4  = (Button)findViewById(R.id.btn4);
        btn5  = (Button)findViewById(R.id.btn5);
        btn6  = (Button)findViewById(R.id.btn6);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSmartLink:
                startActivity(new Intent(MainActivity.this,SmartLinkActivity.class));
                break;
            case R.id.btnSearch:
                //广播搜索
                btnSearchDo();
                break;
            case R.id.btn1:

                btnsDo(1);
                break;
            case R.id.btn2:

                btnsDo(2);
                break;
            case R.id.btn3:

                btnsDo(3);
                break;
            case R.id.btn4:

                btnsDo(4);
                break;
            case R.id.btn5:

                btnsDo(5);
                break;
            case R.id.btn6:

                btnsDo(6);
                break;

        }
    }

    //按钮事件
    private void btnsDo(int index) {
        if (selected == -1) {
            Toast.makeText(MainActivity.this, "没有选择",Toast.LENGTH_SHORT).show();
        }else if(spHelper!=null&&singleUdp!=null){
            if(!TextUtils.isEmpty(spHelper.getSpCarIp())){
                if(cList!=null){
                    if(selected!=-1){
                        CarItem carItem = cList.get(selected);
                        //设置IP
                        singleUdp.setUdpIp(spHelper.getSpCarIp());
                        //设置端口
                        singleUdp.setUdpRemotePort(Constant.REMOTE_PORT);
                        singleUdp.start();
                        singleUdp.send(Util.HexString2Bytes(getBtnSendData(index,carItem.getMac(),carItem.getShortAddr())));
                        singleUdp.receiveUdp();
                        //设置接收
                        singleUdp.setOnReceiveListen(this);
                    }
                }
            }
        }
    }

    private String getBtnSendData(int index,String mac,String shortAddr){
        if(index == 1){
            return Constant.SEND_DATA_1(mac, shortAddr).replace(" ", "");
        }else if(index == 2){
            return Constant.SEND_DATA_2(mac, shortAddr).replace(" ", "");
        }
        else if(index == 3){
            return Constant.SEND_DATA_3(mac, shortAddr).replace(" ", "");
        }
        else if(index == 4){
            return Constant.SEND_DATA_4(mac, shortAddr).replace(" ", "");
        }
        else if(index == 5){
            return Constant.SEND_DATA_5(mac, shortAddr).replace(" ", "");
        }
        else if(index == 6){
            return Constant.SEND_DATA_6(mac, shortAddr).replace(" ", "");
        }else{
            return "";
        }
    }

    private void btnSearchDo(){
        selected = -1;
        isSelect = false;
        if(broadcastUdp!=null){
            if(cList!=null&&carAdapter!=null&&listCar!=null){
                //清空数据
                cList.clear();
                carAdapter = new CarAdapter(MainActivity.this,cList);
                listCar.setAdapter(carAdapter);
            }
            broadcastUdp.init();
            broadcastUdp.send(Util.HexString2Bytes(Constant.SEND_DATA_SEARCH.replace(" ","")));
            //广播接收
            broadcastUdp.setReceiveListen(new BroadcastListen() {
                @Override
                public void onReceiveData(byte[] data, int len, String remoteIp) {
                    //转换为字符串
                    String da = Util.bytes2HexString(data, len);
                    analysisData(da, remoteIp);
                    if (Util.checkData(da) &&
                            Constant.CMD_SEARCH_FINISH_RESPOND.equalsIgnoreCase(
                                    da.substring(Constant.DATA_CMD_START, Constant.DATA_CMD_END))) {
                        WaitDialog.immediatelyDismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "搜索完成", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }
            });
            WaitDialog.immediatelyDismiss();
            WaitDialog.showDialog(MainActivity.this, "正在搜索。。。", Constant.SEARCH_WAIT_DIALOG_TIME, null);

        }
    }

    /**
     * 解析广播数据
     * @param da 接收的数据
     * @param remoteIp 远程IP即目标IP
     */
    private void analysisData(String da, String remoteIp) {
        if (Util.checkData(da)){
            String cmd = da.substring(Constant.DATA_CMD_START,Constant.DATA_CMD_END);
            if( Constant.CMD_SEARCH_RESPOND.equalsIgnoreCase(cmd)) {
                if(spHelper!=null){
                    spHelper.saveSpCarIp(remoteIp);
                }else{
                    spHelper = new SpHelper(MainActivity.this);
                    spHelper.saveSpCarIp(remoteIp);
                }
                String mac = da.substring(Constant.DATA_MAC_START,Constant.DATA_MAC_END);
                String shortAddr = da.substring(Constant.DATA_CONTENT_START,Constant.DATA_CONTENT_START+4);
                cList.add(new CarItem(mac, shortAddr));
                carAdapter = new CarAdapter(MainActivity.this,cList);
                runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      listCar.setAdapter(carAdapter);
                  }
              });
            }

        }

    }

    @Override
    public void onReceiveData(byte[] data, int len, @Nullable String remoteIp) {
        String da = Util.bytes2HexString(data, len);
        if (Util.checkData(da)){
            String cmd = da.substring(Constant.DATA_CMD_START,Constant.DATA_CMD_END);
            if( Constant.CMD_SEND_RESPOND.equalsIgnoreCase(cmd)) {
                String status = da.substring(Constant.DATA_CONTENT_START+4,Constant.DATA_CONTENT_START+6);
                if(status.equalsIgnoreCase("01")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else if(status.equalsIgnoreCase("00")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }else if(Constant.CMD_SEND_RESPOND2.equalsIgnoreCase(cmd)){
                int count = Integer.parseInt(da.substring(Constant.DATA_CONTENT_START+12,Constant.DATA_CONTENT_START+14),16);
                final String content = da.substring(Constant.DATA_CONTENT_START+14,Constant.DATA_CONTENT_START+14+count);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示通信信息
                        tvShowInfo.append(content + "\n");
                    }
                });

            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(singleUdp!=null){
            //关闭udp
            singleUdp.stop();
        }
        if(broadcastUdp!=null){
            //关闭广播
            broadcastUdp.stop();
        }
    }
}
