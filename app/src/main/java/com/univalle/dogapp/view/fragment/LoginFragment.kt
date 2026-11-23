package com.univalle.dogapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.univalle.dogapp.R
import com.univalle.dogapp.databinding.FragmentLoginBinding
import com.univalle.dogapp.utils.Constants.LOGIN_AUTHENTICATORS
import com.univalle.dogapp.viewmodel.LoginViewModel

/** HU-01: fingerprint login screen. */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // BiometricPrompt must be created in onCreate so it survives configuration changes
        setupBiometricPrompt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        // Criterion 5: tapping the fingerprint opens the biometric dialog
        binding.lottieFingerprint.setOnClickListener {
            loginViewModel.onFingerprintClicked()
        }
    }

    private fun observeViewModel() {
        observeShowBiometricPrompt()
        observeNavigateToHome()
        observeErrorMessage()
    }

    private fun observeShowBiometricPrompt() {
        loginViewModel.showBiometricPrompt.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                biometricPrompt.authenticate(promptInfo)
            }
        }
    }

    private fun observeNavigateToHome() {
        loginViewModel.navigateToHome.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    private fun observeErrorMessage() {
        loginViewModel.errorMessage.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            // Criterion 6: fingerprint recognized, go to HU-02
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                loginViewModel.onAuthenticationSucceeded()
            }

            // Criterion 6: fingerprint not recognized. The device's own dialog shows the error
            // and lets the user retry, so there is nothing to do here.
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                loginViewModel.onAuthenticationError(errorCode, errString)
            }
        })

        // Criterion 5: title, subtitle and "Cancelar" button
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_prompt_title))
            .setSubtitle(getString(R.string.biometric_prompt_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_prompt_cancel))
            .setAllowedAuthenticators(LOGIN_AUTHENTICATORS)
            .build()
    }
}
