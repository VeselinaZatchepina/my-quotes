package com.github.veselinazatchepina.myquotes.allquote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import io.reactivex.disposables.CompositeDisposable


class AllQuotesPresenter(val quoteDataSource: QuoteDataSource,
                         val allQuotesView: AllQuotesContract.View) : AllQuotesContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        allQuotesView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }


    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}