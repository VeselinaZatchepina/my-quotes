package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.fragments.QuoteCategoryFragment;

public class QuoteCategoryMainActivity extends SingleFragmentActivity {
    public static final String ACTIVITY_TITLE_QUOTE_CATEGORY = "activity_title_quote_category";

    @Override
    public Fragment createFragment() {
        String title = getIntent().getStringExtra(ACTIVITY_TITLE_QUOTE_CATEGORY);
        if (title != null) {
            setTitle(title);
        } else {
            setTitle(getString(R.string.book_quote_type));
        }
        Log.v("MAIN", getTitle().toString());
        return new QuoteCategoryFragment();
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, QuoteCategoryMainActivity.class);
        intent.putExtra(ACTIVITY_TITLE_QUOTE_CATEGORY, titleName);
        return intent;
    }
}
