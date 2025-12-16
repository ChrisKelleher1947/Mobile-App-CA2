package org.wit.petcare.models
import android.os.Parcelable
import kotlinx.serialization.Serializable
import  kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class PetCareModel(
    var id: String = "",
    var petName: String = "",
    var petType: String = "",
    var petBirthday: String = "",
    var notes: String = "",
    var feedingHour: Int = 1,
    var feedingMinute: Int = 0,
    var timePicker: String = "AM",
    var imagePath: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f

) : Parcelable