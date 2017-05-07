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
import com.developer.cookie.myquote.quote.abstract_class.NavigationAbstractActivity;
import com.developer.cookie.myquote.utils.ColorationTextChar;

import java.util.ArrayList;
import java.util.Locale;

/**
 * GetIdeaActivity helps to generate random quotes from db or show quotes by input subject
 */
public class GetIdeaActivity extends NavigationAbstractActivity implements GetIdeaFragment.GetIdeaCallbacks {

    Fragment mCurrentFragment;
    Fragment mDetailFragment;

    @Override
    public void defineInputData(Bundle savedInstanceState) {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_masterdetail_get_idea;
    }

    @Override
    public void defineFragment() {
        // Set new style for toolbar title
        if (findViewById(R.id.detail_fragment_container) != null) {
            String localeLanguage = Locale.getDefault().getLanguage();
            setTitle(ColorationTextChar.setFirstVowelColor(getString(R.string.idea_title), localeLanguage, this));
        } else {
            setTitle(getString(R.string.idea_title));
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        mCurrentFragment = fragmentManager.findFragmentById(R.id.container);
        if (mCurrentFragment == null) {
            mCurrentFragment = new GetIdeaFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, mCurrentFragment)
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
            mDetailFragment = IdeaCoincideQuoteTextFragment.newInstance(listOfQuoteText);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, mDetailFragment)
                    .commit();
        } else {
            if (listOfQuoteText.size() != 0) {
                startActivity(IdeaCoincideQuoteTextActivity.newIntent(this, listOfQuoteText));
            } else {
                Toast.makeText(this, "You have no quote with this subject", Toast.LENGTH_LONG).show();
            }
        }
    }
}
