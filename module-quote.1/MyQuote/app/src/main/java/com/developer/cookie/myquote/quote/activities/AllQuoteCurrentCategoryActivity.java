package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.abstract_class.SingleFragmentAbstractActivity;
import com.developer.cookie.myquote.quote.fragments.AllQuoteCurrentCategoryFragment;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;
import com.developer.cookie.myquote.utils.ColorationTextChar;

import java.util.ArrayList;
import java.util.Locale;

public class AllQuoteCurrentCategoryActivity extends SingleFragmentAbstractActivity implements AllQuoteCurrentCategoryFragment.AllQuoteCurrentCategoryCallbacks {
    private static final String QUOTE_CATEGORY_INTENT = "all_quote_current_category_activity_quote_category_intent";
    private static final String QUOTE_TYPE_INTENT = "all_quote_current_category_activity_quote_type_intent";
    private static final String CURRENT_FRAGMENT_TAG_BUNDLE = "all_quote_current_category_activity_current_fragment_tag_bundle";
    private Fragment mMainFragment;
    private String mQuotesType;
    Fragment mDetailFragment;
    String mCategoryName;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public Fragment createFragment() {
        mMainFragment = AllQuoteCurrentCategoryFragment.newInstance(mCategoryName, mQuotesType);
        return mMainFragment;
    }

    public static Intent newIntent(Context context, String categoryName, String quoteType) {
        Intent intent = new Intent(context, AllQuoteCurrentCategoryActivity.class);
        intent.putExtra(QUOTE_CATEGORY_INTENT, categoryName);
        intent.putExtra(QUOTE_TYPE_INTENT, quoteType);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE, mMainFragment);
        outState.putString(QUOTE_TYPE_INTENT, mQuotesType);
        outState.putString(QUOTE_CATEGORY_INTENT, mCategoryName);
    }

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        super.defineInputData(saveInstanceState);
        if (saveInstanceState != null) {
            mMainFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE);
            mQuotesType = saveInstanceState.getString(QUOTE_TYPE_INTENT);
            mCategoryName = saveInstanceState.getString(QUOTE_CATEGORY_INTENT);
        } else if (getIntent().getSerializableExtra(QUOTE_TYPE_INTENT) != null) {
            mQuotesType = getIntent().getStringExtra(QUOTE_TYPE_INTENT);
            mCategoryName = getIntent()
                    .getStringExtra(QUOTE_CATEGORY_INTENT);
        }
        //Set new text style for toolbar title
        String localeLanguage = Locale.getDefault().getLanguage();
        setTitle(ColorationTextChar.setFirstVowelColor(mQuotesType, localeLanguage, this));
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
    public void defineActionWhenFabIsPressed() {
        if (mCategoryName != null && !mCategoryName.equals("")) {
            Intent intent = AddQuoteActivity.newIntent(this, mQuotesType, mCategoryName);
            startActivity(intent);
        }
    }

    @Override
    public void onQuoteSelected(ArrayList<Long> listOfQuotesId, final Long currentId) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CurrentQuotePagerActivity.newIntent(this, listOfQuotesId, currentId, mQuotesType);
            startActivity(intent);
        } else {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            View.OnClickListener editFabListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = AddQuoteActivity.newIntent(AllQuoteCurrentCategoryActivity.this, currentId, mQuotesType);
                    startActivity(intent);
                }
            };
            setFabBackgroundImage(fab, R.drawable.ic_create_white_24dp);
            fab.setOnClickListener(editFabListener);

            if (!isFinishing()) {
                mDetailFragment = CurrentQuoteFragment.newInstance(currentId, mQuotesType);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_fragment_container, mDetailFragment)
                        .commitAllowingStateLoss();
            }
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
