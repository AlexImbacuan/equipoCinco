package com.univalle.dogapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.univalle.dogapp.model.Appointment

@Dao
interface AppointmentDao {
    /** HU-03: save a new appointment. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAppointment(appointment: Appointment)

    /** HU-02: list of appointments ordered by turn. */
    @Query("SELECT * FROM Appointment ORDER BY id ASC")
    suspend fun getListAppointment(): MutableList<Appointment>

    /** HU-04: delete an appointment. */
    @Delete
    suspend fun deleteAppointment(appointment: Appointment)

    /** HU-05: update an appointment. */
    @Update
    suspend fun updateAppointment(appointment: Appointment)
}
