package com.nanoporetech.scainter.data

import android.util.Log
import com.nanoporetech.scainter.model.Consultation
import com.nanoporetech.scainter.model.Examination
import com.nanoporetech.scainter.model.FamilyMember
import com.nanoporetech.scainter.model.Hospitalisation
import com.nanoporetech.scainter.model.PolicyHolder
import com.nanoporetech.scainter.model.Provider
import com.nanoporetech.scainter.model.isOk
import com.nanoporetech.scainter.network.FetchProviderRequest
import com.nanoporetech.scainter.network.ScaApiService
import okio.IOException

sealed interface FetchProviderResult {
    data class Success(val provider: Provider) : FetchProviderResult
    object AuthenticationFailed : FetchProviderResult
    object NetworkError : FetchProviderResult
    object UnknownError : FetchProviderResult
}

sealed interface FetchConsultationsResult {
    data class Success(val consultations: List<Consultation>) : FetchConsultationsResult
    object NetworkError : FetchConsultationsResult
    object ServerError: FetchConsultationsResult
    object UnknownError : FetchConsultationsResult
}

sealed interface FetchExaminationsResult {
    data class Success(val examinations: List<Examination>) : FetchExaminationsResult
    object NetworkError : FetchExaminationsResult
    object ServerError: FetchExaminationsResult
    object UnknownError : FetchExaminationsResult
}

sealed interface FetchHospitalisationsResult {
    data class Success(val hospitalisations: List<Hospitalisation>) : FetchHospitalisationsResult
    object NetworkError : FetchHospitalisationsResult
    object ServerError: FetchHospitalisationsResult
    object UnknownError : FetchHospitalisationsResult
}

sealed interface FetchFamilyMembersResult {
    data class Success(val members: List<FamilyMember>) : FetchFamilyMembersResult
    object NetworkError : FetchFamilyMembersResult
    object ServerError: FetchFamilyMembersResult
    object UnknownError : FetchFamilyMembersResult
}

sealed interface FetchPolicyHoldersResult {
    data class Success(val members: List<PolicyHolder>) : FetchPolicyHoldersResult
    object NetworkError : FetchPolicyHoldersResult
    object ServerError: FetchPolicyHoldersResult
    object UnknownError : FetchPolicyHoldersResult
}

sealed interface NewDayCareExaminationResult {
    object Success: NewDayCareExaminationResult
    object NetworkError : NewDayCareExaminationResult
    object UnknownError : NewDayCareExaminationResult
}

interface ScaDataRepository {
    suspend fun fetchProvider(username: String, password: String): FetchProviderResult
    suspend fun fetchConsultationsFor(provider: String): FetchConsultationsResult
    suspend fun fetchExaminationsFor(provider: String): FetchExaminationsResult
    suspend fun fetchHospitalisationsFor(provider: String): FetchHospitalisationsResult
    suspend fun fetchFamilyMembers(familyId: String): FetchFamilyMembersResult
    suspend fun fetchPolicyHolders(memberIds: String, providerName: String): FetchPolicyHoldersResult
    suspend fun newConsultation(provider: String, userId: String, cost: String, act: String): Boolean

    suspend fun updatePrescription(consultationId: String, doctor: String, affection: String, medicament: String, quantity: String, posologie: String,
                                    medicament1: String, quantity1: String, posologie1: String, medicament2: String, quantity2: String,
                                    posologie2: String, medicament3: String, quantity3: String, posologie3: String): Boolean

    suspend fun newDayCareExamination(userId: String, provider: String, reason: String, exam1: String, cost: String): NewDayCareExaminationResult
}

private const val TAG = "ScaNetworkDataRepository"

