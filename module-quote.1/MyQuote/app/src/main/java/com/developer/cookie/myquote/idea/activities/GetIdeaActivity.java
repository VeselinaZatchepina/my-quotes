package com.developer.cookie.myquote.idea.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.idea.fragments.GetIdeaFragment;
import com.developer.cookie.myquote.idea.fragments.IdeaCoincideQuoteTextFragment;
import com.developer.cookie.myquote.quote.Types;
import com.developer.cookie.myquote.quote.abstract_class.NavigationAbstractActivity;

import java.util.ArrayList;

/**
 * GetIdeaActivity helps to generate random quotes from db or show quotes by input subject
 */
public class GetIdeaActivity extends NavigationAbstractActivity implements GetIdeaFragment.GetIdeaCallbacks {

    @Override
    public void defineInputData(Bundle savedInstanceState) {
        setTitle(Types.GET_IDEA);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_masterdetail_get_idea;
    }

    @Override
    public void defineFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment mMainFragment = fragmentManager.findFragmentById(R.id.container);
        if (mMainFragment == null) {
            mMainFragment = new GetIdeaFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, mMainFragment)
                    .commit();
        }
    }

    @Override
    public void defineFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, GetIdeaActivity.class);
    }

    @Override
    public void generateIdea(ArrayList<String> listOfQuoteText) {
        if (findViewById(R.id.detail_fragment_container) != null) {
            Fragment mDetailFragment = IdeaCoincideQuoteTextFragment.newInstance(listOfQuoteText);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, mDetailFragment)
                    .commit();
        } else {
            if (listOfQuoteText.size() != 0) {
                startActivity(IdeaCoincideQuoteTextActivity.newIntent(this, listOfQuoteText));
            } else {
                Toast.makeText(this, getString(R.string.idea_toast), Toast.LENGTH_LONG).show();
            }
        }
    }
}
