package com.github.veselinazatchepina.myquotes.quotecategories

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.BookCategory
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote


interface BookCategoriesContract {

    interface View : BaseView<Presenter> {
        fun showBookCategoriesList(bookCategories: List<BookCategory>)


    }

    interface Presenter : BasePresenter {
        fun getBookCategoriesList()

        fun getQuotesByCategory(categoryName: String) : List<Quote>


    }
}