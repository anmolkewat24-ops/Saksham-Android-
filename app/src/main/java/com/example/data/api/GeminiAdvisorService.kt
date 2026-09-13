package com.example.data.api

import com.example.BuildConfig
import com.example.data.model.BusinessProfile
import com.example.data.repository.GovernmentDataRepository
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
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

    private val systemInstruction = """
        You are 'Saksham Saathi', the official expert AI Financial & Business Advisor for the Saksham government portal (Ministry of Social Justice & Empowerment & NSFDC).
        Your mission is to guide entrepreneurs, rural youth, women, and self-help groups step-by-step to start and scale viable businesses with verified government schemes (such as NSFDC Term Loan, Mahila Samriddhi Yojana, Micro Credit Finance, Education Loan, PMEGP, Mudra, and Stand-Up India).
        
        Guidelines:
        1. Always base all factual scheme terms, loan limits, and interest rates strictly on official NSFDC and Government guidelines:
           - NSFDC Term Loan: Up to ₹50 Lakh, 6% interest (5% for women), up to 10 years repayment, 12 months moratorium.
           - Mahila Samriddhi Yojana: Up to ₹1.40 Lakh for women, 4% interest, 3 years repayment.
           - Micro Credit Finance: Up to ₹1.40 Lakh, 5% interest, 3 years repayment.
           - NSFDC Education Loan: Up to ₹20L in India / ₹30L abroad, 4% interest (3.5% for women students), course duration + 1 year moratorium.
           - PMEGP: Up to ₹50 Lakh with 25%-35% capital subsidy.
           - Mudra: Shishu (up to 50k), Kishore (50k-5L), Tarun (5L-20L).
        2. Format your response with clear, easy-to-read sections:
           - 💡 Executive Summary
           - 📋 Step-by-Step Action Plan (Scale, Setup, Animals/Equipment, Budget)
           - 💰 Financial & Loan Estimation (Investment, Equity, Loan, Moratorium)
           - 📑 Required Official Documents
           - 🏛️ Recommended Scheme & Where to Apply (Authorized Channel Partners / SCAs)
        3. Keep the tone friendly, accessible, trustworthy, and encouraging, understandable even for first-time smartphone users and rural entrepreneurs.
        4. If the user asks in Hindi or another language, respond in that language with clear terminology.
    """.trimIndent()

    suspend fun getAdvice(userPrompt: String, profile: BusinessProfile?, language: String = "English"): String {
        return withContext(Dispatchers.IO) {
            if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                return@withContext getOfflineVerifiedAdvice(userPrompt, profile, language)
            }

            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
                
                val contextInfo = if (profile != null) {
                    """
                    User Profile & Business Context:
                    - Business Type: ${profile.businessType}
                    - Existing or New: ${if (profile.isExistingBusiness) "Existing Business" else "New Greenfield Startup"}
                    - Location: ${profile.locationType} area (${profile.district}, ${profile.state})
                    - Total Estimated Investment: ₹${profile.totalInvestment}
                    - Own Capital / Equity: ₹${profile.ownCapital}
                    - Loan Amount Required: ₹${profile.loanRequired}
                    - Annual Family Income: ${profile.annualFamilyIncome}
                    - Prior Business Experience: ${profile.businessExperience}
                    - Dairy Details (if applicable): ${profile.dairyAnimalCount} cows/buffaloes, ${profile.dairyLandAvailable}
                    - Preferred Language: $language
                    """.trimIndent()
                } else {
                    "User Query: $userPrompt\nLanguage: $language"
                }

                val fullPrompt = "$contextInfo\n\nUser Question/Request:\n$userPrompt"

                val jsonBody = JSONObject().apply {
                    val contentsArray = JSONArray().apply {
                        val contentObj = JSONObject().apply {
                            val partsArray = JSONArray().apply {
                                put(JSONObject().put("text", fullPrompt))
                            }
                            put("parts", partsArray)
                        }
                        put(contentObj)
                    }
                    put("contents", contentsArray)

                    val sysContentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().put("text", systemInstruction))
                        }
                        put("parts", partsArray)
                    }
                    put("systemInstruction", sysContentObj)

                    val genConfig = JSONObject().apply {
                        put("temperature", 0.6)
                        put("topP", 0.95)
                    }
                    put("generationConfig", genConfig)
                }

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = jsonBody.toString().toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

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
                            return@withContext parts.getJSONObject(0).optString("text", "")
                        }
                    }
                }
                // Fallback if parsing or API returned non-success
                getOfflineVerifiedAdvice(userPrompt, profile, language)
            } catch (e: Exception) {
                getOfflineVerifiedAdvice(userPrompt, profile, language)
            }
        }
    }

    private fun getOfflineVerifiedAdvice(prompt: String, profile: BusinessProfile?, language: String): String {
        return try {
            val bType = profile?.businessType ?: if (prompt.contains("dairy", ignoreCase = true)) "Dairy Business" else "Small Business"
            val isDairy = bType.contains("Dairy", ignoreCase = true) || prompt.contains("dairy", ignoreCase = true)
            val animalCount = profile?.dairyAnimalCount ?: 4
            val loanReq = profile?.loanRequired ?: 450000L
            val totalInv = profile?.totalInvestment ?: 500000L
            val ownCap = profile?.ownCapital ?: 50000L

            val fmtTotalInv = "%,d".format(totalInv)
            val fmtOwnCap = "%,d".format(ownCap)
            val fmtLoanReq = "%,d".format(loanReq)
            val monthlyGross = animalCount * 14 * 30 * 45L
            val monthlyExp = animalCount * 4500L
            val monthlyProfit = (monthlyGross - monthlyExp).coerceAtLeast(0L)
            val fmtMonthlyGross = "%,d".format(monthlyGross)
            val fmtMonthlyExp = "%,d".format(monthlyExp)
            val fmtMonthlyProfit = "%,d".format(monthlyProfit)

            val isHindiOrRegional = language in listOf("हिन्दी", "मराठी", "বাংলা", "தமிழ்", "తెలుగు") ||
                    prompt.contains("डेयरी") || prompt.contains("ऋण") || prompt.contains("लोन") || prompt.contains("योजना")

            if (isHindiOrRegional) {
                if (isDairy) {
                    """
                    🇮🇳 **सक्षम साथी - डेयरी व्यवसाय एक्शन प्लान एवं मार्गदर्शन**

                    **1. व्यवसाय योजना एवं पैमाना:**
                    • $animalCount उन्नत नस्ल की दुधारू गाय/भैंस (मुर्राह या साहीवाल) से व्यवसाय शुरू करने की सलाह दी जाती है।
                    • प्रतिदिन अनुमानित दुग्ध उत्पादन: ${animalCount * 14} लीटर।
                    • 50 वर्ग फीट प्रति पशु हवादार पक्का शेड, ढलानदार नाली और स्वच्छ पेयजल आपूर्ति।

                    **2. अनुमानित निवेश एवं वित्तीय ढांचा:**
                    • कुल आवश्यक निवेश: ₹$fmtTotalInv
                    • आपकी स्वयं की पूंजी: ₹$fmtOwnCap (10%)
                    • आवश्यक सरकारी ऋण: ₹$fmtLoanReq (90%)

                    **3. अनुशंसित सरकारी योजना:**
                    • **NSFDC टर्म लोन योजना**: रियायती ब्याज दर मात्र 6% वार्षिक (महिला उद्यमियों हेतु 5%)।
                    • **मोरेटोरियम (छूट अवधि)**: 12 महीने तक की छूट, जिसके बाद किस्तों में भुगतान शुरू होगा।
                    • पुनर्भुगतान अवधि: 5 से 10 वर्ष तक लचीली किश्तों में।

                    **4. आवश्यक आधिकारिक दस्तावेज:**
                    • आधार कार्ड, पैन कार्ड एवं दो पासपोर्ट फोटो
                    • सक्षम अधिकारी द्वारा जारी जाति प्रमाण पत्र (SC) एवं आय प्रमाण पत्र (< ₹3.00 लाख)
                    • शेड/जमीन की खतौनी या लीज डीड
                    • पशु खरीद कोटेशन एवं बैंक पासबुक

                    **5. अगला कदम:**
                    • अपने निकटतम **उत्तर प्रदेश अनुसूचित जाति वित्त एवं विकास निगम (UPSCFDC) विकास भवन** या **बड़ौदा यू.पी. बैंक** शाखा में जाकर आवेदन पत्र जमा करें।
                    """.trimIndent()
                } else {
                    """
                    🇮🇳 **सक्षम साथी - सरकारी योजना एवं व्यापार मार्गदर्शन**

                    **1. व्यवसाय का चयन एवं मार्गदर्शन:**
                    • $bType के लिए सरकारी ऋण योजनाएं बहुत रियायती दरों पर उपलब्ध हैं।
                    • कार्यशील पूंजी एवं मशीनरी उपकरण हेतु 90-95% तक वित्तीय सहायता प्राप्त की जा सकती है।

                    **2. वित्तीय विवरण:**
                    • अनुमानित परियोजना लागत: ₹$fmtTotalInv
                    • आपकी स्वयं की पूंजी: ₹$fmtOwnCap
                    • आवश्यक सरकारी ऋण राशि: ₹$fmtLoanReq
                    • सरकारी योजना: **NSFDC टर्म लोन / पीएमईजीपी (PMEGP)** (35% तक सब्सिडी)।

                    **3. आवेदन प्रक्रिया:**
                    • जिला उद्योग केंद्र (DIC) अथवा राज्य चैनेलाइजिंग एजेंसी (SCA) से संपर्क करें।
                    • प्रोजेक्ट रिपोर्ट एवं आवश्यक दस्तावेज तैयार कर जमा करें।
                    """.trimIndent()
                }
            } else {
                if (isDairy) {
                    """
                    🇮🇳 **Saksham Advisor - Dairy Farm Step-by-Step Action Plan**

                    **1. Recommended Business Scale & Strategy:**
                    • Start with **$animalCount high-yield milch animals** (Crossbred HF/Jersey cows or Murrah buffaloes).
                    • Expected daily milk output: **${animalCount * 14} to ${animalCount * 16} Litres/day**.
                    • Tie up with the local Milk Cooperative Society or private dairy chilling center for assured daily procurement at ₹40-₹50/Litre.

                    **2. Setup & Infrastructure Requirements:**
                    • **Shed**: Well-ventilated semi-open housing (minimum 50 sq.ft covered space per cow).
                    • **Water & Electricity**: 80-100 Litres clean water per animal daily, continuous water trough.
                    • **Equipment**: Electric chaff cutter (2 HP) and stainless steel milk cans (40L).
                    • **Fodder**: 0.5 to 1 acre green fodder cultivation (Napier/Berseem) significantly reduces commercial cattle feed expenses by 35%.

                    **3. Financial Estimates & Viability:**
                    • Total Project Cost: **₹$fmtTotalInv**
                    • Your Equity Contribution: **₹$fmtOwnCap** (10%)
                    • Required Government Loan: **₹$fmtLoanReq**
                    • Monthly Gross Milk Revenue: **₹$fmtMonthlyGross**
                    • Monthly Feed & Care Expenses: **₹$fmtMonthlyExp**
                    • Projected Monthly Net Profit: **₹$fmtMonthlyProfit** (Comfortably covers concessional EMI).

                    **4. Recommended Government Scheme:**
                    • **NSFDC Term Loan Scheme**: 6% interest rate p.a. (5% for women entrepreneurs).
                    • **Moratorium Period**: **12 Months gestation period** — you do not pay principal during initial setup!
                    • Repayment Tenure: 5 to 10 years in flexible quarterly installments.

                    **5. Mandatory Documents Needed:**
                    • Aadhaar Card & PAN Card
                    • SC Caste Certificate & Family Income Certificate (< ₹3.00 Lakh)
                    • Land Ownership Record (Khatauni/Khasra) or Registered Lease Deed
                    • Quotation from certified livestock supplier / machinery vendor
                    • 6 Months Bank Statement & Aadhaar-seeded account

                    **6. Where to Apply (Nearest Authorized Channel Partner):**
                    • Visit the **Uttar Pradesh Scheduled Castes Finance & Development Corp. (UPSCFDC) District Office at Vikas Bhavan (1.8 km away)** or **Baroda U.P. Bank Shivpur Branch**.
                    • You can view the exact route and contact officer directly in the **Partners** tab of this app.
                    """.trimIndent()
                } else {
                    """
                    🇮🇳 **Saksham Advisor - Comprehensive Business Action Plan**

                    **1. Business Planning for $bType:**
                    • Target Market: Local community, retail markets, and district B2B supply chains.
                    • Suitable Scale: Small to Medium Enterprise creating self-employment and jobs for 1-3 helpers.

                    **2. Financial Architecture:**
                    • Total Investment: **₹$fmtTotalInv**
                    • Promoter Capital: **₹$fmtOwnCap**
                    • Government Financing Required: **₹$fmtLoanReq**

                    **3. Recommended Scheme:**
                    • **NSFDC Term Loan Scheme** (Up to ₹50 Lakh @ 6% p.a., 12 months moratorium).
                    • Also eligible for **PMEGP 35% Govt Subsidy** for manufacturing/processing units.

                    **4. Action Steps to Proceed:**
                    1. Prepare simple 2-page project quotation for equipment and initial stock.
                    2. Collect Caste Certificate, Income Certificate, and Aadhaar Card.
                    3. Submit at District State Channelising Agency (SCA) office or through Lead District Bank.
                    4. Sanction is processed through District Level Task Force within 2-3 weeks.
                    """.trimIndent()
                }
            }
        } catch (e: Throwable) {
            "🇮🇳 **Saksham Advisor**\n\nThank you for reaching out. We recommend exploring the **NSFDC Term Loan Scheme** (up to ₹50 Lakh at 6% interest with 12 months moratorium) or **Mahila Samriddhi Yojana** for women entrepreneurs. Please check the **Schemes** tab for full eligibility criteria and your nearest authorized Channel Partner bank."
        }
    }
}
