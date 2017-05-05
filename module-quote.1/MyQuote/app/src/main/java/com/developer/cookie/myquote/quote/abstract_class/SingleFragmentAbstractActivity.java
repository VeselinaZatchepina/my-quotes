package com.developer.cookie.myquote.quote.abstract_class;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.activities.AddQuoteActivity;

/**
 * SingleFragmentAbstractActivity helps avoid boilerplate code.
 */
public abstract class SingleFragmentAbstractActivity extends AppCompatActivity {

    public Fragment currentFragment;
    public FloatingActionButton fab;
    public int fabImageResourceId = setFabImageResourceId();

    public int setFabImageResourceId() {
        return R.drawable.ic_add_white_24dp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAndSetDataFromSaveInstanceState(savedInstanceState);
        setContentView(getLayoutResId());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Method for style action (change text color and etc.)
        otherStyleAction();
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = fragmentManager.findFragmentById(R.id.container);
        if (currentFragment == null) {
            currentFragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, currentFragment)
                    .commit();
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(fabImageResourceId, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(fabImageResourceId));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDoWhenFabIsPressed();
            }
        });
    }

    public void getAndSetDataFromSaveInstanceState(Bundle saveInstanceState) {
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }

    public abstract Fragment createFragment();

    public void toDoWhenFabIsPressed() {
        Intent intent = AddQuoteActivity.newIntent(this, getTitle().toString());
        startActivity(intent);
    }

    public void otherStyleAction() { }
}
