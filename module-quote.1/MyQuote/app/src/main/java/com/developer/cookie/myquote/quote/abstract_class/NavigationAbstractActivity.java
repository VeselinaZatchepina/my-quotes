package com.developer.cookie.myquote.quote.abstract_class;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.idea.activities.GetIdeaActivity;
import com.developer.cookie.myquote.quote.activities.AddQuoteActivity;
import com.developer.cookie.myquote.quote.activities.QuoteCategoryMainActivity;

/**
 * NavigationAbstractActivity helps avoid boilerplate code.
 */
public abstract class NavigationAbstractActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAndSetDataFromSaveInstanceState(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_bottom, R.anim.hold);
        setContentView(getLayoutResId());
        // Work with Navigation Drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Create fragment
        workWithFragment();
        //Work with fab
        workWithFab();
    }

    @LayoutRes
    public abstract int getLayoutResId();

    public abstract void workWithFragment();

    public abstract void getAndSetDataFromSaveInstanceState(Bundle savedInstanceState);

    public abstract void workWithFab();

    public int setFabImageResourceId() {
        return R.drawable.ic_add_white_24dp;
    }

    public void setFabBackgroundImage(FloatingActionButton fab, int imageResourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(imageResourceId, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(imageResourceId));
        }
    }

    public void toDoWhenFabIsPressed() {
        Intent intent = AddQuoteActivity.newIntent(this, getTitle().toString());
        startActivity(intent);
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
            Intent intent = QuoteCategoryMainActivity.newIntent(this, "Book quote");
            startActivity(intent);
        } else if (id == R.id.nav_my_quote) {
            Intent intent = QuoteCategoryMainActivity.newIntent(this, "My quote");
            startActivity(intent);
        } else if (id == R.id.nav_get_idea) {
            Intent intent = GetIdeaActivity.newIntent(this);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_bottom);
        super.onPause();
    }
}
