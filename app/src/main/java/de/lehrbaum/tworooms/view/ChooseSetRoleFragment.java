package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvder;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseSetRoleFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String SELECTION_INDIZES = "selection_ind";

    private CursorAdapter mAdapter;
    private Callbacks mCallback;

    public static ChooseSetRoleFragment newInstance(long [] selection) {
        ChooseSetRoleFragment fragment = new ChooseSetRoleFragment();
        if(selection != null) {
            Bundle args = new Bundle();
            args.putLongArray(SELECTION_INDIZES, selection);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long [] selections;
        if (getArguments() != null) {
            selections = getArguments().getLongArray(SELECTION_INDIZES);
        }
        else {
            selections = new long[0];
        }

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_activated_1, null,
                new String[] {"name"}, new int[]{android.R.id.text1}, 0);

        setListAdapter(mAdapter);

        //TODO: need to find some way to select depending long
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //possibilty for future save selected roles in savedInstanceState and copy out again
        return inflater.inflate(R.layout.fragment_choose_set_roles, container, false);
    }*/

    public void onSubmitClicked(View view) {
        //react to submit button
        long [] selected = getListView().getCheckedItemIds();
        Cursor c = mAdapter.getCursor();
        //replace the array ids with the ids from the database.
        for(int i = 0; i < selected.length; i++) {
            c.moveToPosition((int) selected[i]);
            selected[i] = c.getLong(0);
        }
        mCallback.onListSubmitted(selected);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvder.CONTENT_URI, "roles");
        return new CursorLoader(getActivity(), uri, new String[]{"_id", "name"}, null /*TODO: count = people*/, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mCallback = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface Callbacks {
        /**
         * Callback for when the list has been submitted.
         * @param entries The IDs of the selected roles.
         */
        void onListSubmitted(long[] entries);
    }
}
