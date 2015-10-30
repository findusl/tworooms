package de.lehrbaum.tworooms.io;

import android.accounts.Account;
import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.*;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public final class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();
	private static final String LAST_DOWN_PREF = "last down";
    private static final String [] LIST_SYNC_TABLES = new String [] {CATEGORIES_TABLE + "_en",
            CATEGORIES_TABLE + "_de", TEAMS_TABLE + "_en", TEAMS_TABLE + "_de",
            ROLES_TABLE + "_en", ROLES_TABLE + "_de", SETS_TABLE, SET_ROLES_TABLE};

    public static ContentObserver SYNC_OBSERVER;

    // Define a variable to contain a content resolver instance
    final ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "on Perform Sync called");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int retries = 0;
        SQLiteDatabase db = new LocalDatabaseConnection(getContext()).getWritableDatabase();
        try {//outer try to close database
            while (retries < 5) {
                try {//inner try to retry up/download
                    //wish: set numInsertions etc in syncResult correctly
                    uploadChanges(syncResult, db);
                    downloadChanges(syncResult, prefs, db);
                    return;
                } catch (MalformedURLException | JSONException | SQLiteException e) {
                    Log.e(TAG, "Problem with the database.", e);
                    syncResult.stats.numParseExceptions++;
                    return;
                } catch (IOException e) {
                    Log.w(TAG, "Problem sending. Probably just network error.", e);
                    syncResult.stats.numIoExceptions++;
                }
                retries++;
            }
        } finally {
            if (syncResult.madeSomeProgress()) {
                getContext().getContentResolver().notifyChange(CONTENT_URI, SYNC_OBSERVER);
            }
            closeHelper(db);
            syncResult.delayUntil = 60;
        }
        //If we reach this point there have been to many retries
        syncResult.tooManyRetries = true;
    }

    //==============================================================================================
    //uploading=====================================================================================

    private void uploadChanges(SyncResult syncResult, SQLiteDatabase db) throws IOException {
        InputStream is = null;
        URL target;
        try {
            String addJsonString = getChangeData(db, 0);
            String delJsonString = getChangeData(db, 2);
            target = new URL("http", "lehrbaum.de", "twoRoomsWriteSQL.php");
            is = sendPostRequest(target, addJsonString, delJsonString);
            //after successful sending, delete the entries from the database
            db.delete(SETS_TABLE, FROM_SERVER_COLUMN + "=0||" + FROM_SERVER_COLUMN + "=2", null);
        } finally {
            closeHelper(is);
        }
    }

    private String getChangeData(SQLiteDatabase db, int fromServer) throws MalformedURLException {
        Cursor c = null;
        Cursor roleC = null;
        try {
            //TODO: for fromServer = 2 do not get name/description/count
            c = db.query(SETS_TABLE, null, FROM_SERVER_COLUMN + " = " + fromServer, null, null, null, PARENT_COLUMN + " ASC");
            if(c.getCount() > 0) {//Only do work if sets have changed
                JSONArray sets = new JSONArray();
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                    //write the set information
                    JSONArray set = new JSONArray();
                    int id = c.getInt(0);
                    set.put(id);
                    set.put(c.getString(1));
                    set.put(c.getInt(2));
                    set.put(c.getInt(3));
                    set.put(c.getString(4));

                    //get the roles of the set
                    JSONArray roles = new JSONArray();
                    roleC = db.query(SET_ROLES_TABLE, new String[]{ID_ROLE_COLUMN},
                            ID_SET_COLUMN + " = " + id, null, null, null, null);
                    for (roleC.moveToFirst(); !roleC.isAfterLast(); roleC.moveToNext()) {
                        roles.put(roleC.getInt(0));
                    }
                    roleC.close();
                    set.put(roles);
                    sets.put(set);
                }
                return sets.toString();
            } else
                return null;
        } finally {
            closeHelper(c);
            closeHelper(roleC);
        }
    }

    private InputStream sendPostRequest(URL target, String addjsonData, String deljsonData) throws IOException {
        //still have to encode for the url:
        String addJsonURLData = URLEncoder.encode(Html.escapeHtml(addjsonData), "utf-8");
        String delJsonURLData = URLEncoder.encode(Html.escapeHtml(deljsonData), "utf-8");

        OutputStreamWriter osw = null;
        StringBuilder data = new StringBuilder();
        data.append("add=");
        data.append(addJsonURLData);
        data.append("&del=");
        data.append(delJsonURLData);
        data.append("&version=");
        data.append(LocalDatabaseConnection.DATABASE_VERSION);
        data.append("&userId=");
        data.append(Settings.Secure.ANDROID_ID);
        try {
            URLConnection conn = target.openConnection();
            conn.setDoOutput(true);
            osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(data.toString());
            osw.flush();
            return conn.getInputStream();
        } finally {
            if(osw != null)
                osw.close();
        }
    }

    //==============================================================================================
    //downloading===================================================================================

    private void downloadChanges(SyncResult syncResult, SharedPreferences prefs, SQLiteDatabase db) throws IOException, JSONException {
        String jsonString;
        try {
            //get the data from the server
            jsonString = getDataFromServer(prefs);
            Log.d(TAG, "Json string: " + jsonString);
            JSONObject data = new JSONObject(jsonString);
            db.beginTransactionWithListener(null);

            //read the data for the tables and write to the database
            for(String table : LIST_SYNC_TABLES) {
                insertInformation(db, table, data, syncResult);
            }
            db.setTransactionSuccessful();
            int startTime = data.getInt("time");
            Log.d(TAG, "Start time: " + startTime);
			prefs.edit().putLong(LAST_DOWN_PREF, startTime).commit();
        } finally {
            db.endTransaction();
        }
    }

    private String getDataFromServer(SharedPreferences prefs) throws IOException {
        InputStream is = null;
        try {
            //define the URL
            StringBuilder filepath = new StringBuilder("twoRoomsReadSQL.php?v=");
            filepath.append(LocalDatabaseConnection.DATABASE_VERSION);
            filepath.append("&since=");
            long lastTime = prefs.getLong(LAST_DOWN_PREF, 0);
            filepath.append(lastTime);
            URL target = new URL("http", "lehrbaum.de", filepath.toString());
            Log.d(TAG, "Target URL: " + target.toExternalForm());

            //open connection
            is = target.openStream();
            return Html.fromHtml(readIt(is)).toString();
        } finally {
            closeHelper(is);
        }
    }

    private void insertInformation(SQLiteDatabase db, String table, JSONObject data,
                                   SyncResult syncResult)
            throws SQLiteException, JSONException {
        JSONArray rows = data.getJSONArray(table);
        if(rows.length() == 0)
            return;//no rows to insert
        boolean setsTable = table.equals(SETS_TABLE);

        //start to create the query:
        StringBuilder query = new StringBuilder(30 + table.length() + rows.length()*10);
        query = query.append("INSERT OR REPLACE INTO ");
        query.append(table);
        query.append(" VALUES ");

        //insert the values ot the query:
        for(int i = 0; i < rows.length(); i++) {
            String values = rows.getString(i);
            int offset = query.length();
            query.append(values);

            //change the json to sql fitting strings
            query.replace(offset, offset + 1, "(");
            if(setsTable) {
                query.replace(query.length() - 1, query.length(), ",");
                query.append(" 1)");
            } else
                query.replace(query.length() - 1, query.length(), ")");
            query.append(',');
            syncResult.stats.numInserts++;
        }
        query.replace(query.length()-1, query.length(), ";");
        Log.d(TAG, "Sql to be executed: " + query.toString());
        db.execSQL(query.toString());
    }

    private String readIt(InputStream stream) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char c = (char) reader.read();
        StringBuilder lenSt = new StringBuilder();
        while(Character.isDigit(c)) {
            lenSt.append(c);
            c = (char) reader.read();
        }
        int len = Integer.parseInt(lenSt.toString());
        char[] buffer = new char[len];
        buffer[0] = c;//have to put the c already read
        int read = reader.read(buffer, 1, len-1);
        if(read < len-1)
            Log.w(TAG, "Read only " + read + " bytes when expecting " + (len-1) + " bytes.");
        return new String(buffer);
    }

    /**
     * Closes the closeable and catches any exceptions while writing them to the warn log.
     * @param c The closeable to close. May be NULL.
     */
    private static void closeHelper(Closeable c) {
        if (c != null)
            try {
                c.close();
            } catch (Exception e) {
                Log.w(TAG, e);
            }
    }

    public static class II<T> implements Iterable<T> {

        Iterator<T> it;

        public II(Iterator<T> it) {
            this.it = it;
        }

        @Override
        public Iterator<T> iterator() {
            return it;
        }
    }
}
