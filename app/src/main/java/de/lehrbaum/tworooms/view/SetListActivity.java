package de.lehrbaum.tworooms.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.database.SyncAdapter;
import de.lehrbaum.tworooms.view.util.BaseActivity;

import static de.lehrbaum.tworooms.database.DatabaseContentProvider.Constants.*;


/**
 * An activity representing a list of sets. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SetDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SetListFragment} and the item details
 * (if present) is a {@link SetDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link SetListFragment.Callbacks} interface
 * to listen for item selections.
 */
public final class SetListActivity extends BaseActivity
        implements SetListFragment.Callbacks {
    private static final String TAG = SetListActivity.class.getSimpleName();

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
                    .setActivateOnItemClick();
        }
        setSyncUp();
    }

    private void setSyncUp() {
        final String account = getString(R.string.account_name);
        final String account_type = getString(R.string.account_type);
        final String authority = getString(R.string.account_authority);

        final Account accountInstance = new Account(
                account, account_type);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) getSystemService(
                        ACCOUNT_SERVICE);
        final boolean firstTime = accountManager.addAccountExplicitly(accountInstance, null, null);

        if(firstTime) {
            //set sync enabled by default
            ContentResolver.setSyncAutomatically(accountInstance, authority, true);
            ContentResolver.addPeriodicSync(accountInstance, authority,
                    Bundle.EMPTY, /* 5 hours interval */5 * 60 * 60);
            //request a sync
            final Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            ContentResolver.requestSync(accountInstance, authority, settingsBundle);
        }

        SyncAdapter.SYNC_OBSERVER = new ContentObserver(new Handler(getMainLooper())) {
            @Override
            public boolean deliverSelfNotifications() {
                return false;
            }

            @Override
            public void onChange(boolean selfChange) {
                Log.i(TAG, "On change for sync called. self change: " + selfChange);
                ContentResolver.requestSync(accountInstance, authority, Bundle.EMPTY);
            }
        };
        final ContentResolver resolver = getContentResolver();
        Uri uri = Uri.withAppendedPath(CONTENT_URI, VOTES_TABLE);
        resolver.registerContentObserver(uri, true, SyncAdapter.SYNC_OBSERVER);
        uri = Uri.withAppendedPath(CONTENT_URI, SETS_TABLE);
        resolver.registerContentObserver(uri, true, SyncAdapter.SYNC_OBSERVER);
    }

    @Override
    public void onItemSelected(final int id) {
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
