package com.github.veselinazatchepina.myquotes.data.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class PublishingOffice(@PrimaryKey val officeId: String,
                            val officeName: String)