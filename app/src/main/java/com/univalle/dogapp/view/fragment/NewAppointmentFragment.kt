package com.univalle.dogapp.view.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentNewAppointmentBinding
import com.univalle.dogapp.model.Appointment
import com.univalle.dogapp.viewmodel.AppointmentViewModel

/** HU-03: Nueva Cita. */
class NewAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentNewAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentNewAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        controladores()
        observerViewModel()
    }

    private fun controladores() {
        validarDatos()
        binding.btnSaveAppointment.setOnClickListener {
            // Criterion 8: the 4 fields are filled but no symptom was chosen
            // (position 0 is the default "Síntomas" item)
            if (binding.spSymptoms.selectedItemPosition == 0) {
                Toast.makeText(context, getString(R.string.select_symptom), Toast.LENGTH_SHORT).show()
            } else {
                saveAppointment()
            }
        }
    }

    /**
     * Criterion 10: the ViewModel gets a random image of the chosen breed from the Dog API,
     * saves the appointment in Room and then emits appointmentSaved to go to Home.
     */
    private fun saveAppointment() {
        val appointment = Appointment(
            petName = binding.etPetName.text.toString().trim(),
            breed = binding.actvBreed.text.toString().trim(),
            ownerName = binding.etOwnerName.text.toString().trim(),
            phone = binding.etPhone.text.toString().trim(),
            symptom = binding.spSymptoms.selectedItem.toString(),
            imageUrl = "" // filled by the ViewModel with the breed image
        )
        // Avoid saving the same appointment twice while it is being saved
        binding.btnSaveAppointment.isEnabled = false
        appointmentViewModel.saveAppointment(appointment)
    }

    /**
     * Criteria 7 and 9: the button is enabled only when pet name, breed, owner name and phone
     * are filled. When enabled, its text is white (color selector) and bold.
     */
    private fun validarDatos() {
        val listEditText = listOf(
            binding.etPetName,
            binding.actvBreed,
            binding.etOwnerName,
            binding.etPhone
        )

        for (editText in listEditText) {
            editText.addTextChangedListener {
                val isListFull = listEditText.all {
                    it.text.toString().isNotBlank() // every field has text
                }
                binding.btnSaveAppointment.isEnabled = isListFull
                binding.btnSaveAppointment.setTypeface(
                    null,
                    if (isListFull) Typeface.BOLD else Typeface.NORMAL
                )
            }
        }
    }

    private fun observerViewModel() {
        observerListBreeds()
        observerProgress()
        observerAppointmentSaved()
    }

    // Criterion 10: once saved, go to Home (HU-02), where the new appointment is listed
    private fun observerAppointmentSaved() {
        appointmentViewModel.appointmentSaved.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(context, getString(R.string.appointment_saved), Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_newAppointmentFragment_to_homeFragment)
            }
        }
    }

    /**
     * Criterion 3: breeds come from the Dog API (Retrofit + coroutines in the ViewModel).
     * The AutoCompleteTextView shows the breeds that start with the first 2 letters typed.
     */
    private fun observerListBreeds() {
        appointmentViewModel.getBreeds()
        appointmentViewModel.listBreeds.observe(viewLifecycleOwner) { listBreeds ->
            if (listBreeds.isEmpty()) {
                Toast.makeText(context, getString(R.string.breeds_error), Toast.LENGTH_SHORT).show()
            }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                listBreeds
            )
            binding.actvBreed.setAdapter(adapter)
        }
    }

    private fun observerProgress() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status
        }
    }

    // Criterion 1: title "Nueva Cita" and pink back arrow that returns to Home (HU-02)
    private fun setupToolbar() {
        binding.toolbar.tvToolbarTitle.text = getString(R.string.new_appointment_title)
        binding.toolbar.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
