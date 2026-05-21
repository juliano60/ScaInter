package com.nanoporetech.scainter.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Currency
import java.util.Locale

@Composable
fun displayedDateAndTime(date: String): String {
    val instant = dateFromISOString(date)
    return if (instant != null) {
        formatDate(instant, timeStyle = FormatStyle.SHORT)
    } else {
        "Unknown date"
    }
}

@Composable
fun displayedDate(date: String): String {
    val parsed = dateFromISOString(date)
    return if (parsed != null) {
        formatDate(parsed)
    } else {
        "Unknown date"
    }
}

private fun dateFromISOString(input: String): Instant? {
    return try {
        Instant.parse(input)
    } catch (e: Exception) {
        null
    }
}

@Composable
private fun formatDate(
    instant: Instant,
    dateStyle: FormatStyle = FormatStyle.MEDIUM,
    timeStyle: FormatStyle? = null,
    zoneId: ZoneId = ZoneId.systemDefault(),
): String {
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0]

    val formatter = if (timeStyle != null) {
        DateTimeFormatter.ofLocalizedDateTime(dateStyle, timeStyle)
    } else {
        DateTimeFormatter.ofLocalizedDate(dateStyle)
    }

    return formatter
        .withLocale(locale)
        .withZone(zoneId)
        .format(instant)
}

fun String.capitalized(): String {
    val words = this.split(" ")
    return words.joinToString(separator = " ") { word ->
        word.replaceFirstChar {  it.uppercase() }
    }
}

fun formatDoctorName(name: String): String {
    val words = name.lowercase().split(" ")

    if (words.size == 1) {
        return words[0].capitalized()
    }

    val result = if (words[0] == "dr") {
        "Dr. " + words.drop(1).joinToString(separator = " ")
    } else {
        words.joinToString(separator = " ")
    }
    return result.capitalized()
}

@Composable
fun formatCurrency(amount: Double): String {
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0]

    val currencyCode = Currency.getInstance(locale).currencyCode
    val formatter = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance(currencyCode)
    }
    return formatter.format(amount)
}