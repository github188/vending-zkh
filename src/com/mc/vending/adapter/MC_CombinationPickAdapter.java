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
import com.mc.vending.data.ProductGroupHeadData;

public class MC_CombinationPickAdapter extends BaseAdapter {

    private LayoutInflater             inflater;
    private List<ProductGroupHeadData> dataList;
    Activity                           context;

    public MC_CombinationPickAdapter(Context context, List<ProductGroupHeadData> dataList,
                                     ListView lv) {
        super();
        //		this.restaurantList = restaurantList==null? new ArrayList<RestaurantData>():restaurantList;
        if (dataList == null) {
            this.dataList = new ArrayList<ProductGroupHeadData>();
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
            convertView = inflater.inflate(R.layout.combination_pick_item, parent, false);
            viewHodler.combination_number = (TextView) convertView
                .findViewById(R.id.combination_number);
            viewHodler.combination_name = (TextView) convertView
                .findViewById(R.id.combination_name);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }

        ProductGroupHeadData data = (ProductGroupHeadData) getItem(position);

        viewHodler.combination_number.setText(data.getPg1Code());
        viewHodler.combination_name.setText(data.getPg1Name());

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
        TextView combination_number;
        TextView combination_name;
    }

}
