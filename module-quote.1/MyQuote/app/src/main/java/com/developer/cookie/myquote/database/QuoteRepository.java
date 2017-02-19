package com.developer.cookie.myquote.database;


import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.QuotePropertiesEnum;

import java.util.HashMap;
import java.util.List;

/**
 * QuoteRepository interface for repository pattern.
 * It helps to abstract realm layer.
 */
public interface QuoteRepository {

    /**
     * Method requests QuoteCategory list from the database.
     * @return List<QuoteCategory>
     */
    List<QuoteCategory> getListOfQuoteCategories();

    /**
     * Method saves quotes.
     * @param mapOfQuoteProperties key is field of quote properties (from QuotePropertiesEnum class),
     *                            value is current value (from user input).
     */
    void saveQuote(HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties);

    /**
     * Method requests QuoteText list from the database.
     * @return List<QuoteText>
     */
    List<QuoteText> getListOfQuoteText();

    /**
     * Method requests QuoteText list from the database by category.
     * @param category
     * @return List<QuoteText>
     */
    List<QuoteText> getListOfQuoteTextByCategory(String category);

    /**
     * Method requests QuoteText object from the database by quote text.
     * @param quoteText
     * @return QuoteText
     */
    List<QuoteText> getQuoteTextObjectsByQuoteText(String quoteText);

    /**
     * Method closes realm connection.
     */
    void closeDbConnect();
}
