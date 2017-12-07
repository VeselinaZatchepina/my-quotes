package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
        ForeignKey(
                entity = BookName::class,
                parentColumns = arrayOf("bookNameId"),
                childColumns = arrayOf("quoteId")),

        ForeignKey(
                entity = BookCategory::class,
                parentColumns = arrayOf("categoryId"),
                childColumns = arrayOf("quoteId")),

        ForeignKey(
                entity = QuoteType::class,
                parentColumns = arrayOf("typeId"),
                childColumns = arrayOf("quoteId"))
        )
)
data class Quote(@PrimaryKey(autoGenerate = true) val quoteId: Long,
                 val quoteText: String,
                 val creationDate: String,
                 val comments: String,
                 val pageNumber: String)