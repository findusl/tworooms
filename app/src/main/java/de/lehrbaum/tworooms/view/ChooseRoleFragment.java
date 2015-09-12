package de.lehrbaum.tworooms.view;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.Arrays;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseRoleFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = ChooseRoleFragment.class.getSimpleName();

    public static final String SELECTION_INDIZES = "selection_ind";

    private CursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long [] selections;
        if (getArguments() != null) {
            selections = getArguments().getLongArray(SELECTION_INDIZES);
            Log.i(TAG, "Selection with indizes: " + Arrays.toString(selections));
        }
        else {
            selections = new long[0];
        }

        //TODO: search for premade loading example because list takes time
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_activated_1, null,
                new String[] {"name"}, new int[]{android.R.id.text1}, 0);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

        //TODO: need to find some way to select depending selections. sort selected items on top
        //use setItemChecked method of ListView
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_roles, container, false);
    }

    public long [] getSelection () {
        long [] selected = getListView().getCheckedItemIds();
        Cursor c = mAdapter.getCursor();
        //replace the array ids with the ids from the database.
        for(int i = 0; i < selected.length; i++) {
            c.moveToPosition((int) selected[i]);
            selected[i] = c.getLong(0);
        }
        return selected;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, "roles");
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
}
