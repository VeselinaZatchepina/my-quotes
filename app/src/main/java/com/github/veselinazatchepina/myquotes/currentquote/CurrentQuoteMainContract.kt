package com.github.veselinazatchepina.myquotes.currentquote

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData


interface CurrentQuoteMainContract {

    interface View : BaseView<Presenter> {

        fun createViewPager(quotes: List<AllQuoteData>)


    }

    interface Presenter : BasePresenter {

        fun getAllQuoteData()

        fun getAllQuoteDataByQuoteType(quoteType: String)

        fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String)

    }
}