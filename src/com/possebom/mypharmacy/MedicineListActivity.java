package com.possebom.mypharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MedicineListActivity extends FragmentActivity implements MedicineListFragment.Callbacks {

	private boolean	mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine_list);

		if (findViewById(R.id.medicine_detail_container) != null) {
			mTwoPane = true;
			((MedicineListFragment) getSupportFragmentManager().findFragmentById(R.id.medicine_list)).setActivateOnItemClick(true);
		}
	}

	@Override
	public void onItemSelected(int id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putInt(MedicineDetailFragment.ARG_ITEM_ID, id);
			MedicineDetailFragment fragment = new MedicineDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.medicine_detail_container, fragment).commit();
		} else {
			Intent detailIntent = new Intent(this, MedicineDetailActivity.class);
			detailIntent.putExtra(MedicineDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
	        case R.id.menu_add :
	            Intent intent = new Intent(this, MedicineAddActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
