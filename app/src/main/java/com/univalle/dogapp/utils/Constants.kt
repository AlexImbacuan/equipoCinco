package com.univalle.dogapp.utils

import androidx.biometric.BiometricManager

object Constants {
    /** Authenticators allowed on login: device biometrics (fingerprint). */
    const val LOGIN_AUTHENTICATORS: Int = BiometricManager.Authenticators.BIOMETRIC_WEAK
}
