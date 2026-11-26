package com.univalle.dogapp.utils

import androidx.biometric.BiometricManager

object Constants {
    /** Authenticators allowed on login: device biometrics (fingerprint). */
    const val LOGIN_AUTHENTICATORS: Int = BiometricManager.Authenticators.BIOMETRIC_WEAK

    // Room database (HU-03)
    const val NAME_BD: String = "dogapp_data.db"

    // Dog API (HU-03)
    const val BASE_URL: String = "https://dog.ceo/api/"
    const val END_POINT_BREEDS: String = "breeds/list/all"
    const val END_POINT_BREED_IMAGE: String = "breed/{breed}/images/random"
}
