package com.univalle.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.univalle.dogapp.R
import com.univalle.dogapp.view.adapter.AppointmentAdapter
import com.univalle.dogapp.databinding.FragmentHomeBinding
import com.univalle.dogapp.viewmodel.AppointmentViewModel

/**
 * HU-02: Home Administrador de Citas.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val appointmentViewModel: AppointmentViewModel by viewModels()
    private lateinit var adapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPressHandler()
        controladores()
        observerAppointments()
        appointmentViewModel.getListAppointment()
    }

    /**
     * Criterio 1 de retroceso: intercepta el botón 'Atrás' nativo para minimizar la aplicación
     * al escritorio en lugar de regresar a LoginFragment.
     */
    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(enabled = true) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            }
        )
    }

    private fun controladores() {
        adapter = AppointmentAdapter(emptyList()) { cita ->
            val bundle = Bundle().apply {
                putSerializable("cita", cita)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        }

        binding.rvCitas.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }

        binding.fabNuevaCita.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newAppointmentFragment)
        }
    }

    private fun observerAppointments() {
        appointmentViewModel.listAppointment.observe(viewLifecycleOwner) { listaCitas ->
            adapter.updateData(listaCitas)
        }
    }
}
