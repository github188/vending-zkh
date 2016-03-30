package com.mc.vending.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.data.ReplenishmentHeadData;

public class MC_DifferenceReplenishAllOrderAdapter extends BaseAdapter {

    private LayoutInflater              inflater;
    private List<ReplenishmentHeadData> dataList;
    Activity                            context;

    public MC_DifferenceReplenishAllOrderAdapter(Context context,
                                                  List<ReplenishmentHeadData> dataList, ListView lv) {
        super();
        if (dataList == null) {
            this.dataList = new ArrayList<ReplenishmentHeadData>();
        } else {
            this.dataList = dataList;
        }
        this.context = (Activity) context;
        inflater = this.context.getLayoutInflater();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.difference_replenishall_order_item, parent,
                false);
            viewHodler.replenishall_order_number = (TextView) convertView
                .findViewById(R.id.replenishall_order_number);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        ReplenishmentHeadData data = (ReplenishmentHeadData) getItem(position);

        viewHodler.replenishall_order_number.setText("补满单号：" + data.getRh1Rhcode());
        convertView.setTag(viewHodler);
        return convertView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHodler {
        TextView replenishall_order_number;
    }

}
