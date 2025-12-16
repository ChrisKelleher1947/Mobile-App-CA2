package org.wit.petcare.models

import android.os.Parcelable
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Parcelize
@Serializable
data class Location(
    var lat: Double,
    var lng: Double,
    var zoom: Float
) : Parcelable
