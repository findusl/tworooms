package de.lehrbaum.tworooms.view;
import android.app.*;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;
import de.lehrbaum.tworooms.io.Set;
import de.lehrbaum.tworooms.view.util.BaseActivity;

import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.*;
import android.widget.*;

public final class CreateSetActivity extends BaseActivity implements CreateSetFragment.OnFragmentInteractionListener
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
        setContentView(R.layout.activity_create_set);

        mFragment = ((CreateSetFragment) getFragmentManager()
                .findFragmentById(R.id.create_set_fragment));

        if (findViewById(R.id.choose_role_container) != null) {
            mTwoPane = true;
            mFragment.setActivateOnItemClick();
        }
    }

    private static final String SELECTION_ID = "sel_id";
    private static final String CHOOSE_ROLE_TAG = "crt";

    /**
     * In Two pane mode this saves the data of open choose role fragments. Removes the fragment if
     * saving the data makes it invalid. For example if it created a new variation
     */
	@Override
	public void saveOpenVariation()
	{
		if(mTwoPane) {
            //first retrieve the open fragment
            Fragment open = getFragmentManager().findFragmentByTag(CHOOSE_ROLE_TAG);
            if(open != null && open instanceof ChooseRoleFragment) {
                ChooseRoleFragment fragment = (ChooseRoleFragment) open;
                Set set = fragment.getSet();
                if(set == null)
                    return;//no change
                Bundle arguments = open.getArguments();
                int selId = arguments.getInt(SELECTION_ID);
				mFragment.setRoles(selId, set);
            }
		}
	}

    @Override
    public void onChangeRoles(int id, Set set) {
        if (mTwoPane) {
            //put new fragment
            Bundle arguments = new Bundle();
            set.addToBundle(arguments, true);
            Fragment fragment = new ChooseRoleFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.choose_role_container, fragment, CHOOSE_ROLE_TAG)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ChooseRoleActivity.class);
            set.addToIntent(detailIntent, true);
            startActivityForResult(detailIntent, id);
        }
    }

    @Override
    protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            Set set = new Set(data.getExtras());
			mFragment.setRoles(requestCode, set);
        }
    }

    @Override
    public void onFinishSet(Set set, Set[] variations) {
        Log.v(TAG, "Finish Set clicked. roles");

        //insert parent set
        Uri uri = Uri.withAppendedPath(CONTENT_URI, SETS_TABLE);
        ArrayList<ContentValues> roleInserts =
                new ArrayList<>((set.getCount())*(variations.length + 1));
        long parent = set.writeToDatabase(this, -1, roleInserts);

        //insert variations:
        for(Set variation : variations) {
            variation.writeToDatabase(this, parent, roleInserts);
        }

        //do bulk role insert:
        uri = Uri.withAppendedPath(CONTENT_URI, SET_ROLES_TABLE);
        ContentValues [] roleInsertsArray = roleInserts.toArray(new ContentValues[roleInserts.size()]);
        getContentResolver().bulkInsert(uri, roleInsertsArray);
        Intent result = new Intent();
        result.putExtra(RESULT_SET_ID, parent);
        setResult(RESULT_OK, result);
        finish();
    }
}
