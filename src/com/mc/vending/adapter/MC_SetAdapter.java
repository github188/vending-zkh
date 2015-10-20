package com.mc.vending.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;

public class MC_SetAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String>   dataList;
    Activity               context;

    public MC_SetAdapter(Context context, List<String> dataList, ListView lv) {
        super();
        if (dataList == null) {
            this.dataList = new ArrayList<String>();
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
            convertView = inflater.inflate(R.layout.set_item, parent, false);
            viewHodler.set_icon = (ImageView) convertView.findViewById(R.id.set_icon);
            viewHodler.set_name = (TextView) convertView.findViewById(R.id.set_name);
        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.set_name.setText((String) getItem(position));
        if (position == 0) {
            //一键补货
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.icon_replenishment));
        } else if (position == 1) {
            //差异补货
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.icon_replenishment));
        } else if (position == 2) {
            //紧急补货
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.icon_urgent_replenishment));
        } else if (position == 3) {
            //盘点
            viewHodler.set_icon.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.icon_inventory));
        } else if (position == 4) {
            //退货（正向）
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.icon_returns_forward));
        } else if (position == 5) {
            //退货（反向）
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.icon_returns_forward));
        } else if (position == 6) {
            //领料测试
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_test));
        } else if (position == 7) {
            //库存 同步
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.icon_synchronous));
        } else if (position == 8) {
            //数据同步
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.icon_synchronous));
        } else if (position == 9) {
            //初始化
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_init));
        } else if (position == 10) {
            //退出应用
            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_back));
//        } else if (position == 11) {
//            //退出应用
//            viewHodler.set_icon.setImageDrawable(context.getResources().getDrawable(
//                    R.drawable.icon_returns_forward));
        }

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
        // TODO Auto-generated method stub
        return position;
    }

    class ViewHodler {
        ImageView set_icon;
        TextView  set_name;
    }
}
