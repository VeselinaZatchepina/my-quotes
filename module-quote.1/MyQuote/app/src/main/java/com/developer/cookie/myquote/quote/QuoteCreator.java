package com.developer.cookie.myquote.quote;


import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.BookPage;
import com.developer.cookie.myquote.database.model.BookPublicationYear;
import com.developer.cookie.myquote.database.model.BookPublisher;
import com.developer.cookie.myquote.database.model.QuoteCategory;
import com.developer.cookie.myquote.database.model.QuoteCreationDate;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.database.model.QuoteType;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Class is used for create RealmObject and save it in database.
 */
public class QuoteCreator {

    private final String LOG_TAG = QuoteCreator.class.getSimpleName();
    private Realm realm;

    public QuoteCreator() { }

    /**
     * Method create RealmObjects and save it in database.
     * @param mapOfQuoteProperties it is map of quote properties. Key is field of QuotePropertiesEnum class,
     *                             value is user input data.
     */
    public void createAndSaveQuote(final HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        realm = Realm.getDefaultInstance();
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
                                              } else {
                                                  categoryRealmObject = results.get(0);
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
                                      }
                , new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {

                    }
                });

        realm.close();
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
}
