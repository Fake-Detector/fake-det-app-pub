package com.zhulin.fakedet.ui.helpers

import androidx.compose.ui.res.stringResource
import com.zhulin.fakedet.R
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateHelpers {

    fun LocalDateTime.customFormat(getString: (id: Int) -> String): String {
        val dayOfWeekDict = mapOf(
            DayOfWeek.MONDAY to getString(R.string.date_monday),
            DayOfWeek.TUESDAY to getString(R.string.date_tuesday),
            DayOfWeek.WEDNESDAY to getString(R.string.date_wednesday),
            DayOfWeek.THURSDAY to getString(R.string.date_thursday),
            DayOfWeek.FRIDAY to getString(R.string.date_friday),
            DayOfWeek.SATURDAY to getString(R.string.date_saturday),
            DayOfWeek.SUNDAY to getString(R.string.date_sunday),
        )

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        return when {
            this.toLocalDate()
                .isEqual(today) -> "${getString(R.string.date_today)} ${this.format(DateTimeFormatter.ofPattern("HH:mm"))}"

            this.toLocalDate()
                .isEqual(yesterday) -> "${getString(R.string.date_yesterday)} ${this.format(DateTimeFormatter.ofPattern("HH:mm"))}"

            this.toLocalDate().isAfter(startOfWeek) -> dayOfWeekDict[this.dayOfWeek]!!

            else -> this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }
}