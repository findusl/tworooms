package de.lehrbaum.tworooms.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.lehrbaum.tworooms.R;
import de.lehrbaum.tworooms.view.util.BaseActivity;

public final class RoleListActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role_list);
	}
}
