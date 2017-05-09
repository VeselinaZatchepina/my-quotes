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
    private static final String QUOTE_TEXT_ID_INTENT = "add_quote_activity_quote_text_id_intent";
    private static final String QUOTE_TYPE_INTENT = "add_quote_activity_quote_type_intent";
    private static final String CURRENT_FRAGMENT_TAG_BUNDLE = "add_quote_activity_current_fragment_tag_bundle";
    private static final String QUOTE_TYPE_BUNDLE = "add_quote_activity_quote_type_bundle";
    private static final String QUOTE_CATEGORY_INTENT = "add_quote_activity_quote_category_intent";
    private Fragment mCurrentFragment;
    private String mCurrentCategory;
    private Long mCurrentId;

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        super.defineInputData(saveInstanceState);
        if (saveInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE);
            mQuotesType = saveInstanceState.getString(QUOTE_TYPE_BUNDLE);
        } else {
            mQuotesType = getIntent().getStringExtra(QUOTE_TYPE_INTENT);
            mCurrentCategory = getIntent().getStringExtra(QUOTE_CATEGORY_INTENT);
            mCurrentId = getIntent().getLongExtra(QUOTE_TEXT_ID_INTENT, -1);
        }
    }

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
        if (!addQuoteFragment.isEditTextEmpty() && !addQuoteFragment.isSpinnerSelectedItemHint() &&
                addQuoteFragment.isNumbersPositive()) {
            addQuoteFragment.createMapOfQuoteProperties();
            this.finish();
        }
        if (addQuoteFragment.isSpinnerSelectedItemHint()) {
            Toast.makeText(this, getString(R.string.toast_choose_category), Toast.LENGTH_LONG).show();
        }
    }

    public static Intent newIntent(Context context, Long currentQuoteTextId, String quoteType) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE_TEXT_ID_INTENT, currentQuoteTextId);
        intent.putExtra(QUOTE_TYPE_INTENT, quoteType);
        return intent;
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE_TYPE_INTENT, titleName);
        return intent;
    }

    public static Intent newIntent(Context context, String titleName, String currentCategory) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE_TYPE_INTENT, titleName);
        intent.putExtra(QUOTE_CATEGORY_INTENT, currentCategory);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE, mCurrentFragment);
        outState.putString(QUOTE_TYPE_BUNDLE, mQuotesType);
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
