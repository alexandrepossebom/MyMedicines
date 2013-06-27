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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			id = getIntent().getLongExtra(MedicineDetailFragment.ARG_ITEM_ID,0);
			arguments.putLong(MedicineDetailFragment.ARG_ITEM_ID, id);
			MedicineDetailFragment fragment = new MedicineDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.medicine_detail_container, fragment).commit();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_detail, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_delete:
			MedicineDao.getInstance(getApplicationContext()).deleteById(id);
        	finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
