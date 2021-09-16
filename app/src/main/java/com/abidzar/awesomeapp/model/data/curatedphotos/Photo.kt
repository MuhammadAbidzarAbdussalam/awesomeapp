package com.abidzar.awesomeapp.model.data.curatedphotos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Photo (
    val avg_color: String,
    val height: Int,
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val src: Src,
    val url: String,
    val width: Int
) : Parcelable