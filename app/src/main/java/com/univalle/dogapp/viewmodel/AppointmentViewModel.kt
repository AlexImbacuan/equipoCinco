package com.univalle.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.univalle.dogapp.model.Appointment
import com.univalle.dogapp.repository.AppointmentRepository
import com.univalle.dogapp.utils.Event
import kotlinx.coroutines.launch

/**
 * ViewModel shared by the appointment screens (HU-02 to HU-05).
 */
class AppointmentViewModel(application: Application) : AndroidViewModel(application) {
    val context = getApplication<Application>()
    private val appointmentRepository = AppointmentRepository(context)

    // List of appointments (HU-02)
    private val _listAppointment = MutableLiveData<MutableList<Appointment>>()
    val listAppointment: LiveData<MutableList<Appointment>> get() = _listAppointment

    // Breeds for the AutoCompleteTextView (HU-03 / HU-05)
    private val _listBreeds = MutableLiveData<MutableList<String>>()
    val listBreeds: LiveData<MutableList<String>> = _listBreeds

    // Loading indicator
    private val _progresState = MutableLiveData(false)
    val progresState: LiveData<Boolean> = _progresState

    // One-shot event: the appointment was saved / updated, navigate to Home
    private val _appointmentSaved = MutableLiveData<Event<Unit>>()
    val appointmentSaved: LiveData<Event<Unit>> = _appointmentSaved

    // One-shot event: the appointment was deleted (HU-04), navigate to Home
    private val _appointmentDeleted = MutableLiveData<Event<Unit>>()
    val appointmentDeleted: LiveData<Event<Unit>> = _appointmentDeleted

    fun getBreeds() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listBreeds.value = appointmentRepository.getBreeds()
                _progresState.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _progresState.value = false
            }
        }
    }

    /**
     * HU-03: gets a random image of the chosen breed and then saves the appointment in Room.
     */
    fun saveAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                val imageUrl = appointmentRepository.getBreedImage(appointment.breed)
                appointmentRepository.saveAppointment(appointment.copy(imageUrl = imageUrl))
                _progresState.value = false
                _appointmentSaved.value = Event(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                _progresState.value = false
            }
        }
    }

    fun getListAppointment() {
        viewModelScope.launch {
            _progresState.value = true
            try {
                _listAppointment.value = appointmentRepository.getListAppointment()
                _progresState.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _progresState.value = false
            }
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                appointmentRepository.deleteAppointment(appointment)
                _progresState.value = false
                _appointmentDeleted.value = Event(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                _progresState.value = false
            }
        }
    }

    /**
     * HU-05: gets a new image for the (possibly changed) breed and then updates the appointment.
     */
    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _progresState.value = true
            try {
                val imageUrl = appointmentRepository.getBreedImage(appointment.breed)
                appointmentRepository.updateAppointment(appointment.copy(imageUrl = imageUrl))
                _progresState.value = false
                _appointmentSaved.value = Event(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                _progresState.value = false
            }
        }
    }
}
