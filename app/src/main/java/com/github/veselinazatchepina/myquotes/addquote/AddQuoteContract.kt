package com.github.veselinazatchepina.myquotes.addquote

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear
import com.github.veselinazatchepina.myquotes.enums.QuoteProperties


interface AddQuoteContract {

    interface View : BaseView<Presenter> {

        fun defineCategorySpinner(quoteCategories: List<String>)

        fun updateCategorySpinner(quoteCategories: List<String>)

        fun showBookAuthors(authors: List<BookAuthor>)

        fun showBookReleaseYears(years: List<BookReleaseYear>)


    }

    interface Presenter : BasePresenter {

        fun saveQuote(mapOfQuoteProperties: HashMap<QuoteProperties, String>, authors: List<String>)

        fun getQuoteCategoriesList(quoteType: String)

        fun addQuoteCategory(category: String)

        fun getBookAuthors(bookAuthorId: List<Long>)

        fun getBookReleaseYear(yearIds: List<Long>)

        fun updateQuote(quoteId: Long,
                        mapOfQuoteProperties: HashMap<QuoteProperties, String>,
                        authors: List<String>)

    }
}