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
import org.xml.sax.helpers.*;
import android.view.ContextMenu.*;
import android.view.*;
import android.widget.AdapterView.*;
import android.app.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.text.*;
import android.content.*;


public class CreateSetFragment extends ListFragment{
    private static final String TAG = CreateSetFragment.class.getSimpleName();
    public static final String VARIATIONS_COUNT = "var_count";

    private OnFragmentInteractionListener mListener;
	
	//TODO: rename all variables with m at start
	private TextView nameView;

    private List<long []> variations;
	/** Need extra counter in case of deleting variations. */
	private int autoIncrementVariation = 0;
	private List<String> variationNames;

    private long [] setRoles;

    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        variations = new ArrayList<long[]>();
		variationNames = new ArrayList<String>();
        setRoles = new long [] {7, 8};

        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                new ArrayList<String>());
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_set, container, false);
        nameView = (TextView) view.findViewById(R.id.name_text);
		
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
				variations.remove(menuInfo.position);
				variationNames.remove(menuInfo.position);
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
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
    		@Override
    		public void onClick(DialogInterface dialog, int which) {
        		variationNames.set(position, input.getText().toString());
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

    public void addVariation(long [] variation) {
        variations.add(variation);
		String name = nameView.getText().toString();
		if(name == null || name.length() == 0)
			name = getActivity().getString(R.string.option_set_variation);
		name +=  " " + autoIncrementVariation++;
		variationNames.add(name);
        mAdapter.add(name);
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
		mListener.removeOldVariation();
		if(variations.size() != 0)
			Toast.makeText(getActivity(), R.string.warning_change_set, Toast.LENGTH_SHORT).show();
        mListener.onChangeRoles(-1, setRoles);
    }

    protected void onAddVariationClicked() {
		mListener.removeOldVariation();
        mListener.onCreateNewVariation(setRoles);
    }

    protected void onSaveSetClicked() {
		mListener.removeOldVariation();
        TextView desc = (TextView) getView().findViewById(R.id.desc_text);
        mListener.onFinishSetClick(nameView.getText().toString(),
                desc.getText().toString(),
                setRoles,
				variationNames.toArray(new String[variationNames.size()]),
                variations.toArray(new long [variations.size()][]));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		mListener.removeOldVariation();
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
		
		/**
		 * Removes any old pending choose role operations in two pane mode.
		 */
		public void removeOldVariation();

        public void onFinishSetClick(String name, String description, long [] setRoles,
							String [] variationNames, long [][] variations);
    }

}
