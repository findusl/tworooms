package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.io.DatabaseContentProvider;

public class CreateSetActivityOld extends Activity
        implements ChooseSetRoleFragment.Callbacks, FinishSetFragment.OnFragmentInteractionListener{
    private static final String TAG = ChooseSetRoleFragment.class.getSimpleName();
    private static final String FRAGMENT_FINISH_TAG = "finish";

    //TODO use one variable for standard selection
    private List<long[]> mVariations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_set);
        Fragment f = new ChooseSetRoleFragment();
        getFragmentManager().beginTransaction().add(R.id.fragmentContainer, f).commit();
        mVariations = new ArrayList<long []>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private FinishSetFragment mFinishFragment;

    @Override
    public void onListSubmitted(int position, long[] entries) {
        //show finish fragment
        //TODO use two activities instead of one. Use FLAG_ACTIVITY_CLEAR_TOP for return
        FragmentManager fm = getFragmentManager();
        if(mFinishFragment == null) {
            mFinishFragment = new FinishSetFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, mFinishFragment, FRAGMENT_FINISH_TAG).commit();
        } else {//TODO use intents
            fm.popBackStack(); //return to the existing finish fragment.
        }

        //add variation to list
        if(position == -1) {
            mVariations.add(entries);
            mFinishFragment.increaseVariationsCount();
        } else {
            mVariations.set(position, entries);
        }
    }

    @Override
    public void onVariationClicked(int i) {
        Fragment f = ChooseSetRoleFragment.newInstance(i, mVariations.get(i));
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, f).commit();
    }

    @Override
    public void onCreateNewVariation() {
        Fragment f = ChooseSetRoleFragment.newInstance();
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, f).commit();
    }

    @Override
    public void onFinishSetClick(String name, String description) {
        Uri uri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, "roles");
        ContentValues values = new ContentValues(2);
        values.put("name", name);
        values.put("description", description);
        Uri result = getContentResolver().insert(uri, values);
        Log.i(TAG, "Insert result: " + result);

        for(long [] selection : mVariations) {
            values = new ContentValues(3);
            values.put("name", name);
            values.put("description", description);
            values.put("parent", result.toString());//TODO incorrect
            Uri childResult = getContentResolver().insert(uri, values);
            Log.i(TAG, "Insert child result: " + childResult);
            insertRoleSetReferences(0, selection);
        }

        Intent intent = new Intent(this, SetListActivity.class);
        startActivity(intent);
    }

    private void insertRoleSetReferences(long setId, long[] selection) {
        for(long l : selection) {
            Uri uri = Uri.withAppendedPath(DatabaseContentProvider.CONTENT_URI, "set_roles");
            ContentValues values = new ContentValues(1);
            values.put("set_id", setId);
            values.put("role_id", l);
            getContentResolver().insert(uri, values);
        }
    }
}
