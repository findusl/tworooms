package de.lehrbaum.tworooms.view;

import android.animation.StateListAnimator;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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


public class CreateSetFragment extends ListFragment {
    private static final String TAG = CreateSetFragment.class.getSimpleName();
	private static final int REQUEST_SET = Integer.MAX_VALUE - 1;
	private static final int REQUEST_NEW_VARIATION = Integer.MAX_VALUE - 2;

    public static final String VARIATIONS_COUNT = "var_count";

    private OnFragmentInteractionListener mListener;

	private TextView mNameView;
    private Button mChangeSetButton;

    private List<long []> mVariations;
	/** Need extra counter in case of deleting variations. */
	private int mAutoIncrementVariation = 0;
	private List<String> mVariationNames;

    private long [] mSetRoles;

    private VariationsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVariations = new ArrayList<>();
		mVariationNames = new ArrayList<>();
        mSetRoles = new long [] {7, 8};

        mAdapter = new VariationsAdapter();
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_set, container, false);
        return view;
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

        mNameView = (TextView) view.findViewById(R.id.name_text);
        mChangeSetButton = (Button) view.findViewById(R.id.button_change_set);
        mChangeSetButton.setOnClickListener(new View.OnClickListener() {
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
		registerForContextMenu(getListView());
	}

    //==============================================================================================
    //Context menu==================================================================================

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
				VariationsEntry entry = mAdapter.getItem(position);
				mAdapter.remove(entry);
				entry.name = name;
				mAdapter.insert(entry, position);
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

    //==============================================================================================
    //User interaction==============================================================================

    protected void onChangeSetClicked() {
		mListener.saveOpenVariation();
		if(mVariations.size() != 0)
			Toast.makeText(getActivity(), R.string.warning_change_set, Toast.LENGTH_SHORT).show();
        mListener.onChangeRoles(REQUEST_SET, mSetRoles);
        getListView().setItemChecked(-1, true);
        new Handler(getActivity().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mChangeSetButton.setPressed(true);
            }
        });
    }

    protected void onAddVariationClicked() {
        int pos = addVariation(mSetRoles);
    }

    protected void onSaveSetClicked() {
		mListener.saveOpenVariation();
        TextView desc = (TextView) getView().findViewById(R.id.desc_text);
        mListener.onFinishSetClick(mNameView.getText().toString(),
                desc.getText().toString(),
                mSetRoles,
                mVariationNames.toArray(new String[mVariationNames.size()]),
                mVariations.toArray(new long[mVariations.size()][]));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		mListener.saveOpenVariation();
        mListener.onChangeRoles(position, mVariations.get(position));
        mChangeSetButton.setPressed(false);
    }

    //==============================================================================================
    //Activity interaction==========================================================================

    private int addVariation(long [] variation) {
        mVariations.add(variation);
        String name = mNameView.getText().toString();
        if(name.length() == 0)
            name = getActivity().getString(R.string.default_new_variation);
        //increment before because starting to count at 1 is more intuitive
        name +=  " v" + (++mAutoIncrementVariation+1);
        mVariationNames.add(name);
		VariationsEntry entry = new VariationsEntry();
		entry.name = name;
		entry.count = variation.length;
        mAdapter.add(entry);
        return mVariations.size()-1;
    }

    /**
     * Sets the roles selected by the user.
     *
     * @param id The id of the changed role set.
     * @param selections The roles selected by the user and (if existent) preselected roles that
     *                   the user did not deselect.
     * @return If <CODE>true</CODE> is returned the
     */
    public boolean setRoles(int id, long [] selections) {
        Log.v(TAG, "Set roles called");
        switch (id) {
            case REQUEST_SET:
                mSetRoles = selections;
                break;
            case REQUEST_NEW_VARIATION:
                addVariation(selections);
                return true;
            default:
                mVariations.set(id, selections);
        }
        return false;
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
		void saveOpenVariation();

        void onFinishSetClick(String name, String description, long [] setRoles,
							String [] variationNames, long [][] variations);
    }
	
	class VariationsEntry {
		String name;
		int count;
	}

	class VariationsAdapter extends ArrayAdapter<VariationsEntry> {
		LayoutInflater mInflater;
		VariationsAdapter() {
			super(getActivity(), R.layout.list_item_set,
				  new ArrayList<VariationsEntry>());
			mInflater = LayoutInflater.from(getActivity());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v;
			if (convertView == null) {
				v = mInflater.inflate(R.layout.list_item_set, parent, false);
			} else {
				v = convertView;
			}
			VariationsEntry entry = getItem(position);
			TextView mainText = (TextView) v.findViewById(android.R.id.text1);
			TextView counterText = (TextView) v.findViewById(R.id.counterView);
			mainText.setText(entry.name);
			counterText.setText(entry.count);
			return v;
		}
	}
}
