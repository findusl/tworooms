package de.lehrbaum.tworooms.view.util;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;

import de.lehrbaum.tworooms.R;

import static de.lehrbaum.tworooms.database.DatabaseContentProvider.Constants.NAME_COLUMN;
import static de.lehrbaum.tworooms.database.DatabaseContentProvider.Constants.TEAM_COLUMN;

/**
 * Created by findu on 21.10.2015.
 */
public final class ChooseRolesListAdapter extends SimpleCursorAdapter{

    private LayoutInflater mInflater;
    private Context mContext;

    private SeekBar.OnSeekBarChangeListener blueRole, redRole;

    public ChooseRolesListAdapter (Context context, SeekBar.OnSeekBarChangeListener blueRole, SeekBar.OnSeekBarChangeListener redBar) {
        super(context,
                android.R.layout.simple_list_item_1, null,
                new String[]{NAME_COLUMN, TEAM_COLUMN},
                new int[]{android.R.id.text1, android.R.id.text1}, 0);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        long id = getItemId(position);
        if(id == 100 || id == 101) {
            View v = convertView;
            Cursor c = getCursor();
            c.moveToPosition(position);
            if(v == null || !(v instanceof LinearLayout)) {
                v = mInflater.inflate(R.layout.list_normal_role, parent);
            }
            bindView(v, mContext, c);
            SeekBar sb = (SeekBar) v.findViewById(R.id.seekBar);
            if(id == 100)
                sb.setOnSeekBarChangeListener(blueRole);
            else
                sb.setOnSeekBarChangeListener(redRole);
            v.setClickable(false);
            return v;
        }else {
            if(convertView != null && convertView instanceof LinearLayout)
                convertView = null;
            return super.getView(position, convertView, parent);
        }
    }
}
