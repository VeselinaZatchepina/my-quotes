package com.developer.cookie.myquote.quote;


import com.developer.cookie.myquote.database.model.BookAuthor;
import com.developer.cookie.myquote.database.model.BookName;
import com.developer.cookie.myquote.database.model.Category;
import com.developer.cookie.myquote.database.model.Page;
import com.developer.cookie.myquote.database.model.Publisher;
import com.developer.cookie.myquote.database.model.QuoteDate;
import com.developer.cookie.myquote.database.model.QuoteText;
import com.developer.cookie.myquote.database.model.Type;
import com.developer.cookie.myquote.database.model.Year;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class QuoteCreator {

    private Realm realm;

    public QuoteCreator() { }

    public void createAndSaveQuote(final HashMap<QuotePropertiesEnum, String> mapOfQuoteProperties) {
        realm = Realm.getDefaultInstance();
        //Write data to DB
        realm.executeTransactionAsync(new Realm.Transaction() {
                                          @Override
                                          public void execute(Realm realm) {
                                              Publisher publisherRealmObject = realm.createObject(Publisher.class);
                                              publisherRealmObject.setId(getNextKey(publisherRealmObject, realm));
                                              publisherRealmObject.setPublisherName(mapOfQuoteProperties.get(QuotePropertiesEnum.PUBLISH_NAME));

                                              Year yearRealmObject = realm.createObject(Year.class);
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

                                              Page pageRealmObject = realm.createObject(Page.class);
                                              pageRealmObject.setId(getNextKey(pageRealmObject, realm));
                                              pageRealmObject.setPageNumber(mapOfQuoteProperties.get(QuotePropertiesEnum.PAGE_NUMBER));

                                              String valueOfCategory = mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_CATEGORY);
                                              RealmResults<Category> results = realm.where(Category.class)
                                                      .contains("category", valueOfCategory)
                                                      .findAll();
                                              Category categoryRealmObject;
                                              if (results == null || results.isEmpty()) {
                                                  categoryRealmObject = realm.createObject(Category.class);
                                                  categoryRealmObject.setId(getNextKey(categoryRealmObject, realm));
                                                  categoryRealmObject.setCategory(valueOfCategory);
                                              } else {
                                                  categoryRealmObject = results.get(0);
                                              }

                                              Type type = realm.createObject(Type.class);
                                              type.setId((getNextKey(type, realm)));
                                              type.setType(mapOfQuoteProperties.get(QuotePropertiesEnum.QUOTE_TYPE));

                                              QuoteDate quoteDate = realm.createObject(QuoteDate.class);
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
