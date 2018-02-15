package com.github.veselinazatchepina.myquotes.getidea

import com.github.veselinazatchepina.myquotes.data.QuoteDataSource
import io.reactivex.disposables.CompositeDisposable


class GetIdeaPresenter(val quoteDataSource: QuoteDataSource,
                       val getIdeaView: GetIdeaContract.View) : GetIdeaContract.Presenter {

    private var compositeDisposable: CompositeDisposable

    init {
        getIdeaView.setPresenter(this)
        compositeDisposable = CompositeDisposable()
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}