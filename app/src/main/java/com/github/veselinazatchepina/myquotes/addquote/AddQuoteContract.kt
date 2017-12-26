package com.github.veselinazatchepina.myquotes.addquote

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties


interface AddQuoteContract {

    interface View : BaseView<Presenter> {




    }

    interface Presenter : BasePresenter {

        fun saveQuote(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>)

    }
}