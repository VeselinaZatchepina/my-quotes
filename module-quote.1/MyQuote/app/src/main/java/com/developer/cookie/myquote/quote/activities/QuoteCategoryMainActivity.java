package com.developer.cookie.myquote.quote.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.quote.abstract_class.NavigationAbstractActivity;
import com.developer.cookie.myquote.quote.fragments.QuoteCategoryFragment;
import com.developer.cookie.myquote.utils.AppBarLayoutExpended;

public class QuoteCategoryMainActivity extends NavigationAbstractActivity {

    private static final String LOG_TAG = QuoteCategoryMainActivity.class.getSimpleName();
    public static final String QUOTE_TYPE_INTENT_QCMA = "com.developer.cookie.myquote.quote_type_quote_category_qcma";
    public static final String CURRENT_FRAGMENT_TAG_BUNDLE_QCMA = "com.developer.cookie.myquote.current_fragment_tag_bundle_qcma";
    public static final String TABLET_FRAGMENT_TAG_BUNDLE_QCMA = "com.developer.cookie.myquote.tablet_fragment_tag_bundle_qcma";
    public static final String QUOTE_TYPE_BUNDLE_QCMA = "com.developer.cookie.myquote.quote_type_bundle_qcma";
    public static final String CURRENT_ID_BUNDLE_QCMA = "com.developer.cookie.myquote.current_id_bundle_qcma";
    Fragment mCurrentFragment;
    FloatingActionButton mFab;
    int fabImageResourceId = setFabImageResourceId();
    //Fragment to tablet
    Fragment mDetailFragment;
    String mTitle;
    View.OnClickListener mAddFabListener;
    long mCurrentId;

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager().getFragment(saveInstanceState, CURRENT_FRAGMENT_TAG_BUNDLE_QCMA);
            if (findViewById(R.id.detail_fragment_container) != null && mDetailFragment != null) {
                mDetailFragment = getSupportFragmentManager().getFragment(saveInstanceState, TABLET_FRAGMENT_TAG_BUNDLE_QCMA);
            }
            mTitle = saveInstanceState.getString(QUOTE_TYPE_BUNDLE_QCMA);
            mCurrentId = saveInstanceState.getLong(CURRENT_ID_BUNDLE_QCMA);
        } else if (getIntent().getStringExtra(QUOTE_TYPE_INTENT_QCMA) != null) {
            mTitle = getIntent().getStringExtra(QUOTE_TYPE_INTENT_QCMA);
        } else {
            mTitle = getString(R.string.book_quote_type);
        }
        setTitle(mTitle);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_nav_drawer;
    }

    @Override
    public void defineFragment() {
        getScreenOrientation(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mCurrentFragment = fragmentManager.findFragmentById(R.id.container);
        if (mCurrentFragment == null) {
            mCurrentFragment = QuoteCategoryFragment.newInstance(getTitle().toString());
            fragmentManager.beginTransaction()
                    .add(R.id.container, mCurrentFragment)
                    .commit();
        }
    }

    /**
     * Method checks screen orientation. And if it on landscape or reverse landscape we set
     * AppBarLayout not expandable
     *
     * @param context
     * @return screen orientation as string
     */
    public String getScreenOrientation(Context context) {
        final int screenOrientation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (screenOrientation) {
            case Surface.ROTATION_0:
                return "android portrait screen";
            case Surface.ROTATION_90:
                setAppBarNotExpandable();
                return "android landscape screen";
            case Surface.ROTATION_180:
                return "android reverse portrait screen";
            default:
                setAppBarNotExpandable();
                return "android reverse landscape screen";
        }
    }

    private void setAppBarNotExpandable() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        if (appBarLayout != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            Configuration configuration = getResources().getConfiguration();
            AppBarLayoutExpended.setAppBarLayoutExpended(this, appBarLayout, layoutParams,
                    collapsingToolbarLayout, configuration);
        }
    }

    @Override
    public void defineFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        setFabBackgroundImage(mFab, fabImageResourceId);
        mAddFabListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defineActionWhenFabIsPressed();
            }
        };
        mFab.setOnClickListener(mAddFabListener);
    }

    public static Intent newIntent(Context context, String titleName) {
        Intent intent = new Intent(context, QuoteCategoryMainActivity.class);
        intent.putExtra(QUOTE_TYPE_INTENT_QCMA, titleName);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT_TAG_BUNDLE_QCMA, mCurrentFragment);
        if (findViewById(R.id.detail_fragment_container) != null && mDetailFragment != null) {
            getSupportFragmentManager().putFragment(outState, TABLET_FRAGMENT_TAG_BUNDLE_QCMA, mDetailFragment);
        }
        outState.putString(QUOTE_TYPE_BUNDLE_QCMA, mTitle);
        outState.putLong(CURRENT_ID_BUNDLE_QCMA, mCurrentId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
