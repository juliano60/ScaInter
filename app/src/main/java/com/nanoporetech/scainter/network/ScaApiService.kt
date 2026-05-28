package com.nanoporetech.scainter.network

import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.model.Examination
import com.nanoporetech.scainter.model.FamilyMember
import com.nanoporetech.scainter.model.Hospitalisation
import com.nanoporetech.scainter.model.PolicyHolder
import com.nanoporetech.scainter.model.Provider
import com.nanoporetech.scainter.model.Status
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ScaApiService {
    @POST("provider_api.php")
    suspend fun fetchProvider(@Body request: FetchProviderRequest): Response<Provider?>

    @GET("consultation_api.php")
    suspend fun fetchConsultations(
        @Query(value = "action") action: String,
        @Query(value = "provider") provider: String
    ): Response<List<Consultation>>

    @GET("consultation_api.php")
    suspend fun newConsultation(
        @Query(value = "action") action: String,
        @Query(value = "provider") provider: String,
        @Query(value = "id") userId: String,
        @Query(value = "montant") cost: String,
        @Query(value = "acte") act: String
    ): Response<Status>

    @GET("examination_api.php")
    suspend fun fetchExaminations(
        @Query(value = "action") action: String,
        @Query(value = "provider") provider: String
    ): Response<List<Examination>>

    @GET("hospitalisation_api.php")
    suspend fun fetchHospitalisations(
        @Query(value = "action") action: String,
        @Query(value = "provider") provider: String
    ): Response<List<Hospitalisation>>

    @GET("assure_api.php")
    suspend fun fetchFamilyMembers(
        @Query(value = "action") action: String,
        @Query(value = "family_id") familyId: String
    ): Response<List<FamilyMember>>

    @GET("assure_api.php")
    suspend fun fetchPolicyHolders(
        @Query(value = "action") action: String,
        @Query(value = "ids") memberIds: String,
        @Query(value = "provider") providerName: String
    ): Response<List<PolicyHolder>>
}
