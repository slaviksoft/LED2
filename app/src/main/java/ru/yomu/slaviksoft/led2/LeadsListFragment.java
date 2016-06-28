package ru.yomu.slaviksoft.led2;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class LeadsListFragment extends ListFragment {

    private LeadsListListener mListener;
    private int CurrPosition = 0;
    public LeadsListAdapter adapter;

    public interface LeadsListListener{
        public void onItemClick(LeadItem item);
    }

    public LeadsListFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (LeadsListListener)activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrPosition", CurrPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new LeadsListAdapter(getActivity(), android.R.layout.simple_list_item_activated_1);
        setListAdapter(adapter);

        if (savedInstanceState != null) {
            //CurrPosition = savedInstanceState.getInt("CurrPosition");
            //getListView().performItemClick(getListView().getSelectedView(), CurrPosition, getListView().getItemIdAtPosition(CurrPosition));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DEBUG", "list frag resume");
        adapter.update();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("DEBUG", "list frag pause");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leads_list, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        LeadItem item = adapter.getItem(position);
        mListener.onItemClick(item);
        CurrPosition = position;
    }

    public void deleteLead(){

        DBLeads dbLeads = new DBLeads(getActivity());
        dbLeads.deleteLead( adapter.getItem(CurrPosition));

        CurrPosition = 0;
        adapter.update();

//        LeadItem item = adapter.getItem(CurrPosition);
//        mListener.onItemClick(item);

    }

}
