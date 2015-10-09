package de.lehrbaum.tworooms.view;

import android.app.Fragment;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.view.util.NoneCursorAdapter;
import de.lehrbaum.tworooms.view.util.RolesListFragment;
import de.lehrbaum.tworooms.view.util.SortedCursor;

import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.*;
import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.GROUP_COLUMN;
import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.ID_COLUMN;
import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.TEAM_COLUMN;


public class CategoryRoleListFragment extends RolesListFragment
		implements AdapterView.OnItemSelectedListener {
	protected static final int CATEGORIES_LOADER = 0;

	protected CursorAdapter mSpinnerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSpinnerAdapter = new NoneCursorAdapter(getActivity(),
				android.R.layout.simple_spinner_dropdown_item,
				null, new String [] {NAME_COLUMN}, new int[] {android.R.id.text1}, 0);

		getLoaderManager().initLoader(CATEGORIES_LOADER, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_choose_roles, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//noinspection ConstantConditions
		Spinner spinner = (Spinner) getView().findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);
		spinner.setAdapter(mSpinnerAdapter);
	}

	//==============================================================================================
	//Loader Callbacks==============================================================================

	@Override
	 public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri uri;
		String [] selArgs = null;
		String selection = null;
		String [] columns;
		String sortOrder = null;
		switch (id) {
			case CATEGORIES_LOADER:
				uri = Uri.withAppendedPath(CONTENT_URI, CATEGORIES_TABLE);
				columns = new String[]{ID_COLUMN, NAME_COLUMN};
				sortOrder = NAME_COLUMN + " ASC";
				break;
			case ROLES_LOADER:
				uri = Uri.withAppendedPath(CONTENT_URI, ROLES_TABLE);
				if(args != null && args.size() == 1) {
					selection = CATEGORY_COLUMN + " = " + args.getLong(ChooseRoleFragment.SELECTION_INDEX);
				}
				columns =  new String[]{ID_COLUMN, NAME_COLUMN, TEAM_COLUMN, GROUP_COLUMN};
				sortOrder = orderByClause();
				break;
			default:
				return super.onCreateLoader(id, args);
		}
		return new CursorLoader(getActivity(), uri, columns, selection, selArgs, sortOrder);
	}

	@Override
	 public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		switch (loader.getId()) {
			case CATEGORIES_LOADER:
				mSpinnerAdapter.swapCursor(data);
			default:
				super.onLoadFinished(loader, data);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == CATEGORIES_LOADER)
			mSpinnerAdapter.swapCursor(null);
		else {
			super.onLoaderReset(loader);
		}
	}

	//==============================================================================================
	//Spinner Callbacks=============================================================================

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(id == -1)
			onNothingSelected(parent);
		else {
			Bundle args = new Bundle();
			args.putLong(ChooseRoleFragment.SELECTION_INDEX, id);
			getLoaderManager().restartLoader(1, args, this);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		getLoaderManager().restartLoader(1, null, this);
	}
}
