package com.dicoding.dicodingevent.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var mediaCover: String? = null
) : Parcelable