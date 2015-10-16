package de.lehrbaum.tworooms.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import de.lehrbaum.tworooms.R;

/**
 * Created by Sebastian on 16.10.2015.
 */
public final class AppPreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
