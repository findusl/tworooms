package de.lehrbaum.tworooms.view.util;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.SparseBooleanArray;

import java.util.Arrays;

/**
 * This class sorts a cursor in a way that specific elements are at the front and the others are
 * behind them. The internal sort order does not get changed.
 *
 * @author Sebastian Lehrbaum
 */
public final class SortedCursor extends CursorWrapper {

	protected int [] mPositionMapping;
	protected Cursor mCursor;

	/**
	 * Creates a new sorted cursor with no changes to the cursor so far.
     *
     * Important: You have to call @link{#setSelected} before using this cursor!
     * Otherwise an exception will be thrown.
	 *
	 * @param cursor The underlying cursor to wrap.
	 */
	public SortedCursor(Cursor cursor) {
		super(cursor);
		mCursor = cursor;
	}

    /**
     * Call this to change the elements that should be sorted to the front of the cursor.
     *
     * @param selections The elements that should be sorted to the front of the cursor.
     * @return The number of elements of <code>selections</code> that have been found in the cursor
     *          and sorted to the front.
     */
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
