package com.nanoporetech.scainter.ui.examination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.FetchFamilyMembersResult
import com.nanoporetech.scainter.data.FetchPolicyHoldersResult
import com.nanoporetech.scainter.data.NewExaminationUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.model.PolicyHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewExaminationViewModel(
    private val providerName: String,
    private val repository: ScaDataRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(NewExaminationUiState())
    val uiState = _uiState.asStateFlow()

    private var loadFamilyJob: Job? = null
    private var loadedFamilyId: String? = null

    fun setPolicyHolder(policyHolder: PolicyHolder) {
        _uiState.update {
            it.copy(
                currentPolicyHolder = policyHolder
            )
        }
    }

    fun loadFamily(familyId: String) {
        if (familyId == loadedFamilyId) {
            return
        }

        // clear stale state
        _uiState.update {
            it.copy(
                familyMembers = emptyList(),
                policyHolders = emptyList(),
                currentPolicyHolder = null
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
                                    policyHolders = fetchPolicyResult.members
                                )
                            }
                            loadedFamilyId = familyId
                        }

                        is FetchPolicyHoldersResult.NetworkError -> {
                        }

                        else -> {
                        }
                    }
                }

                is FetchFamilyMembersResult.NetworkError -> {
                }

                else -> {
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

                NewExaminationViewModel(
                    providerName = providerName,
                    repository = repository
                )
            }
        }
    }
}
