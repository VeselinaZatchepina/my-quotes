package com.developer.cookie.myquote.quote.activities;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.developer.cookie.myquote.R;

/**
 * SingleFragmentActivity helps avoid boilerplate code.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

    public Fragment currentFragment;
    FloatingActionButton fab;
    public int fabImageResourceId = setFabImageResourceId();

    public abstract Fragment createFragment();

    public abstract int setFabImageResourceId();

    public abstract void toDoWhenFabIsPressed();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
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
}
