package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.database.DatabaseContentProvider;
import de.lehrbaum.tworooms.database.LocalDatabaseConnection;
import de.lehrbaum.tworooms.view.createSet.CreateSetActivity;

import static de.lehrbaum.tworooms.database.DatabaseContentProvider.Constants.*;

import android.view.*;
import android.widget.AdapterView.*;

/**
 * A list fragment representing a list of sets. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link SetDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public final class SetListFragment extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher{
    private static final String TAG = SetListFragment.class.getSimpleName();
	private static final String SEL_COUNT = COUNT_COLUMN;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private CursorAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SetListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item_set, null,
                new String[] {"name", "count"}, new int[]{android.R.id.text1, R.id.counterView}, 0);
        getLoaderManager().initLoader(0, null, this);
        setListAdapter(mAdapter);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
		//registerForContextMenu(getListView()); make id for creator
		EditText tv = (EditText) view.findViewById(R.id.search_field);
		tv.addTextChangedListener(this);
    }

    //==============================================================================================
	//Menu callbacks================================================================================

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_set_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_add:
                Intent createIntent = new Intent(this.getActivity(), CreateSetActivity.class);
                startActivity(createIntent);
                return true;
			case R.id.action_roles:
				Intent rolesIntent = new Intent(this.getActivity(), RoleListActivity.class);
				startActivity(rolesIntent);
				return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
        Object item = mAdapter.getItem(((AdapterContextMenuInfo) menuInfo).position);
        if(item instanceof Cursor) {
            //check if user is allowed to delete
            Cursor c = (Cursor) item;
            String owner = c.getString(3);
            //is this secure enough?
            if(owner.contentEquals(DatabaseContentProvider.getDeviceID())) {
                MenuInflater inflater = getActivity().getMenuInflater();
                inflater.inflate(R.menu.context_menu_set, menu);
            }
        }
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //because final fragment this id is unique and it user is for sure allowed to delete
                AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
                final long id = mAdapter.getItemId(menuInfo.position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase db = new LocalDatabaseConnection(getActivity()).getWritableDatabase();
                        db.beginTransaction();
                        try {
                            ContentValues values = new ContentValues(1);
                            values.put(FROM_SERVER_COLUMN, 2);
                            db.update(SETS_TABLE, null, ID_COLUMN + "=" + id, null);
                            //TODO: parents/variations

                            Uri uri = Uri.withAppendedPath(CONTENT_URI, SETS_TABLE);
                            getActivity().getContentResolver().notifyChange(uri, null);
                            db.setTransactionSuccessful();
                            showToast(0);//TODO: show success
                        } catch (Exception e) {
                            showToast(1);//TODO: show error
                        } finally {
                            db.endTransaction();
                        }
                    }
                }).start();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showToast(final int msg) {
        new Handler(getActivity().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // Make and show the toast in the posted runnable
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

	//==============================================================================================
	//List callbacks================================================================================

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected((int) id);
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick() {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	//==============================================================================================
	//Loader callbacks==============================================================================

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, SETS_TABLE);
		StringBuilder selection = new StringBuilder(10);
		if(args != null && args.size() > 0) {
			if(args.containsKey(SEL_COUNT)) {
				selection.append(COUNT_COLUMN);
				selection.append(" = ");
				selection.append(args.get(SEL_COUNT));
			}
		}
        if(selection.length() > 0) selection.append("||");
        selection.append(FROM_SERVER_COLUMN);
        selection.append("!=2");
        String [] columns = new String[]{ID_COLUMN, NAME_COLUMN, COUNT_COLUMN, OWNER_COLUMN};
        return new CursorLoader(getActivity(), uri, columns, selection.toString(), null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

	//==============================================================================================
	//Activity communication========================================================================

    @Override
    public void onAttach(Activity activity) {
		super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	//==============================================================================================
	//Query callbacks===============================================================================

	@Override
	public void afterTextChanged(Editable s) {

		if(s.length() == 0) {
			getLoaderManager().restartLoader(0, null, this);
			return;
		}
		try {
			int count = Integer.parseInt(s.toString());
			Bundle args = new Bundle();
			args.putInt(SEL_COUNT, count);
			getLoaderManager().restartLoader(0, args, this);
		} catch (Exception e) {

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		//unused
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		//unused
	}

	/**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         * @param id
         */
        void onItemSelected(int id);
    }
}
