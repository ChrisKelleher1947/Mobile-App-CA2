package org.wit.petcare.models

import android.content.Context
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.File

class PetCareJSONStore(private val context: Context) {

    private val fileName = "pets.json"
    private var petRecords = mutableListOf<PetCareModel>()
    private val json = Json

    init {
        if (File(context.filesDir, fileName).exists()) {
            load()
        }
    }

    fun findAll(): List<PetCareModel> = petRecords

    fun create(pet: PetCareModel) {
        petRecords.add(pet)
        save()
    }

    fun update(pet: PetCareModel) {
        val foundPet = petRecords.find { p -> p.id == pet.id }
        if (foundPet != null) {
            foundPet.petName = pet.petName
            foundPet.petType = pet.petType
            foundPet.petBirthday = pet.petBirthday
            foundPet.notes = pet.notes
            foundPet.feedingHour = pet.feedingHour
            foundPet.feedingMinute = pet.feedingMinute
            foundPet.timePicker = pet.timePicker
            foundPet.imageUri = pet.imageUri
            foundPet.lat = pet.lat
            foundPet.lng = pet.lng
            foundPet.zoom = pet.zoom
            save()
            Timber.i("Updated pet: ${foundPet.petName}")
        } else {
            Timber.w("Pet not found for update: ${pet.id}")
        }
    }


    fun delete(pet: PetCareModel) {
        petRecords.remove(pet)
        save()
    }

    private fun save() {
        try {
            val jsonString = json.encodeToString(petRecords)
            File(context.filesDir, fileName).writeText(jsonString)
            Timber.i("Saved ${petRecords.size} pets to JSON")
        } catch (e: Exception) {
            Timber.e("Error saving pets: ${e.message}")
        }
    }

    private fun load() {
        try {
            val jsonString = File(context.filesDir, fileName).readText()
            petRecords = json.decodeFromString(jsonString)
            Timber.i("Loaded ${petRecords.size} pets from JSON")
        } catch (e: Exception) {
            Timber.e("Error loading pets: ${e.message}")
        }
    }
}
