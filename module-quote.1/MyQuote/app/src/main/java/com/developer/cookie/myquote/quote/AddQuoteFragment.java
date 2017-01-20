package com.developer.cookie.myquote.quote;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.Page;
import com.developer.cookie.myquote.database.model.Publisher;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.database.model.Typification;
import com.developer.cookie.myquote.database.model.Year;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddQuoteFragment extends Fragment {

    private static final String LOG_TAG = AddQuoteFragment.class.getSimpleName();

    private Realm realm;


    public AddQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_add_quote, container, false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save quote
                EditText quoteText = (EditText) rootView.findViewById(R.id.quote_text);
                final EditText bookName = (EditText) rootView.findViewById(R.id.book_name);
                EditText authorName = (EditText) rootView.findViewById(R.id.author_name);
                EditText pageNumber = (EditText) rootView.findViewById(R.id.page_number);
                EditText yearNumber = (EditText) rootView.findViewById(R.id.year_number);
                EditText publishName = (EditText) rootView.findViewById(R.id.publish_name);

                final String currentQuoteText = quoteText.getText().toString();
                final String currentBookName = bookName.getText().toString();
                final String currentAuthorName = authorName.getText().toString();
                final int currentPageNumber = Integer.valueOf(pageNumber.getText().toString());
                final int currentYearNumber = Integer.valueOf(yearNumber.getText().toString());
                final String currentPublishName = publishName.getText().toString();

                // Create the Realm instance
                realm = Realm.getDefaultInstance();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Publisher publisher1 = realm.createObject(Publisher.class);
                        publisher1.setId(1);
                        publisher1.setPublisherName(currentPublishName);

                        Year year1 = realm.createObject(Year.class);
                        year1.setId(1);
                        year1.setYearNumber(currentYearNumber);

                        BookAuthor bookAuthor1 = realm.createObject(BookAuthor.class);
                        bookAuthor1.setId(1);
                        bookAuthor1.setBookAuthor(currentAuthorName);

                        BookName bookName1 = realm.createObject(BookName.class);
                        bookName1.setId(1);
                        bookName1.setBookName(currentBookName);
                        bookName1.setPublisher(publisher1);
                        bookName1.setYear(year1);
                        bookName1.getBookAuthors().add(bookAuthor1);

                        Page page1 = realm.createObject(Page.class);
                        page1.setId(1);
                        page1.setPageNumber(currentPageNumber);

                        Typification typification1 = realm.createObject(Typification.class);
                        typification1.setId(getNextKey(typification1));
                        typification1.setCategory("IT");
                        typification1.setType("QUOTE");

                        QuoteText quoteText1 = realm.createObject(QuoteText.class);
                        quoteText1.setId(getNextKey(quoteText1));
                        quoteText1.setQuoteText(currentQuoteText);
                        quoteText1.setBookName(bookName1);
                        quoteText1.setPage(page1);
                        quoteText1.setTypification(typification1);

                    }
                });

                RealmResults<QuoteText> result2 = realm.where(QuoteText.class)
                        .equalTo("id", 2)
                        .findAll();

                Log.v(LOG_TAG, "size =  " + result2.size());

                Fragment quoteCategoryFragment = new QuoteCategoryFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, quoteCategoryFragment);
                ft.commit();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    public int getNextKey(RealmObject currentClass)
    {
        int id;

        try {
            id = realm.where(currentClass.getClass()).max("id").intValue() + 1;
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

}
