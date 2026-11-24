package com.univalle.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentNewAppointmentBinding

/** HU-03: Nueva Cita. */
class NewAppointmentFragment : Fragment() {

    private lateinit var binding: FragmentNewAppointmentBinding

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
    }

    // Criterion 1: title "Nueva Cita" and pink back arrow that returns to Home (HU-02)
    private fun setupToolbar() {
        binding.toolbar.tvToolbarTitle.text = getString(R.string.new_appointment_title)
        binding.toolbar.ibBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
