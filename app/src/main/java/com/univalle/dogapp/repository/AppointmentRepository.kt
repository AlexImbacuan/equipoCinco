package com.univalle.dogapp.repository

import android.content.Context
import com.univalle.dogapp.data.AppointmentDB
import com.univalle.dogapp.data.AppointmentDao
import com.univalle.dogapp.model.Appointment
import com.univalle.dogapp.webservice.ApiService
import com.univalle.dogapp.webservice.ApiUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Single source of data for appointments: local database (Room) and Dog API (Retrofit).
 */
class AppointmentRepository(val context: Context) {
    private val appointmentDao: AppointmentDao = AppointmentDB.getDatabase(context).appointmentDao()
    private val apiService: ApiService = ApiUtils.getApiService()

    // ---------- Room ----------

    suspend fun saveAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.saveAppointment(appointment)
        }
    }

    suspend fun getListAppointment(): MutableList<Appointment> {
        return withContext(Dispatchers.IO) {
            appointmentDao.getListAppointment()
        }
    }

    suspend fun deleteAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.deleteAppointment(appointment)
        }
    }

    suspend fun updateAppointment(appointment: Appointment) {
        withContext(Dispatchers.IO) {
            appointmentDao.updateAppointment(appointment)
        }
    }

    // ---------- Dog API ----------

    /**
     * Breed names for the AutoCompleteTextView, capitalized for display ("Akita").
     * Returns an empty list if there is no connection.
     */
    suspend fun getBreeds(): MutableList<String> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getBreeds().message.keys
                    .map { breed -> breed.replaceFirstChar { it.uppercase() } }
                    .sorted()
                    .toMutableList()
            } catch (e: Exception) {
                e.printStackTrace()
                mutableListOf()
            }
        }
    }

    /**
     * URL of a random image of the breed. The API expects the name in lowercase ("akita").
     * Returns an empty string if the breed does not exist or there is no connection.
     */
    suspend fun getBreedImage(breed: String): String {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getBreedImage(breed.trim().lowercase()).message
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}
