package com.univalle.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentNewAppointmentBinding
import com.univalle.dogapp.viewmodel.AppointmentViewModel

/** HU-03: Nueva Cita. */
class NewAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentNewAppointmentBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        observerViewModel()
    }

    private fun observerViewModel() {
        observerListBreeds()
        observerProgress()
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
