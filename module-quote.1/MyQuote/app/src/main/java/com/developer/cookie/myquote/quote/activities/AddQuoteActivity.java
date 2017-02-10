package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.fragments.AddQuoteFragment;

public class AddQuoteActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new AddQuoteFragment();
    }

    @Override
    public int setFabImageResourceId() {
        return R.drawable.ic_done_white_24dp;
    }

    @Override
    public void toDoWhenFabIsPressed() {
        ((AddQuoteFragment)currentFragment).createMapOfQuoteProperties();
        Intent intent = QuoteCategoryMainActivity.newIntent(this);
        startActivity(intent);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, AddQuoteActivity.class);
    }
}
