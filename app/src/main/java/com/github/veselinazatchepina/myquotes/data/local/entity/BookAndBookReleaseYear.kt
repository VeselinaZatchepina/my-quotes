package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity(foreignKeys = arrayOf(
        ForeignKey(
                entity = Book::class,
                parentColumns = arrayOf("bookId"),
                childColumns = arrayOf("byBookIdJoin")),
        ForeignKey(
                entity = BookReleaseYear::class,
                parentColumns = arrayOf("yearId"),
                childColumns = arrayOf("yearIdJoin"))
))
data class BookAndBookReleaseYear(val yearIdJoin: Long,
                                  val byBookIdJoin: Long) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var bookAndBookReleaseYearId: Long = 0
}