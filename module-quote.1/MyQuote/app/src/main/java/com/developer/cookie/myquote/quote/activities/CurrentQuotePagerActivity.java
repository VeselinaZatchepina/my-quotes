package com.developer.cookie.myquote.quote.activities;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;
import com.developer.cookie.myquote.utils.AppBarLayoutExpended;
import com.developer.cookie.myquote.utils.ColorationTextChar;

import java.util.ArrayList;
import java.util.Locale;

public class CurrentQuotePagerActivity extends AppCompatActivity {
    private static final String LOG_TAG = CurrentQuotePagerActivity.class.getSimpleName();
    public static final String QUOTE_ID_LIST_INTENT_CQPA = "com.developer.cookie.myquote.quote_id_list_intent_cqpa";
    public static final String CURRENT_ID_INTENT_CQPA = "com.developer.cookie.myquote.current_id_intent_cqpa";
    public static final String QUOTE_TYPE_INTENT_CQPA = "com.developer.cookie.myquote.quote_type_intent_cqpa";
    public static final String CURRENT_FRAGMENT_TAG_BUNDLE_CQPA = "com.developer.cookie.myquote.current_fragmet_tag_bundle_cqpa";
    public static final String QUOTE_TYPE_PAGER_FOR_SAVE_CQPA = "com.developer.cookie.myquote.quote_type_pager_for_save_cqpa";
    public static final String QUOTE_ID_LIST_FOR_SAVE_CQPA = "com.developer.cookie.myquote.quote_id_list_for_save_cqpa";
    public static final String QUOTE_ID_FOR_SAVE_CQPA = "com.developer.cookie.myquote.quote_id_for_save_cqpa";

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (findViewById(R.id.detail_fragment_container) == null) {
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            Configuration configuration = getResources().getConfiguration();
            AppBarLayoutExpended.setAppBarLayoutExpended(this, appBarLayout, layoutParams, collapsingToolbarLayout, configuration);
        }
        if (savedInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager().getFragment(savedInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE_CQPA);
            mQuoteType = savedInstanceState.getString(QUOTE_TYPE_PAGER_FOR_SAVE_CQPA);
            mQuoteIdList = (ArrayList<Long>) savedInstanceState.getSerializable(QUOTE_ID_LIST_FOR_SAVE_CQPA);
            mCurrentQuoteTextId = savedInstanceState.getLong(QUOTE_ID_FOR_SAVE_CQPA);
        } else {
            mQuoteType = getIntent().getStringExtra(QUOTE_TYPE_INTENT_CQPA);
            mQuoteIdList = (ArrayList<Long>) getIntent().getSerializableExtra(QUOTE_ID_LIST_INTENT_CQPA);
            mCurrentQuoteTextId = (long) getIntent().getSerializableExtra(CURRENT_ID_INTENT_CQPA);
        }
        String localeLanguage = Locale.getDefault().getLanguage();
        setTitle(ColorationTextChar.setFirstVowelColor(mQuoteType, localeLanguage, this));
        //Work with ViewPager
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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mQuoteTextIdForIntent = mQuoteIdList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
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
        intent.putExtra(QUOTE_ID_LIST_INTENT_CQPA, quoteList);
        intent.putExtra(CURRENT_ID_INTENT_CQPA, currentId);
        intent.putExtra(QUOTE_TYPE_INTENT_CQPA, quoteType);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE_CQPA, mCurrentFragment);
        outState.putString(QUOTE_TYPE_PAGER_FOR_SAVE_CQPA, mQuoteType);
        outState.putSerializable(QUOTE_ID_LIST_FOR_SAVE_CQPA, mQuoteIdList);
        outState.putLong(QUOTE_ID_FOR_SAVE_CQPA, mCurrentQuoteTextId);
    }
}
