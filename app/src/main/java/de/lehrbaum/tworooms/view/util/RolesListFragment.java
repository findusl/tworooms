package de.lehrbaum.tworooms.view.util;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;

import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.*;
import android.view.*;

/**
 * This fragment is able to display the description of a role in a dialog. It will NOT initialize
 * the role list or anything else. It will register and react to long clicks and try to display
 * the description to the id that has been long clicked.
 *
 * The first 3 columns of the cursor have to be: _id, name, team_id
 */
public class RolesListFragment extends ListFragment implements AdapterView.OnItemLongClickListener,
		LoaderManager.LoaderCallbacks<Cursor>, SimpleCursorAdapter.ViewBinder{
	private static final String TAG = RolesListFragment.class.getSimpleName();
	protected static final int ALPHA_COLOR = 0x38FFFFFF;
	protected static final String ITEM_INDEX = "id";

	protected static final int ROLES_LOADER = 1;
	protected static final int DESCRIPTION_LOADER = 2;

	protected enum SORT_ORDER {
		ALPHABETICAL(R.string.menu_sort_Alphabet),
		GROUP(R.string.menu_sort_group);
		int text;
		SORT_ORDER (int text) {
			this.text = text;
		}
		SORT_ORDER next() {
			switch(this) {
				case ALPHABETICAL:
					return GROUP;
				case GROUP:
					return ALPHABETICAL;
			}
			return null;
		}
	}

	private boolean mUseLongClick = false;
	protected SimpleCursorAdapter mAdapter;
	protected SORT_ORDER mSortOrder = SORT_ORDER.GROUP;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//TODO: Add item @id/android:empty to display while list is empty

		mAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_activated_1, null,
				new String[] {NAME_COLUMN, TEAM_COLUMN},
				new int[]{android.R.id.text1, android.R.id.text1}, 0);
		mAdapter.setViewBinder(this);
		setListAdapter(mAdapter);
		setHasOptionsMenu(true);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if(mUseLongClick)
			getListView().setOnItemLongClickListener(this);
	}
	
	//==============================================================================================
	//Setting background color======================================================================

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		switch (columnIndex) {
			case 1://name column
				((TextView) view).setText(cursor.getString(columnIndex));
				return true;
			case 2://team column
				setBackground(view, cursor.getInt(columnIndex));
				return true;
		}
		return false;
	}

	protected void setBackground(View v, int team) {
		v.setBackgroundColor(getTeamColor(team) & ALPHA_COLOR);
	}

	protected final int getTeamColor(int team) {
		int colorRes = android.R.color.background_dark;
		switch (team) {
			case TEAM_BLUE:
				colorRes = android.R.color.holo_blue_light;
				break;
			case TEAM_RED:
				colorRes = android.R.color.holo_red_light;
				break;
			case TEAM_GRAY:
				colorRes = android.R.color.darker_gray;
				break;
			case TEAM_GREEN:
				colorRes = android.R.color.holo_green_light;
				break;
			case TEAM_YELLOW:
				colorRes = android.R.color.holo_orange_light;
				break;
			case TEAM_VIOLETT:
				colorRes = android.R.color.holo_purple;
				break;
            case TEAM_BLACK:
                return Color.BLACK;
        }
		int color = getActivity().getResources().getColor(colorRes);
		return color;
	}

	//==============================================================================================
	//Options menu==================================================================================

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.menu_role_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
        MenuItem sortItem = menu.findItem(R.id.action_sort);
        sortItem.setTitle(mSortOrder.next().text);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
			case R.id.action_sort:
                mSortOrder = mSortOrder.next();
				getLoaderManager().restartLoader(ROLES_LOADER, null, this);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected String orderByClause() {
		switch(mSortOrder) {
			case GROUP:
				return GROUP_COLUMN + " ASC";
			case ALPHABETICAL:
				return NAME_COLUMN + " ASC";
		}
		return ID_COLUMN + " ASC";
	}

	//==============================================================================================
	//Loader Callbacks==============================================================================

	private AlertDialog currentDialog;
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri uri;
		String [] selArgs = null;
		String selection = null;
		String [] columns;
		String sortOrder = null;
		switch (id) {
			case DESCRIPTION_LOADER:
				uri = Uri.withAppendedPath(CONTENT_URI, ROLES_TABLE);
				selection = ID_COLUMN + " = " + args.getLong(ITEM_INDEX);
				columns = new String[]{NAME_COLUMN, DESCRIPTION_COLUMN};
				break;
			case ROLES_LOADER:
				uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, ROLES_TABLE);
				columns = new String[]{ID_COLUMN, NAME_COLUMN, TEAM_COLUMN};
				sortOrder = orderByClause();
				break;
			default:
				throw new IllegalArgumentException("Unsupported loader id: " + id);
		}
		return new CursorLoader(getActivity(), uri, columns, selection, selArgs, sortOrder);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		switch (loader.getId()) {
			case DESCRIPTION_LOADER:
				if(data.getCount() == 0) {
					Log.w(TAG, "Could not find description.");
					return;
				}
				data.moveToFirst();
				showDescription(data.getString(0), data.getString(1));
				break;
			case ROLES_LOADER:
				mAdapter.swapCursor(data);
				break;
		}
	}

	private void showDescription(String title, String text) {
		Log.d(TAG, "Show description called.");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);

		builder.setMessage(text);

		// Set up the buttons
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				currentDialog = null;
				dialog.dismiss();
			}
		});

		currentDialog = builder.show();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		switch (loader.getId()) {
			case DESCRIPTION_LOADER:
				if(currentDialog != null && currentDialog.isShowing())
					currentDialog.dismiss();
				break;
			case ROLES_LOADER:
				mAdapter.swapCursor(null);
				break;
		}
	}
	

	//==============================================================================================
	//list Callbacks================================================================================

	protected void setUseLongClick(boolean useLongClick) {
		this.mUseLongClick = useLongClick;
	}

	@Override
	public final boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if(mUseLongClick) {
			descriptionClicked(id);
			return true;
		} else
			return false;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if(!mUseLongClick)
			descriptionClicked(id);
	}

	private void descriptionClicked(long id) {
		Bundle args = new Bundle();
		args.putLong(ITEM_INDEX, id);
		getLoaderManager().restartLoader(DESCRIPTION_LOADER, args, this);
	}
}
