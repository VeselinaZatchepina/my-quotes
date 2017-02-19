package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developer.cookie.myquote.quote.fragments.AllQuoteCurrentCategoryFragment;

public class AllQuoteCurrentCategoryActivity extends SingleFragmentActivity {
    public static final String CATEGORY_NAME = "com.developer.cookie.myquote.category_name";

    @Override
    public Fragment createFragment() {
        return new AllQuoteCurrentCategoryFragment();
    }

    public static Intent newIntent(Context context, String categoryName) {
        Intent intent = new Intent(context, AllQuoteCurrentCategoryActivity.class);
        intent.putExtra(CATEGORY_NAME, categoryName);
        return intent;
    }
}
