package com.nanoporetech.scainter.ui.examination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nanoporetech.scainter.R
import com.nanoporetech.scainter.ScaInterApplication
import com.nanoporetech.scainter.data.FetchExaminationOptionsResult
import com.nanoporetech.scainter.data.NewRegularExaminationResult
import com.nanoporetech.scainter.data.RegularExamUiState
import com.nanoporetech.scainter.data.ScaDataRepository
import com.nanoporetech.scainter.model.ExamOption
import com.nanoporetech.scainter.ui.events.UiMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegularExaminationViewModel(
    private val providerName: String,
    private val insuranceType: String,
    private val userId: String,
    private val repository: ScaDataRepository
): ViewModel() {

    private var _uiState = MutableStateFlow(RegularExamUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<UiMessage>()
    val events = _events.asSharedFlow()

    init {
        loadOptions()
    }

    fun loadOptions() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoadingOptions = true)
            }

            when (val result = repository.fetchExaminationOptions(provider = providerName, insuranceType = insuranceType)) {
                is FetchExaminationOptionsResult.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            examOptions = result.examOptions,
                            isLoadingOptions = false
                        )
                    }
                }
                is FetchExaminationOptionsResult.NetworkError -> {
                    _uiState.update { it.copy(isLoadingOptions = false) }
                }
                else -> {
                    _uiState.update { it.copy(isLoadingOptions = false) }
                }
            }
        }
    }

    fun submitRequest() {
        if (_uiState.value.isSubmitting) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSubmitting = true)
            }

            try {
                when (repository.newRegularExamination(
                    provider = providerName,
                    userId = userId,
                    doctor = _uiState.value.doctor,
                    specialty = _uiState.value.specialty,
                    reason = _uiState.value.reason,
                    insuranceType = insuranceType,
                    exam1 = _uiState.value.selectedExaminations[0].id.toString(),
                    exam2 = _uiState.value.selectedExaminations[1].id.toString(),
                    exam3 = _uiState.value.selectedExaminations[2].id.toString(),
                    exam4 = _uiState.value.selectedExaminations[3].id.toString(),
                    exam5 = _uiState.value.selectedExaminations[4].id.toString(),
                    exam6 = _uiState.value.selectedExaminations[5].id.toString(),
                    exam7 = _uiState.value.selectedExaminations[6].id.toString(),
                    exam8 = _uiState.value.selectedExaminations[7].id.toString()
                )) {
                    NewRegularExaminationResult.Success -> {
                        _events.emit(UiMessage.Success(R.string.new_regular_exam_success_message))
                    }

                    NewRegularExaminationResult.NetworkError -> {
                        _events.emit(UiMessage.Error(R.string.err_network_error_message))
                    }

                    NewRegularExaminationResult.UnknownError -> {
                        _events.emit(UiMessage.Error(R.string.err_unknown_error_message))
                    }
                }
            } finally {
                _uiState.update {
                    it.copy(isSubmitting = false)
                }
            }
        }
    }

    fun setDoctor(doctor: String) {
        _uiState.update {
            it.copy(doctor = doctor)
        }
    }

    fun setReason(reason: String) {
        _uiState.update {
            it.copy(reason = reason)
        }
    }

    fun setSpecialty(specialty: String) {
        _uiState.update {
            it.copy(specialty = specialty)
        }
    }

    fun setSelectedExamination(index: Int, examOption: ExamOption) {
        _uiState.update { state ->
            state.copy(
                selectedExaminations = state.selectedExaminations.mapIndexed { i, v ->
                    if (i == index) examOption else v
                },
            )
        }
    }

    companion object {
        fun provideFactory(
            providerName: String,
            insuranceType: String,
            userId: String,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ScaInterApplication
                val repository = application.container.scaDataRepository

                RegularExaminationViewModel(
                    providerName = providerName,
                    insuranceType = insuranceType,
                    userId = userId,
                    repository = repository
                )
            }
        }
    }
}
