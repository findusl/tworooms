package de.lehrbaum.tworooms.io;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DatabaseContentProvder extends ContentProvider {
    private static final String TAG = DatabaseContentProvder.class.getSimpleName();

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
        Log.v(TAG, "insert");
        getContext().getContentResolver().notifyChange(uri, null);
        String table = uri.getLastPathSegment();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        long rowId = db.insert(table, null, values);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(rowId));
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.v(TAG, "update");
        getContext().getContentResolver().notifyChange(uri, null);
        String table = uri.getLastPathSegment();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        int count = db.update(table, values, selection, selectionArgs);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete");
        getContext().getContentResolver().notifyChange(uri, null);
        String table = uri.getLastPathSegment();
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        int count = db.delete(table, selection, selectionArgs);
        return count;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.v(TAG, "query");
        String table = uri.getLastPathSegment();
        SQLiteDatabase db = dbConnection.getReadableDatabase();
        Cursor c = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
}
