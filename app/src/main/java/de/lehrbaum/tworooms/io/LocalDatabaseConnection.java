package de.lehrbaum.tworooms.io;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Sebastian on 23.05.2015.
 */
public class LocalDatabaseConnection extends SQLiteAssetHelper{

    private static final String DATABASE_NAME = "tworooms.db";
    private static final int DATABASE_VERSION = 1;

    public LocalDatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }
}
