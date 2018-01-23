package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.Quote
import io.reactivex.Flowable


@Dao
interface QuoteDao {

    @Query("SELECT * FROM Quote WHERE Quote.quoteId = :quoteId")
    fun getQuoteById(quoteId: Long): Flowable<Quote>

    @Insert
    fun insertQuote(quote: Quote): Long

    @Query("SELECT * FROM Quote")
    fun getAllQuotes(): Flowable<List<Quote>>

    @Query("SELECT * FROM Quote WHERE Quote.quoteText LIKE '%'||:text||'%'")
    fun getAllQuotesByQuoteTextIfContains(text: String): Flowable<List<Quote>>

    @Query("SELECT * FROM Quote " +
            "INNER JOIN (SELECT * FROM QuoteType " +
            "WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id")
    fun getQuotesByType(quoteType: String): Flowable<List<Quote>>


    @Query("SELECT * FROM Quote " +
            "INNER JOIN (SELECT * FROM QuoteType " +
            "WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id " +
            "WHERE Quote.quoteText LIKE '%'||:text||'%'")
    fun getQuotesByTypeAndTextIfContains(quoteType: String, text: String): Flowable<List<Quote>>

    @Query("SELECT * FROM Quote " +
            "INNER JOIN (SELECT * FROM QuoteType " +
            "WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id " +
            "INNER JOIN (SELECT * FROM QuoteCategory " +
            "WHERE QuoteCategory.categoryName = :quoteCategory) b ON b.categoryId = Quote.category_Id")
    fun getQuotesByTypeAndCategory(quoteType: String, quoteCategory: String): Flowable<List<Quote>>


    @Query("SELECT * FROM Quote " +
            "INNER JOIN (SELECT * FROM QuoteType " +
            "WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id " +
            "INNER JOIN (SELECT * FROM QuoteCategory " +
            "WHERE QuoteCategory.categoryName = :quoteCategory) b ON b.categoryId = Quote.category_Id " +
            "WHERE Quote.quoteText LIKE '%'||:text||'%'")
    fun getQuotesByTypeAndCategoryAndTextIfContains(quoteType: String,
                                                    quoteCategory: String,
                                                    text: String): Flowable<List<Quote>>
    @Query("DELETE FROM Quote WHERE quoteId = :qId")
    fun deleteQuote(qId: Long)

}