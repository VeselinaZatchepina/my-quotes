package com.developer.cookie.myquote.utils;


import android.widget.TextView;

import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.Types;

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
        BookName bookName;
        QuoteText quote = realmResults.first();
        fillQuoteTextField(quoteTextView, quote);
        if (quoteType.equals(Types.BOOK_QUOTE)) {
            bookName = fillBookNameField(bookNameView, quote);
            fillBookAuthorField(authorNameView, bookName);
            fillBookPageField(pageNumberView, quote);
            fillBookPublisherField(publisherNameTextView, bookName);
            fillBookPublicationYearField(yearNumberView, bookName);
        }
    }

    private static void fillQuoteTextField(TextView quoteTextView, QuoteText quote) {
        quoteTextView.setText(quote.getQuoteText());
    }

    private static BookName fillBookNameField(TextView bookNameView, QuoteText quote) {
        BookName bookName = quote.getBookName();
        bookNameView.setText(bookName.getBookNameValue());
        return bookName;
    }

    private static void fillBookAuthorField(TextView authorNameView, BookName bookName) {
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
    }

    private static void fillBookPageField(TextView pageNumberView, QuoteText quote) {
        String bookPage = quote.getPage().getPageNumber();
        pageNumberView.setText(bookPage);
    }

    private static void fillBookPublisherField(TextView publisherNameTextView, BookName bookName) {
        String bookPublisher = bookName.getPublisher().getPublisherName();
        publisherNameTextView.setText(bookPublisher);
    }

    private static void fillBookPublicationYearField(TextView yearNumberView, BookName bookName) {
        String bookPublicationYear = bookName.getYear().getYearNumber();
        yearNumberView.setText(bookPublicationYear);
    }
}
