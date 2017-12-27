package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.pojo.BookCategoriesAndQuoteType
import io.reactivex.Flowable

@Dao
interface BookCategoriesAndQuoteTypeDao {

    // TODO is it List or just one object
    @Query("SELECT * FROM Quote, Book, QuoteType WHERE QuoteType.type = :quoteType AND Quote.type_Id = QuoteType.typeId AND Book.bookId = Quote.book_Id")
    fun getBookCategoriesByQuoteType(quoteType: String): Flowable<List<BookCategoriesAndQuoteType>>

}