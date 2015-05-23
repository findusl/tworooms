package de.lehrbaum.tworooms.io;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseContentProvder extends ContentProvider {

    protected LocalDatabaseConnection dbConnection;

    public static final Uri CONTENT_URI = Uri.parse("content://de.lehrbaum.tworooms.database");

    @Override
    public boolean onCreate() {
        dbConnection = new LocalDatabaseConnection(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        getContext().getContentResolver().notifyChange(uri, null);
        String table = uri.getPath();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        long rowId = db.insert(table, null, values);
        db.close();
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(rowId));
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        getContext().getContentResolver().notifyChange(uri, null);
        String table = uri.getPath();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        int count = db.update(table, values, selection, selectionArgs);
        db.close();
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        getContext().getContentResolver().notifyChange(uri, null);
        String table = uri.getPath();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        int count = db.delete(table, selection, selectionArgs);
        db.close();
        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table = uri.getPath();
        SQLiteDatabase db = dbConnection.getReadableDatabase();
        Cursor c = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        db.close();
        return c;
    }
}
