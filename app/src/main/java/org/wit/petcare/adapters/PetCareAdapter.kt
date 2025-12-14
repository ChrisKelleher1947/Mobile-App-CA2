package org.wit.petcare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.petcare.databinding.CardPetHorizontalBinding
import org.wit.petcare.databinding.CardPetRecordBinding
import org.wit.petcare.models.PetCareModel
import java.io.File

interface PetCareListener {
    fun onPetRecordClick(petrecord: PetCareModel)
}

class PetcareAdapter(
    private var petrecords: List<PetCareModel>,
    private val listener: PetCareListener,
    private val isHorizontal: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (isHorizontal) {
            val binding = CardPetHorizontalBinding.inflate(inflater, parent, false)
            HorizontalHolder(binding)
        } else {
            val binding = CardPetRecordBinding.inflate(inflater, parent, false)
            VerticalHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val petrecord = petrecords[position]
        if (holder is HorizontalHolder) holder.bind(petrecord, listener)
        if (holder is VerticalHolder) holder.bind(petrecord, listener)
    }

    override fun getItemCount(): Int = petrecords.size

    class HorizontalHolder(private val binding: CardPetHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(petrecord: PetCareModel, listener: PetCareListener) {
            binding.petcareTitle.text = petrecord.petName
            binding.description.text = petrecord.petType
            if (petrecord.imagePath.isNotEmpty()) {
                Picasso.get()
                    .load(File(petrecord.imagePath))
                    .into(binding.petImage)
            }
            binding.root.setOnClickListener { listener.onPetRecordClick(petrecord) }
        }
    }

    class VerticalHolder(private val binding: CardPetRecordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(petrecord: PetCareModel, listener: PetCareListener) {
            binding.petcareTitle.text = petrecord.petName
            binding.description.text = petrecord.petType
            if (petrecord.imagePath.isNotEmpty()) {
                Picasso.get()
                    .load(File(petrecord.imagePath))
                    .into(binding.petImage)
            }
            binding.root.setOnClickListener { listener.onPetRecordClick(petrecord) }
        }
    }
}
