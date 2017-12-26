package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class PublishingOffice(val officeName: String) {
    @PrimaryKey(autoGenerate = true)
    var officeId: Long = 0
}