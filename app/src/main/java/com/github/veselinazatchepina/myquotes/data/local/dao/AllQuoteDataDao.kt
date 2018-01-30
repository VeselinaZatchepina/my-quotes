package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.model.AllQuoteData
import io.reactivex.Flowable


@Dao
interface AllQuoteDataDao {

    @Query("SELECT * FROM Quote, Book " +
            "WHERE Book.bookId = Quote.book_Id")
    fun getAllQuoteData(): Flowable<List<AllQuoteData>>

    @Query("SELECT * FROM Quote, Book " +
            "WHERE Quote.type_Id = (SELECT typeId FROM QuoteType WHERE QuoteType.type = :quoteType) " +
            "AND Book.bookId = Quote.book_Id")
    fun getAllQuoteDataByQuoteType(quoteType: String): Flowable<List<AllQuoteData>>

    @Query("SELECT * FROM Quote, Book " +
            "WHERE Quote.type_Id = (SELECT typeId FROM QuoteType WHERE QuoteType.type = :quoteType) " +
            "AND Quote.category_Id = (SELECT categoryId FROM QuoteCategory WHERE QuoteCategory.categoryName = :quoteCategory) " +
            "AND Book.bookId = Quote.book_Id ")
    fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<AllQuoteData>>

}