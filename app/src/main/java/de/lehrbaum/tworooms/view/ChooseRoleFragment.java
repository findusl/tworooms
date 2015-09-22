package de.lehrbaum.tworooms.view;

import android.app.ListFragment;
import android.app.LoaderManager;
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

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseRoleFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = ChooseRoleFragment.class.getSimpleName();

    public static final String SELECTION_INDIZES = "selection_ind";

    private CursorAdapter mAdapter;
	
	private long [] mStartSelections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStartSelections = getArguments().getLongArray(SELECTION_INDIZES);
            Log.i(TAG, "Selection with indizes: " + Arrays.toString(mStartSelections));
        }
        else {
            mStartSelections = new long[0];
        }

		//Add item @id/android:empty to display while list is empty
		
        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_activated_1, null,
                new String[] {"name"}, new int[]{android.R.id.text1}, 0);
        setListAdapter(mAdapter);
		
		/*getListView().setOnItemSelectedListener(new OnItemSelectedListener() {
			void onItemSelected(arg0, arg1, arg2, arg3) {
				
			}
		});*/

        getLoaderManager().initLoader(0, null, this);

        //use setItemChecked method of ListView
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_roles, container, false);
    }

    public long [] getSelection () {
        long [] selections = getListView().getCheckedItemIds();;
        if(Arrays.equals(selections, mStartSelections))
            return null;
        else
            return selections;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, "roles");
        return new CursorLoader(getActivity(), uri, new String[]{"_id", "name"}, null /*TODO: count = people*/, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(new SortedCursor(data, mStartSelections));
        ListView lv = getListView();
        for(int i = 0; i < mStartSelections.length; i++) {
            lv.setItemChecked(i, true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    protected class SortedCursor extends CursorWrapper {

        protected int [] mPositionMapping;

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
            mPositionMapping = new int [size];
            int checkedMappings = 0;
            int unCheckedMappings = selections.length;
            cursor.moveToFirst();
            for (int i = 0; i < size; cursor.moveToNext()) {
                long id = cursor.getLong(0);
                if (Arrays.binarySearch(selections, id) >= 0)
                    mPositionMapping[checkedMappings++] = i++;
                else {
                    if(unCheckedMappings >= size) {
                        //this should not happen, but it can happen if some selection is simply no longer in the database.
                        //then i map to the next position underneath selections.length
                        Log.e(TAG, "The unchecked mappings extended size. Selections: " + Arrays.toString(selections));
                        mPositionMapping[selections.length - (++unCheckedMappings - size)] = i++;
                    } else
                        mPositionMapping[unCheckedMappings++] = i++;
                }

            }
            cursor.moveToFirst();
        }

        @Override
        public boolean moveToPosition(int position) {
            return super.moveToPosition(mPositionMapping[position]);
        }

        @Override
        public int getPosition() {
            int actualPos = super.getPosition();
            for(int i = 0; i < mPositionMapping.length; i++) {
                if(mPositionMapping[i] == actualPos)
                    return i;
            }
            throw new UnsupportedOperationException("Cannot find current position in position mapping");
        }
    }
}
