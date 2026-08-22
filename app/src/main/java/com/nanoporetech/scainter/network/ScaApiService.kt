package com.nanoporetech.scainter.network

import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.model.ExamOption
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
    // provider_api.php

    @POST("provider_api.php")
    suspend fun fetchProvider(@Body request: FetchProviderRequest): Response<Provider?>

    // consultation_api.php

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

    @GET("consultation_api.php")
    suspend fun updatePrescription(
        @Query(value= "action") action: String,
        @Query(value= "id") consultationId: String,
        @Query(value= "doctor") doctor: String,
        @Query(value= "affection") affection: String,
        @Query(value= "medicament") medicament: String,
        @Query(value= "quantite") quantity: String,
        @Query(value= "posologie") posologie: String,
        @Query(value= "medicament1") medicament1: String,
        @Query(value= "quantite1") quantity1: String,
        @Query(value= "posologie1") posologie1: String,
        @Query(value= "medicament2") medicament2: String,
        @Query(value= "quantite2") quantity2: String,
        @Query(value= "posologie2") posologie2: String,
        @Query(value= "medicament3") medicament3: String,
        @Query(value= "quantite3") quantity3: String,
        @Query(value= "posologie3") posologie3: String
    ): Response<Status>

    // examination_api.php

    @GET("examination_api.php")
    suspend fun fetchExaminations(
        @Query(value = "action") action: String,
        @Query(value = "provider") provider: String
    ): Response<List<Examination>>

    @GET("examination_api.php")
    suspend fun newDayCareExamination(
        @Query(value = "action") action: String,
        @Query(value = "id") userId: String,
        @Query(value = "provider") provider: String,
        @Query(value = "reason") reason: String,
        @Query(value = "exam1") exam1: String,
        @Query(value = "cost") cost: String
    ): Response<Status>

    @GET("examination_api.php")
    suspend fun newRegularExamination(
        @Query(value = "action") action: String,
        @Query(value = "id") userId: String,
        @Query(value = "provider") provider: String,
        @Query(value = "doctor") doctor: String,
        @Query(value = "specialty") specialty: String,
        @Query(value = "insurance_type") insuranceType: String,
        @Query(value = "reason") reason: String,
        @Query(value = "exam1") exam1: String,
        @Query(value = "exam2") exam2: String,
        @Query(value = "exam3") exam3: String,
        @Query(value = "exam4") exam4: String,
        @Query(value = "exam5") exam5: String,
        @Query(value = "exam6") exam6: String,
        @Query(value = "exam7") exam7: String,
        @Query(value = "exam8") exam8: String
    ): Response<Status>

    @GET("examination_api.php")
    suspend fun fetchExaminationOptions(
        @Query(value = "action") action: String,
        @Query(value = "provider") provider: String,
        @Query(value = "insurance_type") insuranceType: String
    ): Response<List<ExamOption>>

    // hospitalisation.php

    @GET("hospitalisation_api.php")
    suspend fun fetchHospitalisations(
        @Query(value = "action") action: String,
        @Query(value = "provider") provider: String
    ): Response<List<Hospitalisation>>

    // assure_api.php

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
