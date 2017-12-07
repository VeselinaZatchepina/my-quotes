package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class PublishingOffice(@PrimaryKey(autoGenerate = true) val officeId: Long,
                            val officeName: String)