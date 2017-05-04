package com.developer.cookie.myquote.idea.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.idea.fragments.IdeaCoincideQuoteTextFragment;
import com.developer.cookie.myquote.quote.abstract_class.SingleFragmentAbstractActivity;
import com.developer.cookie.myquote.utils.AppBarLayoutExpended;
import com.developer.cookie.myquote.utils.ColorationTextChar;

import java.util.ArrayList;
import java.util.Locale;

/**
 * IdeaCoincideQuoteTextActivity helps to show all quotes from GetIdeaActivity
 */
public class IdeaCoincideQuoteTextActivity extends SingleFragmentAbstractActivity {

    public static final String LIST_COINCIDE_QUOTE_TEXT_INTENT_ICQTA = "com.developer.cookie.myquote.list_coincide_quote_text_intent_icqta";

    @Override
    public void getAndSetDataFromSaveInstanceState(Bundle saveInstanceState) {
        super.getAndSetDataFromSaveInstanceState(saveInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return super.getLayoutResId();
    }

    @Override
    public Fragment createFragment() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        ArrayList<String> listOfCoicideQuoteText = getIntent().getStringArrayListExtra(LIST_COINCIDE_QUOTE_TEXT_INTENT_ICQTA);
        return IdeaCoincideQuoteTextFragment.newInstance(listOfCoicideQuoteText);
    }

    @Override
    public void otherAction() {
        super.otherAction();
        setAppBarNotExpandable();
        //Set new text style for toolbar title
        String localeLanguage = Locale.getDefault().getLanguage();
        setTitle(ColorationTextChar.setFirstVowelColor(getString(R.string.idea_title), localeLanguage, this));
    }

    @Override
    public int setFabImageResourceId() {
        return super.setFabImageResourceId();
    }

    @Override
    public void toDoWhenFabIsPressed() {
        super.toDoWhenFabIsPressed();
    }

    public static Intent newIntent(Context context, ArrayList<String> listOfCoincideQuoteText) {
        Intent intent = new Intent(context, IdeaCoincideQuoteTextActivity.class);
        intent.putExtra(LIST_COINCIDE_QUOTE_TEXT_INTENT_ICQTA, listOfCoincideQuoteText);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = GetIdeaActivity.newIntent(this);
                startActivity(upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
}
