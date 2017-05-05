package com.developer.cookie.myquote.utils;


import android.view.View;
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
                                                     TextView yearNumberView,
                                                     String quoteType) {
        QuoteText quoteTextObject = realmResults.first();
        quoteTextView.setText(quoteTextObject.getQuoteText());
        if (quoteType.equals("Book quote")) {
            BookName bookName = quoteTextObject.getBookName();
            bookNameView.setText(bookName.getBookNameValue());
            RealmList<BookAuthor> bookAuthors = bookName.getBookAuthors();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bookAuthors.size(); i++) {
                if (i != bookAuthors.size() - 1) {
                    builder.append(bookAuthors.get(i).getBookAuthorName()).append(", ");
                } else {
                    builder.append(bookAuthors.get(i).getBookAuthorName());
                }
            }
            String authorName = builder.toString();
            authorNameView.setText(authorName);
            String bookPage = quoteTextObject.getPage().getPageNumber();
            pageNumberView.setText(bookPage);
            String bookPublisher = bookName.getPublisher().getPublisherName();
            publisherNameTextView.setText(bookPublisher);
            String bookPublicationYear = bookName.getYear().getYearNumber();
            yearNumberView.setText(bookPublicationYear);

        } else {
            bookNameView.setVisibility(View.GONE);
            authorNameView.setVisibility(View.GONE);
            pageNumberView.setVisibility(View.GONE);
            yearNumberView.setVisibility(View.GONE);
            publisherNameTextView.setVisibility(View.GONE);
        }
    }
}
