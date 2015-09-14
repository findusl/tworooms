package de.lehrbaum.tworooms.view;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Arrays;
import java.util.HashSet;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseRoleFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = ChooseRoleFragment.class.getSimpleName();

    public static final String SELECTION_INDIZES = "selection_ind";

    private CursorAdapter mAdapter;

    private long [] mSelections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long [] selections;
        if (getArguments() != null) {
            mSelections = getArguments().getLongArray(SELECTION_INDIZES);
            Log.i(TAG, "Selection with indizes: " + Arrays.toString(mSelections));
        }
        else {
            mSelections = new long[0];
        }

        //TODO: search for premade loading example because list takes time
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_activated_1, null,
                new String[] {"name"}, new int[]{android.R.id.text1}, 0);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

        //use setItemChecked method of ListView
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_roles, container, false);
    }

    public long [] getSelection () {
        return getListView().getCheckedItemIds();
        /*long [] selected = getListView().getCheckedItemIds();
        Cursor c = mAdapter.getCursor();
        //replace the array ids with the ids from the database.
        for(int i = 0; i < selected.length; i++) {
            c.moveToPosition((int) selected[i]);
            selected[i] = c.getLong(0);
        }
        return selected;*/
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, "roles");
        return new CursorLoader(getActivity(), uri, new String[]{"_id", "name"}, null /*TODO: count = people*/, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(new SortedCursor(data, mSelections));
        ListView lv = getListView();
        for(int i = 0; i < mSelections.length; i++) {
            lv.setItemChecked(i, true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    protected class SortedCursor extends CursorWrapper {

        protected int [] positionMapping;

        /**
         * Creates a new sorted cursor that sorts specific elements to the top of the cursor.
         *
         * @param selections The IDs of the elements to sort to the top of the cursor.
         * @param cursor The underlying cursor to wrap.
         */
        public SortedCursor(Cursor cursor, long [] selections) {
            super(cursor);
            Arrays.sort(selections);
            int size = cursor.getCount();
            positionMapping = new int [size];
            int checkedMappings = 0;
            int unCheckedMappings = selections.length;
            cursor.moveToFirst();
            for (int i = 0; i < size; cursor.moveToNext()) {
                long id = cursor.getLong(0);
                if (Arrays.binarySearch(selections, id) >= 0)
                    positionMapping[checkedMappings++] = i++;
                else {
                    if(unCheckedMappings >= size) {
                        //this should not happen, but it can happen if some selection is simply no longer in the database.
                        //then i map to the next position underneath selections.length
                        Log.e(TAG, "The unchecked mappings extended size. Selections: " + Arrays.toString(selections));
                        positionMapping[selections.length - (++unCheckedMappings - size)] = i++;
                    } else
                        positionMapping[unCheckedMappings++] = i++;
                }

            }
            cursor.moveToFirst();
        }

        @Override
        public boolean moveToPosition(int position) {
            return super.moveToPosition(positionMapping[position]);
        }

        @Override
        public int getPosition() {
            int actualPos = super.getPosition();
            for(int i = 0; i < positionMapping.length; i++) {
                if(positionMapping[i] == actualPos)
                    return i;
            }
            throw new UnsupportedOperationException("Cannot find current position in position mapping");
        }
    }

    /*protected class SortedCursorAdapter extends SimpleCursorAdapter {

        protected int [] positionMapping;

        public SortedCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public Object getItem(int position) {
            return super.getItem(positionMapping[position]);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(positionMapping[position]);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(positionMapping[position], convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return super.getDropDownView(positionMapping[position], convertView, parent);
        }

        @Override
        public boolean isEnabled(int position) {
            return super.isEnabled(positionMapping[position]);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(positionMapping[position]);
        }
    }*/
}
