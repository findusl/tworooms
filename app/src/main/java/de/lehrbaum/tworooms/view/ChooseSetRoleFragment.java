package de.lehrbaum.tworooms.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.lehrbaum.tworooms.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseSetRoleFragment extends Fragment {

    public ChooseSetRoleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_set_roles, container, false);
    }
}
