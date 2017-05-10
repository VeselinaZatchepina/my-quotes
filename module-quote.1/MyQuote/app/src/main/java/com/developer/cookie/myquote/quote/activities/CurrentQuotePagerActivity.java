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
import android.view.ViewGroup;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.fragments.CurrentQuoteFragment;
import com.developer.cookie.myquote.utils.AppBarLayoutExpended;
import com.developer.cookie.myquote.utils.ColorationTextChar;

import java.util.Locale;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CurrentQuotePagerActivity extends AppCompatActivity {
    private static final String LOG_TAG = CurrentQuotePagerActivity.class.getSimpleName();
    private static final String QUOTE_CATEGORY_INTENT = "current_quote_pager_activity_quote_id_list_intent";
    private static final String CURRENT_ID_INTENT = "current_quote_pager_activity_current_id_intent";
    private static final String QUOTE_TYPE_INTENT = "current_quote_pager_activity_quote_category_intent";
    private static final String CURRENT_FRAGMENT_TAG_BUNDLE = "current_quote_pager_activity_current_fragmet_tag_bundle";
    private static final String QUOTE_TYPE_PAGER_FOR_SAVE = "current_quote_pager_activity_quote_type_pager_for_save";
    private static final String QUOTE_CATEGORY_FOR_SAVE = "current_quote_pager_activity_quote_category_for_save";
    private static final String QUOTE_ID_FOR_SAVE = "current_quote_pager_activity_quote_id_for_save";
    private RealmResults<QuoteText> mQuotes;
    private long mCurrentQuoteTextId;
    private long mQuoteTextIdForIntent;
    private String mQuoteType;
    private Fragment mMainFragment;
    private String mCurrentCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineInputData(savedInstanceState);
        setContentView(R.layout.activity_quote_pager);
        defineToolbar();
        setAppBarNotExpandable();
        setNewTitleStyle();
        mQuotes.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {
                defineViewPager(element);
            }
        });
        defineFab();
    }

    private void defineInputData(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            mMainFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE);
            mQuoteType = saveInstanceState.getString(QUOTE_TYPE_PAGER_FOR_SAVE);
            mCurrentCategory = saveInstanceState.getString(QUOTE_CATEGORY_FOR_SAVE);
            mCurrentQuoteTextId = saveInstanceState.getLong(QUOTE_ID_FOR_SAVE);
        } else {
            mQuoteType = getIntent().getStringExtra(QUOTE_TYPE_INTENT);
            mCurrentCategory = getIntent().getStringExtra(QUOTE_CATEGORY_INTENT);
            mCurrentQuoteTextId = (long) getIntent().getSerializableExtra(CURRENT_ID_INTENT);
        }
        QuoteDataRepository quoteDataRepository = new QuoteDataRepository();
        mQuotes = quoteDataRepository.getListOfQuotesByCategory(mCurrentCategory, mQuoteType);
    }

    private void defineToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setAppBarNotExpandable() {
        if (findViewById(R.id.detail_fragment_container) == null) {
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            Configuration configuration = getResources().getConfiguration();
            AppBarLayoutExpended.setAppBarLayoutExpended(this, appBarLayout, layoutParams, collapsingToolbarLayout, configuration);
        }
    }

    private void setNewTitleStyle() {
        String localeLanguage = Locale.getDefault().getLanguage();
        setTitle(ColorationTextChar.setFirstVowelColor(mQuoteType, localeLanguage, this));
    }

    private void defineViewPager(final RealmResults<QuoteText> element) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.quote_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                mMainFragment = CurrentQuoteFragment.newInstance(element.get(position).getId(), mQuoteType);
                return mMainFragment;
            }

            @Override
            public int getCount() {
                return element.size();
            }

            @Override
            public void finishUpdate(ViewGroup container) {
                try{
                    super.finishUpdate(container);
                } catch (NullPointerException nullPointerException){
                    System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
                }
            }
        });
        setViewPagerOnClickedQuotePosition(viewPager, element);
        setViewPagerOnPageChangeListener(viewPager, element);
    }

    private void setViewPagerOnClickedQuotePosition(ViewPager viewPager, RealmResults<QuoteText> element) {
        for (int i = 0; i < element.size(); i++) {
            if (element.get(i).getId() == mCurrentQuoteTextId) {
                viewPager.setCurrentItem(i);
                mQuoteTextIdForIntent = element.get(viewPager.getCurrentItem()).getId();
            }
        }
    }

    private void setViewPagerOnPageChangeListener(ViewPager viewPager, final RealmResults<QuoteText> element) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mQuoteTextIdForIntent = element.get(position).getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void defineFab() {
        FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.edit_fab);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddQuoteActivity.newIntent(CurrentQuotePagerActivity.this, mQuoteTextIdForIntent, mQuoteType);
                startActivity(intent);
            }
        });
    }

    public static Intent newIntent(Context context, String currentCategory, long currentId, String quoteType) {
        Intent intent = new Intent(context, CurrentQuotePagerActivity.class);
        intent.putExtra(QUOTE_CATEGORY_INTENT, currentCategory);
        intent.putExtra(CURRENT_ID_INTENT, currentId);
        intent.putExtra(QUOTE_TYPE_INTENT, quoteType);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMainFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE, mMainFragment);
        }
        outState.putString(QUOTE_TYPE_PAGER_FOR_SAVE, mQuoteType);
        outState.putString(QUOTE_CATEGORY_FOR_SAVE, mCurrentCategory);
        outState.putLong(QUOTE_ID_FOR_SAVE, mCurrentQuoteTextId);
    }
}
