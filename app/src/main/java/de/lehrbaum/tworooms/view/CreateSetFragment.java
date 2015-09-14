package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.lehrbaum.tworooms.R;


public class CreateSetFragment extends ListFragment{
    private static final String TAG = CreateSetFragment.class.getSimpleName();
    public static final String VARIATIONS_COUNT = "var_count";

    private OnFragmentInteractionListener mListener;

    private List<long []> variations;

    private long [] setRoles;

    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        variations = new ArrayList<long[]>();
        setRoles = new long [] {7, 8};

        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                new String []{});
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_set, container, false);
        Button changeSet = (Button) view.findViewById(R.id.button_change_set);
        changeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeSetClicked();
            }
        });
        Button newVariation = (Button) view.findViewById(R.id.button_new_variation);
        newVariation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddVariationClicked();
            }
        });
        Button save = (Button) view.findViewById(R.id.button_save_set);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveSetClicked();
            }
        });
        return view;
    }



    public void addVariation(long [] variation) {
        variations.add(variation);
        mAdapter.add(getActivity().getString(R.string.option_set_variation) + " " + variations.size());
    }

    public void setRoles(int id, long [] selections) {
        Log.d(TAG, "Set roles called");
        if(id == -1) {
            setRoles = selections;
        } else {
            variations.set(id, selections);
        }
    }

    protected void onChangeSetClicked() {
        mListener.onChangeRoles(-1, setRoles);
    }

    protected void onAddVariationClicked() {
        mListener.onCreateNewVariation(setRoles);
    }

    protected void onSaveSetClicked() {
        TextView name = (TextView) getView().findViewById(R.id.name_text);
        TextView desc = (TextView) getView().findViewById(R.id.desc_text);
        mListener.onFinishSetClick(name.getText().toString(),
                desc.getText().toString(),
                setRoles,
                variations.toArray(new long [variations.size()][]));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onChangeRoles(position, variations.get(position));
    }
	
    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onChangeRoles(int id, long [] selections);

        /**
         * This is called to select the roles of a new variation.
         * @param setRoles The roles of the standard set. They can be preselected for the user.
         */
        public void onCreateNewVariation(long [] setRoles);

        public void onFinishSetClick(String name, String description, long [] setRoles, long [][] variations);
    }

}
