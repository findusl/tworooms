package de.lehrbaum.tworooms.io;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * This class uses the @link{SQLiteAssetHelper} class to read a database file into the database.
 */
public class LocalDatabaseConnection extends SQLiteAssetHelper{

    private static final String DATABASE_NAME = "tworooms.db";
    public static final int DATABASE_VERSION = 2;

    public LocalDatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.onUpgrade(db, oldVersion, newVersion);
	}
}