package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.abstract_class.SingleFragmentAbstractActivity;
import com.developer.cookie.myquote.quote.fragments.AllQuoteCurrentCategoryFragment;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;

import java.util.ArrayList;

public class AllQuoteCurrentCategoryActivity extends SingleFragmentAbstractActivity implements AllQuoteCurrentCategoryFragment.AllQuoteCurrentCategoryCallbacks {

    public static final String QUOTE_CATEGORY_INTENT_AQCCA = "com.developer.cookie.myquote.quote_category_intent_aqcca";
    public static final String QUOTE_TYPE_INTENT_AQCCA = "com.developer.cookie.myquote.quote_type_intent_aqcca";
    public static final String CURRENT_FRAGMENT_TAG_BUNDLE_AQCCA = "com.developer.cookie.myquote.current_fragment_tag_bundle_aqcca";

    private Fragment mCurrentFragment;
    private String mQuoteTypeAllQuotesCategory;

    Fragment mDetailFragment;

    String mTitle;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public Fragment createFragment() {
        if (findViewById(R.id.detail_fragment_container) == null) {
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
            appBarLayout.setExpanded(false);
        }
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
    public void onQuoteSelected(ArrayList<Long> listOfQuotesId, final Long currentId) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CurrentQuotePagerActivity.newIntent(this, listOfQuotesId, currentId, mQuoteTypeAllQuotesCategory);
            startActivity(intent);
        } else {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            View.OnClickListener editFabListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = AddQuoteActivity.newIntent(AllQuoteCurrentCategoryActivity.this, currentId, mQuoteTypeAllQuotesCategory);
                    startActivity(intent);
                }
            };
            setFabBackgroundImage(fab, R.drawable.ic_create_white_24dp);
            fab.setOnClickListener(editFabListener);
            mDetailFragment = CurrentQuoteFragment.newInstance(currentId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, mDetailFragment)
                    .commit();
        }
    }

    public void setFabBackgroundImage(FloatingActionButton fab, int imageResourceId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setImageDrawable(getResources().getDrawable(imageResourceId, getTheme()));
        } else {
            fab.setImageDrawable(getResources().getDrawable(imageResourceId));
        }
    }
}
