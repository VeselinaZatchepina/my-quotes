package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.pojo.AllQuoteData
import io.reactivex.Flowable


@Dao
interface AllQuoteDataDao {

//    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor WHERE Quote.quoteId = :quoteId AND Book.bookId = Quote.book_Id AND BookAndBookAuthor.byBookIdJoin = Book.bookId")
//    fun getAllQuoteDataByQuoteId(quoteId: Long): Flowable<AllQuoteData>

    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor, BookAndBookReleaseYear WHERE Quote.book_Id = Book.bookId AND Book.bookId = BookAndBookAuthor.bookIdJoin AND Book.bookId = BookAndBookReleaseYear.byBookIdJoin GROUP BY quoteId")
    fun getAllQuoteData(): Flowable<List<AllQuoteData>>

//    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor WHERE (SELECT * FROM QuoteType WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id AND Book.bookId = Quote.book_Id AND BookAndBookAuthor.byBookIdJoin = Book.bookId")
//    fun getAllQuoteDataByQuoteType(quoteType: String): Flowable<List<AllQuoteData>>
//
//    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor WHERE (SELECT * FROM QuoteType WHERE QuoteType.type = :quoteType) c ON c.typeId = Quote.type_Id AND (SELECT * FROM QuoteCategory WHERE QuoteCategory.categoryName = :quoteCategory) b ON b.categoryId = Quote.category_Id AND Book.bookId = Quote.book_Id AND BookAndBookAuthor.byBookIdJoin = Book.bookId")
//    fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<AllQuoteData>>

}