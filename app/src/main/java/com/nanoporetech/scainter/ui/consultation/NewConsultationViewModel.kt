package com.nanoporetech.scainter.ui.consultation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.FetchFamilyMembersResult
import com.nanoporetech.scainter.data.FetchPolicyHoldersResult
import com.nanoporetech.scainter.data.NewConsultationUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.model.PolicyHolder
import com.nanoporetech.scainter.ui.events.UiMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class NewConsultationViewModel(
    private val providerName: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(NewConsultationUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiMessage>()
    val events = _events.asSharedFlow()

    private var loadFamilyJob: Job? = null
    private var loadedFamilyId: String? = null


    fun loadFamily(familyId: String) {
        if (familyId == loadedFamilyId) {
            return
        }

        // clear stale state
        _uiState.update {
            it.copy(
                familyMembers = emptyList(),
                policyHolders = emptyList(),
                currentPolicyHolder = null,
                isLoading = true
            )
        }

        loadFamilyJob?.cancel()

        loadFamilyJob = viewModelScope.launch {
            // load family members
            when (val fetchFamilyResult = repository.fetchFamilyMembers(familyId = familyId)) {
                is FetchFamilyMembersResult.Success -> {
                    _uiState.update {
                        it.copy(
                            familyMembers = fetchFamilyResult.members
                        )
                    }

                    if (fetchFamilyResult.members.isEmpty()) {
                        loadedFamilyId = familyId
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                        return@launch
                    }

                    val memberIds = fetchFamilyResult.members.map { it.id }

                    when (val fetchPolicyResult = repository.fetchPolicyHolders(
                        memberIds = memberIds.joinToString(","),
                        providerName = providerName)
                    ) {
                        is FetchPolicyHoldersResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    policyHolders = fetchPolicyResult.members,
                                    isLoading = false
                                )
                            }
                            loadedFamilyId = familyId
                        }

                        is FetchPolicyHoldersResult.NetworkError -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }

                        else -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }
                    }
                }

                is FetchFamilyMembersResult.NetworkError -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun setConsultation(value: String) {
        _uiState.update {
            it.copy(
                selectedConsultation = value
            )
        }
    }

    fun setCost(value: String) {
        _uiState.update {
            it.copy(
                selectedCost = value
            )
        }
    }

    fun setPolicyHolder(policyHolder: PolicyHolder) {
        _uiState.update {
            it.copy(
                currentPolicyHolder = policyHolder
            )
        }
    }

    fun newConsultation() {
        val policyHolder = _uiState.value.currentPolicyHolder ?: return

        viewModelScope.launch {
            // load family members
            when (val result = repository.newConsultation(
                provider = providerName,
                userId = policyHolder.id.toString(),
                cost =  getCost(_uiState.value.selectedCost),
                act = _uiState.value.selectedConsultation
            )) {
                true -> {
                    _events.emit(UiMessage.Success(R.string.new_consultation_success_message))
                }
                else -> {
                    _events.emit(UiMessage.Error(R.string.err_new_consultation_message))
                }
            }
        }
    }

    private fun getCost(value: String): String {
        val cost = value
            .trim()
            .split(Regex("\\s+"))
            .dropLast(1)
            .lastOrNull()
            ?.toDoubleOrNull()
            ?: 0.0
        return cost.toString()
    }

    companion object {
        fun provideFactory(
            providerName: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                NewConsultationViewModel(
                    providerName = providerName,
                    repository = repository
                )
            }
        }
    }
}
