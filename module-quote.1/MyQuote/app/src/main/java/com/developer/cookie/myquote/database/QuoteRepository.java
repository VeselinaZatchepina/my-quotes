package com.developer.cookie.myquote.database;


import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.QuotePropertiesEnum;

import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 * QuoteRepository interface for repository pattern.
 * It helps to work with realm db.
 */
interface QuoteRepository {

    /**
     * Method requests QuoteCategory list from the database.
     *
     * @param quoteType
     * @return List<QuoteCategory>
     */
    List<QuoteCategory> getListOfQuoteCategories(String quoteType);

    /**
     * Method saves quotes.
     *
     * @param mapOfQuoteProperties key is field of quote properties (from QuotePropertiesEnum class),
     *                             value is current value (from user input).
     */
    void saveQuote(HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties);

    /**
     * Method requests QuoteText list from the database by category.
     *
     * @param category
     * @param quoteType
     * @return List<QuoteText>
     */
    List<QuoteText> getListOfQuoteTextByCategory(String category, String quoteType);

    /**
     * Method requests QuoteText object from the database by quote text.
     *
     * @param quoteText
     * @param quoteType
     * @return QuoteText
     */
    List<QuoteText> getQuoteTextObjectsByQuoteText(String quoteText, String quoteType);

    /**
     * Method requests QuoteText object from the database by quote text id.
     *
     * @param quoteTextId
     * @return QuoteText
     */
    List<QuoteText> getQuoteTextObjectsByQuoteId(Long quoteTextId);

    /**
     * Method saves edited quote
     *
     * @param quoteTextId          id current quote
     * @param mapOfQuoteProperties
     */
    void saveChangedQuoteObject(long quoteTextId, HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties);

    /**
     * Method delete quote object from db
     *
     * @param currentQuoteTextId
     * @param quoteType
     */
    void deleteQuoteTextObjectById(long currentQuoteTextId, String quoteType);

    /**
     * Method delete all quotes with current category
     *
     * @param currentCategory
     * @param quoteType
     */
    void deleteAllQuotesWithCurrentCategory(final String currentCategory, String quoteType);

    /**
     * Method requests all QuoteText object from the database.
     *
     * @return list of quote text
     */
    RealmResults<QuoteText> getAllQuoteText();

    /**
     * Method closes realm connection.
     */
    void closeDbConnect();
}
