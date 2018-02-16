package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.BookReleaseYear
import io.reactivex.Flowable


@Dao
interface BookReleaseYearDao {

    @Insert
    fun insertBookYear(bookReleaseYear: BookReleaseYear): Long

    @Query("SELECT * FROM BookReleaseYear WHERE BookReleaseYear.year = :yearValue")
    fun getYearByValue(yearValue: Long) : BookReleaseYear?

    @Query("SELECT * FROM BookReleaseYear " +
            "WHERE yearId IN (:ids)")
    fun getBookReleaseYearsByIds(ids: List<Long>): Flowable<List<BookReleaseYear>>
}