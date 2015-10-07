package de.lehrbaum.tworooms.io;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.Locale;

public class DatabaseContentProvider extends ContentProvider {
    private static final String TAG = DatabaseContentProvider.class.getSimpleName();

    protected LocalDatabaseConnection dbConnection;

    public static final class Constants {

        public static final Uri CONTENT_URI = Uri.parse("content://de.lehrbaum.tworooms.database");
        public static final String SET_ROLE_SELECTION = "set = ?";
        public static final String SETS_TABLE = "sets";
        public static final String ROLE_COMBINATIONS_TABLE = "roleCombinations";
        public static final String ROLES_TABLE = "roles";
        public static final String TEAMS_TABLE = "teams";
		public static final String CATEGORIES_TABLE = "categories";
        public static final String SET_ROLES_TABLE = "set_roles";
        public static final String ID_COLUMN = "_id";
        public static final String NAME_COLUMN = "name";
		public static final String TEAM_COLUMN = "team_id";
		public static final String GROUP_COLUMN = "\"group\"";
        public static final String COUNT_COLUMN = "count";
        public static final String PARENT_COLUMN = "parent";
        public static final String DESCRIPTION_COLUMN = "description";
        public static final String CATEGORY_COLUMN = "category";
        public static final String ID_SET_COLUMN = "id_set";
        public static final String ID_ROLE_COLUMN = "id_role";
        public static final String FROM_SERVER_COLUMN = "from_server";
		public static final int TEAM_RED = 2;
		public static final int TEAM_BLUE = 1;
		public static final int TEAM_GRAY = 3;
		public static final int TEAM_GREEN = 4;
        public static final int TEAM_YELLOW = 5;
        public static final int TEAM_VIOLETT = 6;
        public static final int TEAM_BLACK = 7;
    }


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
        String table = localize(uri.getLastPathSegment());
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        long rowId = db.insert(table, null, values);
        return Uri.withAppendedPath(Constants.CONTENT_URI, String.valueOf(rowId));
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.v(TAG, "update");
        getContext().getContentResolver().notifyChange(uri, null);
        String table = localize(uri.getLastPathSegment());
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        return db.update(table, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete");
        getContext().getContentResolver().notifyChange(uri, null);
        String table = localize(uri.getLastPathSegment());
        SQLiteDatabase db = dbConnection.getWritableDatabase();
        return db.delete(table, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.v(TAG, "query");
        String table = localize(uri.getLastPathSegment());
        SQLiteDatabase db = dbConnection.getReadableDatabase();
        Cursor c;
        if(selection == null) selection = "";
        switch (selection) {
            case Constants.SET_ROLE_SELECTION:
                /*
                 * This means selection of roles depending on a set id.
                 * For this we need to use joins which cannot be done with the normal query method.
                 * Therefore we use raw query.
                 */
                StringBuilder query = new StringBuilder("SELECT ");
                if(projection != null) {
                    for (String column : projection)
                        query.append(column).append(',');
                    query.setCharAt(query.length() - 1, ' ');//remove last colon
                } else
                    query.append("* ");
				query.append("FROM ");
                query.append(table);
                query.append(" JOIN set_roles ON _id = id_role WHERE id_set = ? ");
				if(sortOrder != null) {
					query.append(" ORDER BY ");
					query.append(sortOrder);
				}
                c = db.rawQuery(query.toString(), selectionArgs);
                break;
            default:
                c = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    private String localize(String table) {
        //String locale = "_" + Locale.getDefault().getLanguage();
        String locale = "_en";
        switch(table) {
            case Constants.ROLES_TABLE:
            case Constants.CATEGORIES_TABLE:
            case Constants.TEAMS_TABLE:
                return table + locale;
            default:
                return table;
        }
    }

    public static class TableObserver extends ContentObserver {

        private final Runnable onChange;

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
        @Override
        public void onChange(boolean selfChange, Uri changeUri) {
            onChange(selfChange);
        }
    }
}
