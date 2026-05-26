package com.nanoporetech.scainter.ui.hospitalisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.FetchFamilyMembersResult
import com.nanoporetech.scainter.data.NewConsultationUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewHospitalisationViewModel(
    private val familyId: String,
    private val repository: ScaDataRepository
    ): ViewModel() {
        private var _uiState = MutableStateFlow(NewConsultationUiState())
        val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            when (val result = repository.fetchFamilyMembers(familyId = familyId)) {
                is FetchFamilyMembersResult.Success -> {
                    _uiState.update {
                        it.copy(
                            familyMembers = result.members
                        )
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

    companion object {
        fun provideFactory(
            familyId: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                NewHospitalisationViewModel(
                    familyId = familyId,
                    repository = repository
                )
            }
        }
    }
}