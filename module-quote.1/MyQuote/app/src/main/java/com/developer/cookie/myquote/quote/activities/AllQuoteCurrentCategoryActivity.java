package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developer.cookie.myquote.quote.fragments.AllQuoteCurrentCategoryFragment;

public class AllQuoteCurrentCategoryActivity extends SingleFragmentActivity {
    public static final String CATEGORY_NAME = "com.developer.cookie.myquote.category_name";
    public static final String QUOTE_TYPE_ALL_QUOTE_CURRENT = "com.developer.cookie.myquote.quote_type_all_quote_current";

    @Override
    public Fragment createFragment() {
        setTitle(getIntent().getStringExtra(QUOTE_TYPE_ALL_QUOTE_CURRENT));
        return new AllQuoteCurrentCategoryFragment();
    }

    public static Intent newIntent(Context context, String categoryName, String quoteType) {
        Intent intent = new Intent(context, AllQuoteCurrentCategoryActivity.class);
        intent.putExtra(CATEGORY_NAME, categoryName);
        intent.putExtra(QUOTE_TYPE_ALL_QUOTE_CURRENT, quoteType);
        return intent;
    }
}
