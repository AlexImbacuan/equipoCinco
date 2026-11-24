package com.univalle.dogapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.univalle.dogapp.model.Appointment
import com.univalle.dogapp.utils.Constants.NAME_BD

@Database(entities = [Appointment::class], version = 1, exportSchema = false)
abstract class AppointmentDB : RoomDatabase() {

    abstract fun appointmentDao(): AppointmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppointmentDB? = null

        /** Single instance shared by the whole app. */
        fun getDatabase(context: Context): AppointmentDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppointmentDB::class.java,
                    NAME_BD
                ).build().also { INSTANCE = it }
            }
        }
    }
}
