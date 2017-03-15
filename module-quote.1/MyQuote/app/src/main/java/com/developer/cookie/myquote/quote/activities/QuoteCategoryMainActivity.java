package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.fragments.QuoteCategoryFragment;

public class QuoteCategoryMainActivity extends SingleFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String QUOTE_TYPE_QUOTE_CATEGORY = "com.developer.cookie.myquote.quote_type_quote_category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_activity);
        // Work with Navigation Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String title = getIntent().getStringExtra(QUOTE_TYPE_QUOTE_CATEGORY);
        if (title != null) {
            setTitle(title);
        } else {
            setTitle(getString(R.string.book_quote_type));
        }

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFab.setImageDrawable(getResources().getDrawable(fabImageResourceId, getTheme()));
        } else {
            mFab.setImageDrawable(getResources().getDrawable(fabImageResourceId));
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDoWhenFabIsPressed();
            }
        });
    }

    @Override
    public Fragment createFragment() {
        return new QuoteCategoryFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_book_quote) {
            Intent intent = QuoteCategoryMainActivity.newIntent(this, getString(R.string.book_quote_type));
            startActivity(intent);
        } else if (id == R.id.nav_my_quote) {
            Intent intent = QuoteCategoryMainActivity.newIntent(this, getString(R.string.my_quote_type));
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, QuoteCategoryMainActivity.class);
        intent.putExtra(QUOTE_TYPE_QUOTE_CATEGORY, titleName);
        return intent;
    }
}
