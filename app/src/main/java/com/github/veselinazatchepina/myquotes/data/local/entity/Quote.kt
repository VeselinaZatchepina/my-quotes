package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
        ForeignKey(
                entity = Book::class,
                parentColumns = arrayOf("bookId"),
                childColumns = arrayOf("book_Id")),

        ForeignKey(
                entity = QuoteType::class,
                parentColumns = arrayOf("typeId"),
                childColumns = arrayOf("type_Id"))
)
)
data class Quote(val quoteText: String,
                 val creationDate: String,
                 val comments: String,
                 val pageNumber: Long,
                 val book_Id: Long,
                 val type_Id: Long) {
    @PrimaryKey(autoGenerate = true)
    var quoteId: Long = 0
}