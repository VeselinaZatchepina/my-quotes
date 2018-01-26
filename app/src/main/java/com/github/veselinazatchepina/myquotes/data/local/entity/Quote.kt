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
        childColumns = arrayOf("book_Id"),
        onDelete = CASCADE)),
    (ForeignKey(
        entity = QuoteType::class,
        parentColumns = arrayOf("typeId"),
        childColumns = arrayOf("type_Id"),
        onDelete = CASCADE)),
    (ForeignKey(
        entity = QuoteCategory::class,
        parentColumns = arrayOf("categoryId"),
        childColumns = arrayOf("category_Id"),
        onDelete = CASCADE))
])
data class Quote(val quoteText: String,
                 val creationDate: String,
                 val comments: String,
                 val pageNumber: Long,
                 val book_Id: Long,
                 val type_Id: Long,
                 val category_Id: Long) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var quoteId: Long = 0
}