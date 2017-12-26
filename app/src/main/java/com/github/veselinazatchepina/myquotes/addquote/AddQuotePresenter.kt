package com.github.veselinazatchepina.myquotes.addquote

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties
import io.reactivex.disposables.CompositeDisposable


class AddQuotePresenter(val quoteDataSource: QuoteDataSource,
                        val addQuoteView: AddQuoteContract.View) : AddQuoteContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        addQuoteView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun saveQuote(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>) {
        quoteDataSource.saveQuoteData(mapOfQuoteProperties, authors)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {

    }


}