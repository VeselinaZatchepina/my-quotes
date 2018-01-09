package com.github.veselinazatchepina.myquotes.currentquote

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote


interface CurrentQuoteContract {

    interface View : BaseView<Presenter> {

        fun showQuote(quote: Quote)


    }

    interface Presenter : BasePresenter {

        fun getQuoteById(quoteId: Long)

    }
}