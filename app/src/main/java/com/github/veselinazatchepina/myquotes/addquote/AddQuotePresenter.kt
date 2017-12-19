package com.github.veselinazatchepina.myquotes.addquote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import io.reactivex.disposables.CompositeDisposable


class AddQuotePresenter(val quoteDataSource: QuoteDataSource,
                        val addQuoteView: AddQuoteContract.View) : AddQuoteContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        addQuoteView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }


}