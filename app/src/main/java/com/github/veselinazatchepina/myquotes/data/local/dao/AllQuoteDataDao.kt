package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.pojo.AllQuoteData
import io.reactivex.Flowable


@Dao
interface AllQuoteDataDao {

    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor, BookAndBookReleaseYear " +
            "WHERE Quote.book_Id = Book.bookId " +
            "AND Book.bookId = BookAndBookAuthor.bookIdJoin " +
            "AND Book.bookId = BookAndBookReleaseYear.byBookIdJoin " +
            "GROUP BY quoteId")
    fun getAllQuoteData(): Flowable<List<AllQuoteData>>

    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor, BookAndBookReleaseYear, QuoteType " +
            "WHERE QuoteType.type = :quoteType " +
            "AND Quote.type_Id = QuoteType.typeId " +
            "AND Quote.book_Id = Book.bookId " +
            "AND Book.bookId = BookAndBookAuthor.bookIdJoin " +
            "AND Book.bookId = BookAndBookReleaseYear.byBookIdJoin " +
            "GROUP BY quoteId")
    fun getAllQuoteDataByQuoteType(quoteType: String): Flowable<List<AllQuoteData>>

    @Query("SELECT * FROM Quote, Book, BookAndBookAuthor, BookAndBookReleaseYear, QuoteType, QuoteCategory" +
            " WHERE QuoteType.type = :quoteType " +
            "AND QuoteCategory.categoryName = :quoteCategory " +
            "AND Quote.category_Id = QuoteCategory.categoryId " +
            "AND Quote.type_Id = QuoteType.typeId " +
            "AND Quote.book_Id = Book.bookId " +
            "AND Book.bookId = BookAndBookAuthor.bookIdJoin " +
            "AND Book.bookId = BookAndBookReleaseYear.byBookIdJoin " +
            "GROUP BY quoteId")
    fun getAllQuoteDataByQuoteTypeAndQuoteCategory(quoteType: String, quoteCategory: String): Flowable<List<AllQuoteData>>

}