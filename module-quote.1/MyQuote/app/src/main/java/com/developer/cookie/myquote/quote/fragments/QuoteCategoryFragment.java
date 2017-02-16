package com.developer.cookie.myquote.quote.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.quote.adapters.QuoteCategoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * QuoteCategoryFragment is used for presentation list of all categories (quote category) and
 * count of the quotes for every this category.
 */
public class QuoteCategoryFragment extends Fragment {

    private static final String LOG_TAG = QuoteCategoryFragment.class.getSimpleName();
    View rootView;
    QuoteDataRepository quoteDataRepository;

    List<Integer> quoteCountListOfEveryCategory;
    List<String> listOfCategories;
    Pair<List<String>,List<Integer>> pair;

    QuoteCategoryRecyclerViewAdapter quoteCategoryRecyclerViewAdapter;

    public QuoteCategoryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quote_category, container, false);

        quoteDataRepository = new QuoteDataRepository();
        RealmResults<QuoteCategory> quoteCategoryList = quoteDataRepository.getListOfQuoteCategories();
        quoteCategoryList.addChangeListener(new RealmChangeListener<RealmResults<QuoteCategory>>() {
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
        return rootView;
    }

    /**
     * Method create Pair object for custom adapter. First parameter is name list of category,
     * second is quote count list of this categories.
     * @param element list of quote category realm object.
     */
    private void createPairObject(List<QuoteCategory> element) {
                // Create parameters for pair object
                quoteCountListOfEveryCategory = new ArrayList<Integer>();
                for (int i = 0; i < element.size(); i++) {
                    quoteCountListOfEveryCategory.add(element.get(i).getQuoteCountCurrentCategory());
                }
                listOfCategories = new ArrayList<>();
                for (int i = 0; i<element.size(); i++) {
                    String currentCategory = element.get(i).getCategory();
                    listOfCategories.add(currentCategory);
                }
                pair = new Pair<>(listOfCategories, quoteCountListOfEveryCategory);
            }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quoteDataRepository.closeDbConnect();
    }
}