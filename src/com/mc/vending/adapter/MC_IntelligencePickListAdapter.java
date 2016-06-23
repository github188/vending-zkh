/**
 * 
 */
package com.mc.vending.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.mc.vending.R;
import com.mc.vending.data.IntelligenceListItemData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author junjie.you
 *
 */
public class MC_IntelligencePickListAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;

	public MC_IntelligencePickListAdapter(Context context, ArrayList<HashMap<String, Object>> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		  return data.size(); 
	}

	/*
	 *  获取某一位置的数据 
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		   return data.get(position); 
	}

	/*
	 * 获取唯一标识 
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		 return position; 
	}

	/*
	 * android绘制每一列的时候，都会调用这个方法 
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		IntelligenceListItemData IntelligenceListItemData = null; 
	        if (convertView == null) { 
	            IntelligenceListItemData = new IntelligenceListItemData(); 
	            // 获取组件布局 
	            convertView = layoutInflater.inflate(R.layout.activity_intelligence_pick_listitem, null); 
	            IntelligenceListItemData.setStockNum( (TextView) convertView.findViewById(R.id.intelligence_pick_list_item_stock_num)); 
	            IntelligenceListItemData.setPdName((TextView) convertView.findViewById(R.id.intelligence_pick_list_item_pd_name)); 
	            IntelligenceListItemData.setQuantity( (TextView) convertView.findViewById(R.id.intelligence_pick_list_item_quantity)); 
	            // 这里要注意，是使用的tag来存储数据的。 
	            convertView.setTag(IntelligenceListItemData); 
	        } else { 
	            IntelligenceListItemData = (IntelligenceListItemData) convertView.getTag(); 
	        } 
	        // 绑定数据、以及事件触发 
	        IntelligenceListItemData.getStockNum().setText((String) data.get(position).get("stockNum")); 
	        IntelligenceListItemData.getPdName().setText((String) data.get(position).get("pdName")); 
	        IntelligenceListItemData.getQuantity().setText((String) data.get(position).get("quantity")); 
	        return convertView; 
	    } 
	}

