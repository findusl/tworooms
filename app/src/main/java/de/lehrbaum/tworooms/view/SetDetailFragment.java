package de.lehrbaum.tworooms.view;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.database.DatabaseContentProvider;
import de.lehrbaum.tworooms.view.util.RolesListFragment;

import static de.lehrbaum.tworooms.database.DatabaseContentProvider.Constants.*;

/**
 * A fragment representing a single set detail screen.
 * This fragment is either contained in a {@link SetListActivity}
 * in two-pane mode (on tablets) or a {@link SetDetailActivity}
 * on handsets.
 */
public final class SetDetailFragment extends RolesListFragment {

    private static final String TAG = SetDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_SET_ID = "set_id";

    private static final int INFORMATION_LOADER = 0;

    private int mSetId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSetId = getArguments().getInt(ARG_SET_ID, Integer.MIN_VALUE);
		getLoaderManager().initLoader(ROLES_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLoaderManager().initLoader(INFORMATION_LOADER, getArguments(), this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        Uri uri;
		String [] selArgs = null;
		String selection = null;
		String [] columns;
		String sortOrder = null;
        switch (loaderId) {
            case INFORMATION_LOADER:
                uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, SETS_TABLE);
				columns = new String[]{NAME_COLUMN, DESCRIPTION_COLUMN, COUNT_COLUMN};
				selection = ID_COLUMN + " = " + mSetId;
				break;
            case ROLES_LOADER:
                uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, ROLES_TABLE);
				columns = new String[]{ID_COLUMN, NAME_COLUMN, TEAM_COLUMN};
				selection = DatabaseContentProvider.Constants.SET_ROLE_SELECTION;
				selArgs = new String[]{Integer.toString(mSetId)};
				sortOrder = orderByClause();
				break;
            default:
				return super.onCreateLoader(loaderId, bundle);
        }
		return new CursorLoader(getActivity(), uri, columns, selection, selArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()) {
            case INFORMATION_LOADER:
                data.moveToFirst();

                String name = data.getString(0);
                TextView nameView = (TextView) getView().findViewById(R.id.name_view);
                nameView.setText(name);

                String desc = data.getString(1);
                TextView descView = (TextView) getView().findViewById(R.id.desc_view);
                descView.setText(desc);

                int count = data.getInt(2);
                TextView countView = (TextView) getView().findViewById(R.id.count_view);
                countView.setText(Integer.toString(count));
                break;
			default:
				super.onLoadFinished(loader, data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
		switch (loader.getId()) {
			case INFORMATION_LOADER:
				break;
			default:
				super.onLoaderReset(loader);
		}
    }
}
