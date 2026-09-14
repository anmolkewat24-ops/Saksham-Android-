package com.example.util

import android.util.Base64

object KeySecurityUtil {
    private const val MASK_PREFIX_LENGTH = 4
    private const val MASK_SUFFIX_LENGTH = 4
    private const val XOR_KEY = 0x5A.toByte()

    /**
     * Safely masks an API key so full key is never displayed in UI or logs.
     * Example: "AIzaSyB1234567890" -> "AIza...7890"
     */
    fun maskKey(key: String): String {
        val trimmed = key.trim()
        if (trimmed.isEmpty()) return "No Key Configured"
        if (trimmed.length <= (MASK_PREFIX_LENGTH + MASK_SUFFIX_LENGTH)) {
            return "${trimmed.take(2)}...${trimmed.takeLast(2)}"
        }
        return "${trimmed.take(MASK_PREFIX_LENGTH)}...${trimmed.takeLast(MASK_SUFFIX_LENGTH)}"
    }

    /**
     * Light XOR + Base64 encryption to avoid storing raw plaintext keys in SQLite DB.
     */
    fun encryptKey(rawKey: String): String {
        if (rawKey.isBlank()) return ""
        return try {
            val bytes = rawKey.toByteArray(Charsets.UTF_8)
            val obfuscated = ByteArray(bytes.size) { i -> (bytes[i].toInt() xor XOR_KEY.toInt()).toByte() }
            Base64.encodeToString(obfuscated, Base64.NO_WRAP)
        } catch (e: Throwable) {
            rawKey
        }
    }

    /**
     * Decrypts key back to raw string for API call execution.
     */
    fun decryptKey(encryptedKey: String): String {
        if (encryptedKey.isBlank()) return ""
        return try {
            val decoded = Base64.decode(encryptedKey, Base64.NO_WRAP)
            val original = ByteArray(decoded.size) { i -> (decoded[i].toInt() xor XOR_KEY.toInt()).toByte() }
            String(original, Charsets.UTF_8)
        } catch (e: Throwable) {
            encryptedKey
        }
    }

    /**
     * Checks basic format validity for Gemini API keys.
     */
    fun isValidKey(key: String?): Boolean {
        if (key.isNullOrBlank()) return false
        val trimmed = key.trim()
        if (trimmed == "MY_GEMINI_API_KEY" || trimmed == "MY_GEMINI_API_KEY_1") return false
        if (trimmed.length < 15) return false
        return true
    }
}
