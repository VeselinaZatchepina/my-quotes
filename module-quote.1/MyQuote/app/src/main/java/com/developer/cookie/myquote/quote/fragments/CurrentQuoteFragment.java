package com.developer.cookie.myquote.quote.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.cookie.myquote.R;
import com.developer.cookie.myquote.database.QuoteDataRepository;
import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.QuoteText;

import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * CurrentQuoteFragment is used for display information about quote.
 */
public class CurrentQuoteFragment extends Fragment {
    private static final String CURRENT_QUOTE_ID = "current_quote_text";
    Long currentQuoteTextId;
    QuoteDataRepository quoteDataRepository;
    RealmResults<QuoteText> currentQuoteObjectList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentQuoteTextId = getArguments().getLong(CURRENT_QUOTE_ID);
        quoteDataRepository = new QuoteDataRepository();
        currentQuoteObjectList = quoteDataRepository.getQuoteTextObjectsByQuoteId(currentQuoteTextId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.current_quote_fragment, container, false);
        final TextView quoteTextView = (TextView) rootView.findViewById(R.id.current_quote_text);
        final TextView bookNameView = (TextView) rootView.findViewById(R.id.current_book_name);
        final TextView authorNameView = (TextView) rootView.findViewById(R.id.current_author_name);
        final TextView pageNumberView = (TextView) rootView.findViewById(R.id.current_page_number);
        final TextView publisherNameTextView = (TextView) rootView.findViewById(R.id.current_publisher_name);
        final TextView yearNumberView = (TextView) rootView.findViewById(R.id.current_year_number);

        currentQuoteObjectList.addChangeListener(new RealmChangeListener<RealmResults<QuoteText>>() {
            @Override
            public void onChange(RealmResults<QuoteText> element) {
                QuoteText quoteTextObject = element.first();
                quoteTextView.setText(quoteTextObject.getQuoteText());
                BookName bookName = quoteTextObject.getBookName();
                bookNameView.setText(bookName.getBookName());
                RealmList<BookAuthor> bookAuthors = bookName.getBookAuthors();
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < bookAuthors.size(); i++) {
                    if (i != bookAuthors.size() - 1) {
                        builder.append(bookAuthors.get(i).getBookAuthor()).append(", ");
                    } else {
                        builder.append(bookAuthors.get(i).getBookAuthor());
                    }
                }
                String authorName = builder.toString();
                authorNameView.setText(authorName);
                pageNumberView.setText(quoteTextObject.getPage().getPageNumber());
                publisherNameTextView.setText(bookName.getPublisher().getPublisherName());
                yearNumberView.setText(bookName.getYear().getYearNumber());
            }
        });
        return rootView;
    }

    public static CurrentQuoteFragment newInstance(Long currentQuoteTextId) {
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_QUOTE_ID, currentQuoteTextId);
        CurrentQuoteFragment fragment = new CurrentQuoteFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
