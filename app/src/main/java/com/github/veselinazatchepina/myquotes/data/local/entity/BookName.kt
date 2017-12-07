package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = arrayOf(
        ForeignKey(
                entity = PublishingOffice::class,
                parentColumns = arrayOf("officeId"),
                childColumns = arrayOf("bookNameId"))
))
data class BookName(@PrimaryKey(autoGenerate = true) val bookNameId: Long,
                    val bookName: String)