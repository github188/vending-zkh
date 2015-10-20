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
import com.mc.vending.data.RetreatHeadData;

public class MC_ReurnForwardListAdapter extends BaseAdapter {

    private LayoutInflater        inflater;
    private List<RetreatHeadData> dataList;
    Activity                      context;

    public MC_ReurnForwardListAdapter(Context context, List<RetreatHeadData> dataList, ListView lv) {
        super();
        if (dataList == null) {
            this.dataList = new ArrayList<RetreatHeadData>();
        } else {
            this.dataList = dataList;
        }
        this.context = (Activity) context;
        inflater = this.context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.difference_replenishment_order_item, parent, false);
            viewHodler.replenishment_order_number = (TextView) convertView
                    .findViewById(R.id.replenishment_order_number);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        RetreatHeadData data = (RetreatHeadData) getItem(position);

        String status = data.getRt1Status().equals("0") ? "创建" : "完成";
        viewHodler.replenishment_order_number.setText("退货单号：" + data.getRt1Rtcode() + "(" + status + ")");
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
