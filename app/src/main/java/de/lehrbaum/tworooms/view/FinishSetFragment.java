package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
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
 * {@link FinishSetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinishSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishSetFragment extends ListFragment {
    public static final String VARIATIONS_COUNT = "var_count";

    private OnFragmentInteractionListener mListener;

    private int variationsCount;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param variationsCount
     * @return A new instance of fragment FinishSetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinishSetFragment newInstance(int variationsCount) {
        FinishSetFragment fragment = new FinishSetFragment();
        Bundle args = new Bundle();
        args.putInt(VARIATIONS_COUNT, variationsCount);
        fragment.setArguments(args);
        return fragment;
    }

    public FinishSetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            variationsCount = getArguments().getInt(VARIATIONS_COUNT);
        }

        String [] options = new String [variationsCount+2];
        for(int i = 0; i < variationsCount; i++) {
            options[i] = getActivity().getString(R.string.option_set_variations) + ' ' + i;
        }
        options[variationsCount] = getActivity().getString(R.string.option_create_new_variation);
        options[variationsCount+1] = getActivity().getString(R.string.option_finish_set);

        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, options));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_set, container, false);
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
