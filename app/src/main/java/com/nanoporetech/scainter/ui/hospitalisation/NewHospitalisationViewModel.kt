package com.nanoporetech.scainter.ui.hospitalisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.FetchFamilyMembersResult
import com.nanoporetech.scainter.data.FetchPolicyHoldersResult
import com.nanoporetech.scainter.data.NewHospitalisationUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.ui.events.UiMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewHospitalisationViewModel(
    private val providerName: String,
    private val repository: ScaDataRepository
    ): ViewModel() {
    private var _uiState = MutableStateFlow(NewHospitalisationUiState())
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

    companion object {
        fun provideFactory(
            providerName: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                NewHospitalisationViewModel(
                    providerName = providerName,
                    repository = repository
                )
            }
        }
    }
}
