package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.Arrays;

import de.lehrbaum.tworooms.R;


public class ChooseRoleActivity extends Activity {

    private static final String TAG = ChooseRoleActivity.class.getSimpleName();

	private ChooseRoleFragment mFragment;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
			long [] selIndizes = getIntent().getLongArrayExtra(ChooseRoleFragment.SELECTION_INDIZES);
			arguments.putLongArray(ChooseRoleFragment.SELECTION_INDIZES, selIndizes);
            mFragment = new ChooseRoleFragment();
            mFragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, CreateSetActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onStop()
	{
		super.onStop();
        Log.d(TAG, "On Stop passing Intent " + Arrays.toString(mFragment.getSelection()));
		Intent intent = new Intent();
		intent.putExtra(ChooseRoleFragment.SELECTION_INDIZES, mFragment.getSelection());
		setResult(RESULT_OK, intent);
	}
	
	
}
