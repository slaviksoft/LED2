package ru.yomu.slaviksoft.led2;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;

public class LeadDetailsFragment extends Fragment {

    private LeadItem mLead;
    private DBCatalogs mDBCatalogs;

    public static LeadDetailsFragment newInstance(LeadItem item) {

        LeadDetailsFragment fragment = new LeadDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("lead", item);
        fragment.setArguments(args);
        return fragment;
    }

    public LeadDetailsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLead = getArguments().getParcelable("lead");
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lead_details, container, false);
        ((EditText) v.findViewById(R.id.edId)).setText(String.valueOf(mLead.id));
        ((EditText) v.findViewById(R.id.edTitle)).setText(mLead.title);
        ((EditText) v.findViewById(R.id.edDescription)).setText(mLead.description);

        AutoCompleteTextView edStreet = (AutoCompleteTextView) v.findViewById(R.id.edStreet);
        edStreet.setText(mLead.street);

        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null,
                new String[] { DBCatalogs.STREETS_NAME },
                new int[] {android.R.id.text1},
                0);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint == null || constraint.equals("")){
                    return adapter.getCursor();
                }
                Cursor cursor = mDBCatalogs.getStreetsCursor(constraint.toString());
                return cursor;
            }
        });
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return DBCatalogs.getStreet(cursor);
            }
        });

        edStreet.setAdapter(adapter);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDBCatalogs = new DBCatalogs(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public LeadItem getLead() {
        return mLead;
    }

}
