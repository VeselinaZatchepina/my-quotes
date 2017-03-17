package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;

import java.util.ArrayList;

public class CurrentQuotePagerActivity extends AppCompatActivity {
    private static final String LOG_TAG = CurrentQuotePagerActivity.class.getSimpleName();
    public static final String QUOTE_ID_LIST = "com.developer.cookie.myquote.quote_list";
    public static final String CURRENT_ID = "com.developer.cookie.myquote.current_position";
    public static final String QUOTE_TYPE_PAGER = "com.developer.cookie.myquote.quote_type_pager";
    public static final String CURRENT_FRAGMENT_TAG_PA = "com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment";
    public static final String QUOTE_TYPE_PAGER_FOR_SAVE = "com.developer.cookie.myquote.quote_type_pager_for_save";
    public static final String QUOTE_ID_LIST_FOR_SAVE = "com.developer.cookie.myquote.quote_id_list_for_save";
    public static final String QUOTE_ID_FOR_SAVE = "com.developer.cookie.myquote.quote_id_for_save";
    private ViewPager mViewPager;
    ArrayList<Long> mQuoteIdList;
    long mCurrentQuoteTextId;
    long mQuoteTextIdForIntent;
    String mQuoteType;

    Fragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_pager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState != null) {
            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_PAGER_FOR_SAVE);
            mQuoteIdList = (ArrayList<Long>) savedInstanceState.getSerializable(QUOTE_ID_LIST_FOR_SAVE);
            mCurrentQuoteTextId = savedInstanceState.getLong(QUOTE_ID_FOR_SAVE);
        } else {
            mQuoteType = getIntent().getStringExtra(QUOTE_TYPE_PAGER);
            mQuoteIdList = (ArrayList<Long>) getIntent().getSerializableExtra(QUOTE_ID_LIST);
            mCurrentQuoteTextId = (long) getIntent().getSerializableExtra(CURRENT_ID);
        }
        setTitle(mQuoteType);
        mViewPager = (ViewPager) findViewById(R.id.quote_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                mCurrentFragment = CurrentQuoteFragment.newInstance(mQuoteIdList.get(position));
                return mCurrentFragment;
            }
            @Override
            public int getCount() {
                return mQuoteIdList.size();
            }
        });

        // set mViewPager on position of current clicked quote
        for (int i = 0; i < mQuoteIdList.size(); i++) {
            if (mQuoteIdList.get(i) == mCurrentQuoteTextId) {
                mViewPager.setCurrentItem(i);
                mQuoteTextIdForIntent = mQuoteIdList.get(mViewPager.getCurrentItem());
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                mQuoteTextIdForIntent = mQuoteIdList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.edit_fab);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddQuoteActivity.newIntent(CurrentQuotePagerActivity.this, mQuoteTextIdForIntent, mQuoteType);
                startActivity(intent);
            }
        });
    }

    public static Intent newIntent(Context context, ArrayList<Long> quoteList, long currentId, String quoteType) {
        Intent intent = new Intent(context, CurrentQuotePagerActivity.class);
        intent.putExtra(QUOTE_ID_LIST, quoteList);
        intent.putExtra(CURRENT_ID, currentId);
        intent.putExtra(QUOTE_TYPE_PAGER, quoteType);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_PA, mCurrentFragment);
        outState.putString(QUOTE_TYPE_PAGER_FOR_SAVE, mQuoteType);
        outState.putSerializable(QUOTE_ID_LIST_FOR_SAVE, mQuoteIdList);
        outState.putLong(QUOTE_ID_FOR_SAVE, mCurrentQuoteTextId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT_TAG_PA);
        mQuoteType = savedInstanceState.getString(QUOTE_TYPE_PAGER_FOR_SAVE);
        mQuoteIdList = (ArrayList<Long>) savedInstanceState.getSerializable(QUOTE_ID_LIST_FOR_SAVE);
        mCurrentQuoteTextId = savedInstanceState.getLong(QUOTE_ID_FOR_SAVE);
    }
}
