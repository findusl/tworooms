package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.lehrbaum.tworooms.R;

public class CreateSetActivity extends Activity implements ChooseSetRoleFragment.Callbacks, FinishSetFragment.OnFragmentInteractionListener{

    private List<long[]> mVariations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_set);
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
        mVariations.add(entries);
        //TODO: future reuse fragment
        Fragment f = FinishSetFragment.newInstance(mVariations.size());
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, f).commit();
    }

    @Override
    public void onVariationClicked(int i) {
        Fragment f = ChooseSetRoleFragment.newInstance(mVariations.get(i));
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, f).commit();
    }

    @Override
    public void onCreateNewVariation() {
        Fragment f = ChooseSetRoleFragment.newInstance(null);
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, f).commit();
    }

    @Override
    public void onFinishSetClick(String name, String description) {

    }
}