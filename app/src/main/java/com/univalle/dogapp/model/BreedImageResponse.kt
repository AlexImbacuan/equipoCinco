package com.univalle.dogapp.model

import com.google.gson.annotations.SerializedName

/**
 * Response of GET breed/{breed}/images/random.
 * "message" is the URL of a random image of that breed.
 */
data class BreedImageResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: String
)
