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
import com.mc.vending.data.ProductGroupWrapperData;

public class MC_CombinationPickDetailAdapter extends BaseAdapter {

    private LayoutInflater                inflater;
    private List<ProductGroupWrapperData> dataList;
    Activity                              context;

    public MC_CombinationPickDetailAdapter(Context context, List<ProductGroupWrapperData> dataList,
                                           ListView lv) {
        super();

        if (dataList == null) {
            this.dataList = new ArrayList<ProductGroupWrapperData>();
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
            convertView = inflater.inflate(R.layout.combination_pick_detail_item, parent, false);
            viewHodler.sku_name = (TextView) convertView.findViewById(R.id.sku_name);
            viewHodler.sku_number = (TextView) convertView.findViewById(R.id.sku_number);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        ProductGroupWrapperData data = (ProductGroupWrapperData) getItem(position);
        viewHodler.sku_name.setText(data.getProductName());
        viewHodler.sku_number.setText(String.valueOf(data.getGroupQty()));

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
        TextView sku_number;
        TextView sku_name;
    }

}
