package de.lehrbaum.tworooms.view;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import de.lehrbaum.tworooms.R;

public class CreateSetActivity extends Activity
{
    private static final String TAG = CreateSetActivity.class.getSimpleName();
	/**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "on create");
        setContentView(R.layout.activity_set_list);

        if (findViewById(R.id.set_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((SetListFragment) getFragmentManager()
				.findFragmentById(R.id.set_list))
				.setActivateOnItemClick(true);
        }
    }

    public void onItemSelected(int id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(SetDetailFragment.ARG_SET_ID, id);
            Fragment fragment = new SetDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
				.replace(R.id.set_detail_container, fragment)
				.commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, SetDetailActivity.class);
            detailIntent.putExtra(SetDetailFragment.ARG_SET_ID, id);
            startActivity(detailIntent);
        }
    }
}
