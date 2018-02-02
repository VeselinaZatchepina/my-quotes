package com.github.veselinazatchepina.myquotes.addquote

import android.util.Log
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class AddQuotePresenter(val quoteDataSource: QuoteDataSource,
                        val addQuoteView: AddQuoteContract.View) : AddQuoteContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private var bookCategoriesForSpinner = ArrayList<String>()

    init {
        addQuoteView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    //TODO add to compositedisposible
    override fun saveQuote(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        quoteDataSource.saveQuoteData(mapOfQuoteProperties, authors)
    }

    override fun getQuoteCategoriesList(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getQuoteCategories(quoteType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<QuoteCategoryModel>>() {
                    override fun onNext(list: List<QuoteCategoryModel>?) {
                        if (list != null) {
                            if (bookCategoriesForSpinner.isEmpty()) {
                                bookCategoriesForSpinner.clear()
                                addQuoteView.defineCategorySpinner(defineBookCategoriesListForView(list))
                            } else {
                                bookCategoriesForSpinner.clear()
                                addQuoteView.updateCategorySpinner(defineBookCategoriesListForView(list))
                            }
                        } else {
                            Log.v("GET BOOK CATEGORIES", "NULL")
                        }
                    }

                    override fun onComplete() {
                        Log.v("GET BOOK CATEGORIES", "COMPLETE")
                    }

                    override fun onError(t: Throwable?) {
                        Log.v("GET BOOK CATEGORIES", "OOPS")
                    }
                }))
    }

    private fun defineBookCategoriesListForView(list: List<QuoteCategoryModel>): List<String> {
        list.mapTo(bookCategoriesForSpinner) {
            it.quoteCategory?.categoryName?.toUpperCase() ?: ""
        }
        bookCategoriesForSpinner.add("+ add new category")
        bookCategoriesForSpinner.add("Select quote category")
        return bookCategoriesForSpinner
    }

    override fun addQuoteCategory(category: String) {
        bookCategoriesForSpinner.add(0, category)
        addQuoteView.updateCategorySpinner(bookCategoriesForSpinner)
    }

    override fun getBookAuthors(bookAuthorId: List<Long>) {
        compositeDisposable.add(quoteDataSource.getBookAuthorsByIds(bookAuthorId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<BookAuthor>>() {
                    override fun onNext(list: List<BookAuthor>?) {
                        if (list != null) {
                            addQuoteView.showBookAuthors(list)
                            Log.v("LIST_SIZE", list.size.toString() + "OK")
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getBookReleaseYear(yearIds: List<Long>) {
        compositeDisposable.add(quoteDataSource.getBookReleaseYearsByIds(yearIds).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<BookReleaseYear>>() {
                    override fun onNext(list: List<BookReleaseYear>?) {
                        if (list != null) {
                            addQuoteView.showBookReleaseYears(list)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }


}