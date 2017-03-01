package com.developer.cookie.myquote.utils;


import android.widget.TextView;

import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.QuoteText;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * FillViewsWithCurrentQuoteDataHelper class helps us to fill all views we needed
 * with current quote data
 */
public class FillViewsWithCurrentQuoteDataHelper {

    public static void fillViewsWithCurrentQuoteData(RealmResults<QuoteText> realmResults,
                                                     TextView quoteTextView, TextView bookNameView,
                                                     TextView authorNameView, TextView pageNumberView,
                                                     TextView publisherNameTextView,
                                                     TextView yearNumberView) {
        QuoteText quoteTextObject = realmResults.first();
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
}
