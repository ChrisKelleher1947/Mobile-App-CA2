package org.wit.petcare.models
import android.os.Parcelable
import kotlinx.serialization.Serializable
import  kotlinx.parcelize.Parcelize
import java.util.UUID

@Serializable
@Parcelize
data class PetCareModel(
    var id: String = UUID.randomUUID().toString(),
    var petName: String = "",
    var petType: String = "",
    var petBirthday: String = "",
    var notes: String = "",
    var feedingHour: Int = 1,
    var feedingMinute: Int = 0,
    var timePicker: String = "AM",
    var imageUri: String = ""

) : Parcelable