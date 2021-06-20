package com.example.mobilepresence.model.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mobilepresence.model.response.RecordObject
import kotlinx.parcelize.Parcelize

@Entity(tableName = "trackrecord")
@Parcelize
data class TrackRecord(
    @PrimaryKey(autoGenerate = false) val id_post: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "location") val location: String
) : Parcelable {
    companion object {
        fun from(data: RecordObject.Trackrecord) = TrackRecord(
            data.id_post,
            data.date,
            data.location
        )
    }
}