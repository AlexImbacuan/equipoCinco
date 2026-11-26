package com.univalle.dogapp.view.fragment

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentEditAppointmentBinding
import com.univalle.dogapp.model.Appointment
import com.univalle.dogapp.viewmodel.AppointmentViewModel

class EditAppointmentFragment : Fragment() {
    private lateinit var binding: FragmentEditAppointmentBinding
    private lateinit var currentAppointment: Appointment
    private val appointmentViewModel: AppointmentViewModel by viewModels()

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
        populateFields()
        setupValidation()
        setupListeners()
        observeViewModel()
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

    // Criteria 2 to 5: the fields start with the current appointment data to make editing easier
    private fun populateFields() {
        binding.etPetName.setText(currentAppointment.petName)
        binding.actvBreed.setText(currentAppointment.breed, false) // false: don't open the suggestions
        binding.etOwnerName.setText(currentAppointment.ownerName)
        binding.etPhone.setText(currentAppointment.phone)
    }

    /**
     * Criteria 6 and 7: the button is enabled only when pet name, breed, owner name and phone
     * are filled. When enabled, its text is white (color selector) and bold.
     */
    private fun setupValidation() {
        val fields = listOf(
            binding.etPetName,
            binding.actvBreed,
            binding.etOwnerName,
            binding.etPhone
        )
        for (field in fields) {
            field.addTextChangedListener {
                updateEditButtonState(fields)
            }
        }
        // The fields start filled, so check once now
        updateEditButtonState(fields)
    }

    private fun updateEditButtonState(fields: List<EditText>) {
        val allFilled = fields.all { it.text.toString().isNotBlank() }
        binding.btnEditAppointment.isEnabled = allFilled
        binding.btnEditAppointment.setTypeface(
            null,
            if (allFilled) Typeface.BOLD else Typeface.NORMAL
        )
    }

    private fun setupListeners() {
        binding.btnEditAppointment.setOnClickListener {
            updateAppointment()
        }
    }

    /**
     * Criterion 8: copy() keeps the same id (Room needs it to know which row to update)
     * and the symptom. The ViewModel gets a new random image of the chosen breed from the
     * Dog API, updates the appointment in Room and then emits appointmentSaved.
     */
    private fun updateAppointment() {
        val updatedAppointment = currentAppointment.copy(
            petName = binding.etPetName.text.toString().trim(),
            breed = binding.actvBreed.text.toString().trim(),
            ownerName = binding.etOwnerName.text.toString().trim(),
            phone = binding.etPhone.text.toString().trim()
        )
        // Avoid updating twice while it is being saved
        binding.btnEditAppointment.isEnabled = false
        appointmentViewModel.updateAppointment(updatedAppointment)
    }

    private fun observeViewModel() {
        observeListBreeds()
        observeProgress()
        observeAppointmentUpdated()
    }

    /**
     * Criterion 3: breeds come from the Dog API (Retrofit + coroutines in the ViewModel).
     * The AutoCompleteTextView shows the breeds that start with the first 2 letters typed.
     */
    private fun observeListBreeds() {
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

    private fun observeProgress() {
        appointmentViewModel.progresState.observe(viewLifecycleOwner) { status ->
            binding.progress.isVisible = status
        }
    }

    // Criterion 8: once updated, go to Home (HU-02), where the edited appointment is listed
    private fun observeAppointmentUpdated() {
        appointmentViewModel.appointmentSaved.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(context, getString(R.string.appointment_updated), Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_editAppointmentFragment_to_homeFragment)
            }
        }
    }
}