package org.wit.petcare.main.repository

import com.google.firebase.firestore.FirebaseFirestore
import org.wit.petcare.models.PetCareModel

class PetCareFirestore : PetCareStore {

    private val db = FirebaseFirestore.getInstance()
    private val petsCollection = db.collection("pets")

    override fun findAll(callback: (List<PetCareModel>) -> Unit) {
        petsCollection.get()
            .addOnSuccessListener { result ->
                val pets = result.toObjects(PetCareModel::class.java)
                callback(pets)
            }
    }

    override fun create(pet: PetCareModel, callback: () -> Unit) {
        val doc = petsCollection.document()
        pet.id = doc.id
        doc.set(pet)
            .addOnSuccessListener { callback() }
    }

    override fun update(pet: PetCareModel, callback: () -> Unit) {
        petsCollection.document(pet.id)
            .set(pet)
            .addOnSuccessListener { callback() }
    }

    override fun delete(petId: String, callback: () -> Unit) {
        petsCollection.document(petId)
            .delete()
            .addOnSuccessListener { callback() }
    }
}
