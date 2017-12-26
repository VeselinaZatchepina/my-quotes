package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.Book


@Dao
interface BookDao {

    @Insert
    fun insertBook(book: Book): Long

    @Query("SELECT * FROM Book INNER JOIN BookAndBookReleaseYear ON Book.bookId = BookAndBookReleaseYear.bookIdJoin WHERE Book.bookName = :bookName AND Book.office_Id = :publishingOfficeId AND BookAndBookReleaseYear.yearIdJoin = :yearId")
    fun getBookByNamePublishingYear(bookName: String, publishingOfficeId: Long, yearId: Long) : Book?
}