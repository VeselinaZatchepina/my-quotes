package com.github.veselinazatchepina.myquotes.addquote

import android.util.Log
import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class AddQuotePresenter(val quoteDataSource: QuoteDataSource,
                        val addQuoteView: AddQuoteContract.View) : AddQuoteContract.Presenter {

    private var compositeDisposable: CompositeDisposable
    private var quoteCategories = ArrayList<String>()

    init {
        addQuoteView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun saveQuote(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        compositeDisposable.add(Observable.fromCallable {
            quoteDataSource.saveQuoteData(mapOfQuoteProperties, authors)
        }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("SAVE_QUOTE_CAT", "OK $it")
                }, {
                    Log.d("SAVE_QUOTE_CAT", "ERROR ${it.message}")
                }, {
                    Log.d("SAVE_QUOTE_CAT", "COMPLETE")
                }))

    }

    override fun getQuoteCategories(quoteType: String) {
        compositeDisposable.add(quoteDataSource.getQuoteCategories(quoteType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<QuoteCategoryModel>>() {
                    override fun onNext(quoteCategoryModels: List<QuoteCategoryModel>?) {
                        if (quoteCategoryModels != null) {
                            if (quoteCategories.isEmpty()) {
                                quoteCategories.clear()
                                addQuoteView.defineCategorySpinner(defineBookCategoriesListForView(quoteCategoryModels))
                            } else {
                                quoteCategories.clear()
                                addQuoteView.updateCategorySpinner(defineBookCategoriesListForView(quoteCategoryModels))
                            }
                        } else {
                            Log.v("GET_BOOK_CATEGORIES", "NULL")
                        }
                    }

                    override fun onComplete() {
                        Log.v("GET_BOOK_CATEGORIES", "COMPLETE")
                    }

                    override fun onError(t: Throwable?) {
                        Log.v("GET_BOOK_CATEGORIES", "ERROR ${t?.message}")
                    }
                }))
    }

    private fun defineBookCategoriesListForView(quoteCategoryModels: List<QuoteCategoryModel>): List<String> {
        quoteCategoryModels.mapTo(quoteCategories) {
            it.quoteCategory!!.categoryName.toUpperCase()
        }
        quoteCategories.add("+ add new category")
        quoteCategories.add("Select quote category")
        return quoteCategories
    }

    override fun addQuoteCategory(category: String) {
        quoteCategories.add(0, category)
        addQuoteView.updateCategorySpinner(quoteCategories)
    }

    override fun getBookAuthors(bookAuthorId: List<Long>) {
        compositeDisposable.add(quoteDataSource.getBookAuthorsByIds(bookAuthorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<BookAuthor>>() {
                    override fun onNext(bookAuthors: List<BookAuthor>?) {
                        if (bookAuthors != null) {
                            addQuoteView.showBookAuthors(bookAuthors)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getBookReleaseYears(yearIds: List<Long>) {
        compositeDisposable.add(quoteDataSource.getBookReleaseYearsByIds(yearIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<BookReleaseYear>>() {
                    override fun onNext(years: List<BookReleaseYear>?) {
                        if (years != null) {
                            addQuoteView.showBookReleaseYears(years)
                        }
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun updateQuote(quoteId: Long,
                             mapOfQuoteProperties: HashMap<QuoteProperties, String>,
                             authors: List<String>) {
        compositeDisposable.add(Observable.fromCallable {
            quoteDataSource.updateQuote(quoteId, mapOfQuoteProperties, authors)
        }
                .subscribeOn(Schedulers.io())
                .subscribe {

                })
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }


}