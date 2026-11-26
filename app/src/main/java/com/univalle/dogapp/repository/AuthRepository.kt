package com.univalle.dogapp.repository

import android.content.Context
import androidx.biometric.BiometricManager
import com.univalle.dogapp.model.BiometricAvailability
import com.univalle.dogapp.utils.Constants.LOGIN_AUTHENTICATORS

/**
 * Authentication data source. Asks the system whether the device can
 * authenticate with a fingerprint.
 */
class AuthRepository(context: Context) {

    private val biometricManager: BiometricManager = BiometricManager.from(context.applicationContext)

    fun checkBiometricAvailability(): BiometricAvailability =
        when (biometricManager.canAuthenticate(LOGIN_AUTHENTICATORS)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAvailability.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAvailability.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAvailability.HARDWARE_UNAVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAvailability.NONE_ENROLLED
            else -> BiometricAvailability.UNSUPPORTED
        }
}
