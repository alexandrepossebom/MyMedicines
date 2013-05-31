package com.possebom.mymedicines;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mobeta.android.dslv.DragSortListView;
import com.possebom.mymedicines.dao.MedicineDao;
import com.possebom.mymedicines.model.Medicine;
import com.possebom.mymedicines.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

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
    private List<Medicine> list;

    private ListAdapter	adapter;

    public MedicineListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        md = new MedicineDao(getActivity().getApplicationContext());
        list = md.getAll();
        adapter = new ListAdapter(getActivity(), R.layout.list_medicine_adapter, list);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DragSortListView mDslv = (DragSortListView) inflater.inflate(R.layout.list_root, container, false);
        mDslv.setRemoveListener(onRemove);
        return mDslv;
    }

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            Medicine m = list.get(which);
            adapter.remove(m);
            md.deleteById(m.getId());
            list.remove(m);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
        list = md.getAll();
        adapter.addAll(list);
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
        mCallbacks.onItemSelected(list.get(position).getId());
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

}