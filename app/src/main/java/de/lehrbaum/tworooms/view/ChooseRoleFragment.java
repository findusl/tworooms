package de.lehrbaum.tworooms.view;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.view.util.NoneCursorAdapter;
import de.lehrbaum.tworooms.view.util.RolesListFragment;
import de.lehrbaum.tworooms.view.util.SortedCursor;

import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseRoleFragment extends CategoryRoleListFragment {
    private static final String TAG = ChooseRoleFragment.class.getSimpleName();

    public static final String SELECTION_INDEX = "sel_id";
	
	private SparseBooleanArray mSelections;
	private boolean mChanged;
	private int mSelectionCount;
	private Map<Integer, SparseIntArray> mRoleCombinations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setUseLongClick(true);
		long [] startSelections;
        if (getArguments() != null) {
			startSelections = getArguments().getLongArray(SELECTION_INDEX);
            Log.i(TAG, "Selection with indices: " + Arrays.toString(startSelections));
        }
        else {
			startSelections = new long[0];
        }

		mSelections = new SparseBooleanArray();
		for (long startSelection : startSelections)
			mSelections.put((int) startSelection, true);
		mSelectionCount = startSelections.length;

		getLoaderManager().initLoader(ROLES_LOADER, null, this);

    }

	@Override
	protected void setBackground(View v, int team) {
		int color = getTeamColor(team);
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] {android.R.attr.state_activated},
				new ColorDrawable(color & 0xA0FFFFFF));
		states.addState(new int[] {},
				new ColorDrawable(color & ALPHA_COLOR));
		v.setBackground(states);
	}

	//==============================================================================================
	//List callbacks================================================================================

	public long [] getSelection () {
		if(!mChanged)
			return null;
        long [] selections = new long[mSelectionCount];

		for(int i = 0, j = 0; i < mSelections.size(); i++) {
			if(mSelections.valueAt(i))
				selections[j++] = mSelections.keyAt(i);
		}

		return selections;
    }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		boolean getsSelected = !mSelections.get((int) id, false);
		if(getsSelected) {
			Cursor c = (Cursor) mAdapter.getItem(position);
			int team = c.getInt(3);
			SparseIntArray arr = mRoleCombinations.get(team);
			for(int i = 0; i < arr.size(); i++) {
				l.setItemChecked(arr.keyAt(i), true);
				onListItemSelection(arr.valueAt(i), true);
			}
		} else {
			onListItemSelection(id, false);
		}
	}

	protected void onListItemSelection(long id, boolean selected) {
		if(!mChanged)
			mChanged = true;
		mSelections.put((int) id, selected);
		if(selected)
			mSelectionCount++;
		else
			mSelectionCount--;
	}

	//==============================================================================================
	//Loader Callbacks==============================================================================

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(loader.getId() == ROLES_LOADER) {
			SortedCursor sc = new SortedCursor(data);
			int selected = sc.setSelected(mSelections);
			mAdapter.swapCursor(sc);
			ListView lv = getListView();
			for(int i = 0; i < selected; i++) {
				lv.setItemChecked(i, true);
			}
			initRoleComps(sc);
		} else  {
			super.onLoadFinished(loader, data);
		}
    }

	private void initRoleComps(Cursor sc) {
		mRoleCombinations = new HashMap<>();
		SparseIntArray arr = null;
		int last_team = -1;
		for(sc.moveToFirst(); !sc.isAfterLast(); sc.moveToNext()) {
			int group = sc.getInt(3);//group column
			if(group != last_team) {
				if(arr != null)
					mRoleCombinations.put(last_team, arr);
				last_team = group;
				arr = mRoleCombinations.get(group);
				if(arr == null)
					arr = new SparseIntArray();
			}
			int pos = sc.getPosition();
			int id = sc.getInt(0);
			arr.put(pos, id);
		}
		mRoleCombinations.put(last_team, arr);
	}
}