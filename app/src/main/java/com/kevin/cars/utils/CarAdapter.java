package com.kevin.cars.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kevin.cars.R;

import java.util.List;

/**
 * Created by kevin
 * on 2016/8/5.
 */
public class CarAdapter extends BaseAdapter {


    private Context mContext;//context
    private List<CarItem> mList;//list
    private int selected = -1;//选择的item
    private boolean isSelect;//是否选择

    public CarAdapter(Context context,List<CarItem> list){
        mContext = context;
        mList = list;
    }

    public void setSelected(int positioned,boolean isSelect){
        this.selected = positioned;
        this.isSelect = isSelect;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CarItem carItem = mList.get(position);
        ViewHolder viewHolder;
        View view;
        if(convertView==null){
            viewHolder = new ViewHolder();
            view  = LayoutInflater.from(mContext).inflate(R.layout.car_item_layout,null);
            viewHolder.tvCarMac = (TextView)view.findViewById(R.id.tvCarMac);
            viewHolder.tvCarShortAddr = (TextView)view.findViewById(R.id.tvCarShortAddr);
            viewHolder.rlAgvItem = (RelativeLayout)view.findViewById(R.id.rlAgvItem);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.tvCarMac.setText(carItem.getMac());
        viewHolder.tvCarShortAddr.setText(carItem.getShortAddr());

        if(selected == position){
            if(isSelect){
                viewHolder.rlAgvItem.setBackgroundResource(R.drawable.car_list_item_press_bg);
            }else{
                viewHolder.rlAgvItem.setBackgroundResource(R.drawable.car_list_item_bg);
            }

        }else{
            viewHolder.rlAgvItem.setBackgroundResource(R.drawable.car_list_item_bg);
        }

        return view;
    }

    static class ViewHolder{
        TextView tvCarMac;
        TextView tvCarShortAddr;
        RelativeLayout rlAgvItem;
    }

}
