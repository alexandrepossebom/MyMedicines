package com.possebom.mymedicines;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.possebom.mymedicines.dao.MedicineDao;

public class MedicineDetailActivity extends FragmentActivity {
	private long	id;

	@Override
	protected void onCreate(final Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_medicine_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstance == null) {
			final Bundle arguments = new Bundle();
			id = getIntent().getLongExtra(MedicineDetailFragment.ARG_ITEM_ID,0);
			arguments.putLong(MedicineDetailFragment.ARG_ITEM_ID, id);
			final MedicineDetailFragment fragment = new MedicineDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.medicine_detail_container, fragment).commit();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
	    final MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_detail, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			finish();
		}
		if(item.getItemId() == R.id.menu_delete){
			MedicineDao.getInstance(getApplicationContext()).deleteById(id);
        	finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
