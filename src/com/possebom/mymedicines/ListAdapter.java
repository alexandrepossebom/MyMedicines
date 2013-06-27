package com.possebom.mymedicines;


import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.possebom.mymedicines.model.Medicine;

public class ListAdapter extends ArrayAdapter<Medicine> {
	
	private Context context;
	private RemoveListener removeListener;
	private List<Medicine> list;

	public ListAdapter(Context context, int textViewResourceId, List<Medicine> list, RemoveListener removeListener) {
		super(context, textViewResourceId, list);
		this.context = context;
		this.list = list;
		this.removeListener = removeListener;
	}
	
	@Override
	public long getItemId(int position) {
		return list.get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Medicine item = getItem(position);
		final ViewHolder holder;
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.medicine_row, parent, false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.medicine_row_imageview_medicine);
			holder.textViewBrand = (TextView) convertView.findViewById(R.id.medicine_row_textview_brand);
			holder.textViewValidity = (TextView) convertView.findViewById(R.id.medicine_row_textview_validity);
			holder.imageButtonDetails = (ImageButton) convertView.findViewById(R.id.medicine_row_image_button_details);
			holder.imageButtonShare = (ImageButton) convertView.findViewById(R.id.medicine_row_image_button_share);
			holder.imageButtonDelete = (ImageButton) convertView.findViewById(R.id.medicine_row_image_button_delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageResource(R.drawable.ic_medicine);
		holder.textViewBrand.setText(item.getBrandName());
		holder.textViewValidity.setText(item.getValidity());

		holder.imageButtonDetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String query = String.format("%s, %s", item.getBrandName(), item.getDrug());
				Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, query);
				context.startActivity(intent);
			}
		});

		holder.imageButtonShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = context.getString(R.string.hint_medicine_name);
				String drug = context.getString(R.string.drug);
				String form = context.getString(R.string.form);
				String text = String.format("%s : %s%n%s : %s%n%s : %s", name,item.getBrandName(), drug, item.getDrug(),form, item.getForm());
				
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, text);
				context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.title_medicine_detail)));
			}
		});

		holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeListener.removeMedicine(item);
			}
		});

		return convertView;
	}

	static class ViewHolder {
		private ImageView imageView;
		private TextView textViewBrand;
		private TextView textViewValidity;
		private ImageButton imageButtonDetails;
		private ImageButton imageButtonShare;
		private ImageButton imageButtonDelete;
	}

}