package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.QuoteCategory
import io.reactivex.Flowable


@Dao
interface QuoteCategoryDao {

    @Insert
    fun insertQuoteCategory(quoteCategory: QuoteCategory): Long

    @Query("SELECT * FROM QuoteCategory WHERE QuoteCategory.categoryName = :name")
    fun getQuoteCategoryByName(name: String): QuoteCategory?

    @Query("SELECT * FROM QuoteCategory " +
            "INNER JOIN Quote ON QuoteCategory.categoryId = Quote.category_Id " +
            "INNER JOIN (SELECT * FROM QuoteType WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id " +
            "GROUP BY categoryId")
    fun getQuoteCategoryByQuoteType(quoteType: String): Flowable<List<QuoteCategory>>

    @Query("DELETE FROM QuoteCategory WHERE categoryName = :quoteCategory AND " +
            "categoryId = (SELECT category_Id FROM Quote " +
            "INNER JOIN QuoteType ON Quote.type_Id = QuoteType.typeId " +
            "INNER JOIN QuoteCategory ON Quote.category_Id = QuoteCategory.categoryId " +
            "WHERE type = :quoteType AND QuoteCategory.categoryName = :quoteCategory)")
    fun deleteQuoteCategory(quoteType: String, quoteCategory: String)

}