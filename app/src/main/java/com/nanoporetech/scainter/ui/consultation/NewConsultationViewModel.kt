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
import com.nanoporetech.scainter.ui.events.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class NewConsultationViewModel(
    private val familyId: String,
    private val providerName: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(NewConsultationUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
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
                    _events.emit(UiEvent.Success(R.string.new_consultation_success_message))
                }
                else -> {
                    _events.emit(UiEvent.Error(R.string.err_new_consultation_message))
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
            familyId: String,
            providerName: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                NewConsultationViewModel(
                    familyId = familyId,
                    providerName = providerName,
                    repository = repository
                )
            }
        }
    }
}