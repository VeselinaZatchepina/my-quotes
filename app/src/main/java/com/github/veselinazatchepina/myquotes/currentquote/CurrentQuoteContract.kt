package com.github.veselinazatchepina.myquotes.currentquote

import com.github.veselinazatchepina.myquotes.BasePresenter
import com.github.veselinazatchepina.myquotes.BaseView
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear


interface CurrentQuoteContract {

    interface View : BaseView<Presenter> {

        fun showBookAuthors(authors: List<BookAuthor>)

        fun showBookReleaseYears(years: List<BookReleaseYear>)

        fun updateCategory()


    }

    interface Presenter : BasePresenter {

        fun getBookAuthors(bookAuthorId: List<Long>)

        fun getBookReleaseYear(yearIds: List<Long>)

        fun deleteQuote(qId: Long)

        fun updateCategoryCount(quoteCount: Int, quoteCategoryId: Long)

    }
}