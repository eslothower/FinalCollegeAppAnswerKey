package org.pltw.examples.collegeappanswerkey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jlinburg on 3/9/17.
 */
public class FamilyListFragment extends ListFragment {
    public static String TAG = "FAMILYLISTFRAGMENT";
    private Family mFamily;

    public void FamilyListFragment(){
        mFamily = Family.get();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.family_members_title);
        mFamily = Family.get();

        final FamilyMemberAdapter adapter = new FamilyMemberAdapter(mFamily.getFamily());
        setListAdapter(adapter);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);
        registerForContextMenu(listView);

        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_family_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FamilyMemberAdapter adapter = (FamilyMemberAdapter)getListAdapter();
        switch (item.getItemId()) {
            case R.id.menu_item_new_guardian:
                Log.d(TAG, "Selected add new guardian.");
                Guardian guardian = new Guardian();
                for (FamilyMember f: Family.get().getFamily()){
                    if (f.equals(guardian)){
                        Log.i(TAG, "Found Match of Guardian.");
                    }
                }
                Family.get().addFamilyMember(guardian);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.menu_item_new_sibling:
                Log.d(TAG, "Selected add new sibling.");
                Sibling sibling = new Sibling();
                for (FamilyMember f: Family.get().getFamily()){
                    if (f.equals(sibling)){
                        Log.i(TAG, "Found Match of Sibling.");
                    }
                }
                Family.get().addFamilyMember(sibling);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FamilyMember f = (FamilyMember) ((FamilyMemberAdapter)getListAdapter()).getItem(position);
        Log.d(TAG, f.toString() + " was clicked." + FamilyMemberActivity.class);
        Intent i = new Intent(getActivity(), FamilyMemberActivity.class);
        i.putExtra(FamilyMember.EXTRA_RELATION, f.getClass().getName());
        i.putExtra(FamilyMember.EXTRA_INDEX, position);
        startActivity(i);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        Log.d(TAG, "Creating Context Menu.");
        getActivity().getMenuInflater().inflate(R.menu.family_list_item_context, menu);
    }

    @Override
    public void onStart(){
        final FamilyMemberAdapter adapter = (FamilyMemberAdapter)getListAdapter();
        BackendlessDataQuery dq = new BackendlessDataQuery();
        QueryOptions qo = new QueryOptions();
        qo.setPageSize(50);
        dq.setQueryOptions(qo);
        Backendless.Persistence.of(Guardian.class).find(dq, new AsyncCallback<BackendlessCollection<Guardian>>() {
            @Override
            public void handleResponse(BackendlessCollection<Guardian> familyBackendlessCollection) {
                List<Guardian> resultList = familyBackendlessCollection.getData();
                for (Guardian fm:resultList){
                  if (!mFamily.getFamily().contains(fm)) {
                      mFamily.addFamilyMember(fm);
                  }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i(TAG, backendlessFault.toString());
            }
        });
        Backendless.Persistence.of(Sibling.class).find(dq, new AsyncCallback<BackendlessCollection<Sibling>>() {
            @Override
            public void handleResponse(BackendlessCollection<Sibling> familyBackendlessCollection) {
                List<Sibling> resultList = familyBackendlessCollection.getData();
                for (Sibling fm:resultList){
                    if (!mFamily.getFamily().contains(fm)) {
                        mFamily.addFamilyMember(fm);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.i(TAG, backendlessFault.toString());
            }
        });
        super.onStart();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.d(TAG, "Context item selected.");
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        FamilyMemberAdapter adapter = (FamilyMemberAdapter) getListAdapter();
        final FamilyMember familyMember = (FamilyMember)adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_family_member:
                Family.get().deleteFamilyMember(familyMember);
                adapter.notifyDataSetChanged();

                Backendless.Persistence.of(FamilyMember.class).remove(familyMember, new
                        AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                Log.i(TAG, familyMember.toString() + " deleted");
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Log.e(TAG, fault.getMessage());
                            }
                        });
            case R.id.menu_item_save_family_member:
                ArrayList<FamilyMember> familyArray = mFamily.getFamily();
                for (FamilyMember fm:familyArray){
                    Backendless.Persistence.save(fm, new AsyncCallback<FamilyMember>() {
                        @Override
                        public void handleResponse(FamilyMember familyMember) {
                            Log.i(TAG, "Saved " + familyMember.toString());
                        }

                        @Override
                        public void handleFault(BackendlessFault backendlessFault) {
                            Log.i(TAG, backendlessFault.toString());
                        }
                    });
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        FamilyMemberAdapter adapter = (FamilyMemberAdapter) getListAdapter();
        adapter.notifyDataSetChanged();

    }

    private class FamilyMemberAdapter extends ArrayAdapter {
        public FamilyMemberAdapter(ArrayList family) {
            super(getActivity(), 0, family);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_family_member, null);
            }

            FamilyMember f = (FamilyMember)getItem(position);
            Log.d(TAG, "The type of FamilyMember at position " + position + " is "
                    + f.getClass().getName());

            TextView nameTextView =
                    (TextView)convertView
                            .findViewById(R.id.family_member_list_item_nameTextView);
            nameTextView.setText(f.toString());

            return convertView;
        }
    }

}
