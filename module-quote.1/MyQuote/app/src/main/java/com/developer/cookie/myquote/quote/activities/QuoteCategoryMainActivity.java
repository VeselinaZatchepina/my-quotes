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
import com.developer.cookie.myquote.quote.fragments.AllQuoteCurrentCategoryFragment;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;
import com.developer.cookie.myquote.quote.fragments.QuoteCategoryFragment;

import java.util.ArrayList;

public class QuoteCategoryMainActivity extends SingleFragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener, QuoteCategoryFragment.Callbacks,
                    AllQuoteCurrentCategoryFragment.AllQuoteCurrentCategoryCallbacks {

    private static final String LOG_TAG = QuoteCategoryMainActivity.class.getSimpleName();
    public static final String QUOTE_TYPE_INTENT_QCMA = "com.developer.cookie.myquote.quote_type_quote_category_qcma";
    public static final String CURRENT_FRAGMENT_TAG_BUNDLE_QCMA = "com.developer.cookie.myquote.current_fragment_tag_bundle_qcma";
    public static final String TABLET_FRAGMENT_TAG_BUNDLE_QCMA = "com.developer.cookie.myquote.tablet_fragment_tag_bundle_qcma";
    public static final String QUOTE_TYPE_BUNDLE_QCMA = "com.developer.cookie.myquote.quote_type_bundle_qcma";

    Fragment mCurrentFragment;
    //Fragment to tablet
    Fragment newCurrentQuoteFragment;
    String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
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

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        setFabBackgroundImage(fabImageResourceId);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDoWhenFabIsPressed();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public Fragment createFragment() {
        mCurrentFragment = new QuoteCategoryFragment();
        return mCurrentFragment;
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, QuoteCategoryMainActivity.class);
        intent.putExtra(QUOTE_TYPE_INTENT_QCMA, titleName);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE_QCMA, mCurrentFragment);
        outState.putString(QUOTE_TYPE_BUNDLE_QCMA, mTitle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE_QCMA);
        mTitle = savedInstanceState.getString(QUOTE_TYPE_BUNDLE_QCMA);
    }

    @Override
    public void getAndSetDataFromSaveInstanceState(Bundle saveInstanceState) {
        super.getAndSetDataFromSaveInstanceState(saveInstanceState);
        if (saveInstanceState != null) {
            mTitle = saveInstanceState.getString(QUOTE_TYPE_BUNDLE_QCMA);
            setTitle(mTitle);
            return;
        } else if (getIntent().getStringExtra(QUOTE_TYPE_INTENT_QCMA) != null) {
            mTitle = getIntent().getStringExtra(QUOTE_TYPE_INTENT_QCMA);
            setTitle(mTitle);
            return;
        }
        setTitle(getString(R.string.book_quote_type));
    }

    @Override
    public void onCategorySelected(String quoteCategory, String quoteType) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = AllQuoteCurrentCategoryActivity.newIntent(this,
                    quoteCategory, quoteType);
            startActivity(intent);
        } else {
            Fragment newAllQuoteCurrentCategoryFragment = AllQuoteCurrentCategoryFragment.newInstance(quoteCategory, quoteType);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, newAllQuoteCurrentCategoryFragment)
                    .commit();
        }
    }

    @Override
    public void onQuoteSelected(ArrayList<Long> listOfQuotesId, final Long currentId, final String quoteType) {
        setFabBackgroundImage(R.drawable.ic_create_white_24dp);
        mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = AddQuoteActivity.newIntent(QuoteCategoryMainActivity.this, currentId, quoteType);
                    startActivity(intent);
                }
            });
        newCurrentQuoteFragment = CurrentQuoteFragment.newInstance(currentId);
        getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, newCurrentQuoteFragment)
                    .commit();
    }

    private void setFabBackgroundImage(int imageResourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFab.setImageDrawable(getResources().getDrawable(imageResourceId, getTheme()));
        } else {
            mFab.setImageDrawable(getResources().getDrawable(imageResourceId));
        }
    }
}
