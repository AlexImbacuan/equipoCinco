package com.univalle.dogapp.viewmodel

import android.app.Application
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.univalle.dogapp.R
import com.univalle.dogapp.model.BiometricAvailability
import com.univalle.dogapp.repository.AuthRepository
import com.univalle.dogapp.utils.Event

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()
    private val authRepository = AuthRepository(context)

    // Ask the fragment to open the biometric dialog
    private val _showBiometricPrompt = MutableLiveData<Event<Unit>>()
    val showBiometricPrompt: LiveData<Event<Unit>> = _showBiometricPrompt

    // Fingerprint recognized: go to HU-02 (Home Administrador de Citas)
    private val _navigateToHome = MutableLiveData<Event<Unit>>()
    val navigateToHome: LiveData<Event<Unit>> = _navigateToHome

    // Error message to show on screen
    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    /** The user tapped the fingerprint animation. */
    fun onFingerprintClicked() {
        when (authRepository.checkBiometricAvailability()) {
            BiometricAvailability.AVAILABLE -> _showBiometricPrompt.value = Event(Unit)
            BiometricAvailability.NO_HARDWARE -> showError(R.string.biometric_error_no_hardware)
            BiometricAvailability.HARDWARE_UNAVAILABLE -> showError(R.string.biometric_error_unavailable)
            BiometricAvailability.NONE_ENROLLED -> showError(R.string.biometric_error_none_enrolled)
            BiometricAvailability.UNSUPPORTED -> showError(R.string.biometric_error_generic)
        }
    }

    /** The device recognized the fingerprint. */
    fun onAuthenticationSucceeded() {
        _navigateToHome.value = Event(Unit)
    }

    /**
     * The dialog closed with an error (e.g. too many attempts). The message comes from the
     * device itself. If the user just cancelled, nothing is shown.
     */
    fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        val cancelledByUser = errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
            errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
            errorCode == BiometricPrompt.ERROR_CANCELED
        if (!cancelledByUser) {
            _errorMessage.value = Event(errString.toString())
        }
    }

    private fun showError(messageRes: Int) {
        _errorMessage.value = Event(context.getString(messageRes))
    }
}
