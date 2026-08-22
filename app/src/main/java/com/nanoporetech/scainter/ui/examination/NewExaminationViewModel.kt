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
import com.nanoporetech.scainter.ui.events.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewExaminationViewModel(
    private val providerName: String,
    private val repository: ScaDataRepository
): ViewModel() {
    private var _uiState = MutableStateFlow(NewExaminationUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

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

        viewModelScope.launch {
            // load family members
            when (val result = repository.fetchFamilyMembers(familyId = familyId)) {
                is FetchFamilyMembersResult.Success -> {
                    _uiState.update {
                        it.copy(
                            familyMembers = result.members
                        )
                    }

                    // then fetch their policy details
                    if (result.members.isEmpty()) {
                        return@launch
                    }
                    val memberIds = result.members.map { it.id }
                    when (val result = repository.fetchPolicyHolders(
                        memberIds = memberIds.joinToString(","),
                        providerName = providerName)
                    ) {
                        is FetchPolicyHoldersResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    policyHolders = result.members
                                )
                            }
                        }

                        is FetchPolicyHoldersResult.NetworkError -> {
                            //_events.emit(UiEvent.Error(R.string.err_connection_offline))
                        }

                        else -> {
                            //_events.emit(UiEvent.Error(R.string.err_unknown_error))
                        }
                    }
                }

                is FetchFamilyMembersResult.NetworkError -> {
                    //_events.emit(UiEvent.Error(R.string.err_connection_offline))
                }

                else -> {
                    //_events.emit(UiEvent.Error(R.string.err_unknown_error))
                }
            }
        }
        loadedFamilyId = familyId
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
