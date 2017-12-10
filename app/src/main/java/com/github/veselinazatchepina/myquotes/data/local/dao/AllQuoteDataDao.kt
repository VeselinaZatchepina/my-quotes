package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.pojo.AllQuoteData


@Dao
interface AllQuoteDataDao {

    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor WHERE Quote.quoteId = :quoteId AND Book.bookId = Quote.book_Id AND BookAndBookAuthor.bookIdJoin = Book.bookId")
    fun getAllQuoteDataByQuoteId(quoteId: Long) : AllQuoteData

    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor")
    fun getAllQuote() : List<AllQuoteData>

}