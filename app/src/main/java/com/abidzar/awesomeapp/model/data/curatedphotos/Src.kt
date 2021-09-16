package com.abidzar.awesomeapp.model.data.curatedphotos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Src(
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String
) : Parcelable