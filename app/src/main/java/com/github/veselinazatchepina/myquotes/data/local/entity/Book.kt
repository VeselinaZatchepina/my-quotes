package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
        ForeignKey(
                entity = PublishingOffice::class,
                parentColumns = arrayOf("officeId"),
                childColumns = arrayOf("office_Id"))
))
data class Book(val bookName: String,
                val office_Id: Long) {
    @PrimaryKey(autoGenerate = true)
    var bookId: Long = 0
}