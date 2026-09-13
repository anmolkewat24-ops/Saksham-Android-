package com.example.data.api

import com.example.BuildConfig
import com.example.data.model.BusinessProfile
import com.example.data.model.ChatMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiAdvisorService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val apiKey: String
        get() = try {
            val envKey1 = System.getenv("GEMINI_API_KEY_1")
            val envKey = System.getenv("GEMINI_API_KEY")
            val buildKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Throwable) { "" }
            val buildKey1 = try {
                BuildConfig::class.java.getField("GEMINI_API_KEY_1").get(null) as? String ?: ""
            } catch (e: Throwable) {
                ""
            }
            listOf(envKey1, envKey, buildKey, buildKey1)
                .firstOrNull { isValidKey(it) } ?: ""
        } catch (e: Throwable) {
            ""
        }

    private fun isValidKey(k: String?): Boolean {
        if (k.isNullOrBlank()) return false
        if (k == "MY_GEMINI_API_KEY" || k == "MY_GEMINI_API_KEY_1") return false
        return true
    }

    private val systemInstruction = """
        You are 'Saksham Saathi', the official expert AI Financial & Business Advisor for the Saksham government portal under the Ministry of Social Justice & Empowerment & NSFDC (National Scheduled Castes Finance & Development Corporation), Government of India.
        
        CRITICAL INSTRUCTIONS:
        1. ANSWER SPECIFICALLY AND CONTEXTUALLY: 
           - Identify the exact core of the user's question. Answer that exact question clearly.
           - Understand conversation history. If the user says "10 cows", they mean 10 cows for the dairy business discussed earlier.
           - If the user's profile info is provided, use it to give personalized recommendations (e.g., if their project cost is ₹8 lakh, base your answers on that).
           - Provide actionable entrepreneur guidance (idea, business selection, market, investment, loan selection, application process, etc.).
           - NEVER give identical generic answers to different questions.
           - If the user asks in Hindi or Hinglish, answer in the same language.
        2. FACTUAL ACCURACY (DO NOT FABRICATE):
           - Rely on official NSFDC guidelines. Do not invent government schemes, channel partners, or interest rates.
           - If you don't know exact official details, state that clearly and give general business advice.
        3. FORMATTING RULES:
           - Use clean, bold headings with emojis.
           - Format details in short, scannable bullet points (•) and numbered steps (1., 2.).
           - Avoid huge walls of text. Be concise, friendly, professional, and practical.
           - DO NOT constantly start with "Sure! Here is..." or "Based on your query...". Be natural.
        4. SUGGESTED QUESTIONS (CRITICAL):
           - At the very end of your response, you MUST provide 3 to 4 suggested follow-up questions tailored to the current context.
           - Format them EXACTLY like this:
             SUGGESTIONS:
             - Your first question here
             - Your second question here
             - Your third question here
    """.trimIndent()

    suspend fun getAdvice(
        userPrompt: String,
        profile: BusinessProfile?,
        language: String = "English",
        chatHistory: List<ChatMessage> = emptyList()
    ): String {
        return withContext(Dispatchers.IO) {
            if (!isValidKey(apiKey)) {
                return@withContext getOfflineVerifiedAdvice(userPrompt, profile, language)
            }

            try {
                // Using Gemini 3.5 Flash as required by system standards.
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=${'$'}apiKey"

                val jsonBody = JSONObject()
                val contentsArray = JSONArray()

                // Inject Context into the FIRST user message or append if history is empty
                val contextInfo = if (profile != null) {
                    """
                    [SYSTEM NOTE: User Profile Data (Use this for context)]
                    • Business Focus: ${'$'}{profile.businessType} (${'$'}{if (profile.isExistingBusiness) "Existing" else "New Greenfield Startup"})
                    • Location: ${'$'}{profile.locationType} area (${'$'}{profile.district}, ${'$'}{profile.state})
                    • Estimated Project Cost: ₹${'$'}{profile.totalInvestment}
                    • Promoter Equity: ₹${'$'}{profile.ownCapital} | Loan Required: ₹${'$'}{profile.loanRequired}
                    • Annual Family Income: ${'$'}{profile.annualFamilyIncome} | Experience: ${'$'}{profile.businessExperience}
                    • Cattle Scale (if dairy): ${'$'}{profile.dairyAnimalCount} cows/buffaloes, ${'$'}{profile.dairyLandAvailable}
                    • Preferred Language: ${'$'}language
                    """.trimIndent() + "\n\n"
                } else {
                    "[SYSTEM NOTE: Preferred Language: ${'$'}language]\n\n"
                }

                if (chatHistory.isEmpty()) {
                    // First message
                    contentsArray.put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", contextInfo + userPrompt))
                        })
                    })
                } else {
                    // Reconstruct history
                    chatHistory.forEachIndexed { index, msg ->
                        val text = if (index == 0 && msg.isUser) contextInfo + msg.text else msg.text
                        contentsArray.put(JSONObject().apply {
                            put("role", if (msg.isUser) "user" else "model")
                            put("parts", JSONArray().apply {
                                put(JSONObject().put("text", text))
                            })
                        })
                    }
                    // Add current prompt
                    contentsArray.put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", userPrompt))
                        })
                    })
                }

                jsonBody.put("contents", contentsArray)

                jsonBody.put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemInstruction))
                    })
                })

                jsonBody.put("generationConfig", JSONObject().apply {
                    put("temperature", 0.6)
                })

                val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder().url(url).post(requestBody).build()

                val response = client.newCall(request).execute()
                val responseBodyStr = response.body?.string() ?: ""

                if (response.isSuccessful) {
                    val respJson = JSONObject(responseBodyStr)
                    val candidates = respJson.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCandidate = candidates.getJSONObject(0)
                        val content = firstCandidate.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val responseText = parts.getJSONObject(0).optString("text", "")
                            if (responseText.isNotBlank()) {
                                return@withContext responseText
                            }
                        }
                    }
                }
                
                // Fallback
                getOfflineVerifiedAdvice(userPrompt, profile, language)
            } catch (e: Exception) {
                getOfflineVerifiedAdvice(userPrompt, profile, language)
            }
        }
    }

    fun getOfflineVerifiedAdvice(prompt: String, profile: BusinessProfile?, language: String): String {
        val p = prompt.lowercase()
        val isHindi = language in listOf("हिन्दी", "Hindi", "मराठी", "Marathi") ||
                p.contains("दस्तावेज") || p.contains("कागज") || p.contains("ब्याज") || p.contains("छूट") ||
                p.contains("डेयरी") || p.contains("लोन") || p.contains("पात्रता") || p.contains("गाय") || p.contains("भैंस")

        val animalCount = profile?.dairyAnimalCount ?: 4
        val totalInv = profile?.totalInvestment ?: 500000L
        val loanReq = profile?.loanRequired ?: 450000L
        val ownCap = profile?.ownCapital ?: 50000L
        val fmtTotal = "%,d".format(totalInv)
        val fmtLoan = "%,d".format(loanReq)
        val fmtOwn = "%,d".format(ownCap)

        val response = when {
            p.contains("document") || p.contains("dastavez") || p.contains("paper") || p.contains("kagaaz") ||
                    p.contains("kagaz") || p.contains("certificate") || p.contains("praman") || p.contains("दस्तावेज") || p.contains("कागज") -> {
                if (isHindi) {
                    """
                    📑 **आवश्यक आधिकारिक दस्तावेजों की सूची (Checklist)**
                    सरकारी ऋण (NSFDC / PMEGP) के लिए निम्नलिखित दस्तावेज तैयार रखें:
                    1. **पहचान प्रमाण**: आधार कार्ड या पैन कार्ड।
                    2. **जाति प्रमाण पत्र**: डिजिटल सक्षम अधिकारी द्वारा जारी अनुसूचित जाति (SC) प्रमाण पत्र।
                    3. **आय प्रमाण पत्र**: तहसीलदार/राजस्व अधिकारी द्वारा जारी (₹3.00 लाख से कम वार्षिक)।
                    4. **निवास प्रमाण**: बिजली का बिल, वोटर आईडी या राशन कार्ड।
                    5. **बैंक खाता**: कम से कम 6 महीने का बैंक स्टेटमेंट।
                    6. **प्रोजेक्ट रिपोर्ट (DPR)**: व्यवसाय का विवरण और खर्च का अनुमान।
                    7. **कोटेशन (Quotation)**: मशीनरी/सामग्री खरीदने के लिए सप्लायर का पक्का बिल।
                    """.trimIndent()
                } else {
                    """
                    📑 **Official Required Documents Checklist**
                    Keep these essential documents ready for NSFDC / PMEGP loan processing:
                    1. **Identity Proof**: Aadhaar Card or PAN Card.
                    2. **Caste Certificate**: Digitally signed Scheduled Caste (SC) certificate.
                    3. **Family Income Certificate**: Issued by Revenue Authority (below ₹3.00 Lakh p.a.).
                    4. **Address Proof**: Electricity bill, Voter ID, or valid Ration Card.
                    5. **Bank Account Details**: Last 6 months' statement of active savings/current account.
                    6. **Detailed Project Report (DPR)**: Business plan, cost breakdown, and projected earnings.
                    7. **Vendor Quotations**: Proforma invoices for machinery, cattle, or raw materials.
                    """.trimIndent()
                }
            }
            // Add fallback generic response
            else -> {
                val bType = profile?.businessType ?: "Small Business"
                if (isHindi) {
                    """
                    🇮🇳 **सक्षम साथी - ${'$'}bType व्यवसाय एवं ऋण मार्गदर्शन**
                    **1. वित्तीय योजना एवं पैमाना:**
                    • कुल अनुमानित निवेश: **₹${'$'}fmtTotal**
                    • आवेदक की पूंजी: **₹${'$'}fmtOwn**
                    • आवश्यक सरकारी ऋण: **₹${'$'}fmtLoan**
                    
                    **2. त्वरित अगला कदम:**
                    1. व्यवसाय के लिए आवश्यक मशीनरी या माल का अधिकृत डीलर से कोटेशन लें।
                    2. जाति व आय प्रमाण पत्र के साथ अपने जिले के **विकास भवन स्थित SCA कार्यालय** में संपर्क करें।
                    """.trimIndent()
                } else {
                    """
                    🇮🇳 **Saksham Advisor - Comprehensive Business Guidance for ${'$'}bType**
                    **1. Capital & Financial Architecture:**
                    • Total Estimated Capital: **₹${'$'}fmtTotal**
                    • Promoter Equity: **₹${'$'}fmtOwn**
                    • Required Concessional Debt: **₹${'$'}fmtLoan**
                    
                    **2. Action Plan to Proceed:**
                    1. Secure a formal proforma invoice / quotation from approved equipment vendors.
                    2. Submit the docket at your District **Vikas Bhavan SCA Office** or designated Lead Bank.
                    """.trimIndent()
                }
            }
        }
        
        val suggestions = if (isHindi) {
            """
            
            SUGGESTIONS:
            - मुझे प्रोजेक्ट रिपोर्ट (DPR) कैसे बनानी चाहिए?
            - EMI कितनी आएगी?
            - आवेदन कैसे करें?
            """.trimIndent()
        } else {
            """
            
            SUGGESTIONS:
            - How to prepare a Project Report (DPR)?
            - How to apply?
            - What will be my EMI?
            """.trimIndent()
        }
        return response + suggestions
    }
}
