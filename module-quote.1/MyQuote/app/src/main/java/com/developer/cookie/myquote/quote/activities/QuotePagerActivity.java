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

public class QuotePagerActivity extends AppCompatActivity {
    private static final String QUOTE_LIST = "com.developer.cookie.myquote.quote_list";
    private ViewPager viewPager;
    ArrayList<String> quoteList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_pager);
        quoteList = getIntent().getStringArrayListExtra(QUOTE_LIST);

        viewPager = (ViewPager) findViewById(R.id.quote_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CurrentQuoteFragment.newInstance(quoteList.get(position));
            }

            @Override
            public int getCount() {
                return quoteList.size();
            }
        });

        FloatingActionButton editFab = (FloatingActionButton) findViewById(R.id.edit_fab);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddQuoteActivity.newIntent(QuotePagerActivity.this);
                startActivity(intent);
            }
        });
    }

    public static Intent newIntent(Context context, ArrayList<String> quoteList) {
        Intent intent = new Intent(context, QuotePagerActivity.class);
        intent.putStringArrayListExtra(QUOTE_LIST, quoteList);
        return intent;
    }
}
