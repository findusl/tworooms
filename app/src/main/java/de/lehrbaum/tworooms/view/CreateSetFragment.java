package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.lehrbaum.tworooms.R;

import android.view.*;
import android.widget.AdapterView.*;
import android.app.*;
import android.widget.*;
import android.text.*;
import android.content.*;


public class CreateSetFragment extends ListFragment{
    private static final String TAG = CreateSetFragment.class.getSimpleName();
	private static final int REQUEST_SET = Integer.MAX_VALUE - 1;
	private static final int REQUEST_NEW_VARIATION = Integer.MAX_VALUE - 2;

    public static final String VARIATIONS_COUNT = "var_count";

    private OnFragmentInteractionListener mListener;

	private TextView mNameView;

    private List<long []> mVariations;
	/** Need extra counter in case of deleting variations. */
	private int mAutoIncrementVariation = 0;
	private List<String> mVariationNames;

    private long [] mSetRoles;

    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVariations = new ArrayList<>();
		mVariationNames = new ArrayList<>();
        mSetRoles = new long [] {7, 8};

        mAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                new ArrayList<String>());
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_set, container, false);
        mNameView = (TextView) view.findViewById(R.id.name_text);
		
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

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		registerForContextMenu(getListView());
	}
	
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		if(v.getId() == android.R.id.list) {
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.context_menu_variation, menu);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		if(item.getMenuInfo() instanceof AdapterContextMenuInfo) {
			AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		
			switch(item.getItemId()) {
			case R.id.action_delete:
				mAdapter.remove(mAdapter.getItem(menuInfo.position));
				mVariations.remove(menuInfo.position);
				mVariationNames.remove(menuInfo.position);
				return true;
			case R.id.action_rename:
				changeName(menuInfo.position);
				return true;
			}
		}
		return super.onContextItemSelected(item);
	}
	
	private void changeName(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.title_dialog_change_name);

		// Set up the input
		final EditText input = new EditText(getActivity());
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE |
                InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		input.setText(mVariationNames.get(position));
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
				String name = input.getText().toString();
				if(name.length() == 0)
					return;
				mAdapter.remove(mAdapter.getItem(position));
				mAdapter.insert(name, position);
        		mVariationNames.set(position, name);
    		}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
	
		builder.show();
	}

    private void addVariation(long [] variation) {
        mVariations.add(variation);
		String name = mNameView.getText().toString();
		if(name.length() == 0)
			name = getActivity().getString(R.string.option_set_variation);
		name +=  " " + mAutoIncrementVariation++;
		mVariationNames.add(name);
        mAdapter.add(name);
    }

    public void setRoles(int id, long [] selections) {
        Log.v(TAG, "Set roles called");
		switch (id) {
			case REQUEST_SET:
				mSetRoles = selections;
				break;
			case REQUEST_NEW_VARIATION:
				addVariation(selections);
				break;
			default:
				mVariations.set(id, selections);
		}
    }

    protected void onChangeSetClicked() {
		mListener.removeOldVariation();
		if(mVariations.size() != 0)
			Toast.makeText(getActivity(), R.string.warning_change_set, Toast.LENGTH_SHORT).show();
        mListener.onChangeRoles(REQUEST_SET, mSetRoles);
    }

    protected void onAddVariationClicked() {
		mListener.removeOldVariation();
        mListener.onChangeRoles(REQUEST_NEW_VARIATION, mSetRoles);
    }

    protected void onSaveSetClicked() {
		mListener.removeOldVariation();
        TextView desc = (TextView) getView().findViewById(R.id.desc_text);
        mListener.onFinishSetClick(mNameView.getText().toString(),
                desc.getText().toString(),
                mSetRoles,
                mVariationNames.toArray(new String[mVariationNames.size()]),
                mVariations.toArray(new long[mVariations.size()][]));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		mListener.removeOldVariation();
        mListener.onChangeRoles(position, mVariations.get(position));
    }
	
    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick() {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
        void onChangeRoles(int id, long [] selections);
		
		/**
		 * Removes any old pending choose role operations in two pane mode.
		 */
		void removeOldVariation();

        void onFinishSetClick(String name, String description, long [] setRoles,
							String [] variationNames, long [][] variations);
    }

}
