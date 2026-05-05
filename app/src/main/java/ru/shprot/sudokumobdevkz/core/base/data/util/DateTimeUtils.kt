package ru.shprot.sudokumobdevkz.core.base.data.util

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    private val shortDateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
    private val crashTimestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
    private val crashFileFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    private val localizedLongDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

    fun formatTimer(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }

    fun formatShortTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%d:%02d".format(m, s)
    }

    fun formatShortDate(timestampMs: Long): String =
        shortDateFormat.format(Date(timestampMs))

    fun formatLocalizedDate(dateKey: String): String =
        LocalDate.parse(dateKey).format(localizedLongDate)

    fun formatCrashTimestamp(): String =
        crashTimestampFormat.format(Date())

    fun formatCrashFileName(): String =
        crashFileFormat.format(Date())

    fun generateTimeLabels(maxSeconds: Int): List<String> {
        val step = when {
            maxSeconds <= 120 -> 30
            maxSeconds <= 300 -> 60
            maxSeconds <= 600 -> 120
            maxSeconds <= 1800 -> 300
            else -> 600
        }
        val labels = mutableListOf("0:00")
        var current = step
        while (current <= maxSeconds) {
            labels.add(formatShortTime(current))
            current += step
        }
        if (labels.size < 3) labels.add(formatShortTime(maxSeconds))
        return labels
    }
}
