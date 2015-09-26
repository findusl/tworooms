package de.lehrbaum.tworooms.view.util;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.SparseBooleanArray;

import java.util.Arrays;

/**
 * Created by Sebastian on 26.09.2015.
 */
public class SortedCursor extends CursorWrapper {

	protected int [] mPositionMapping;
	protected Cursor mCursor;

	/**
	 * Creates a new sorted cursor that sorts specific elements to the top of the cursor.
	 *
	 * @param cursor The underlying cursor to wrap.
	 */
	public SortedCursor(Cursor cursor) {
		super(cursor);
		mCursor = cursor;
	}

	public int setSelected(SparseBooleanArray selections) {
		int size = mCursor.getCount();
		mPositionMapping = new int [size];
		int [] tempMapping = new int [size];
		int checkedMappings = 0;
		int unCheckedMappings = 0;
		mCursor.moveToFirst();
		for (int i = 0; i < size; mCursor.moveToNext()) {
			int id = (int) mCursor.getLong(0);
			if (selections.get(id, false))
				mPositionMapping[checkedMappings++] = i++;
			else {
				tempMapping[unCheckedMappings++] = i++;
			}
		}
		System.arraycopy(tempMapping, 0, mPositionMapping, checkedMappings, unCheckedMappings);
		mCursor.moveToFirst();
		return checkedMappings;
	}

	@Override
	public boolean moveToPosition(int position) {
		if(position >= getCount())
			return false;
		return super.moveToPosition(mPositionMapping[position]);
	}

	@Override
	public int getPosition() {
		int actualPos = super.getPosition();
		for(int i = 0; i < mPositionMapping.length; i++) {
			if(mPositionMapping[i] == actualPos)
				return i;
		}
		throw new UnsupportedOperationException("Cannot find current position in position mapping");
	}
}
