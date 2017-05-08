package com.developer.cookie.myquote.quote.abstract_class;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.activities.AddQuoteActivity;
import com.developer.cookie.myquote.utils.AppBarLayoutExpended;
import com.developer.cookie.myquote.utils.ColorationTextChar;

import java.util.Locale;

/**
 * SingleFragmentAbstractActivity helps avoid boilerplate code.
 */
public abstract class SingleFragmentAbstractActivity extends AppCompatActivity {
    public Fragment currentFragment;
    public FloatingActionButton fab;
    public int fabImageResourceId;
    public String mQuotesType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        setContentView(getLayoutResId());
        defineToolbar();
        setAppBarNotExpandable();
        setNewTitleStyle(mQuotesType);
        defineFragment();
        defineFab();
    }

    public void defineInputData(Bundle saveInstanceState) { }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

    private void defineToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setAppBarNotExpandable() {
        if (findViewById(R.id.detail_fragment_container) == null) {
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            Configuration configuration = getResources().getConfiguration();
            AppBarLayoutExpended.setAppBarLayoutExpended(this, appBarLayout, layoutParams, collapsingToolbarLayout, configuration);
        }
    }

    public void setNewTitleStyle(String quoteType) {
        String localeLanguage = Locale.getDefault().getLanguage();
        setTitle(ColorationTextChar.setFirstVowelColor(quoteType, localeLanguage, this));
    }

    private void defineFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = fragmentManager.findFragmentById(R.id.container);
        if (currentFragment == null) {
            currentFragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, currentFragment)
                    .commit();
        }
    }

    public abstract Fragment createFragment();

    private void defineFab() {
        fabImageResourceId = setFabImageResourceId();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(fabImageResourceId, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(fabImageResourceId));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineActionWhenFabIsPressed();
            }
        });
    }

    public int setFabImageResourceId() {
        return R.drawable.ic_add_white_24dp;
    }

    public void defineActionWhenFabIsPressed() {
        Intent intent = AddQuoteActivity.newIntent(this, getTitle().toString());
        startActivity(intent);
    }
}
