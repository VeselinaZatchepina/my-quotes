package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.abstract_class.NavigationAbstractActivity;
import com.developer.cookie.myquote.quote.fragments.AllQuoteCurrentCategoryFragment;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;
import com.developer.cookie.myquote.quote.fragments.QuoteCategoryFragment;

import java.util.ArrayList;

public class QuoteCategoryMainActivity extends NavigationAbstractActivity
        implements QuoteCategoryFragment.Callbacks,
        AllQuoteCurrentCategoryFragment.AllQuoteCurrentCategoryCallbacks {

    private static final String LOG_TAG = QuoteCategoryMainActivity.class.getSimpleName();
    public static final String QUOTE_TYPE_INTENT_QCMA = "com.developer.cookie.myquote.quote_type_quote_category_qcma";
    public static final String CURRENT_FRAGMENT_TAG_BUNDLE_QCMA = "com.developer.cookie.myquote.current_fragment_tag_bundle_qcma";
    public static final String TABLET_FRAGMENT_TAG_BUNDLE_QCMA = "com.developer.cookie.myquote.tablet_fragment_tag_bundle_qcma";
    public static final String QUOTE_TYPE_BUNDLE_QCMA = "com.developer.cookie.myquote.quote_type_bundle_qcma";
    public static final String FAB_TAG_BUNDLE_QCMA = "com.developer.cookie.myquote.fab_tag_bundle_qcma";
    public static final String CURRENT_ID_BUNDLE_QCMA = "com.developer.cookie.myquote.current_id_bundle_qcma";
    Fragment mCurrentFragment;
    FloatingActionButton mFab;
    int fabImageResourceId = setFabImageResourceId();
    //Fragment to tablet
    Fragment mDetailFragment;
    String mTitle;
    View.OnClickListener mAddFabListener;
    View.OnClickListener mEditFabListener;
    int mFabListenerTag = 0;
    long mCurrentId;

    String mQuoteCategoryFirstTabletLunch;

    @Override
    public void getAndSetDataFromSaveInstanceState(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE_QCMA);
            if (findViewById(R.id.detail_fragment_container) != null && mDetailFragment != null) {
                mDetailFragment = getSupportFragmentManager().getFragment(saveInstanceState, TABLET_FRAGMENT_TAG_BUNDLE_QCMA);
            }
            mTitle = saveInstanceState.getString(QUOTE_TYPE_BUNDLE_QCMA);
            mFabListenerTag = saveInstanceState.getInt(FAB_TAG_BUNDLE_QCMA);
            mCurrentId = saveInstanceState.getLong(CURRENT_ID_BUNDLE_QCMA);
        } else if (getIntent().getStringExtra(QUOTE_TYPE_INTENT_QCMA) != null) {
            mTitle = getIntent().getStringExtra(QUOTE_TYPE_INTENT_QCMA);
        } else {
            mTitle = getString(R.string.book_quote_type);
        }
        setTitle(mTitle);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void workWithFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mCurrentFragment = fragmentManager.findFragmentById(R.id.container);
        if (mCurrentFragment == null) {
            mCurrentFragment = new QuoteCategoryFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.container, mCurrentFragment)
                    .commit();
        }
    }

    @Override
    public void workWithFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        setFabBackgroundImage(mFab, fabImageResourceId);
        mAddFabListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDoWhenFabIsPressed();
            }
        };
        mEditFabListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddQuoteActivity.newIntent(QuoteCategoryMainActivity.this, mCurrentId, mTitle);
                startActivity(intent);
            }
        };
        if (mFabListenerTag == 0) {
            mFab.setOnClickListener(mAddFabListener);
        } else {
            setFabBackgroundImage(mFab, R.drawable.ic_create_white_24dp);
            mFab.setOnClickListener(mEditFabListener);
        }
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, QuoteCategoryMainActivity.class);
        intent.putExtra(QUOTE_TYPE_INTENT_QCMA, titleName);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE_QCMA, mCurrentFragment);
        if (findViewById(R.id.detail_fragment_container) != null && mDetailFragment != null) {
            getSupportFragmentManager().putFragment(outState, TABLET_FRAGMENT_TAG_BUNDLE_QCMA, mDetailFragment);
        }
        outState.putString(QUOTE_TYPE_BUNDLE_QCMA, mTitle);
        outState.putInt(FAB_TAG_BUNDLE_QCMA, mFabListenerTag);
        outState.putLong(CURRENT_ID_BUNDLE_QCMA, mCurrentId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCategorySelected(String quoteCategory, String quoteType) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = AllQuoteCurrentCategoryActivity.newIntent(this,
                    quoteCategory, quoteType);
            startActivity(intent);
        } else {
            setFabBackgroundImage(mFab, fabImageResourceId);
            mFab.setOnClickListener(mAddFabListener);
            mDetailFragment = AllQuoteCurrentCategoryFragment.newInstance(quoteCategory, quoteType);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, mDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onQuoteSelected(ArrayList<Long> listOfQuotesId, final Long currentId) {
        setFabBackgroundImage(mFab, R.drawable.ic_create_white_24dp);
        mFabListenerTag = -1;
        mCurrentId = currentId;
        mFab.setOnClickListener(mEditFabListener);
        mDetailFragment = CurrentQuoteFragment.newInstance(currentId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_fragment_container, mDetailFragment)
                .commit();
    }
}
