package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.Book


@Dao
interface BookDao {

    @Insert
    fun insertBook(book: Book): Long

    @Query("UPDATE Book SET bookName = :currentBookName  WHERE bookId = :bId")
    fun updateBook(bId: Long, currentBookName: String)

    @Query("SELECT * FROM Book " +
            "INNER JOIN BookAndBookReleaseYear ON Book.bookId = BookAndBookReleaseYear.byBookIdJoin " +
            "WHERE Book.bookName = :bookName " +
            "AND Book.office_Id = :publishingOfficeId " +
            "AND BookAndBookReleaseYear.yearIdJoin = :yearId")
    fun getBookByNamePublishingYear(bookName: String, publishingOfficeId: Long, yearId: Long) : Book?
}