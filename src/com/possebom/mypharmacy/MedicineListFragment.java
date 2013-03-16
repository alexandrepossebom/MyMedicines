package com.possebom.mypharmacy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.possebom.mypharmacy.dao.MedicineDao;
import com.possebom.mypharmacy.model.Medicine;

public class MedicineListFragment extends ListFragment {

	private static final String	STATE_ACTIVATED_POSITION	= "activated_position";

	private Callbacks			mCallbacks					= sDummyCallbacks;
	private int					mActivatedPosition			= ListView.INVALID_POSITION;

	public interface Callbacks {
		public void onItemSelected(int id);
	}

	private static Callbacks	sDummyCallbacks	= new Callbacks() {
		@Override
		public void onItemSelected(int id) {
		}
	};


	private MedicineDao md;

	public MedicineListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		md = new MedicineDao(getActivity().getApplicationContext());
	}

	@Override
	public void onResume() {
		super.onResume();
		setListAdapter(new ListAdapter(getActivity(), R.layout.list_medicine_adapter, md.getAll()));
		getListView().setOnItemLongClickListener(listener);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		mCallbacks.onItemSelected(md.getAll().get(position).getId());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}
		mActivatedPosition = position;
	}
	
	private OnItemLongClickListener listener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
			Medicine medicine = md.getAll().get(position);
			md.deleteById(medicine.getId());
			onResume();
			return true;
		}
	};
}
