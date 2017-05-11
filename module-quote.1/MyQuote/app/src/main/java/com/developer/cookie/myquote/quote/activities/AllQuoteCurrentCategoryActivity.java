package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.abstract_class.SingleFragmentAbstractActivity;
import com.developer.cookie.myquote.quote.fragments.AllQuoteCurrentCategoryFragment;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;

public class AllQuoteCurrentCategoryActivity extends SingleFragmentAbstractActivity implements AllQuoteCurrentCategoryFragment.AllQuoteCurrentCategoryCallbacks {
    private static final String QUOTE_CATEGORY_INTENT = "all_quote_current_category_activity_quote_category_intent";
    private static final String QUOTE_TYPE_INTENT = "all_quote_current_category_activity_quote_type_intent";
    private static final String CURRENT_FRAGMENT_TAG_BUNDLE = "all_quote_current_category_activity_current_fragment_tag_bundle";
    private static final String DETAIL_FRAGMENT_TAG_BUNDLE = "all_quote_current_category_activity_detail_fragment_tag_bundle";
    private Fragment mMainFragment;
    private Fragment mDetailFragment;
    private String mCategoryName;

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        super.defineInputData(saveInstanceState);
        if (saveInstanceState != null) {
            mMainFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE);
            if (findViewById(R.id.detail_fragment_container) != null && mDetailFragment != null) {
                mDetailFragment = getSupportFragmentManager().getFragment(saveInstanceState, DETAIL_FRAGMENT_TAG_BUNDLE);
            }
            mQuotesType = saveInstanceState.getString(QUOTE_TYPE_INTENT);
            mCategoryName = saveInstanceState.getString(QUOTE_CATEGORY_INTENT);
        } else if (getIntent().getStringExtra(QUOTE_TYPE_INTENT) != null &&
                getIntent().getStringExtra(QUOTE_CATEGORY_INTENT) != null) {
            mQuotesType = getIntent().getStringExtra(QUOTE_TYPE_INTENT);
            mCategoryName = getIntent().getStringExtra(QUOTE_CATEGORY_INTENT);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public Fragment createFragment() {
        mMainFragment = AllQuoteCurrentCategoryFragment.newInstance(mCategoryName, mQuotesType);
        return mMainFragment;
    }

    @Override
    public int setFabImageResourceId() {
        if (findViewById(R.id.detail_fragment_container) != null) {
            return R.drawable.ic_create_white_24dp;
        }
        return super.setFabImageResourceId();
    }

    @Override
    public void defineActionWhenFabIsPressed() {
        if (!mCategoryName.equals("")) {
            Intent intent = AddQuoteActivity.newIntent(this, mQuotesType, mCategoryName);
            startActivity(intent);
        }
    }

    public static Intent newIntent(Context context, String categoryName, String quoteType) {
        Intent intent = new Intent(context, AllQuoteCurrentCategoryActivity.class);
        intent.putExtra(QUOTE_CATEGORY_INTENT, categoryName);
        intent.putExtra(QUOTE_TYPE_INTENT, quoteType);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = QuoteCategoryMainActivity.newIntent(this, mQuotesType);
                startActivity(upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMainFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE, mMainFragment);
        }
        if (findViewById(R.id.detail_fragment_container) != null && mDetailFragment != null && mDetailFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, DETAIL_FRAGMENT_TAG_BUNDLE, mDetailFragment);
        }
        outState.putString(QUOTE_TYPE_INTENT, mQuotesType);
        outState.putString(QUOTE_CATEGORY_INTENT, mCategoryName);
    }

    @Override
    public void onQuoteSelected(long currentId) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CurrentQuotePagerActivity.newIntent(this, mCategoryName, currentId, mQuotesType);
            startActivity(intent);
        } else {
            defineDetailFragment(currentId);
            defineFabListenerForTablet(currentId);
        }
    }

    private void defineDetailFragment(Long currentId) {
        if (!isFinishing()) {
            mDetailFragment = CurrentQuoteFragment.newInstance(currentId, mQuotesType);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, mDetailFragment)
                    .commitAllowingStateLoss();
        }
    }

    private void defineFabListenerForTablet(final Long currentId) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddQuoteActivity.newIntent(AllQuoteCurrentCategoryActivity.this, currentId, mQuotesType);
                startActivity(intent);
            }
        });
    }
}
