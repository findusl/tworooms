package de.lehrbaum.tworooms.view;

import android.app.ListFragment;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import java.security.InvalidParameterException;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;

/**
 * A fragment representing a single set detail screen.
 * This fragment is either contained in a {@link SetListActivity}
 * in two-pane mode (on tablets) or a {@link SetDetailActivity}
 * on handsets.
 */
public class SetDetailFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = SetDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_SET_ID = "set_id";

    private static final int ROLES_LOADER_ID = 0;
    private static final int INFORMATION_LOADER_ID = 1;

    private CursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_activated_1, null,
                new String[] {"name"}, new int[]{android.R.id.text1}, 0);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(ROLES_LOADER_ID, getArguments(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLoaderManager().initLoader(INFORMATION_LOADER_ID, getArguments(), this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        int setId = bundle.getInt(ARG_SET_ID, Integer.MIN_VALUE);
        Uri uri;
        switch (loaderId) {
            case INFORMATION_LOADER_ID:
                uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, "sets");
                return new CursorLoader(getActivity(), uri,
                        new String[]{"name", "description"},
                        "_id = ?",
                        new String[]{Integer.toString(setId)},
                        null);
            case ROLES_LOADER_ID:
                uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, "roles");
                return new CursorLoader(getActivity(), uri,
                        new String[]{"_id", "name"},
                        DatabaseContentProvider.Constants.SET_ROLE_SELECTION,
                        new String[]{Integer.toString(setId)},
                        null);
            default:
                throw new InvalidParameterException("No such loader id known " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Cursor count: " + data.getCount());
        switch(loader.getId()) {
            case ROLES_LOADER_ID:
                mAdapter.swapCursor(data);
                break;
            case INFORMATION_LOADER_ID:
                if(!isVisible())
                    return;
                data.moveToFirst();

                String name = data.getString(0);
                TextView nameView = (TextView) getView().findViewById(R.id.name_view);
                nameView.setText(name);

                String desc = data.getString(1);
                TextView descView = (TextView) getView().findViewById(R.id.desc_view);
                descView.setText(desc);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == ROLES_LOADER_ID)
            mAdapter.swapCursor(null);
    }
}
