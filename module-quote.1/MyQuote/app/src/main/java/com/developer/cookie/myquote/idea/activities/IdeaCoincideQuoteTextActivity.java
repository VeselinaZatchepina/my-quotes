package com.developer.cookie.myquote.idea.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.idea.fragments.IdeaCoincideQuoteTextFragment;
import com.developer.cookie.myquote.quote.abstract_class.SingleFragmentAbstractActivity;

import java.util.ArrayList;

/**
 * IdeaCoincideQuoteTextActivity helps to show all quotes from GetIdeaActivity
 */
public class IdeaCoincideQuoteTextActivity extends SingleFragmentAbstractActivity {

    public static final String LIST_COINCIDE_QUOTE_TEXT_INTENT_ICQTA = "com.developer.cookie.myquote.list_coincide_quote_text_intent_icqta";

    @Override
    public void defineInputData(Bundle saveInstanceState) {
        super.defineInputData(saveInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return super.getLayoutResId();
    }

    @Override
    public Fragment createFragment() {
        mQuotesType = getString(R.string.idea_title);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        ArrayList<String> listOfCoicideQuoteText = getIntent().getStringArrayListExtra(LIST_COINCIDE_QUOTE_TEXT_INTENT_ICQTA);
        return IdeaCoincideQuoteTextFragment.newInstance(listOfCoicideQuoteText);
    }

    @Override
    public int setFabImageResourceId() {
        return super.setFabImageResourceId();
    }

    @Override
    public void defineActionWhenFabIsPressed() {
        super.defineActionWhenFabIsPressed();
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
}
