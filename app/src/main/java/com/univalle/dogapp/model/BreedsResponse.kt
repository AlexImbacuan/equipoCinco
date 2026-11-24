package com.univalle.dogapp.model

import com.google.gson.annotations.SerializedName

/**
 * Response of GET breeds/list/all.
 * "message" maps each breed to its sub-breeds, e.g. "bulldog" -> ["boston", "english", "french"].
 */
data class BreedsResponse(
    @SerializedName("message")
    val message: Map<String, List<String>>,

    @SerializedName("status")
    val status: String
)
