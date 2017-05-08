package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.abstract_class.SingleFragmentAbstractActivity;
import com.developer.cookie.myquote.quote.fragments.AddQuoteFragment;

public class AddQuoteActivity extends SingleFragmentAbstractActivity {
    public static final String QUOTE_TEXT_ID_INTENT_AQA = "com.developer.cookie.myquote.quote_text_id_intent_aqa";
    public static final String QUOTE_TYPE_INTENT_AQA = "com.developer.cookie.myquote.quote_type_intent_aqa";
    public static final String CURRENT_FRAGMENT_TAG_BUNDLE_AQA = "com.developer.cookie.myquote.current_fragment_tag_bundle_aqa";
    public static final String QUOTE_TYPE_BUNDLE_AQA = "com.developer.cookie.myquote.fragments.quote_type_bundle_aqa";
    public static final String QUOTE_CATEGORY_INTENT_AQA = "com.developer.cookie.myquote.fragments.quote_category_intent_aqa";
    Fragment mCurrentFragment;
    String mCurrentCategory;
    Long mCurrentId;

    @Override
    public Fragment createFragment() {
        mCurrentFragment = AddQuoteFragment.newInstance(mCurrentId, mQuotesType, mCurrentCategory);
        return mCurrentFragment;
    }

    @Override
    public int setFabImageResourceId() {
        return R.drawable.ic_done_white_24dp;
    }

    @Override
    public void defineActionWhenFabIsPressed() {
        AddQuoteFragment addQuoteFragment = ((AddQuoteFragment) currentFragment);
        if (!addQuoteFragment.isEditTextEmpty() && !addQuoteFragment.isSpinnerSelectedItemHint()) {
            addQuoteFragment.createMapOfQuoteProperties();
            this.finish();
        }
        if (addQuoteFragment.isSpinnerSelectedItemHint()) {
            Toast.makeText(this, "Choose category", Toast.LENGTH_LONG).show();
        }
    }

    public static Intent newIntent(Context context, Long currentQuoteTextId, String quoteType) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE_TEXT_ID_INTENT_AQA, currentQuoteTextId);
        intent.putExtra(QUOTE_TYPE_INTENT_AQA, quoteType);
        return intent;
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE_TYPE_INTENT_AQA, titleName);
        return intent;
    }

    public static Intent newIntent(Context context, String titleName, String currentCategory) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE_TYPE_INTENT_AQA, titleName);
        intent.putExtra(QUOTE_CATEGORY_INTENT_AQA, currentCategory);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE_AQA, mCurrentFragment);
        outState.putString(QUOTE_TYPE_BUNDLE_AQA, mQuotesType);
    }

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        super.defineInputData(saveInstanceState);
        if (saveInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE_AQA);
            mQuotesType = saveInstanceState.getString(QUOTE_TYPE_BUNDLE_AQA);
        } else {
            mQuotesType = getIntent().getStringExtra(QUOTE_TYPE_INTENT_AQA);
            mCurrentCategory = getIntent()
                    .getStringExtra(QUOTE_CATEGORY_INTENT_AQA);
            mCurrentId = getIntent().getLongExtra(QUOTE_TEXT_ID_INTENT_AQA, -1);
        }
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
}
