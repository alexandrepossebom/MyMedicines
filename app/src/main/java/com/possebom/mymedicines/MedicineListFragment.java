package com.possebom.mymedicines;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.possebom.mymedicines.dao.MedicineDao;
import com.possebom.mymedicines.model.Medicine;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MedicineListFragment extends ListFragment implements RemoveListener{

	private Callbacks			mCallbacks					= sDummyCallbacks;

	public interface Callbacks {
		public void onItemSelected(long id);
	}

	private static Callbacks	sDummyCallbacks	= new Callbacks() {

		@Override
		public void onItemSelected(long id) {
			
		}
		
	};

	private ListAdapter	adapter;
	private List<Medicine> list;
	private SwipeListView listview;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstance) {
		listview = (SwipeListView) inflater.inflate(R.layout.list_root, container, false);

		list = MedicineDao.getInstance(getActivity()).getAll();
		adapter = new ListAdapter(getActivity(), R.layout.list_medicine_adapter, list,this);
		setListAdapter(adapter);

		listview.setSwipeListViewListener( new BaseSwipeListViewListener(){
			@Override
			public void onClickFrontView(final int position) {
				mCallbacks.onItemSelected(adapter.getItemId(position));
			}
		});
		listview.setAdapter(adapter);
		return listview;
	}

	@Override
	public void onAttach(final Activity activity) {
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

	public void onNewItem(final boolean result) {
		if(result){
			adapter.clear();
			list = MedicineDao.getInstance(getActivity()).getAll();
			adapter.addAll(list);
			adapter.notifyDataSetChanged();
			Crouton.makeText(getActivity(),R.string.item_added, Style.INFO).show();
		}else{
			Crouton.makeText(getActivity(),R.string.item_not_added, Style.CONFIRM).show();
		}
	}

	@Override
	public void removeMedicine(final Medicine medicine) {
		adapter.remove(medicine);
		MedicineDao.getInstance(getActivity()).deleteById(medicine.getId());
		list.remove(medicine);
		adapter.notifyDataSetChanged();
		listview.closeOpenedItems();
	}

}