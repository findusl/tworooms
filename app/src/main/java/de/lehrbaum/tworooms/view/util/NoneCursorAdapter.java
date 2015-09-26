package de.lehrbaum.tworooms.view.util;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.lehrbaum.tworooms.R;

/**
 * Created by Sebastian on 26.09.2015.
 */
public class NoneCursorAdapter extends SimpleCursorAdapter {
	private final String mNoFilterString;
	private final Context mContext;
	private final Cursor mCursor;
	/**
	 * Standard constructor.
	 *
	 * @param context The context where the ListView associated with this
	 *                SimpleListItemFactory is running
	 * @param layout  resource identifier of a layout file that defines the views
	 *                for this list item. The layout file should include at least
	 *                those named views defined in "to"
	 * @param c       The database cursor.  Can be null if the cursor is not available yet.
	 * @param from    A list of column names representing the data to bind to the UI.  Can be null
	 *                if the cursor is not available yet.
	 * @param to      The views that should display column in the "from" parameter.
	 *                These should all be TextViews. The first N views in this list
	 *                are given the values of the first N columns in the from
	 *                parameter.  Can be null if the cursor is not available yet.
	 * @param flags   Flags used to determine the behavior of the adapter,
	 *                as per {@link CursorAdapter#CursorAdapter(Context, Cursor, int)}.
	 */
	public NoneCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mNoFilterString = context.getString(R.string.no_filter);
		mContext = context;
		mCursor = c;
	}

	@Override
	public int getCount() {
		return super.getCount() + 1;
	}

	@Override
	public long getItemId(int position) {
		if(position == 0)
			return -1;
		return super.getItemId(position-1);
	}

	@Override
	public Object getItem(int position) {
		if(position == 0)
			return mNoFilterString;
		return super.getItem(position-1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position == 0) {
			View v;
			if (convertView == null) {
				v = newView(mContext, mCursor, parent);
			} else {
				v = convertView;
			}
			TextView tv = (TextView) v.findViewById(android.R.id.text1);
			tv.setText(mNoFilterString);
			return v;
		}
		return super.getView(position-1, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if(position == 0)
			return getView(position, convertView, parent);
		return super.getDropDownView(position-1, convertView, parent);
	}
}
