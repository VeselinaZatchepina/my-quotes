package com.developer.cookie.myquote.quote;

/**
 * Class is a enumeration of quotes properties.
 * Every quote has text, book name where it placed, book author or authors,
 * publisher name, year number when this book was published,
 * page number where quote is placed (for simplification searching process),
 * quote category (IT, Math, philosophy and others),
 * quote type (quotes from books or my own quotes)
 * and date when this quote was saved by user.
 * */
public enum QuotePropertiesEnum {
    QUOTE_TEXT,
    BOOK_NAME,
    BOOK_AUTHOR,
    QUOTE_CATEGORY,
    PAGE_NUMBER,
    YEAR_NUMBER,
    PUBLISHER_NAME,
    QUOTE_CREATION_DATE,
    QUOTE_TYPE,
    CATEGORY_ICON
}
