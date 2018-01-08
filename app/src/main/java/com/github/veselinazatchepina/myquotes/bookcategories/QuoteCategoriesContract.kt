package com.github.veselinazatchepina.myquotes.bookcategories

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory


interface QuoteCategoriesContract {

    interface View : BaseView<Presenter> {
        fun showQuoteCategoriesList(quoteCategories: List<QuoteCategory>)


    }

    interface Presenter : BasePresenter {
        fun getQuoteCategoriesList(quoteType: String)

        fun getQuotesByCategory(categoryName: String): List<Quote>


    }
}