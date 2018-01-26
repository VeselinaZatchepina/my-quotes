package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity(foreignKeys = [
    (ForeignKey(
        entity = Book::class,
        parentColumns = arrayOf("bookId"),
        childColumns = arrayOf("bookIdJoin"),
        onDelete = CASCADE)),
    (ForeignKey(
        entity = BookAuthor::class,
        parentColumns = arrayOf("authorId"),
        childColumns = arrayOf("authorIdJoin"),
        onDelete = CASCADE))
])
data class BookAndBookAuthor(val bookIdJoin: Long,
                             val authorIdJoin: Long) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var bookAndBookAuthorId: Long = 0
}