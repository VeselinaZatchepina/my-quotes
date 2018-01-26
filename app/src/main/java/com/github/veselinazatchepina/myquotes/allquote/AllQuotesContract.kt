package com.github.veselinazatchepina.myquotes.allquote

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote


interface AllQuotesContract {

    interface View : BaseView<Presenter> {

        fun showQuotes(quotes: List<Quote>)

        fun showQuotesFromSearchView(quotes: List<Quote>)

        fun updateCategory()

    }

    interface Presenter : BasePresenter {

        fun getAllQuotes()

        fun getQuotesByType(quoteType: String)

        fun getQuotesByTypeAndCategory(quoteType: String, quoteCategory: String)

        fun getQuotesByTextIfContains(quoteText: String)

        fun getQuotesByTypeAndTextIfContains(quoteType: String, text: String)

        fun getQuotesByTypeAndCategoryAndTextIfContains(quoteType: String, quoteCategory: String, text: String)

        fun deleteQuote(quoteId: Long)

        fun updateCategoryCountById(quoteCategoryId: Long)

    }
}