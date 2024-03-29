package de.lehrbaum.tworooms.io;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * This class uses the @link{SQLiteAssetHelper} class to read a database file into the database.
 */
public class LocalDatabaseConnection extends SQLiteAssetHelper{

    private static final String DATABASE_NAME = "tworooms.db";
    public static final int DATABASE_VERSION = 3;//Version for second play store release
    public static final long DATABASE_SINCE = 1444939978L;

    public LocalDatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly())
            db.setForeignKeyConstraintsEnabled(true);
    }
}