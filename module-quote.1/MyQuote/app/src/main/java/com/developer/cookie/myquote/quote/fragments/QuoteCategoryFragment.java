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
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.adapters.QuoteCategoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * QuoteCategoryFragment is used for presentation list of all categories (quote category) and
 * count of the quotes for every this category.
 */
public class QuoteCategoryFragment extends Fragment {

    View rootView;
    QuoteCategoryRecyclerViewAdapter quoteCategoryRecyclerViewAdapter;
    List<String> listOfCategories;
    List<Integer> quoteCountListOfEveryCategory;
    Pair<List<String>,List<Integer>> pair;
    QuoteDataRepository quoteDataRepository;

    public QuoteCategoryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quote_category, container, false);

        quoteDataRepository = new QuoteDataRepository();
        List<QuoteCategory> quoteCategoryList = quoteDataRepository.getListOfQuoteCategories();
                createPairObject(quoteCategoryList);
                // Create and set custom adapter for recyclerview
                RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                quoteCategoryRecyclerViewAdapter= new QuoteCategoryRecyclerViewAdapter(pair);
                recyclerView.setAdapter(quoteCategoryRecyclerViewAdapter);
        return rootView;
    }

    /**
     * Method create Pair object for custom adapter.
     * @param element list of quote category realm object.
     */
    private void createPairObject(List<QuoteCategory> element) {
                // Create parameters for pair object
                quoteCountListOfEveryCategory = new ArrayList<Integer>();
                for (int i = 0; i < element.size(); i++) {
                    String category = element.get(i).getCategory();
                    List<QuoteText> quoteTexts = quoteDataRepository.getListOfQuoteTextByCategory(category);
                    quoteCountListOfEveryCategory.add(quoteTexts.size());
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