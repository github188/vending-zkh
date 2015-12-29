package com.mc.vending.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.data.ReplenishmentDetailWrapperData;
import com.mc.vending.tools.ZillionLog;

public class MC_DifferenceReplenishmentAdapter extends BaseAdapter {

    private LayoutInflater                       inflater;
    private Map<String, ViewHodler>              viewMap;
    private List<ReplenishmentDetailWrapperData> dataList;
    Activity                                     context;

    public MC_DifferenceReplenishmentAdapter(Context context,
                                             List<ReplenishmentDetailWrapperData> dataList,
                                             ListView lv) {
        super();
        if (dataList == null) {
            this.dataList = new ArrayList<ReplenishmentDetailWrapperData>();
        } else {
            this.dataList = dataList;
        }
        viewMap = new HashMap<String, ViewHodler>();
        this.context = (Activity) context;
        inflater = this.context.getLayoutInflater();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //System.out.println("======" + position);
        ViewHodler viewHodler;
        if (convertView == null) {
            viewHodler = new ViewHodler();
            convertView = inflater.inflate(R.layout.difference_replenishment_item, parent, false);
            viewHodler.channle_number = (TextView) convertView.findViewById(R.id.channle_number);
            viewHodler.sku_name = (TextView) convertView.findViewById(R.id.sku_name);
            viewHodler.replenishment_number = (TextView) convertView
                .findViewById(R.id.replenishment_number);
            viewHodler.difference_number = (EditText) convertView
                .findViewById(R.id.difference_number);
            viewHodler.btn_sub = (Button) convertView.findViewById(R.id.btn_sub);
            viewHodler.btn_sum = (Button) convertView.findViewById(R.id.btn_sum);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        ReplenishmentDetailWrapperData data = (ReplenishmentDetailWrapperData) getItem(position);

        viewHodler.channle_number.setText(data.getReplenishmentDetail().getRh2Vc1Code());
        viewHodler.sku_name.setText(data.getProductName());
        viewHodler.replenishment_number.setText(String.valueOf(data.getReplenishmentDetail()
            .getRh2ActualQty()));
        viewHodler.difference_number.setText(String.valueOf(data.getReplenishmentDetail().getRh2DifferentiaQty()));
        viewHodler.btn_sub.setTag(position);
        viewHodler.btn_sum.setTag(100 + position);

        convertView.setTag(viewHodler);
        viewMap.put(String.valueOf(position), viewHodler);
        return convertView;
    }

    @Override
    public int getCount() {

        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void reloadViewHolder(int index) {
        ViewHodler viewHodler = viewMap.get(String.valueOf(index));
        ReplenishmentDetailWrapperData data = (ReplenishmentDetailWrapperData) getItem(index);
        viewHodler.difference_number.setText(String.valueOf(data.getReplenishmentDetail()
            .getRh2DifferentiaQty()));
    }

    class ViewHodler {
        TextView channle_number;
        TextView sku_name;
        TextView replenishment_number;
        EditText difference_number;
        Button   btn_sub;
        Button   btn_sum;
    }

}
