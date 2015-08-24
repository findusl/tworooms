package de.lehrbaum.tworooms.view;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.Arrays;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseSetRoleFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = ChooseSetRoleFragment.class.getSimpleName();

    private static final String SELECTION_INDIZES = "selection_ind";
    private static final String SELECTION_POSITION = "selection_pos";

    private CursorAdapter mAdapter;
    private Callbacks mCallback;
    private int mSelectionPosition = -1;

    /**
     *
     * @param position The position of the existing selection
     * @param selection An exisiting selection that can be modified.
     * @return
     */
    public static ChooseSetRoleFragment newInstance(int position, long [] selection) {
        ChooseSetRoleFragment fragment = new ChooseSetRoleFragment();
        if(selection != null) {
            Bundle args = new Bundle();
            args.putLongArray(SELECTION_INDIZES, selection);
            args.putInt(SELECTION_POSITION, position);
            fragment.setArguments(args);
        }
        return fragment;
    }

    public static ChooseSetRoleFragment newInstance() {
        ChooseSetRoleFragment fragment = new ChooseSetRoleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long [] selections;
        if (getArguments() != null) {
            selections = getArguments().getLongArray(SELECTION_INDIZES);
            mSelectionPosition = getArguments().getInt(SELECTION_POSITION);
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
        return inflater.inflate(R.layout.fragment_choose_set_roles, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.v(TAG, "onViewCreated");
        Button b = (Button)view.findViewById(R.id.buttonNext);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClicked(v);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void onSubmitClicked(View view) {
        //react to submit button
        long [] selected = getListView().getCheckedItemIds();
        Cursor c = mAdapter.getCursor();
        //replace the array ids with the ids from the database.
        for(int i = 0; i < selected.length; i++) {
            c.moveToPosition((int) selected[i]);
            selected[i] = c.getLong(0);
        }
        mCallback.onListSubmitted(mSelectionPosition, selected);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, "roles");
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
         * @param position The position to replace, or -1 if new variation.
         * @param entries The IDs of the selected roles.
         */
        void onListSubmitted(int position, long[] entries);
    }
}
