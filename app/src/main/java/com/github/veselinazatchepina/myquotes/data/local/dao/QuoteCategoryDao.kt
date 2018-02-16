package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
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

    @Query("DELETE FROM Quote WHERE Quote.category_Id = " +
            "(SELECT categoryId FROM QuoteCategory WHERE QuoteCategory.categoryName = :quoteCategory) AND " +
            "Quote.type_Id = " +
            "(SELECT typeId FROM QuoteType WHERE QuoteType.type = :quoteType)")
    fun deleteQuoteCategory(quoteType: String, quoteCategory: String): Int

    @Query("SELECT * FROM QuoteCategory WHERE QuoteCategory.categoryId = :quoteCategoryId")
    fun getJustSimpleQuoteCategoryById(quoteCategoryId: Long): QuoteCategory

    @Update
    fun updateQuoteCategory(quoteCategory: QuoteCategory)

}