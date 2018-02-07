package com.github.veselinazatchepina.myquotes.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.github.veselinazatchepina.myquotes.data.local.entity.PublishingOffice


@Dao
interface PublishingOfficeDao {

    @Insert
    fun insertPublishingOffice(publishingOffice: PublishingOffice) : Long

    @Query("UPDATE PublishingOffice SET officeName = :currentOfficeName WHERE officeId = :oId")
    fun updatePublishingOffice(oId: Long, currentOfficeName: String)

    @Query("SELECT * FROM PublishingOffice WHERE PublishingOffice.officeName = :name")
    fun getPublishingOfficeByName(name: String) : PublishingOffice?

    @Update
    fun updatePublishingOffice(publishingOffice: PublishingOffice)

    @Query("SELECT * FROM PublishingOffice WHERE PublishingOffice.officeId = :publishingOfficeId")
    fun getSimplePublishingOfficeById(publishingOfficeId: Long): PublishingOffice
}