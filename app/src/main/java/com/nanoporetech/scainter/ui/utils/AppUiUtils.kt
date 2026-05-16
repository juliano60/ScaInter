package com.nanoporetech.scainter.ui.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun displayedDateAndTime(date: String): String {
    val instant = dateFromISOString(date)
    return if (instant != null) {
        formatDate(instant, timeStyle = FormatStyle.SHORT)
    } else {
        "Unknown date"
    }
}

fun displayedDate(date: String): String {
    val parsed = dateFromISOString(date)
    return if (parsed != null) {
        formatDate(parsed)
    } else {
        "Unknown date"
    }
}

fun dateFromISOString(input: String): Instant? {
    return try {
        Instant.parse(input)
    } catch (e: Exception) {
        null
    }
}

fun formatDate(
    instant: Instant,
    dateStyle: FormatStyle = FormatStyle.MEDIUM,
    timeStyle: FormatStyle? = null,
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault()
): String {
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
