package com.univalle.dogapp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.ItemAppointmentBinding
import com.univalle.dogapp.model.Appointment

class AppointmentViewHolder(
    private val binding: ItemAppointmentBinding,
    private val onAppointmentClick: (Appointment) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(appointment: Appointment) {
        binding.tvTurno.text = binding.root.context.getString(R.string.turn_format, appointment.id)
        binding.tvNombreMascota.text = appointment.petName
        binding.tvSintoma.text = appointment.symptom

        Glide.with(binding.root.context)
            .load(appointment.imageUrl)
            .placeholder(R.drawable.dog_head)
            .error(R.drawable.dog_head)
            .into(binding.ivFotoMascota)

        // The fragment decides what happens on click (navigation stays in the view layer)
        binding.root.setOnClickListener {
            onAppointmentClick(appointment)
        }
    }
}
