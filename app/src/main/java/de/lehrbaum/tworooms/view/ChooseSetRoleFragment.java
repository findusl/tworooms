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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvder;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseSetRoleFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = ChooseSetRoleFragment.class.getSimpleName();

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

        //TODO: search for premade loading example because list takes time
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_activated_1, null,
                new String[] {"name"}, new int[]{android.R.id.text1}, 0);

        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

        //TODO: need to find some way to select depending selections
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
        mCallback.onListSubmitted(selected);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvder.CONTENT_URI, "roles");
        return new CursorLoader(getActivity(), uri, new String[]{"_id", "name"}, null /*TODO: count = people*/, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "on load finished called: " + data);
        //TODO: make list show colors and maybe short description
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
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
