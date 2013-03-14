package com.possebom.mypharmacy;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.possebom.mypharmacy.model.Medicine;

public class ListAdapter extends ArrayAdapter<Medicine> {
	Context context;
	List<Medicine> list;
	int textViewResourceId;

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

		TextView textViewBrandName = (TextView) view.findViewById(R.id.name);
		TextView textViewValidity = (TextView) view.findViewById(R.id.validity);
		
		textViewBrandName.setText(medicine.getBrandName());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy", Locale.ENGLISH);
		String validity = dateFormat.format(medicine.getValidity().getTime());
		
		textViewValidity.setText(validity);

		return view;
	}

}
