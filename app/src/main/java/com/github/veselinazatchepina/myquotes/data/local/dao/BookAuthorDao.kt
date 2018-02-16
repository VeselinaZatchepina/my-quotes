package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.github.veselinazatchepina.myquotes.data.local.entity.BookAuthor
import io.reactivex.Flowable


@Dao
interface BookAuthorDao {

    @Insert
    fun insertAuthor(author: BookAuthor): Long

    @Query("UPDATE BookAuthor SET name = :currentAuthorName WHERE authorId = :aId")
    fun updateBookAuthor(aId: Long, currentAuthorName: String)

    @Query("SELECT * FROM BookAuthor " +
            "WHERE BookAuthor.surname = :surname " +
            "AND BookAuthor.name = :name " +
            "AND BookAuthor.patronymic = :patronymic")
    fun getAuthor(surname: String, name: String, patronymic: String): BookAuthor?

    @Query("SELECT * FROM BookAuthor " +
            "WHERE authorId IN (:ids)")
    fun getBookAuthorsByIds(ids: List<Long>): Flowable<List<BookAuthor>>
}