package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import de.lehrbaum.tworooms.R;

public class CreateSetActivity extends Activity implements ChooseSetRoleFragment.Callbacks, FinishSetFragment.OnFragmentInteractionListener{
    private static final String FRAGMENT_FINISH_TAG = "finish";

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

    @Override
    public void onListSubmitted(long[] entries) {
        mVariations.add(entries);//TODO: replace old entry and not increase if changed
        FragmentManager fm = getFragmentManager();
        if(fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            FinishSetFragment f = (FinishSetFragment) fm.findFragmentByTag(FRAGMENT_FINISH_TAG);
            f.increaseVariationsCount();
        } else {
            Fragment f = FinishSetFragment.newInstance(mVariations.size());
            getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, f, FRAGMENT_FINISH_TAG).commit();
        }
    }

    @Override
    public void onVariationClicked(int i) {
        Fragment f = ChooseSetRoleFragment.newInstance(mVariations.get(i));
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, f).commit();
    }

    @Override
    public void onCreateNewVariation() {
        Fragment f = ChooseSetRoleFragment.newInstance(null);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, f).commit();
    }

    @Override
    public void onFinishSetClick(String name, String description) {

    }
}