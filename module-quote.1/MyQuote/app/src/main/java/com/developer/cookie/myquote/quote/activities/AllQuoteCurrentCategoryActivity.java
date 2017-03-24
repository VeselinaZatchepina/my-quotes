package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.developer.cookie.myquote.quote.fragments.AllQuoteCurrentCategoryFragment;

import java.util.ArrayList;

public class AllQuoteCurrentCategoryActivity extends SingleFragmentActivity implements AllQuoteCurrentCategoryFragment.AllQuoteCurrentCategoryCallbacks
{
    public static final String QUOTE_CATEGORY_INTENT_AQCCA = "com.developer.cookie.myquote.quote_category_intent_aqcca";
    public static final String QUOTE_TYPE_INTENT_AQCCA = "com.developer.cookie.myquote.quote_type_intent_aqcca";
    public static final String CURRENT_FRAGMENT_TAG_BUNDLE_AQCCA = "com.developer.cookie.myquote.current_fragment_tag_bundle_aqcca";

    private Fragment mCurrentFragment;
    private String mQuoteTypeAllQuotesCategory;

    @Override
    public Fragment createFragment() {
        mCurrentFragment = new AllQuoteCurrentCategoryFragment();
        return mCurrentFragment;
    }

    public static Intent newIntent(Context context, String categoryName, String quoteType) {
        Intent intent = new Intent(context, AllQuoteCurrentCategoryActivity.class);
        intent.putExtra(QUOTE_CATEGORY_INTENT_AQCCA, categoryName);
        intent.putExtra(QUOTE_TYPE_INTENT_AQCCA, quoteType);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE_AQCCA, mCurrentFragment);
        outState.putString(QUOTE_TYPE_INTENT_AQCCA, mQuoteTypeAllQuotesCategory);
    }

    @Override
    public void getAndSetDataFromSaveInstanceState(Bundle saveInstanceState) {
        super.getAndSetDataFromSaveInstanceState(saveInstanceState);
        if (saveInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE_AQCCA);
            mQuoteTypeAllQuotesCategory = saveInstanceState.getString(QUOTE_TYPE_INTENT_AQCCA);
        } else if (getIntent().getSerializableExtra(QUOTE_TYPE_INTENT_AQCCA) != null) {
            mQuoteTypeAllQuotesCategory = getIntent().getStringExtra(QUOTE_TYPE_INTENT_AQCCA);
        }
        setTitle(mQuoteTypeAllQuotesCategory);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = QuoteCategoryMainActivity.newIntent(this, getTitle().toString());
                startActivity(upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQuoteSelected(ArrayList<Long> listOfQuotesId, Long currentId) {
        Intent intent = CurrentQuotePagerActivity.newIntent(this, listOfQuotesId, currentId, mQuoteTypeAllQuotesCategory);
        startActivity(intent);
    }
}
