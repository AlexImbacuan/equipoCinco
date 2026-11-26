package com.univalle.dogapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.univalle.dogapp.databinding.ItemAppointmentBinding
import com.univalle.dogapp.model.Appointment
import com.univalle.dogapp.view.viewholder.AppointmentViewHolder

class AppointmentAdapter(
    private var appointmentList: List<Appointment>,
    private val onAppointmentClick: (Appointment) -> Unit,
) : RecyclerView.Adapter<AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentViewHolder(binding, onAppointmentClick)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(appointmentList[position])
    }

    override fun getItemCount(): Int = appointmentList.size

    fun updateData(newList: List<Appointment>) {
        appointmentList = newList
        notifyDataSetChanged()
    }
}
