package com.developer.cookie.myquote.database;


import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.BookPage;
import com.developer.cookie.myquote.database.model.BookPublicationYear;
import com.developer.cookie.myquote.database.model.BookPublisher;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteCreationDate;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.database.model.QuoteType;
import com.developer.cookie.myquote.quote.QuotePropertiesEnum;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * QuoteDataRepository helps to abstract realm layer.
 */
public class QuoteDataRepository implements QuoteRepository {

    private static final String LOG_TAG = QuoteDataRepository.class.getSimpleName();
    private final Realm realm;

    public QuoteDataRepository() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public RealmResults<QuoteCategory> getListOfQuoteCategories() {
        return realm.where(QuoteCategory.class).findAllSortedAsync("id");
    }

    @Override
    public void saveQuote(final HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm realm) {
                                              BookPublisher publisherRealmObject = realm.createObject(BookPublisher.class);
                                              publisherRealmObject.setId(getNextKey(publisherRealmObject, realm));
                                              publisherRealmObject.setPublisherName(mapOfQuoteProperties.get(QuotePropertiesEnum.PUBLISHER_NAME));

                                              BookPublicationYear yearRealmObject = realm.createObject(BookPublicationYear.class);
                                              yearRealmObject.setId(getNextKey(yearRealmObject, realm));
                                              yearRealmObject.setYearNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.YEAR_NUMBER));

                                              BookAuthor bookAuthorRealmObject = realm.createObject(BookAuthor.class);
                                              bookAuthorRealmObject.setId(getNextKey(bookAuthorRealmObject, realm));
                                              bookAuthorRealmObject.setBookAuthor(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_AUTHOR));

                                              BookName bookNameRealmObject = realm.createObject(BookName.class);
                                              bookNameRealmObject.setId(getNextKey(bookNameRealmObject, realm));
                                              bookNameRealmObject.setBookName(mapOfQuoteProperties.get(QuotePropertiesEnum.BOOK_NAME));
                                              bookNameRealmObject.setPublisher(publisherRealmObject);
                                              bookNameRealmObject.setYear(yearRealmObject);
                                              bookNameRealmObject.getBookAuthors().add(bookAuthorRealmObject);

                                              BookPage pageRealmObject = realm.createObject(BookPage.class);
                                              pageRealmObject.setId(getNextKey(pageRealmObject, realm));
                                              pageRealmObject.setPageNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.PAGE_NUMBER));

                                              String valueOfCategory = mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CATEGORY);
                                              RealmResults<QuoteCategory> results = realm.where(QuoteCategory.class)
                                                      .contains("category", valueOfCategory)
                                                      .findAll();
                                              QuoteCategory categoryRealmObject;
                                              if (results == null || results.isEmpty()) {
                                                  categoryRealmObject = realm.createObject(QuoteCategory.class);
                                                  categoryRealmObject.setId(getNextKey(categoryRealmObject, realm));
                                                  categoryRealmObject.setCategory(valueOfCategory);
                                                  categoryRealmObject.setQuoteCountCurrentCategory(
                                                          categoryRealmObject.getQuoteCountCurrentCategory() + 1);
                                              } else {
                                                  categoryRealmObject = results.get(0);
                                                  categoryRealmObject.setQuoteCountCurrentCategory(
                                                          categoryRealmObject.getQuoteCountCurrentCategory() + 1);
                                              }

                                              QuoteType type = realm.createObject(QuoteType.class);
                                              type.setId((getNextKey(type, realm)));
                                              type.setType(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TYPE));

                                              QuoteCreationDate quoteDate = realm.createObject(QuoteCreationDate.class);
                                              quoteDate.setId(getNextKey(quoteDate, realm));
                                              quoteDate.setQuoteDate(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CREATE_DATE));

                                              QuoteText quoteTextRealmObject = realm.createObject(QuoteText.class);
                                              quoteTextRealmObject.setId(getNextKey(quoteTextRealmObject, realm));
                                              quoteTextRealmObject.setQuoteText(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TEXT));
                                              quoteTextRealmObject.setBookName(bookNameRealmObject);
                                              quoteTextRealmObject.setPage(pageRealmObject);
                                              quoteTextRealmObject.setCategory(categoryRealmObject);
                                              quoteTextRealmObject.setType(type);
                                          }
                                      });
    }

    /**
     * Method is used for increment "id".
     * @param currentClass
     * @param realm
     * @return id
     */
    private int getNextKey(RealmObject currentClass, Realm realm) {
        int id;
        try {
            id = realm.where(currentClass.getClass()).max("id").intValue() + 1;
        } catch(ArrayIndexOutOfBoundsException ex) {
            id = 0;
        }
        return id;
    }

    @Override
    public RealmResults<QuoteText> getListOfQuoteText() {
        return realm.where(QuoteText.class).findAllAsync();
    }

    @Override
    public RealmResults<QuoteText> getListOfQuoteTextByCategory(String category) {
        return realm.where(QuoteText.class)
                .equalTo("category.category", category).findAllAsync();
    }

    @Override
    public RealmResults<QuoteText> getQuoteTextObjectsByQuoteText(String quoteText) {
        return realm.where(QuoteText.class).equalTo("quoteText", quoteText).findAllAsync();
    }

    @Override
    public RealmResults<QuoteText> getQuoteTextObjectsByQuoteId(Long quoteTextId) {
        return realm.where(QuoteText.class).equalTo("id", quoteTextId).findAllAsync();
    }

    @Override
    public void closeDbConnect() {
        realm.close();
    }
}
