package de.lehrbaum.tworooms.io;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

import de.lehrbaum.tworooms.R;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();
	public static final String SYNC_PREFERENCES = "Sync Preferences";
	private static final String LAST_SYNC_PREF = "last sync";

    // Define a variable to contain a content resolver instance
    final ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    /*
     * If your app uses a content resolver, get an instance of it
     * from the incoming Context
     */
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
    /*
     * If your app uses a content resolver, get an instance of it
     * from the incoming Context
     */
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "on Perform Sync called");
        showNotification("On perform Sync called", getContext());
		//TODO untested, missing the last changed date
        URL target = null;
        InputStream is = null;
        String jsonString = "";
        try {
			StringBuilder filepath = new StringBuilder("twoRooms/getChanges.php?v=");
			filepath.append(LocalDatabaseConnection.DATABASE_VERSION);
			filepath.append("&since=");
			SharedPreferences prefs = getContext().getSharedPreferences(SYNC_PREFERENCES, 0);
			long lastTime = prefs.getLong(LAST_SYNC_PREF, 0);
			filepath.append(lastTime);
            target = new URL("http", "lehrbaum.de", filepath.toString());
			long startTime = System.currentTimeMillis();//TODO: replace with server time
            is = downloadUrl(target);
            String htmlString = readIt(is);
			jsonString = Html.fromHtml(htmlString).toString();
            JSONObject tables = new JSONObject(jsonString);
			StringBuilder query = new StringBuilder(jsonString.length());
            for(String table : new II<String>(tables.keys())) {
                String rows = tables.getString(table);
                Log.d(TAG, "rows: " + rows);
				query = createQuery(query, table, rows);
			}
			Log.d(TAG, "Query to be executed " + query.toString());
			SQLiteDatabase db = new LocalDatabaseConnection(getContext()).getWritableDatabase();
			db.execSQL(query.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformated url? It is static...", e);
        } catch (IOException e) {
            Log.e(TAG, "Error downloading.", e);
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing json string: " + jsonString, e);
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    Log.w(TAG, "Unable to close inputstream", e);
                }
        }
    }

	private StringBuilder createQuery(StringBuilder query, String table, String values) {
		query = new StringBuilder("INSERT OR REPLACE INTO ");
		query.append(table);
		query.append(" VALUES ");
		int offset = query.length();
		query.append(values);
		query.deleteCharAt(offset).deleteCharAt(query.length() - 1);
		for(int i = offset; i < query.length(); i++) {
			char c = query.charAt(i);
			if(c == '[')
				query.setCharAt(i, '(');
			else if(c == ']')
				query.setCharAt(i, ')');
		}
		query.append("; ");
		return query;
	}

    private InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(false);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
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

    /**
     * @deprecated Just for debugging purpose
     */
    public static void showNotification(String eventtext, Context context) {
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Two rooms message")
                        .setContentText(eventtext);
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(1, mBuilder.build());
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