package com.sportevents.util

import android.util.Log
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class Util {
    companion object {

        fun log(s: String) {
            Log.e("---", s)
        }

        fun millisecondsToDuration(milliseconds: Long): String {
            val duration = milliseconds.toDuration(DurationUnit.MILLISECONDS)

            val days = duration.inWholeDays

            val hours = duration.inWholeHours
            val remainingMinutes =
                duration.inWholeMinutes - hours * 60L  // Remaining minutes after hours
            val seconds =
                duration.inWholeSeconds - hours * 3600L - remainingMinutes * 60L  // Remaining seconds

            // Build the string with appropriate formatting
            if (days > 0) {
                return "$days days"
            }
            return String.format("%02d:%02d:%02d", hours, remainingMinutes, seconds)
        }

    }
}