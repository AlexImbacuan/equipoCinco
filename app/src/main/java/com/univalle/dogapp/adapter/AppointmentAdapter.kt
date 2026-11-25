package com.univalle.dogapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.ItemAppointmentBinding
import com.univalle.dogapp.model.Appointment

class AppointmentAdapter(
    private var listaCitas: List<Appointment>,
    private val onCitaClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    inner class AppointmentViewHolder(val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cita: Appointment) {
            binding.tvTurno.text = "#${cita.id}"
            binding.tvNombreMascota.text = cita.petName
            binding.tvSintoma.text = cita.symptom

            Glide.with(binding.root.context)
                .load(cita.imageUrl)
                .placeholder(R.drawable.dog_head)
                .error(R.drawable.dog_head)
                .into(binding.ivFotoMascota)

            binding.root.setOnClickListener {
                onCitaClick(cita)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(listaCitas[position])
    }

    override fun getItemCount(): Int = listaCitas.size

    fun updateData(nuevaLista: List<Appointment>) {
        listaCitas = nuevaLista
        notifyDataSetChanged()
    }
}
