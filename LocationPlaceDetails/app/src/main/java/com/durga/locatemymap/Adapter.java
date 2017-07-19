package com.durga.locatemymap;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.durga.locatemymap.utility.DataDTO;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	ArrayList<DataDTO> category_array;


	int count = 0;

	public Adapter(Context context, ArrayList<DataDTO> category_array) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.category_array=category_array;

	}

	@Override
	public int getCount() {
		return category_array.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.mylocation_item, null);
			holder = new Holder();
			holder.tvSSID = (TextView) view.findViewById(R.id.textView);


			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}
        holder.tvSSID.setText(category_array.get(position).getPlace_name());





		return view;
	}

	class Holder {
		TextView tvSSID;

	}
}
