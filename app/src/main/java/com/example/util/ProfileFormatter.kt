package com.example.util

import java.util.Locale

object ProfileFormatter {

    val CASTE_OPTIONS = listOf(
        "Scheduled Caste (SC)",
        "Scheduled Tribe (ST)",
        "Other Backward Class (OBC)",
        "Minority Community",
        "General / EWS",
        "Other"
    )

    val GENDER_OPTIONS = listOf(
        "Male",
        "Female",
        "Other",
        "Prefer not to say"
    )

    val FAMILY_INCOME_OPTIONS = listOf(
        "Below ₹1.50 Lakh",
        "₹1.50 - 3.00 Lakh",
        "₹3.00 - 6.00 Lakh",
        "Above ₹6.00 Lakh"
    )

    /**
     * Formats raw caste/social category into standardized user-friendly label.
     * Prevents raw enum strings like SCHEDULED_CASTE, SC, OBC from leaking to UI.
     */
    fun formatCaste(raw: String?): String {
        if (raw.isNullOrBlank()) return "Scheduled Caste (SC)"
        val clean = raw.trim()
        val upper = clean.uppercase(Locale.getDefault())

        return when {
            upper.contains("SCHEDULED_CASTE") || upper.contains("SCHEDULED CASTE") || upper == "SC" -> "Scheduled Caste (SC)"
            upper.contains("SCHEDULED_TRIBE") || upper.contains("SCHEDULED TRIBE") || upper == "ST" -> "Scheduled Tribe (ST)"
            upper.contains("OBC") || upper.contains("OTHER_BACKWARD") || upper.contains("OTHER BACKWARD") -> "Other Backward Class (OBC)"
            upper.contains("MINORITY") -> "Minority Community"
            upper.contains("GENERAL") || upper.contains("EWS") -> "General / EWS"
            upper.contains("OTHER") -> "Other"
            clean in CASTE_OPTIONS -> clean
            else -> {
                clean.replace("_", " ")
                    .split(" ")
                    .joinToString(" ") { word ->
                        word.lowercase(Locale.getDefault()).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    }
            }
        }
    }

    /**
     * Formats full name with proper title casing and trimmed spaces.
     */
    fun formatName(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        val trimmed = raw.trim().replace("\\s+".toRegex(), " ")
        return trimmed.split(" ").joinToString(" ") { word ->
            word.lowercase(Locale.getDefault()).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }

    /**
     * Validates and returns integer age between 18 and 99.
     */
    fun formatAge(raw: Any?): Int {
        val num = when (raw) {
            is Int -> raw
            is Number -> raw.toInt()
            is String -> raw.filter { it.isDigit() }.toIntOrNull()
            else -> null
        }
        return num?.coerceIn(18, 99) ?: 25
    }

    /**
     * Standardizes gender selection.
     */
    fun formatGender(raw: String?): String {
        if (raw.isNullOrBlank()) return "Male"
        val clean = raw.trim()
        val lower = clean.lowercase(Locale.getDefault())
        return when {
            lower.startsWith("m") && !lower.contains("fe") -> "Male"
            lower.startsWith("f") || lower.contains("female") -> "Female"
            lower.contains("prefer") -> "Prefer not to say"
            clean in GENDER_OPTIONS -> clean
            else -> "Other"
        }
    }

    /**
     * Validates and formats 10-digit mobile number with +91 prefix.
     */
    fun formatPhone(raw: String?): String {
        if (raw.isNullOrBlank()) return "+91 98765 43210"
        val digits = raw.filter { it.isDigit() }
        val phoneNum = if (digits.length >= 10) digits.takeLast(10) else digits
        return if (phoneNum.length == 10) "+91 $phoneNum" else raw.trim()
    }

    /**
     * Validates and normalizes email string.
     */
    fun formatEmail(raw: String?): String {
        if (raw.isNullOrBlank()) return ""
        return raw.trim().lowercase(Locale.getDefault())
    }

    /**
     * Formats annual family income for display.
     */
    fun formatIncome(raw: String?): String {
        if (raw.isNullOrBlank()) return "₹1.50 - 3.00 Lakh"
        val clean = raw.trim()
        val lower = clean.lowercase(Locale.getDefault())
        return when {
            lower.contains("below 1.5") || lower.contains("under 1.5") || lower.contains("below ₹1.5") -> "Below ₹1.50 Lakh"
            lower.contains("1.50 - 3") || lower.contains("1.5 - 3") || lower.contains("1.50-3.00") -> "₹1.50 - 3.00 Lakh"
            lower.contains("3.00 - 6") || lower.contains("3 - 6") || lower.contains("3.00-6.00") -> "₹3.00 - 6.00 Lakh"
            lower.contains("above 6") || lower.contains("above ₹6") -> "Above ₹6.00 Lakh"
            clean in FAMILY_INCOME_OPTIONS -> clean
            else -> clean
        }
    }

    /**
     * Converts raw family income string into numeric Long (in Rupees) for rule evaluation.
     */
    fun parseIncomeToNumeric(raw: String?): Long {
        if (raw.isNullOrBlank()) return 200000L
        val clean = raw.trim()
        val lower = clean.lowercase(Locale.getDefault())
        return when {
            lower.contains("below 1.5") || lower.contains("under 1.5") -> 120000L
            lower.contains("1.50 - 3") || lower.contains("1.5 - 3") || lower.contains("1.50-3.00") -> 225000L
            lower.contains("3.00 - 6") || lower.contains("3 - 6") || lower.contains("3.00-6.00") -> 450000L
            lower.contains("above 6") || lower.contains("> 6") -> 750000L
            else -> {
                val digitsOnly = clean.filter { it.isDigit() }
                if (digitsOnly.isNotEmpty()) {
                    val parsed = digitsOnly.toLongOrNull() ?: 200000L
                    if (parsed in 1..99) parsed * 100000L else parsed
                } else 200000L
            }
        }
    }
}
