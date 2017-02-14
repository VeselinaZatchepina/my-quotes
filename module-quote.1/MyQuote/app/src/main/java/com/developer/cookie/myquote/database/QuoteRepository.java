package com.developer.cookie.myquote.database;


import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.quote.QuotePropertiesEnum;

import java.util.HashMap;
import java.util.List;

/**
 * QuoteRepository interface for repository pattern.
 */
public interface QuoteRepository {

    List<QuoteCategory> getListOfQuoteCategories();

    void saveQuote(HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties);

    List<QuoteText> getListOfQuoteText();

    List<QuoteText> getListOfQuoteTextByCategory(String category);

    void closeDbConnect();
}
