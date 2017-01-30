package com.developer.cookie.myquote.quote;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.model.Category;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.adapters.QuoteCategoryRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuoteCategoryFragment extends Fragment {

    Realm realm;
    View rootView;
    QuoteCategoryRecyclerViewAdapter quoteCategoryRecyclerViewAdapter;
    List<String> listOfCategories;

    public QuoteCategoryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_quote_category, container, false);

        //Create adapter for category recyclerView
        realm = Realm.getDefaultInstance();

        RealmResults<Category> typifications = realm
                .where(Category.class)
                .findAllSorted("id", Sort.DESCENDING);

        listOfCategories = new ArrayList<>();
        for (int i = 0; i<typifications.size(); i++) {
            String currentCategory = typifications.get(i).getCategory();
//            if (listOfCategories.contains(currentCategory)) {
//                continue;
//            }
            listOfCategories.add(currentCategory);
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        quoteCategoryRecyclerViewAdapter= new QuoteCategoryRecyclerViewAdapter(listOfCategories);
        recyclerView.setAdapter(quoteCategoryRecyclerViewAdapter);

        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container, new AddQuoteFragment()).commit();
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

        //realm = Realm.getDefaultInstance();
        RealmResults<QuoteText> quoteTexts = realm.where(QuoteText.class).findAll();

        // set up a Realm change listener
        quoteTexts.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {

                RealmResults<Category> typifications = realm
                        .where(Category.class)
                        .findAllSorted("id", Sort.DESCENDING);

                listOfCategories = new ArrayList<>();
                for (int i = 0; i<typifications.size(); i++) {
                    String currentCategory = typifications.get(i).getCategory();
//                    if (listOfCategories.contains(currentCategory)) {
//                        continue;
//                    }
                    listOfCategories.add(currentCategory);
                }

                quoteCategoryRecyclerViewAdapter.changeDate(listOfCategories);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
