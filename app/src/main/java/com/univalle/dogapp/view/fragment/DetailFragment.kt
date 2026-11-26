package com.univalle.dogapp.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentDetailBinding
import com.univalle.dogapp.model.Appointment
import com.univalle.dogapp.viewmodel.AppointmentViewModel

/**
 * HU-04: Ventana Detalle de la Cita.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private var currentAppointment: Appointment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractArguments()
        setupToolbar()
        populateAppointmentDetails()
        controladores()
        observerViewModel()
    }

    private fun extractArguments() {
        currentAppointment = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("cita", Appointment::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable("cita") as? Appointment
        }
    }

    // Criterio 1: Toolbar con el nombre de la mascota y flecha para retornar a Home
    private fun setupToolbar() {
        val petName = currentAppointment?.petName ?: ""
        binding.toolbar.tvToolbarTitle.text = petName
        binding.toolbar.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // Criterios 2 y 3: Mostrar foto, turno, raza, síntoma, propietario y teléfono
    private fun populateAppointmentDetails() {
        val appointment = currentAppointment ?: return

        // Criterio 2: Cargar imagen con Glide
        Glide.with(requireContext())
            .load(appointment.imageUrl)
            .placeholder(R.drawable.dog_head)
            .error(R.drawable.dog_head)
            .into(binding.ivFotoMascotaDetail)

        // Criterio 3: Turno, Raza, Síntoma, Propietario y Teléfono
        binding.tvTurnoDetail.text = getString(R.string.turn_format, appointment.id)
        binding.tvBreedDetail.text = appointment.breed
        binding.tvSymptomDetail.text = appointment.symptom
        binding.tvOwnerNameDetail.text = appointment.ownerName
        binding.tvPhoneDetail.text = appointment.phone
    }

    private fun controladores() {
        // Criterio 4: FAB de eliminación (conectado en la Fase 3)
        binding.fabDelete.setOnClickListener {
            currentAppointment?.let { appointment ->
                appointmentViewModel.deleteAppointment(appointment)
            }
        }

        // Criterio 5: FAB de edición (dummy button para HU 5.0)
        binding.fabEdit.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.edit_not_implemented), Toast.LENGTH_SHORT).show()
        }
    }

    private fun observerViewModel() {
        observerAppointmentDeleted()
    }

    private fun observerAppointmentDeleted() {
        appointmentViewModel.appointmentDeleted.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), getString(R.string.appointment_deleted), Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
            }
        }
    }
}
