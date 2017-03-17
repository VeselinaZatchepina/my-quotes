package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.fragments.AddQuoteFragment;

public class AddQuoteActivity extends SingleFragmentActivity {
    public static final String QUOTE_TEXT_ID = "com.developer.cookie.myquote.quote_text_id";
    public static final String QUOTE_TYPE_ADD_QUOTE = "com.developer.cookie.myquote.quote_type_add_quote";
    public static final String CURRENT_FRAGMENT_TAG_AQA = "com.developer.cookie.myquote.quote.fragments.current_fragment_tag_aqa";
    public static final String QUOTE_TYPE_AQA = "com.developer.cookie.myquote.quote.fragments.quote_type_aqa";
    Fragment mCurrentFragment;
    String mQuoteType;

    @Override
    public Fragment createFragment() {
        mCurrentFragment = new AddQuoteFragment();
        return mCurrentFragment;
    }

    @Override
    public int setFabImageResourceId() {
        return R.drawable.ic_done_white_24dp;
    }

    @Override
    public void toDoWhenFabIsPressed() {
        AddQuoteFragment addQuoteFragment =  ((AddQuoteFragment)currentFragment);
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
        intent.putExtra(QUOTE_TEXT_ID, currentQuoteTextId);
        intent.putExtra(QUOTE_TYPE_ADD_QUOTE, quoteType);
        return intent;
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, AddQuoteActivity.class);
        intent.putExtra(QUOTE_TYPE_ADD_QUOTE, titleName);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_AQA, mCurrentFragment);
        outState.putString(QUOTE_TYPE_AQA, mQuoteType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT_TAG_AQA);
        mQuoteType = savedInstanceState.getString(QUOTE_TYPE_AQA);
    }

    @Override
    public void getAndSetDataFromSaveInstanceState(Bundle saveInstanceState) {
        super.getAndSetDataFromSaveInstanceState(saveInstanceState);
        if (saveInstanceState != null) {
            mQuoteType = saveInstanceState.getString(QUOTE_TYPE_AQA);
        } else if (getIntent().getSerializableExtra(QUOTE_TYPE_ADD_QUOTE) != null) {
            mQuoteType = getIntent().getStringExtra(QUOTE_TYPE_ADD_QUOTE);
        }
        setTitle(mQuoteType);
    }
}
