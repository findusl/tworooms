package de.lehrbaum.tworooms.view.createSet;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.database.Set;
import de.lehrbaum.tworooms.view.util.BaseActivity;


public final class ChooseRoleActivity extends BaseActivity {

    private static final String TAG = ChooseRoleActivity.class.getSimpleName();

	private ChooseRoleFragment mFragment;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        // Show the Up button in the action bar.
        ActionBar aBar = getActionBar();
        if(aBar != null)
            aBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            mFragment = new ChooseRoleFragment();
            mFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
			setResult();
//          Intent intent = new Intent(this, CreateSetActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onBackPressed() {
		setResult();
		super.onBackPressed();
	}

	private void setResult() {
		//standard result is canceled
		if(mFragment == null)
			return;
		Set set = mFragment.getSet();
		if(set != null) {
			Intent intent = new Intent();
            set.addToIntent(intent, true);
			setResult(RESULT_OK, intent);
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	
}
