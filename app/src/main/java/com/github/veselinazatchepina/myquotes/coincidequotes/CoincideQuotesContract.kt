package com.github.veselinazatchepina.myquotes.coincidequotes

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote


class CoincideQuotesContract {

    interface View : BaseView<Presenter> {

        fun showQuotes(quotes: List<Quote>)

    }

    interface Presenter : BasePresenter {

        fun getCoincideQuotesByInputText(inputText: String)

    }
}