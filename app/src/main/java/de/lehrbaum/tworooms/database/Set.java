package de.lehrbaum.tworooms.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.List;

import static de.lehrbaum.tworooms.database.DatabaseContentProvider.Constants.*;

/**
 * This class represents a single set and is used when manipulating a set.
 * The name and description are often null, as it is only important which roles
 * are selected.
 */
public class Set {
    private static final Uri URI_TARGET = Uri.withAppendedPath(CONTENT_URI, SET_ROLES_TABLE);
    private static final String SEL_INDIZES = "SET_sI", BR_COUNT = "SET_br", RR_COUNT = "SET_rr",
            SET_NAME = "SET_n", SET_DESC = "SET_d";

    /**
     * These variables may be null
     */
    private String mName, mDescription;
    private int mCount, mBlueRoleCount, mRedRoleCount;
    private long [] mSelection;

    public Set() {
    }

    public Set(Set baseSet) {
        this.mCount = baseSet.mCount;
        this.mBlueRoleCount = baseSet.mBlueRoleCount;
        this.mRedRoleCount = baseSet.mRedRoleCount;
        this.mSelection = baseSet.mSelection;
    }

    public Set(long [] selection, int blueRoleCount, int redRoleCount) {
        this.mSelection = selection;
        this.mBlueRoleCount = blueRoleCount;
        this.mRedRoleCount = redRoleCount;
        if(mSelection == null)
            mSelection = new long [0];
        setCount(mSelection.length + mBlueRoleCount + mRedRoleCount);
    }

    public Set(Bundle bundle) {
        this(bundle.getLongArray(SEL_INDIZES), bundle.getInt(BR_COUNT, 0),
                bundle.getInt(RR_COUNT, 0));
        mName = bundle.getString(SET_NAME);
        mDescription = bundle.getString(SET_DESC);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getCount() {
        return mCount;
    }

    protected void setCount(int count) {
        this.mCount = count;
    }

    public int getBlueRoleCount() {
        return mBlueRoleCount;
    }

    public void setBlueRoleCount(int blueRoleCount) {
        this.mBlueRoleCount = blueRoleCount;
    }

    public int getRedRoleCount() {
        return mRedRoleCount;
    }

    public void setRedRoleCount(int redRoleCount) {
        this.mRedRoleCount = redRoleCount;
    }

    public long[] getSelection() {
        return mSelection;
    }

    public void setSelection(long[] selection) {
        this.mSelection = selection;
    }

    public void copySelection(Set baseSet) {
        this.mSelection = baseSet.mSelection;
        this.mRedRoleCount = baseSet.mRedRoleCount;
        this.mBlueRoleCount = baseSet.mBlueRoleCount;
        this.mCount = baseSet.mCount;
    }

    public void addToIntent(Intent intent, boolean selectionOnly) {
        if(!selectionOnly) {
            intent.putExtra(SET_NAME, mName);
            intent.putExtra(SET_DESC, mDescription);
        }
        intent.putExtra(SEL_INDIZES, mSelection);
        intent.putExtra(BR_COUNT, mBlueRoleCount);
        intent.putExtra(RR_COUNT, mRedRoleCount);
    }

    public void addToBundle(Bundle bundle, boolean selectionOnly) {
        if(!selectionOnly) {
            bundle.putString(SET_NAME, mName);
            bundle.putString(SET_DESC, mDescription);
        }
        bundle.putInt(BR_COUNT, mBlueRoleCount);
        bundle.putInt(RR_COUNT, mRedRoleCount);
        bundle.putLongArray(SEL_INDIZES, mSelection);
    }

    public long writeToDatabase(Context c, long parent,List<ContentValues> roleInserts) {
        ContentValues values = new ContentValues(5);
        values.put(NAME_COLUMN, mName);
        values.put(DESCRIPTION_COLUMN, mDescription);
        values.put(COUNT_COLUMN, mCount);
        if(parent != -1)
            values.put(PARENT_COLUMN, parent);
        values.put(OWNER_COLUMN, DatabaseContentProvider.getDeviceID());
        values.put(RED_ROLES_COLUMN, mRedRoleCount);
        values.put(BLUE_ROLES_COLUMN, mBlueRoleCount);
        Uri parentUri = c.getContentResolver().insert(URI_TARGET, values);
        long current = Long.parseLong(parentUri.getLastPathSegment());
        for(long l : mSelection) {
            values = new ContentValues(2);
            values.put(ID_SET_COLUMN, current);
            values.put(ID_ROLE_COLUMN, l);
            roleInserts.add(values);
        }
        return current;
    }
}