class ScaNetworkDataRepository(
    private val scaApiService: ScaApiService
): ScaDataRepository {
    override suspend fun fetchProvider(username: String, password: String): FetchProviderResult {
        return try {
            val request = FetchProviderRequest(
                action = "fetch",
                username = username,
                password = password
            )

            val response = scaApiService.fetchProvider(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    FetchProviderResult.Success(it)
                } ?: FetchProviderResult.NetworkError
            } else {
                when (response.code()) {
                    in 400..409 -> FetchProviderResult.AuthenticationFailed
                    else -> FetchProviderResult.NetworkError
                }
            }
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
            FetchProviderResult.NetworkError
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            FetchProviderResult.UnknownError
        }
    }

    override suspend fun fetchConsultationsFor(provider: String): FetchConsultationsResult {
        return try {
            val response = scaApiService.fetchConsultations(
                action = "fetch",
                provider = provider,
            )
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        FetchConsultationsResult.Success(it)
                    } ?: FetchConsultationsResult.UnknownError
                }
                response.code() in 500..599 -> {
                    FetchConsultationsResult.ServerError
                }
                else -> {
                    FetchConsultationsResult.UnknownError
                }
            }
        } catch(e: IOException) {
            Log.d(TAG, e.toString())
            FetchConsultationsResult.NetworkError
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            FetchConsultationsResult.UnknownError
        }
    }

    override suspend fun fetchExaminationsFor(provider: String): FetchExaminationsResult {
        return try {
            val response = scaApiService.fetchExaminations(
                action = "fetch",
                provider = provider,
            )
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        FetchExaminationsResult.Success(it)
                    } ?: FetchExaminationsResult.UnknownError
                }
                response.code() in 500..599 -> {
                    FetchExaminationsResult.ServerError
                }
                else -> {
                    FetchExaminationsResult.UnknownError
                }
            }
        } catch(e: IOException) {
            Log.d(TAG, e.toString())
            FetchExaminationsResult.NetworkError
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            FetchExaminationsResult.UnknownError
        }
    }

    override suspend fun fetchHospitalisationsFor(provider: String): FetchHospitalisationsResult {
        return try {
            val response = scaApiService.fetchHospitalisations(
                action = "fetch",
                provider = provider,
            )
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        FetchHospitalisationsResult.Success(it)
                    } ?: FetchHospitalisationsResult.UnknownError
                }
                response.code() in 500..599 -> {
                    FetchHospitalisationsResult.ServerError
                }
                else -> {
                    FetchHospitalisationsResult.UnknownError
                }
            }
        } catch(e: IOException) {
            Log.d(TAG, e.toString())
            FetchHospitalisationsResult.NetworkError
        }
        catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            FetchHospitalisationsResult.UnknownError
        }
    }

    override suspend fun fetchFamilyMembers(familyId: String): FetchFamilyMembersResult {
        return try {
            val response = scaApiService.fetchFamilyMembers(
                action = "fetch_family",
                familyId = familyId,
            )
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        FetchFamilyMembersResult.Success(it)
                    } ?: FetchFamilyMembersResult.UnknownError
                }
                response.code() in 500..599 -> {
                    FetchFamilyMembersResult.ServerError
                }
                else -> {
                    FetchFamilyMembersResult.UnknownError
                }
            }
        } catch(e: IOException) {
            Log.d(TAG, e.toString())
            FetchFamilyMembersResult.NetworkError
        }
        catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            FetchFamilyMembersResult.UnknownError
        }
    }

    override suspend fun fetchPolicyHolders(
        memberIds: String,
        providerName: String
    ): FetchPolicyHoldersResult {
        return try {
            val response = scaApiService.fetchPolicyHolders(
                action = "fetch_clients",
                memberIds = memberIds,
                providerName = providerName
            )
            when {
                response.isSuccessful -> {
                    response.body()?.let {
                        FetchPolicyHoldersResult.Success(it)
                    } ?: FetchPolicyHoldersResult.UnknownError
                }
                response.code() in 500..599 -> {
                    FetchPolicyHoldersResult.ServerError
                }
                else -> {
                    FetchPolicyHoldersResult.UnknownError
                }
            }
        } catch(e: IOException) {
            Log.d(TAG, e.toString())
            FetchPolicyHoldersResult.NetworkError
        }
        catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            FetchPolicyHoldersResult.UnknownError
        }
    }

    override suspend fun newConsultation(
        provider: String,
        userId: String,
        cost: String,
        act: String
    ): Boolean {
        return try {
            val response = scaApiService.newConsultation(
                action = "confirm_soin",
                provider = provider,
                userId = userId,
                cost = cost,
                act = act
            )
            when {
                response.isSuccessful -> {
                    response.body()?.isOk() ?: false
                }

                response.code() in 500..599 -> {
                    Log.e(TAG, "Server error: ${response.code()}")
                    false
                }

                else -> {
                    Log.e(TAG, "Request failed: ${response.code()}")
                    false
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            false
        }
    }

    override suspend fun updatePrescription(
        consultationId: String,
        doctor: String,
        affection: String,
        medicament: String,
        quantity: String,
        posologie: String,
        medicament1: String,
        quantity1: String,
        posologie1: String,
        medicament2: String,
        quantity2: String,
        posologie2: String,
        medicament3: String,
        quantity3: String,
        posologie3: String
    ): Boolean {
        return try {
            val response = scaApiService.updatePrescription(
                action = "confirm_prescription",
                consultationId = consultationId,
                doctor = doctor,
                affection = affection,
                medicament = medicament,
                quantity = quantity,
                posologie = posologie,
                medicament1 = medicament1,
                quantity1 = quantity1,
                posologie1 = posologie1,
                medicament2 = medicament2,
                quantity2 = quantity2,
                posologie2 = posologie2,
                medicament3 = medicament3,
                quantity3 = quantity3,
                posologie3 = posologie3
            )
            when {
                response.isSuccessful -> {
                    response.body()?.isOk() ?: false
                }

                response.code() in 500..599 -> {
                    Log.e(TAG, "Server error: ${response.code()}")
                    false
                }

                else -> {
                    Log.e(TAG, "Request failed: ${response.code()}")
                    false
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            false
        }
    }

    override suspend fun newDayCareExamination(
        userId: String,
        provider: String,
        reason: String,
        exam1: String,
        cost: String
    ): NewDayCareExaminationResult {
        return try {
            val response = scaApiService.newDayCareExamination(
                action = "confirm_one_day_care",
                userId = userId,
                provider = provider,
                reason = reason,
                exam1 = exam1,
                cost = cost,
            )
            when {
                response.isSuccessful -> {
                    if (response.body()?.isOk() == true) {
                        NewDayCareExaminationResult.Success
                    } else {
                        NewDayCareExaminationResult.UnknownError
                    }
                }

                response.code() in 500..599 -> {
                    Log.e(TAG, "Server error: ${response.errorBody()?.string()}")
                    NewDayCareExaminationResult.UnknownError
                }

                else -> {
                    Log.e(TAG, "Request failed: ${response.errorBody()?.string()}")
                    NewDayCareExaminationResult.UnknownError
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network error", e)
            NewDayCareExaminationResult.NetworkError
        } catch (e: Exception) {
            Log.e(TAG, "Unknown error", e)
            NewDayCareExaminationResult.UnknownError
        }
    }
}