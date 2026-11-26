package com.univalle.dogapp.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentEditAppointmentBinding
import com.univalle.dogapp.model.Appointment

class EditAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentEditAppointmentBinding
    private lateinit var currentAppointment: Appointment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveAppointment()
        setupToolbar()
    }

    // Criterion 2: the appointment comes from the detail screen (HU-04) in the bundle
    private fun receiveAppointment() {
        currentAppointment = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable("cita", Appointment::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getSerializable("cita") as Appointment
        }
    }

    // Criterion 1: title "Editar Cita" and pink back arrow that returns to the detail (HU-04)
    private fun setupToolbar() {
        binding.toolbar.tvToolbarTitle.text = getString(R.string.edit_appointment_title)
        binding.toolbar.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}