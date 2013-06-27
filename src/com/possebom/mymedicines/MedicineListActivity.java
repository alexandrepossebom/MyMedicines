package com.possebom.mymedicines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.possebom.mymedicines.dao.MedicineDao;

import de.keyboardsurfer.android.widget.crouton.Crouton;


public class MedicineListActivity extends FragmentActivity implements MedicineListFragment.Callbacks {

	public static final String TAG = "MEDICINE";
	private boolean	mTwoPane;
	private Menu	menu;
	private long	id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine_list);

		if (findViewById(R.id.medicine_detail_container) != null) {
			mTwoPane = true;
//			((MedicineListFragment) getSupportFragmentManager().findFragmentById(R.id.medicine_list)).setActivateOnItemClick(true);
		}
	}

	@Override
	public void onItemSelected(long id) {
		this.id = id;
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putLong(MedicineDetailFragment.ARG_ITEM_ID, id);
			MedicineDetailFragment fragment = new MedicineDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.medicine_detail_container, fragment).commit();
			if (menu != null) {
				menu.findItem(R.id.menu_delete).setVisible(true);
			}
		} else {
			Intent detailIntent = new Intent(this, MedicineDetailActivity.class);
			detailIntent.putExtra(MedicineDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		boolean result = false;
		if(resultCode == RESULT_OK)
			result = true;

		((MedicineListFragment) getSupportFragmentManager().findFragmentById(R.id.medicine_list)).onNewItem(result);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Crouton.cancelAllCroutons();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add :
			Intent intent = new Intent(this, MedicineAddActivity.class);
			startActivityForResult(intent, 0);
			return true;
		case R.id.menu_delete:
			MedicineDao.getInstance(getApplicationContext()).deleteById(id);
			getSupportFragmentManager().findFragmentById(R.id.medicine_list).onResume();
			onItemSelected(0);
			if (menu != null) {
				menu.findItem(R.id.menu_delete).setVisible(false);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
