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
	
	private final Context context;
	private final RemoveListener removeListener;
	private final List<Medicine> list;
	private ViewHolder	holder;

	public ListAdapter(final Context context, final int resourceId, final List<Medicine> list, final RemoveListener removeListener) {
		super(context, resourceId, list);
		this.context = context;
		this.list = list;
		this.removeListener = removeListener;
	}
	
	@Override
	public long getItemId(final int position) {
		return list.get(position).getId();
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = null;
		final Medicine item = getItem(position);
		if (convertView == null) {
			final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.medicine_row, parent, false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) view.findViewById(R.id.medicine_row_imageview_medicine);
			holder.textViewBrand = (TextView) view.findViewById(R.id.medicine_row_textview_brand);
			holder.textViewValidity = (TextView) view.findViewById(R.id.medicine_row_textview_validity);
			holder.imgBtnDetails = (ImageButton) view.findViewById(R.id.medicine_row_image_button_details);
			holder.imgBtnShare = (ImageButton) view.findViewById(R.id.medicine_row_image_button_share);
			holder.imgBtnDelete = (ImageButton) view.findViewById(R.id.medicine_row_image_button_delete);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageResource(R.drawable.ic_medicine);
		holder.textViewBrand.setText(item.getBrandName());
		holder.textViewValidity.setText(item.getValidity());

		holder.imgBtnDetails.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				final String query = String.format("%s, %s", item.getBrandName(), item.getDrug());
				final Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, query);
				context.startActivity(intent);
			}
		});

		holder.imgBtnShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				final String name = context.getString(R.string.hint_medicine_name);
				final String drug = context.getString(R.string.drug);
				final String form = context.getString(R.string.form);
				final String text = String.format("%s : %s%n%s : %s%n%s : %s", name,item.getBrandName(), drug, item.getDrug(),form, item.getForm());
				
				final Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, text);
				context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.title_medicine_detail)));
			}
		});

		holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				removeListener.removeMedicine(item);
			}
		});

		return view;
	}

	static class ViewHolder {
		private ImageView imageView;
		private TextView textViewBrand;
		private TextView textViewValidity;
		private ImageButton imgBtnDetails;
		private ImageButton imgBtnShare;
		private ImageButton imgBtnDelete;
	}

}