package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(foreignKeys = [
    (ForeignKey(
        entity = PublishingOffice::class,
        parentColumns = arrayOf("officeId"),
        childColumns = arrayOf("office_Id"),
        onDelete = CASCADE))
])
data class Book(val bookName: String,
                val office_Id: Long) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var bookId: Long = 0
}