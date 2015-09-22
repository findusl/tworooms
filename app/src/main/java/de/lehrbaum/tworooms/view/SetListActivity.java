package de.lehrbaum.tworooms.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.Authenticator;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;
import de.lehrbaum.tworooms.io.SyncAdapter;


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
public class SetListActivity extends Activity
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
        final String account = "TwoRoomsSync";
        final String account_type = Authenticator.ACCOUNT_TYPE;
        final String authority = "de.lehrbaum.tworooms.database";

        final Account newAccount = new Account(
                account, account_type);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) getSystemService(
                        ACCOUNT_SERVICE);
        accountManager.addAccountExplicitly(newAccount, null, null);

        ContentResolver.addPeriodicSync(newAccount, authority,
                Bundle.EMPTY, /* 5 hours interval */5 * 60 * 60);

        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, "votes");

        Runnable onChange = new Runnable() {
            @Override
            public void run() {
                ContentResolver.requestSync(newAccount, authority, Bundle.EMPTY);
            }
        };
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(uri, true, new DatabaseContentProvider.TableObserver(onChange));
    }

    /**
     * Callback method from {@link SetListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     * @param id
     */
    @Override
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
