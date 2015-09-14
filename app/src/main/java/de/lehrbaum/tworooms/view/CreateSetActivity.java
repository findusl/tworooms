package de.lehrbaum.tworooms.view;
import android.app.*;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;
import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.*;

public class CreateSetActivity extends Activity implements CreateSetFragment.OnFragmentInteractionListener
{
    private static final String TAG = CreateSetActivity.class.getSimpleName();
    public static final String RESULT_SET_ID = "resultSetId";
	/**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private CreateSetFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "on create");
        setContentView(R.layout.activity_create_set);

        mFragment = ((CreateSetFragment) getFragmentManager()
                .findFragmentById(R.id.create_set_fragment));

        if (findViewById(R.id.choose_role_container) != null) {
            mTwoPane = true;
            mFragment.setActivateOnItemClick(true);
        }
    }

    private static final String SELECTION_ID = "sel_id";
    private static final String CHOOSE_ROLE_TAG = "crt";

    @Override
    public void onChangeRoles(int id, long[] selections) {
        if (mTwoPane) {
            //first retrieve the old fragment
            Fragment old = getFragmentManager().findFragmentByTag(CHOOSE_ROLE_TAG);
            if(old != null && old instanceof ChooseRoleFragment) {
                ChooseRoleFragment fragment = (ChooseRoleFragment) old;
                Bundle arguments = old.getArguments();
                int old_id = arguments.getInt(SELECTION_ID);
                long [] old_selection = fragment.getSelection();
                if(old_id == -2)
                    mFragment.addVariation(old_selection);
                else
                    mFragment.setRoles(old_id, old_selection);
            }
            //put new fragment
            Bundle arguments = new Bundle();
            arguments.putLongArray(ChooseRoleFragment.SELECTION_INDIZES, selections);
            arguments.putInt(SELECTION_ID, id);
            Fragment fragment = new ChooseRoleFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.choose_role_container, fragment, CHOOSE_ROLE_TAG)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ChooseRoleActivity.class);
            detailIntent.putExtra(ChooseRoleFragment.SELECTION_INDIZES, selections);
            startActivityForResult(detailIntent, id);
        }
    }

    @Override
    public void onCreateNewVariation(long[] setRoles) {
		//not so nice. The ids should be fully managed 
		//by the fragment or not at all. But will have to change anyway
		//when name and description for variations become possible so i
		//leave it for now
        onChangeRoles(-2, setRoles);
    }

    @Override
    protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Log.d(TAG, "Result = ok");
            long [] selection = data.getLongArrayExtra(ChooseRoleFragment.SELECTION_INDIZES);
            if(requestCode == -2)
                mFragment.addVariation(selection);
            else
                mFragment.setRoles(requestCode, selection);
        }
    }

    @Override
    public void onFinishSetClick(String name, String description, long[] setRoles, long[][] variations) {
        Log.d(TAG, "Finish Set clicked");
        if(setRoles == null || setRoles.length == 0) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, SETS_TABLE);
        ArrayList<ContentValues> roleInserts = new ArrayList<ContentValues>((setRoles.length)*(variations.length + 1));
        ContentValues values = new ContentValues(3);
        values.put(NAME_COLUMN, name);
        values.put(DESCRIPTION_COLUMN, description);
        values.put(COUNT_COLUMN, setRoles.length);
        Uri parentUri = getContentResolver().insert(uri, values);
        long parent = Long.parseLong(parentUri.getLastPathSegment());
        Log.d(TAG, "Parent: " + parent);
        for(long l : setRoles) {
            values = new ContentValues(2);
            values.put(ID_SET_COLUMN, parent);
            values.put(ID_ROLE_COLUMN, l);
            roleInserts.add(values);
        }
        for(long [] variation : variations) {
            values = new ContentValues(4);
            values.put(NAME_COLUMN, name);
            values.put(DESCRIPTION_COLUMN, description);
            values.put(COUNT_COLUMN, setRoles.length);
            values.put(PARENT_COLUMN, parent);
            Uri currentUri = getContentResolver().insert(uri, values);
            long current = Long.parseLong(currentUri.getLastPathSegment());
            for(long l : variation) {
                values = new ContentValues(2);
                values.put(ID_SET_COLUMN, current);
                values.put(ID_ROLE_COLUMN, l);
                roleInserts.add(values);
            }
        }
        uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, SET_ROLES_TABLE);
        ContentValues [] roleInsertsArray = roleInserts.toArray(new ContentValues[roleInserts.size()]);
        getContentResolver().bulkInsert(uri, roleInsertsArray);
        Intent result = new Intent();
        result.putExtra(RESULT_SET_ID, parent);
        setResult(RESULT_OK, result);
        finish();
    }
}
