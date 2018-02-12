package com.github.veselinazatchepina.myquotes.quotecategories

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.model.QuoteCategoryModel


interface QuoteCategoriesContract {

    interface View : BaseView<Presenter> {
        fun showQuoteCategoriesList(quoteCategories: List<QuoteCategoryModel>)


    }

    interface Presenter : BasePresenter {
        fun getQuoteCategoriesList(quoteType: String)

        fun deleteQuoteCategory(quoteType: String, quoteCategory: String)

    }
}