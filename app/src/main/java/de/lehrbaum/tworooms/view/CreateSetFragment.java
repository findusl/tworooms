package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import de.lehrbaum.tworooms.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateSetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreateSetFragment extends ListFragment {
    public static final String VARIATIONS_COUNT = "var_count";

    private OnFragmentInteractionListener mListener;

    private int variationsCount = 0;

    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String [] options = new String [2];//at the start there are no variations
        options[0] = getActivity().getString(R.string.option_create_new_variation);
        options[1] = getActivity().getString(R.string.option_finish_set);

        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, options);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_set, container, false);
    }

    public void increaseVariationsCount() {
        mAdapter.insert(getActivity().getString(R.string.option_set_variation) + " " + variationsCount, variationsCount);
        variationsCount++;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(position < variationsCount) {
            mListener.onVariationClicked(position);
        } else {
            switch (position-variationsCount) {
                case 0:
                    mListener.onCreateNewVariation();
                    break;
                case 1:
                    TextView name = (TextView) getView().findViewById(R.id.editText);
                    mListener.onFinishSetClick(name.getText().toString(), "");
                    break;
            }
        }
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
        public void onVariationClicked(int i);

        public void onCreateNewVariation();

        public void onFinishSetClick(String name, String description);
    }

}
