package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


@Entity
data class QuoteCategory(var categoryName: String) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var categoryId: Long = 0

}