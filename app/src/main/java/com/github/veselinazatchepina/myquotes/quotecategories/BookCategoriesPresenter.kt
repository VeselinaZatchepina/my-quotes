package com.github.veselinazatchepina.myquotes.quotecategories

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


class BookCategoriesPresenter(val quoteDataSource: QuoteDataSource,
                              val bookCategoriesView: BookCategoriesContract.View) : BookCategoriesContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        bookCategoriesView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun getBookCategoriesList() {
        compositeDisposable.add(quoteDataSource.getBookCategories().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<List<BookCategory>>() {
                    override fun onNext(list: List<BookCategory>?) {
                       // if (list != null) {
                        val list2 = arrayListOf<BookCategory>(BookCategory(1, "first1", 1),
                                BookCategory(2, "second2", 2),
                                BookCategory(3, "third3", 3))
                            bookCategoriesView.showBookCategoriesList(list2)
                        //}
                    }

                    override fun onComplete() {

                    }

                    override fun onError(t: Throwable?) {

                    }

                }))
    }

    override fun getQuotesByCategory(categoryName: String) : List<Quote> {
        return arrayListOf()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}