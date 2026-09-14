package com.example.data.api

import com.example.data.model.BusinessProfile
import com.example.data.model.ChatMessage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

// 1. Request & Response Data Models
data class ChatRequest(
    val message: String,
    val language: String,
    val profile: BusinessProfile?,
    val chatHistory: List<ChatMessage>
)

data class ChatResponse(
    val reply: String?,
    val error: String? = null
)

// 2. API Service Interface
interface ISakshamBackendService {
    suspend fun getChatResponse(request: ChatRequest): ChatResponse
}

// 3. API Repository Implementation using OkHttp
class SakshamBackendRepository(private val client: OkHttpClient) : ISakshamBackendService {
    
    companion object {
        // Both the dynamic deployment preview URL and local loopback address are configured
        private const val LIVE_URL = "https://ais-dev-cn3bcz2l4s6epgfcmtvrwi-564829943939.asia-east1.run.app/api/ai/chat"
        private const val LOCAL_URL = "http://10.0.2.2:3000/api/ai/chat"
        
        // Active backend endpoint URL (Defaults to the live development URL)
        var activeUrl: String = LIVE_URL
    }

    override suspend fun getChatResponse(request: ChatRequest): ChatResponse {
        val json = JSONObject().apply {
            put("message", request.message)
            put("language", request.language)
            
            request.profile?.let { prof ->
                put("profile", JSONObject().apply {
                    put("businessType", prof.businessType)
                    put("isExistingBusiness", prof.isExistingBusiness)
                    put("locationType", prof.locationType)
                    put("district", prof.district)
                    put("state", prof.state)
                    put("totalInvestment", prof.totalInvestment)
                    put("ownCapital", prof.ownCapital)
                    put("loanRequired", prof.loanRequired)
                    put("annualFamilyIncome", prof.annualFamilyIncome)
                    put("businessExperience", prof.businessExperience)
                    put("dairyAnimalCount", prof.dairyAnimalCount)
                    put("dairyLandAvailable", prof.dairyLandAvailable)
                })
            }

            if (request.chatHistory.isNotEmpty()) {
                val history = JSONArray()
                request.chatHistory.forEach { msg ->
                    history.put(JSONObject().apply {
                        put("text", msg.text)
                        put("isUser", msg.isUser)
                    })
                }
                put("chatHistory", history)
            }
        }

        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        
        val okRequest = Request.Builder()
            .url(activeUrl)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(okRequest).execute()
            val bodyStr = response.body?.string() ?: ""
            
            if (response.isSuccessful) {
                val respJson = JSONObject(bodyStr)
                val reply = respJson.optString("reply", null)
                return ChatResponse(reply = reply)
            } else {
                val respJson = try { JSONObject(bodyStr) } catch(e: Exception) { null }
                val errorMsg = respJson?.optString("error") ?: "Server Error (HTTP ${response.code})"
                return ChatResponse(reply = null, error = errorMsg)
            }
        } catch (e: Exception) {
            // Attempt auto-fallback to local development loopback if live is unreachable
            if (activeUrl == LIVE_URL) {
                try {
                    val localOkRequest = Request.Builder()
                        .url(LOCAL_URL)
                        .post(requestBody)
                        .build()
                    val response = client.newCall(localOkRequest).execute()
                    val bodyStr = response.body?.string() ?: ""
                    if (response.isSuccessful) {
                        val respJson = JSONObject(bodyStr)
                        return ChatResponse(reply = respJson.optString("reply", null))
                    }
                } catch (localEx: Exception) {
                    // Ignore, return original error
                }
            }
            return ChatResponse(reply = null, error = "Connection failed: ${e.localizedMessage ?: "Timeout"}")
        }
    }
}
