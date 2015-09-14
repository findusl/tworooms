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

    private CursorAdapter mAdapter;
    private int setId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setId = getArguments().getInt(ARG_SET_ID, Integer.MIN_VALUE);
        if(setId == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Need item id to display item details.");
        }

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_activated_1, null,
                new String[] {"name"}, new int[]{android.R.id.text1}, 0);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(setId, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_set_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, "sets");
        Cursor c = getActivity().getContentResolver().query(uri, new String[]{"name", "description"},
                "_id = ?", new String[]{Integer.toString(setId)}, null);
        c.moveToFirst();

        String name = c.getString(0);
        TextView nameView = (TextView) view.findViewById(R.id.name_view);
        nameView.setText(name);

        String desc = c.getString(1);
        TextView descView = (TextView) view.findViewById(R.id.desc_view);
        descView.setText(desc);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int setId, Bundle bundle) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, "roles");
        return new CursorLoader(getActivity(), uri,
                new String[]{"_id", "name"},
                DatabaseContentProvider.Constants.SET_ROLE_SELECTION,
                new String[]{Integer.toString(setId)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Cursor count: " + data.getCount());
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
