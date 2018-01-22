package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class PublishingOffice(val officeName: String) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var officeId: Long = 0
}