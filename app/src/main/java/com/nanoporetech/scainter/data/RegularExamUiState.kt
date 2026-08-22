package com.nanoporetech.scainter.data

import com.nanoporetech.scainter.model.ExamOption

data class RegularExamUiState(
    /** then primary care physician */
    val doctor: String = "",
    /** his/her specialty */
    val specialty: String = "",
    /** the reason for care */
    val reason: String = "",
    /** the requested examinations */
    val selectedExaminations: List<ExamOption> = listOf(
        ExamOption(0, ""), // 1st exam
        ExamOption(0, ""), // 2nd exam
        ExamOption(0, ""), // 3rd exam
        ExamOption(0, ""), // 4th exam
        ExamOption(0, ""), // 5th exam
        ExamOption(0, ""), // 6th exam
        ExamOption(0, ""), // 7th exam
        ExamOption(0, ""), // 8th exam
    ),
    /** the list of examination options */
    val examOptions: List<ExamOption>  = listOf(),
    /** we are fetching examination options */
    val isLoadingOptions: Boolean = false,
    /** the request is being submitted */
    val isSubmitting: Boolean = false
)
