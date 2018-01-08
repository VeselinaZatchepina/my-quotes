package com.github.veselinazatchepina.myquotes.allquote

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote


interface AllQuotesContract {

    interface View : BaseView<Presenter> {

        fun showQuotes(quotes: List<Quote>)


    }

    interface Presenter : BasePresenter {

        fun getAllQuotes()

        fun getQuotesByQuoteType(quoteType: String)

        fun getQuotesByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String)

    }
}