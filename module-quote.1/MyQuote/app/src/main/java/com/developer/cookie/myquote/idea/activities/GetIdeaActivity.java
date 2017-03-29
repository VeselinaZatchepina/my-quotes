package com.developer.cookie.myquote.idea.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.idea.fragments.GetIdeaFragment;
import com.developer.cookie.myquote.idea.fragments.IdeaCoincideQuoteTextFragment;
import com.developer.cookie.myquote.quote.abstract_class.NavigationAbstractActivity;

import java.util.ArrayList;

public class GetIdeaActivity extends NavigationAbstractActivity implements GetIdeaFragment.GetIdeaCallbacks {

    Fragment mCurrentFragment;
    Fragment mDetailFragment;

    @Override
    public void getAndSetDataFromSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void workWithFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void workWithFragment() {
        setTitle(getString(R.string.idea_title));
        FragmentManager fragmentManager = getSupportFragmentManager();
        mCurrentFragment = fragmentManager.findFragmentById(R.id.container);
        if (mCurrentFragment == null) {
            mCurrentFragment = new GetIdeaFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, mCurrentFragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, GetIdeaActivity.class);
    }

    @Override
    public void generateIdea(ArrayList<String> listOfQuoteText) {
        if (findViewById(R.id.detail_fragment_container) != null) {
            mDetailFragment = IdeaCoincideQuoteTextFragment.newInstance(listOfQuoteText);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, mDetailFragment)
                    .commit();
        } else {
            startActivity(IdeaCoincideQuoteTextActivity.newIntent(this, listOfQuoteText));
        }
    }
}
