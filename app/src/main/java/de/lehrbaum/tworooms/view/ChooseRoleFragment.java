package de.lehrbaum.tworooms.view;

import android.content.Loader;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.Set;
import de.lehrbaum.tworooms.view.util.ChooseRolesListAdapter;
import de.lehrbaum.tworooms.view.util.FloatingInfoView;
import de.lehrbaum.tworooms.view.util.SortedCursor;

import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.*;

/**
 * A placeholder fragment containing a simple view.
 */
public final class ChooseRoleFragment extends CategoryRoleListFragment {
    private static final String TAG = ChooseRoleFragment.class.getSimpleName();

	
	private SparseBooleanArray mSelections;
	private boolean mChanged;
    private Set mSet;
	private Map<Integer, SparseIntArray> mRoleCombinations;
	private FloatingInfoView mInfoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setUseLongClick(true);
		long [] startSelections;
        if (getArguments() != null) {
            mSet = new Set(getArguments());
            startSelections = mSet.getSelection();
        }
        else {
			startSelections = new long[0];
        }

		mSelections = new SparseBooleanArray();
		for (long startSelection : startSelections)
			mSelections.put((int) startSelection, true);

		getLoaderManager().initLoader(ROLES_LOADER, null, this);
    }

	@Override
	public CursorAdapter onCreateListAdapter() {
        SeekBar.OnSeekBarChangeListener blueRole = new SeekBarListenerAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSet.setBlueRoleCount(progress);
            }
        };
        SeekBar.OnSeekBarChangeListener redRole = new SeekBarListenerAdapter() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSet.setRedRoleCount(progress);
            }
        };
        return new ChooseRolesListAdapter(getActivity(), blueRole, redRole);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_choose_roles, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
        mInfoView = (FloatingInfoView) view.findViewById(R.id.floating_count_view);
	}

	@Override
	protected void setBackground(View v, int team) {
		int color = getTeamColor(team);
		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] {android.R.attr.state_activated},
				new ColorDrawable(color & 0xC0FFFFFF));
		states.addState(new int[] {},
				new ColorDrawable(color & ALPHA_COLOR));
		v.setBackground(states);
	}

	//==============================================================================================
	//List callbacks================================================================================

	public Set getSet () {
		if(!mChanged)
			return null;
        long [] selections = new long[mSelections.size()];

		for(int i = 0, j = 0; i < mSelections.size(); i++) {
			if(mSelections.valueAt(i))
				selections[j++] = mSelections.keyAt(i);
		}
        mSet.setSelection(selections);
		return mSet;
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
            mInfoView.setText(Integer.toString(selected));
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

    private abstract class SeekBarListenerAdapter implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
}