package com.example.mobilepresence.model.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.mobilepresence.model.local.entity.TrackRecord
import io.reactivex.Flowable
import io.reactivex.Maybe

@Dao
interface TrackRecordDao : BaseDao<TrackRecord>{

    @Query("SELECT * FROM `trackrecord` ORDER by id_post DESC")
    fun findAllTrackRecor() : Maybe<List<TrackRecord>>

    @Query("SELECT * FROM `trackrecord` ORDER by id_post DESC")
    fun streamAll() : Flowable<List<TrackRecord>>

    @Query("DELETE FROM `trackrecord`")
    fun deleteAll()
}