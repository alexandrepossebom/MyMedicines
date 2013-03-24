package com.possebom.mymedicines;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.possebom.mymedicines.model.Medicine;
import com.possebom.mymedicines.R;

public class ListAdapter extends ArrayAdapter<Medicine> {
	private Context context;
	private List<Medicine> list;
	private int textViewResourceId;

	public ListAdapter(Context context, int textViewResourceId, List<Medicine> list) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.list = list;
		this.textViewResourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View view = inflater.inflate(textViewResourceId, parent, false);

		Medicine medicine = list.get(position);

		TextView textViewBrandName = (TextView) view.findViewById(R.id.list_adapter_brand_name);
		TextView textViewValidity = (TextView) view.findViewById(R.id.list_adapter_validity);
		
		textViewBrandName.setText(medicine.getBrandName());
		textViewValidity.setText(medicine.getValidity());

		return view;
	}

}
