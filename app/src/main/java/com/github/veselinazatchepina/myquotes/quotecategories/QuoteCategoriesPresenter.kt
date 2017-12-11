package com.github.veselinazatchepina.myquotes.quotecategories

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import io.reactivex.disposables.CompositeDisposable


class QuoteCategoriesPresenter(val quoteDataSource: QuoteDataSource,
                               val quoteCategoriesView: QuoteCategoriesContract.View) : QuoteCategoriesContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        quoteCategoriesView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}