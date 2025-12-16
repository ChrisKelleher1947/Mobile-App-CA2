package org.wit.petcare.main.repository

import org.wit.petcare.models.PetCareModel

interface PetCareStore {
    fun findAll(callback: (List<PetCareModel>) -> Unit)
    fun create(pet: PetCareModel, callback: () -> Unit)
    fun update(pet: PetCareModel, callback: () -> Unit)
    fun delete(petId: String, callback: () -> Unit)
}
