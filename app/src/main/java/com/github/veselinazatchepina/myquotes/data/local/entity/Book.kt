package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
        ForeignKey(
                entity = PublishingOffice::class,
                parentColumns = arrayOf("officeId"),
                childColumns = arrayOf("office_Id")),
        ForeignKey(
                entity = BookCategory::class,
                parentColumns = arrayOf("categoryId"),
                childColumns = arrayOf("category_Id"))
))
data class Book(@PrimaryKey(autoGenerate = true) val bookId: Long,
                val bookName: String,
                val office_Id: Long,
                val category_Id: Long)