plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.univalle.dogapp"
    compileSdk {
        version = release(37)
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.univalle.dogapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // Navigation Component
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-common:$navVersion")

    //cardview
    implementation("androidx.cardview:cardview:1.0.0")

    // ViewModel + LiveData + Fragment KTX (MVVM)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Biometric authentication (HU-01 Login)
    implementation(libs.androidx.biometric)

    // Lottie animations (login fingerprint)
    implementation(libs.lottie)

    // Retrofit + Gson (Dog API, HU-03)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Room (local database, HU-03)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Glide (image loading)
    implementation(libs.glide)

}
