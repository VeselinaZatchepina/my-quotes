package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.Types;
import com.developer.cookie.myquote.quote.abstract_class.NavigationAbstractActivity;
import com.developer.cookie.myquote.quote.fragments.QuoteCategoryFragment;

public class QuoteCategoryMainActivity extends NavigationAbstractActivity implements
                                        QuoteCategoryFragment.QuoteCategoryCallbacks {
    private static final String LOG_TAG = QuoteCategoryMainActivity.class.getSimpleName();
    private static final String QUOTE_TYPE_INTENT = "quote_category_main_activity_quote_type_intent";
    private static final String MAIN_FRAGMENT_TAG_BUNDLE = "quote_category_main_activity_main_fragment_tag_bundle";
    private static final String DETAIL_FRAGMENT_TAG_BUNDLE = "quote_category_main_activity_detail_fragment_tag_bundle";
    private static final String QUOTE_TYPE_BUNDLE = "quote_category_main_activity_main_quote type_bundle";
    private static final String CURRENT_ID_BUNDLE = "quote_category_main_activity_main_current_id_bundle";
    Fragment mMainFragment;
    Fragment mDetailFragment;
    FloatingActionButton mFab;
    int fabImageResourceId = setFabImageResourceId();
    String mTitle;
    long mCurrentId;
    String mQuoteType;

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            mMainFragment = getSupportFragmentManager().getFragment(saveInstanceState, MAIN_FRAGMENT_TAG_BUNDLE);
            if (findViewById(R.id.detail_fragment_container) != null && mDetailFragment != null) {
                mDetailFragment = getSupportFragmentManager().getFragment(saveInstanceState, DETAIL_FRAGMENT_TAG_BUNDLE);
            }
            mTitle = saveInstanceState.getString(QUOTE_TYPE_BUNDLE);
            mCurrentId = saveInstanceState.getLong(CURRENT_ID_BUNDLE);
        } else if (getIntent().getStringExtra(QUOTE_TYPE_INTENT) != null) {
            mTitle = getIntent().getStringExtra(QUOTE_TYPE_INTENT);
        } else {
            mTitle = Types.BOOK_QUOTE;
        }
        setTitle(mTitle);
        defineQuoteType();
    }

    private void defineQuoteType() {
        if (mTitle.equals(Types.BOOK_QUOTE)) {
            mQuoteType = Types.BOOK_QUOTE;
        } else {
            mQuoteType = Types.MY_QUOTE;
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_nav_drawer;
    }

    @Override
    public void defineFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMainFragment = fragmentManager.findFragmentById(R.id.container);
        if (mMainFragment == null) {
            mMainFragment = QuoteCategoryFragment.newInstance(mQuoteType);
            fragmentManager.beginTransaction()
                    .add(R.id.container, mMainFragment)
                    .commit();
        }
    }

    @Override
    public void defineFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        setFabBackgroundImage(mFab, fabImageResourceId);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineActionWhenFabIsPressed();
            }
        });
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, QuoteCategoryMainActivity.class);
        intent.putExtra(QUOTE_TYPE_INTENT, titleName);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, MAIN_FRAGMENT_TAG_BUNDLE, mMainFragment);
        if (findViewById(R.id.detail_fragment_container) != null && mDetailFragment != null) {
            getSupportFragmentManager().putFragment(outState, DETAIL_FRAGMENT_TAG_BUNDLE, mDetailFragment);
        }
        outState.putString(QUOTE_TYPE_BUNDLE, mTitle);
        outState.putLong(CURRENT_ID_BUNDLE, mCurrentId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCategorySelected(String currentCategory, String quoteType) {
        startActivity(AllQuoteCurrentCategoryActivity.newIntent(this, currentCategory, quoteType));
    }
}
