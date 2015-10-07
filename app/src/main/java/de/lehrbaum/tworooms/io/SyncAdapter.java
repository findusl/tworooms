package de.lehrbaum.tworooms.io;

import android.accounts.Account;
import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static de.lehrbaum.tworooms.io.DatabaseContentProvider.Constants.*;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();
	private static final String LAST_DOWN_PREF = "last down";

    public static final String SYNC_PREFERENCES = "Sync Preferences";

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
        SharedPreferences prefs = getContext().getSharedPreferences(SYNC_PREFERENCES, 0);
        int [] ids;
        int retries = 0;
        while(retries < 5) {
            try {
                //TODO: set numInsertions etc in syncResult correctly
                ids = uploadChanges(syncResult, prefs);
                downloadChanges(syncResult, prefs, ids);
                return;
            } catch (MalformedURLException e) {
                Log.e(TAG, "Malformed url? How can that be with a static URL?", e);
                syncResult.stats.numParseExceptions++;
                return;
            } catch (IOException e) {
                Log.w(TAG, "Problem sending. Probably just network error.", e);
                syncResult.stats.numIoExceptions++;
            } catch (JSONException e) {
                Log.e(TAG, "Problem with the JSON from the server.", e);
                syncResult.stats.numParseExceptions++;
                return;
            }
        }
        //If we reach this point there have been to many retries
        syncResult.tooManyRetries = true;
    }

    private int [] uploadChanges(SyncResult syncResult, SharedPreferences prefs) throws IOException {
        String userId = Settings.Secure.ANDROID_ID;
        InputStream is = null;
        URL target = null;
        try {
            Uri uri = Uri.withAppendedPath(DatabaseContentProvider.Constants.CONTENT_URI, SETS_TABLE);
            Cursor c = mContentResolver.query(uri, null, FROM_SERVER_COLUMN + " = 1", null, PARENT_COLUMN + " ASC");
            int[] ids = new int[c.getCount()];
            if(c.getCount() > 0) {//if no changed sets do nothing.
                target = new URL("http", "lehrbaum.de", "twoRoomWriteSQL.php");
                JSONArray sets = new JSONArray();
                int i = 0;
                for (c.moveToFirst(); c.isAfterLast(); c.moveToNext()) {
                    JSONArray set = new JSONArray();
                    int id = c.getInt(0);
                    ids[i++] = id;
                    set.put(id);
                    set.put(c.getString(1));
                    set.put(c.getInt(2));
                    set.put(c.getInt(3));
                    set.put(c.getString(4));
                    JSONArray roles = new JSONArray();
                    Cursor roleC = mContentResolver.query(uri, new String[]{ID_ROLE_COLUMN},
                            ID_SET_COLUMN + " = " + id, null, null);
                    for (roleC.moveToFirst(); roleC.isAfterLast(); roleC.moveToNext()) {
                        roles.put(roleC.getInt(0));
                    }
                    set.put(roles);
                    sets.put(set);
                }
                String jsonString = sets.toString();
                String setsHtmlString = Html.escapeHtml(jsonString).toString();
                is = sendPostRequest(target, setsHtmlString);
            }
            return ids;
        } finally {
            if(is != null)
                is.close();
        }
    }

    private InputStream sendPostRequest(URL target, String jsonData) throws IOException {
        return null;
    }

    //==============================================================================================
    //downloading===================================================================================

    private void downloadChanges(SyncResult syncResult, SharedPreferences prefs, int [] uploadedIDs) throws IOException, JSONException {
        URL target = null;
        InputStream is = null;
        String jsonString = "";
        SQLiteDatabase db = null;
        try {
			StringBuilder filepath = new StringBuilder("twoRoomsReadSQL.php?v=");
			filepath.append(LocalDatabaseConnection.DATABASE_VERSION);
			filepath.append("&since=");
			long lastTime = prefs.getLong(LAST_DOWN_PREF, 0);
			filepath.append(lastTime);
            target = new URL("http", "lehrbaum.de", filepath.toString());
			Log.d(TAG, "Target URL: " + target.toExternalForm());
            is = target.openStream();
            String htmlString = readIt(is);
			int timeEnd = htmlString.indexOf('{');
			long startTime = Long.parseLong(htmlString.substring(5, timeEnd));
			Log.d(TAG, "Start time: " + startTime);
			jsonString = Html.fromHtml(htmlString.substring(timeEnd)).toString();
			Log.d(TAG, "JSON string: " + jsonString);
            JSONObject tables = new JSONObject(jsonString);
			StringBuilder query = new StringBuilder(jsonString.length());
            for(String table : new II<String>(tables.keys())) {
                String rows = tables.getString(table);
				Log.v(TAG, "rows: " + rows);
				query = createQuery(query, table, rows);
			}
			Log.d(TAG, "Query to be executed " + query.toString());
            if(query.length() > 0) {
                if(uploadedIDs != null) {

                }
                //using raw query to be able to directly use the answer string.
                db = new LocalDatabaseConnection(getContext()).getWritableDatabase();
                db.execSQL(query.toString());
            }
			prefs.edit().putLong(LAST_DOWN_PREF, startTime).commit();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    Log.w(TAG, "Unable to close inputstream", e);
                }
            if(db != null)
                db.close();
        }
    }

    private StringBuilder createQuery(StringBuilder query, String table, String values) {
		if(values.length() < 3)
			return query;
		query = query.append("INSERT OR REPLACE INTO ");
		query.append(table);
		query.append(" VALUES ");
		int offset = query.length();
		query.append(values);
        //delete first and last bracket []
		query.deleteCharAt(offset).deleteCharAt(query.length() - 1);
		for(int i = offset; i < query.length(); i++) {
			char c = query.charAt(i);
			if(c == '[')
				query.setCharAt(i, '(');
			else if(c == ']')
				query.setCharAt(i, ')');
		}
		query.append(";");
		return query;
	}

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
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