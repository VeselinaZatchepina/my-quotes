package com.developer.cookie.myquote.quote.activities;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.developer.cookie.myquote.R;

/**
 * SingleFragmentActivity helps avoid boilerplate code.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    public Fragment currentFragment;
    FloatingActionButton mFab;
    public int fabImageResourceId = setFabImageResourceId();

    public abstract Fragment createFragment();

    public int setFabImageResourceId() {
        return R.drawable.ic_add_white_24dp;
    }

    public void toDoWhenFabIsPressed() {
        Intent intent = AddQuoteActivity.newIntent(this, getTitle().toString());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAndSetDataFromSaveInstanceState(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = fragmentManager.findFragmentById(R.id.container);
        if (currentFragment == null) {
            currentFragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, currentFragment)
                    .commit();
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
    public void getAndSetDataFromSaveInstanceState(Bundle saveInstanceState) { }
}
