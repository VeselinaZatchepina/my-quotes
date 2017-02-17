package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developer.cookie.myquote.quote.fragments.QuoteCategoryFragment;

public class QuoteCategoryMainActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new QuoteCategoryFragment();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, QuoteCategoryMainActivity.class);
    }
}
