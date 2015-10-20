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

public class MC_DifferenceReplenishmentOrderAdapter extends BaseAdapter {

    private LayoutInflater              inflater;
    private List<ReplenishmentHeadData> dataList;
    Activity                            context;

    public MC_DifferenceReplenishmentOrderAdapter(Context context,
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
            convertView = inflater.inflate(R.layout.difference_replenishment_order_item, parent,
                false);
            viewHodler.replenishment_order_number = (TextView) convertView
                .findViewById(R.id.replenishment_order_number);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        ReplenishmentHeadData data = (ReplenishmentHeadData) getItem(position);

        viewHodler.replenishment_order_number.setText("补货单号：" + data.getRh1Rhcode());
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
        TextView replenishment_order_number;
    }

}
