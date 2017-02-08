package com.developer.cookie.myquote.quote.fragments;


import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.adapters.QuoteCategoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * QuoteCategoryFragment is used for presentation list of all categories (quote category) and
 * count of the quotes for every this category.
 */
public class QuoteCategoryFragment extends Fragment {

    Realm realm;
    View rootView;
    QuoteCategoryRecyclerViewAdapter quoteCategoryRecyclerViewAdapter;
    List<String> listOfCategories;
    List<Integer> quoteCountListOfEveryCategory;
    Pair<List<String>,List<Integer>> pair;

    public QuoteCategoryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quote_category, container, false);

        realm = Realm.getDefaultInstance();
        final RealmResults<QuoteCategory> categories = realm
                .where(QuoteCategory.class)
                .findAllSortedAsync("id", Sort.DESCENDING);
        categories.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
            @Override
            public void onChange(RealmResults<QuoteCategory> element) {
                createPairObject(element);
                // Create and set custom adapter for recyclerview
                RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                quoteCategoryRecyclerViewAdapter= new QuoteCategoryRecyclerViewAdapter(pair);
                recyclerView.setAdapter(quoteCategoryRecyclerViewAdapter);
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new AddQuoteFragment())
                        .commit();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp, getActivity().getTheme()));
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_white_24dp));
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RealmResults<QuoteText> quoteTexts = realm.where(QuoteText.class).findAllAsync();
        // Hear changes in QuoteText class and set new dates to adapter
        quoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {
                RealmResults<QuoteCategory> categories = realm
                        .where(QuoteCategory.class)
                        .findAllSortedAsync("id", Sort.DESCENDING);
                categories.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
                    @Override
                    public void onChange(RealmResults<QuoteCategory> element) {
                        createPairObject(element);
                        quoteCategoryRecyclerViewAdapter.changeDate(pair);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    /**
     * Method create Pair object for custom adapter.
     * @param element list of quote category realm object.
     */
    private void createPairObject(RealmResults<QuoteCategory> element) {
                // Create parameters for pair object
                quoteCountListOfEveryCategory = new ArrayList<Integer>();
                for (int i = 0; i < element.size(); i++) {
                    RealmResults<QuoteText> quoteTexts = realm.where(QuoteText.class)
                            .equalTo("category.category", element.get(i).getCategory()).findAll();
                    quoteCountListOfEveryCategory.add(quoteTexts.size());
                }
                listOfCategories = new ArrayList<>();
                for (int i = 0; i<element.size(); i++) {
                    String currentCategory = element.get(i).getCategory();
                    listOfCategories.add(currentCategory);
                }
                pair = new Pair<>(listOfCategories, quoteCountListOfEveryCategory);
            }
    }