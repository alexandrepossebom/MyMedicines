package com.possebom.mypharmacy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * An activity representing a list of Medicines. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MedicineDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MedicineListFragment} and the item details (if present) is a
 * {@link MedicineDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MedicineListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class MedicineListActivity extends FragmentActivity implements MedicineListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean	mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("MEDICINE","MedicineListActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine_list);

		if (findViewById(R.id.medicine_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MedicineListFragment) getSupportFragmentManager().findFragmentById(R.id.medicine_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link MedicineListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(int id) {
		Log.d("MEDICINE","onItemSelected id is : "+id);
		
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putInt(MedicineDetailFragment.ARG_ITEM_ID, id);
			MedicineDetailFragment fragment = new MedicineDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.medicine_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
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
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, MedicineAddActivity.class);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
