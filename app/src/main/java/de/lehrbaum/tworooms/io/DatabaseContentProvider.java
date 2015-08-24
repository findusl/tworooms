package de.lehrbaum.tworooms.io;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DatabaseContentProvider extends ContentProvider {
    private static final String TAG = DatabaseContentProvider.class.getSimpleName();

    protected LocalDatabaseConnection dbConnection;

    public static final Uri CONTENT_URI = Uri.parse("content://de.lehrbaum.tworooms.database");
    public static final String SET_ROLE_SELECTION = "set = ?";

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
        Cursor c;
        switch (selection) {
            case SET_ROLE_SELECTION:
                /*
                 * This means selection of roles depending on a set id.
                 * For this we need to use joins which cannot be done with the normal query method.
                 * Therefore we use raw query.
                 */
                String setId = selectionArgs[0];
                StringBuilder columns;
                if(projection != null) {
                    columns = new StringBuilder();
                    for (String column : projection)
                        columns.append(column + ',');
                    columns.deleteCharAt(columns.length() - 1);//remove last colon
                } else
                    columns = new StringBuilder("*");
                /*
                 *
                 * Any sort order passed to this function is just appended at the end.
                 */
                c = db.rawQuery("SELECT _id, name, description " +
                                "FROM roles JOIN set_roles ON _id = id_role " +
                                "WHERE id_set = ? or id_set = " +
                                "(SELECT parent FROM sets s WHERE s._id = ?)" +
                                (sortOrder == null ? "" : " ORDER BY " + sortOrder),
                        new String[]{setId, setId});
                break;
            default:
                c = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    public static class TableObserver extends ContentObserver {

        private Runnable onChange;

        public TableObserver(Runnable onChange) {
            super(null);
            this.onChange = onChange;
        }

        /*
         * Define a method that's called when data in the
         * observed content provider changes.
         * This method signature is provided for compatibility with
         * older platforms.
         */
        @Override
        public void onChange(boolean selfChange) {
            if(!selfChange)
                onChange.run();
        }
        /*
         * Define a method that's called when data in the
         * observed content provider changes.
         */
        @Override
        public void onChange(boolean selfChange, Uri changeUri) {
            onChange(selfChange);
        }
    }
}
