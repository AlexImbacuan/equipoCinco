package com.univalle.dogapp.webservice

import com.univalle.dogapp.model.BreedImageResponse
import com.univalle.dogapp.model.BreedsResponse
import com.univalle.dogapp.utils.Constants.END_POINT_BREEDS
import com.univalle.dogapp.utils.Constants.END_POINT_BREED_IMAGE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    /** All dog breeds, used by the breed AutoCompleteTextView. */
    @GET(END_POINT_BREEDS)
    suspend fun getBreeds(): BreedsResponse

    /** Random image of the given breed, e.g. "akita". */
    @GET(END_POINT_BREED_IMAGE)
    suspend fun getBreedImage(@Path("breed") breed: String): BreedImageResponse
}
