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
        1. ANSWER SPECIFICALLY: Directly answer the user's EXACT query. Do not dump a generic one-size-fits-all business plan unless the user explicitly asks for a full business plan.
           - If the user asks about DOCUMENTS: List the exact required documents with brief practical notes on how to obtain or verify them.
           - If the user asks about INTEREST RATES / SUBSIDIES: Compare official concessional rates (NSFDC 6%, 5% for women, Mahila Samriddhi 4%, Micro Credit 5%, Education Loan 3.5%-4%, PMEGP 25%-35% subsidy) against high commercial rates.
           - If the user asks about MORATORIUM: Explain how the 12-month gestation moratorium works (no principal repayment during setup).
           - If the user asks about ELIGIBILITY: Specify SC community criteria, family income ceiling (< ₹3.00 Lakh, priority < ₹1.50 Lakh), age 18-50, and credit history.
           - If the user asks about DAIRY / CATTLE / FEED: Give practical cattle advice (Murrah buffaloes/crossbred cows, 14-16 L/day, balanced ration, 50 sq.ft shed with drainage, chaff cutter).
           - If the user asks about MARKETING / SALES: Explain dairy cooperatives (Amul/Parag), chilling centers, local retail, and value addition.
           - If the user asks about PARTNERS / HOW TO APPLY: Guide them to the State Channelising Agency (SCA) at Vikas Bhavan or designated Lead District Banks.
           - If the user asks about WOMEN'S SCHEMES: Highlight Mahila Samriddhi Yojana (₹1.40L @ 4%) and 1% rebate on term loans.
        2. FORMATTING RULES:
           - Use clean, bold headings with emojis.
           - Format details in short, scannable bullet points (•) and numbered steps (1., 2.).
           - Avoid long unbroken paragraphs.
           - Keep tone encouraging, professional, and accessible.
        3. FACTUAL ACCURACY:
           - Ground all numbers strictly in official NSFDC guidelines. Never invent interest rates or loan ceilings.
        4. LANGUAGE:
           - If the user writes in Hindi or asks in Hindi, answer in clear, conversational Hindi with easy terminology. Otherwise answer in English.
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
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

                val contextInfo = if (profile != null) {
                    """
                    User Beneficiary Context:
                    • Business Focus: ${profile.businessType} (${if (profile.isExistingBusiness) "Existing" else "New Greenfield Startup"})
                    • Location: ${profile.locationType} area (${profile.district}, ${profile.state})
                    • Estimated Project Cost: ₹${profile.totalInvestment}
                    • Promoter Equity: ₹${profile.ownCapital} | Loan Required: ₹${profile.loanRequired}
                    • Annual Family Income: ${profile.annualFamilyIncome} | Experience: ${profile.businessExperience}
                    • Cattle Scale (if dairy): ${profile.dairyAnimalCount} cows/buffaloes, ${profile.dairyLandAvailable}
                    • Preferred Language: $language
                    """.trimIndent()
                } else {
                    "Beneficiary Query: $userPrompt\nLanguage: $language"
                }

                // Append recent dialogue context to maintain conversational continuity
                val historyContext = if (chatHistory.isNotEmpty()) {
                    val recentTurns = chatHistory.takeLast(6).joinToString("\n") { msg ->
                        if (msg.isUser) "Beneficiary: ${msg.text}" else "Saksham Saathi: ${msg.text.take(250)}..."
                    }
                    "Recent Conversation History:\n$recentTurns\n\n"
                } else ""

                val fullPrompt = "$contextInfo\n\n$historyContext" +
                        "Current Beneficiary Question:\n$userPrompt\n\n" +
                        "Respond directly, specifically, and concisely to this question in $language."

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
                        put("temperature", 0.4) // Slightly lower temperature for high factual accuracy
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
                            val responseText = parts.getJSONObject(0).optString("text", "")
                            if (responseText.isNotBlank()) {
                                return@withContext responseText
                            }
                        }
                    }
                }
                // Fallback to grounded local engine if API returned unexpected response
                getOfflineVerifiedAdvice(userPrompt, profile, language)
            } catch (e: Exception) {
                // Safe offline fallback on network or API failure
                getOfflineVerifiedAdvice(userPrompt, profile, language)
            }
        }
    }

    /**
     * Grounded, topic-specific offline fallback answering the user's exact subject.
     */
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

        return when {
            // 1. DOCUMENTS CHECKLIST
            p.contains("document") || p.contains("dastavez") || p.contains("paper") || p.contains("kagaaz") ||
                    p.contains("kagaz") || p.contains("certificate") || p.contains("praman") || p.contains("दस्तावेज") || p.contains("कागज") -> {
                if (isHindi) {
                    """
                    📑 **आवश्यक आधिकारिक दस्तावेजों की सूची (Checklist)**

                    सरकारी ऋण (NSFDC / PMEGP) के लिए निम्नलिखित दस्तावेज तैयार रखें:

                    **1. पहचान एवं निवास प्रमाण:**
                    • आधार कार्ड (Aadhaar Card) — मोबाइल नंबर से लिंक होना अनिवार्य।
                    • पैन कार्ड (PAN Card) अथवा फॉर्म 60।
                    • मतदाता पहचान पत्र / राशन कार्ड।

                    **2. पात्रता एवं जाति प्रमाण:**
                    • सक्षम राजस्व अधिकारी (Tehsildar / SDO) द्वारा जारी डिजिटल **SC जाति प्रमाण पत्र**।
                    • वार्षिक परिवार **आय प्रमाण पत्र** (पारिवारिक आय ₹3.00 लाख से कम)।

                    **3. व्यवसाय एवं स्थान प्रमाण:**
                    • शेड / दुकान के लिए भूमि की **खतौनी / खसरा** अथवा 5 वर्ष का रजिस्टर्ड किरायानामा (Lease Deed)।
                    • अधिकृत डीलर से मशीनरी / पशु खरीद का **पक्का कोटेशन (Vendor Quotation)**।

                    **4. बैंकिंग दस्तावेज:**
                    • आधार-सीडेड सक्रिय बैंक खाते की 6 महीने की पासबुक / स्टेटमेंट।
                    • आवेदक के 3 नवीनतम पासपोर्ट साइज रंगीन फोटो।

                    💡 **सुझाव:** इन सभी प्रतियों को स्व-प्रमाणित (Self-attest) करके अपने जिले के **विकास भवन (UPSCFDC / SCA)** में जमा करें।
                    """.trimIndent()
                } else {
                    """
                    📑 **Required Official Documents Checklist**

                    To apply for NSFDC and Government concessional loans, keep the following self-attested documents ready:

                    **1. Identity & Address Verification:**
                    • **Aadhaar Card**: Must be linked with your active mobile number.
                    • **PAN Card**: Or Form 60 if PAN is not yet issued.
                    • **Voter ID Card / Ration Card** for address confirmation.

                    **2. Category & Income Eligibility:**
                    • **SC Caste Certificate**: Digitally verified certificate issued by Revenue Authority (Tehsildar/SDM).
                    • **Family Income Certificate**: Showing annual income up to ₹3.00 Lakh (priority given to < ₹1.50 Lakh).

                    **3. Business Project Documentation:**
                    • **Land Record (Khatauni/Khasra)**: Or registered 5-year lease deed for shed/premises.
                    • **Machinery / Cattle Vendor Quotation**: Proforma invoice with GST number from authorized suppliers.
                    • **2-Page Project Summary**: Detailed cost estimation.

                    **4. Banking Records:**
                    • Active bank passbook / statement of last 6 months with IFSC code.
                    • 3 recent passport-sized color photographs.

                    💡 **Next Action:** Submit these documents at your District **State Channelising Agency (SCA) office at Vikas Bhavan**.
                    """.trimIndent()
                }
            }

            // 2. INTEREST RATES & SUBSIDIES
            p.contains("interest") || p.contains("byaj") || p.contains("byaaj") || p.contains("rate") ||
                    p.contains("subsidy") || p.contains("percent") || p.contains("%") || p.contains("ब्याज") || p.contains("दर") || p.contains("सब्सिडी") -> {
                if (isHindi) {
                    """
                    💰 **सरकारी रियायती ब्याज दरें एवं सब्सिडी विवरण**

                    निजी साहूकारों (24%–36%) और वाणिज्यिक बैंकों (12%–16%) की तुलना में सरकारी योजनाएं अत्यंत सस्ती हैं:

                    **1. NSFDC टर्म लोन (Term Loan):**
                    • **सामान्य ब्याज दर**: मात्र **6.0% वार्षिक**।
                    • **महिला उद्यमियों हेतु**: विशेष छूट के साथ मात्र **5.0% वार्षिक** (1% की छूट)।
                    • ऋण सीमा: ₹50 लाख तक (प्रोजेक्ट लागत का 90%–95% तक वित्तपोषण)।

                    **2. महिला समृद्धि योजना (MSY):**
                    • ब्याज दर: मात्र **4.0% वार्षिक**।
                    • ऋण सीमा: ₹1,40,000 तक (शून्य मार्जिन मनी — 100% ऋण)।

                    **3. माइक्रो क्रेडिट वित्त (MCF):**
                    • ब्याज दर: **5.0% वार्षिक**।
                    • ऋण सीमा: ₹1,40,000 तक स्वयं सहायता समूहों एवं सूक्ष्म व्यवसायों हेतु।

                    **4. PMEGP कैपिटल सब्सिडी:**
                    • अनुसूचित जाति (SC), ग्रामीण क्षेत्र एवं महिला उद्यमियों को **35% तक पूंजीगत सब्सिडी** सरकार द्वारा सीधे प्रदान की जाती है।

                    💡 **सक्षम कैलकुलेटर:** आप ऐप के **Calculator** टैब में जाकर अपनी सटीक मासिक ईएमआई (EMI) और ब्याज बचत देख सकते हैं।
                    """.trimIndent()
                } else {
                    """
                    💰 **Concessional Interest Rates & Subsidies Comparison**

                    Official government programs offer heavily subsidized credit compared to commercial banks (12%–16%) or informal moneylenders (24%–36%):

                    **1. NSFDC Term Loan Scheme:**
                    • **Standard Interest Rate**: **6.0% per annum**.
                    • **Women Entrepreneurs**: **5.0% per annum** (flat 1.0% interest rebate).
                    • **Loan Ceiling**: Up to **₹50 Lakh** with up to 95% project funding.

                    **2. Mahila Samriddhi Yojana (MSY):**
                    • **Interest Rate**: **4.0% per annum**.
                    • **Loan Ceiling**: Up to **₹1,40,000** with 0% promoter equity required.

                    **3. Micro Credit Finance (MCF):**
                    • **Interest Rate**: **5.0% per annum** for quick micro-enterprise working capital.

                    **4. PMEGP Capital Subsidy:**
                    • **Rural SC Beneficiaries & Women**: Get **35% Government Capital Subsidy** on project costs up to ₹50 Lakh.

                    💡 **Tip:** Use the **Calculator** tab in Saksham to simulate your monthly installment with these exact concessional rates.
                    """.trimIndent()
                }
            }

            // 3. MORATORIUM / GESTATION PERIOD
            p.contains("moratorium") || p.contains("gestation") || p.contains("chhut") || p.contains("chhoot") ||
                    p.contains("delay") || p.contains("grace") || p.contains("छूट") || p.contains("किस्त") || p.contains("कब") -> {
                if (isHindi) {
                    """
                    ⏳ **12 महीने का मोरेटोरियम (छूट अवधि) कैसे काम करता है?**

                    मोरेटोरियम का अर्थ है ऋण वितरण के बाद का **शुरुआती ग्रेस पीरियड**, जब आपको मूलधन (Principal) नहीं चुकाना होता:

                    **1. मोरेटोरियम की अवधि:**
                    • NSFDC टर्म लोन एवं डेयरी योजनाओं में **6 से 12 महीने** की छूट अवधि मिलती है।
                    • स्टैंड-अप इंडिया एवं निर्माण इकाइयों में यह **18 महीने** तक हो सकती है।

                    **2. इस अवधि का लाभ:**
                    • शुरुआती 12 महीनों में शेड निर्माण, पशु खरीद, और उत्पादन शुरू करने का समय मिलता है।
                    • व्यवसाय जब तक पूरी कमाई शुरू न कर दे, तब तक बड़ी ईएमआई (EMI) का कोई वित्तीय दबाव नहीं रहता।

                    **3. किस्तों की शुरुआत:**
                    • 12वें महीने के बाद नियमित मासिक या त्रैमासिक ईएमआई शुरू होती है।
                    • पुनर्भुगतान अवधि: 5 से 10 वर्ष की लचीली किस्तों में।

                    💡 **उदाहरण:** यदि आप आज ₹5 लाख का ऋण लेते हैं, तो आपकी पहली मूलधन किस्त अगले वर्ष व्यवसाय स्थापित होने के बाद ही लगेगी!
                    """.trimIndent()
                } else {
                    """
                    ⏳ **How the 12-Month Moratorium (Gestation Period) Works**

                    A moratorium is a statutory grace period granted immediately after loan disbursement where you are not required to repay the principal loan amount:

                    **1. Duration & Purpose:**
                    • **Standard Period**: **6 to 12 months** for agricultural, livestock, and small business setups.
                    • **Objective**: Gives you sufficient time to construct the cattle shed/factory, purchase equipment, and establish regular cash flows.

                    **2. Financial Benefit:**
                    • **Zero Principal Pressure**: You do not repay the principal installment during the initial setup months.
                    • Only nominal interest accrues, allowing you to reinvest all early earnings back into feed and business operations.

                    **3. Repayment Schedule:**
                    • Full monthly amortization (EMI) begins only after the 12-month gestation ends.
                    • Total tenure spans **5 to 10 years** in convenient quarterly or monthly installments.

                    💡 **Try it in App:** Open the **Calculator** tab and slide the Moratorium slider to see the impact on your cash flow.
                    """.trimIndent()
                }
            }

            // 4. ELIGIBILITY CRITERIA
            p.contains("eligib") || p.contains("patrata") || p.contains("yogya") || p.contains("kaun") ||
                    p.contains("criteria") || p.contains("पात्रता") || p.contains("योग्य") || p.contains("शर्त") -> {
                if (isHindi) {
                    """
                    ✅ **सक्षम सरकारी योजनाओं के लिए पात्रता मानदंड (Eligibility Criteria)**

                    NSFDC एवं राष्ट्रीय योजनाओं के अंतर्गत आवेदन करने हेतु आवश्यक शर्तें:

                    **1. सामाजिक वर्ग (Social Category):**
                    • आवेदक **अनुसूचित जाति (Scheduled Caste - SC)** समुदाय से होना चाहिए।
                    • सक्षम राजस्व अधिकारी (Tehsildar) द्वारा जारी वैध जाति प्रमाण पत्र आवश्यक है।

                    **2. आयु सीमा (Age Limit):**
                    • आवेदक की न्यूनतम आयु **18 वर्ष** और अधिकतम **50 वर्ष** होनी चाहिए।

                    **3. पारिवारिक आय सीमा (Income Ceiling):**
                    • आवेदक के परिवार की कुल वार्षिक आय **₹3,00,000** तक होनी चाहिए।
                    • ग्रामीण क्षेत्रों में ₹1.50 लाख से कम आय वाले परिवारों को चयन में सर्वोच्च प्राथमिकता दी जाती है।

                    **4. पूर्व ऋण स्थिति (Credit Record):**
                    • आवेदक किसी भी राष्ट्रीयकृत बैंक, क्षेत्रीय ग्रामीण बैंक (RRB) या सहकारी बैंक का डिफ़ॉल्टर नहीं होना चाहिए।

                    💡 **सुझाव:** यदि आपके परिवार की महिला के नाम से आवेदन करते हैं, तो **1% कम ब्याज दर** और विशेष प्राथमिकता मिलती है!
                    """.trimIndent()
                } else {
                    """
                    ✅ **Eligibility Criteria for Government Concessional Schemes**

                    To qualify for NSFDC and allied central schemes, applicants must meet the following criteria:

                    **1. Social Category:**
                    • Must belong to the **Scheduled Caste (SC)** community.
                    • Digitally verified Caste Certificate from the competent district revenue authority (Tehsildar/SDM).

                    **2. Age Bracket:**
                    • Between **18 and 50 years** of age at the date of application.

                    **3. Family Income Ceiling:**
                    • Total annual household income must not exceed **₹3,00,000**.
                    • **Top Priority**: Beneficiaries earning under ₹1,50,000 in rural areas receive primary allocation.

                    **4. Credit Standing:**
                    • Applicant must have no active defaults with any public sector bank, regional rural bank (RRB), or cooperative society.

                    💡 **Bonus Advantage:** Applications under a woman entrepreneur qualify for an additional **1.0% interest rebate** under Mahila Samriddhi Yojana.
                    """.trimIndent()
                }
            }

            // 5. DAIRY FARMING / CATTLE / FEED SPECIFICS
            p.contains("cow") || p.contains("buffalo") || p.contains("cattle") || p.contains("गाय") ||
                    p.contains("भैंस") || p.contains("डेयरी") || p.contains("chara") || p.contains("चारा") || p.contains("दूध") || p.contains("doodh") -> {
                if (isHindi) {
                    """
                    🐄 **डेयरी फार्मिंग: उन्नत पशु चयन, शेड एवं चारा प्रबंधन**

                    $animalCount दुधारू पशुओं की डेयरी इकाई स्थापित करने की व्यावहारिक रूपरेखा:

                    **1. उन्नत नस्ल का चयन:**
                    • **मुर्राह भैंस**: 12–16 लीटर प्रतिदिन दूध (7%–8% फैट, अधिक मूल्य)।
                    • **साहीवाल / एचएफ क्रॉस गाय**: 14–18 लीटर प्रतिदिन दूध (सस्ता चारा, आसान रख-रखाव)।
                    • पहले चरण में 2 पशु और 4 महीने बाद 2 पशु खरीदें ताकि साल भर दूध का उत्पादन बना रहे।

                    **2. शेड एवं ढांचागत आवश्यकताएं:**
                    • प्रति पशु **50 वर्ग फीट** पक्का हवादार शेड और 50 वर्ग फीट खुला अहाता।
                    • फर्श खुरदरा और 1.5 इंच ढलानदार हो ताकि गोबर-मूत्र की तुरंत सफाई हो सके।
                    • 2 HP इलेक्ट्रिक कुट्टी मशीन (Chaff Cutter) और 40 लीटर के स्टील केन।

                    **3. वैज्ञानिक चारा प्रबंधन (लागत 35% कम करें):**
                    • **हरा चारा**: नेपियर (Super Napier) या बरसीम — प्रति पशु 25-30 किग्रा।
                    • **सूखा चारा**: गेहूं का भूसा — 5-6 किग्रा।
                    • **पशु आहार (संतुलित दाना)**: 2 किग्रा रख-रखाव हेतु + प्रति 2.5 लीटर दूध पर 1 किग्रा दाना + 50 ग्राम मिनरल मिक्चर।

                    **4. अनुमानित लाभ:**
                    • $animalCount पशुओं से दैनिक उत्पादन: **${animalCount * 14} लीटर**।
                    • मासिक अनुमानित शुद्ध लाभ: **₹$fmtLoan के ऋण पर ईएमआई चुकाने के बाद ₹35,000–₹50,000 मासिक**।

                    💡 **अगला कदम:** अधिकृत पशु मेले अथवा रजिस्टर्ड डेयरी फार्म से कोटेशन लेकर विकास भवन में आवेदन करें।
                    """.trimIndent()
                } else {
                    """
                    🐄 **Dairy Farming: Breed Selection, Shed & Feed Management**

                    Operational guidelines for setting up a viable **$animalCount-milch animal unit**:

                    **1. Breed Recommendation:**
                    • **Murrah Buffalo**: Yields 12–16 L/day with high fat content (7%–8%), commanding premium pricing.
                    • **Sahiwal / HF Crossbred Cow**: Yields 14–18 L/day with high disease resistance and lower feed costs.
                    • **Staggered Purchase**: Purchase animals in two batches (e.g., 2 now, 2 after 4 months) to ensure non-stop lactation throughout the year.

                    **2. Shed & Housing Specs:**
                    • **Covered Area**: 50 sq.ft per cow, well-ventilated with asbestos/tin roofing (height 10–12 ft).
                    • **Flooring**: Grooved non-slip concrete with 1.5-inch slope toward urine drainage channels.
                    • **Equipment**: 2 HP motorized chaff cutter, automatic drinking water troughs, and 40L SS milk cans.

                    **3. Scientific Feeding (Cuts Costs by 35%):**
                    • **Green Fodder**: 25–30 kg green fodder (Super Napier, Maize, or Berseem) daily.
                    • **Dry Fodder**: 5–6 kg wheat straw (Bhoosa).
                    • **Concentrate Feed**: 2 kg maintenance + 1 kg concentrate for every 2.5 Litres of milk yield + 50g mineral mixture.

                    **4. Financial Cashflow:**
                    • Total Projected Gross Revenue: **₹70,000–₹85,000/month**.
                    • Feed, Veterinary & Labor Expenses: **₹22,000–₹28,000/month**.
                    • Projected Monthly Net Profit: **₹45,000–₹55,000/month** (comfortably amortizing the 6% concessional EMI).
                    """.trimIndent()
                }
            }

            // 6. MARKETING / SELLING MILK & PRODUCTS
            p.contains("market") || p.contains("sell") || p.contains("bechna") || p.contains("customer") ||
                    p.contains("grahak") || p.contains("बिक्री") || p.contains("बेचना") || p.contains("ग्राहक") -> {
                if (isHindi) {
                    """
                    📈 **उत्पाद विपणन (Marketing) एवं बिक्री की रणनीति**

                    दूध एवं व्यवसाय उत्पादों की अधिकतम बिक्री के लिए 4 प्रमुख चैनल:

                    **1. स्थानीय दुग्ध सहकारी समिति (Dairy Cooperative):**
                    • अमूल (Amul), पराग (Parag), अथवा सुधा (Sudha) के स्थानीय मिल्क कलेक्शन सेंटर से अनुबंध।
                    • लाभ: प्रतिदिन सुबह-शाम 100% दूध की पक्की खरीद, समय पर सीधा बैंक खाते में भुगतान।
                    • मूल्य: फैट और एसएनएफ (SNF) के आधार पर ₹40–₹52 प्रति लीटर।

                    **2. सीधा घर-घर वितरण (Direct-to-Consumer):**
                    • नजदीकी कॉलोनी या कस्बे में परिवारों को शुद्ध ताजा दूध की सीधी डिलीवरी।
                    • लाभ: बिचौलियों का कोई कमीशन नहीं — सीधा **₹60 से ₹70 प्रति लीटर** का शुद्ध भाव!

                    **3. स्थानीय मिठाई निर्माता व चाय स्टॉल (B2B Bulk):**
                    • स्थानीय हलवाई, होटल एवं चाय दुकानों से 20–30 लीटर दैनिक आपूर्ति का अनुबंध।

                    **4. मूल्य संवर्धन (Value Addition - 40% अधिक मुनाफा):**
                    • शाम के बचे दूध से **पनीर, घी, और खोया** बनाएं।
                    • 1 किग्रा शुद्ध देसी घी ₹700–₹900 में आसानी से बिकता है।

                    💡 **सलाह:** शुरुआती चरण में 60% दूध सहकारी समिति को दें और 40% खुदरा में सीधे ग्राहकों को बेचें।
                    """.trimIndent()
                } else {
                    """
                    📈 **Marketing Strategy & Sales Channels**

                    To maximize daily realization and protect profit margins, deploy these 4 sales channels:

                    **1. Dairy Cooperative Procurement (Assured Daily Off-Take):**
                    • Tie up with authorized procurement centers (Amul, Mother Dairy, Parag, or local state dairy federation).
                    • **Advantage**: 100% daily purchase guarantee with transparent FAT/SNF testing and bi-weekly direct bank transfers.
                    • Rate: ₹40–₹52 per Litre based on fat percentage.

                    **2. Direct-to-Consumer (D2C) Retail Delivery:**
                    • Supply bottled/canned farm-fresh milk directly to residential housing colonies and apartments.
                    • **Advantage**: Bypasses middlemen, yielding premium pricing of **₹60 to ₹72 per Litre** (40% higher profit margin).

                    **3. Commercial B2B Supply:**
                    • Tie up with local bakeries, sweet shops (halwais), tea stalls, and small restaurants for bulk daily supply of 20–40 Litres.

                    **4. Value-Added Dairy Products:**
                    • Convert surplus milk into Paneer (cottage cheese), Curd (dahi), and Clarified Butter (Ghee).
                    • Desi Cow/Buffalo Ghee retails at ₹700–₹950/kg, generating substantial additional cashflow.
                    """.trimIndent()
                }
            }

            // 7. CHANNEL PARTNERS & HOW TO APPLY
            p.contains("partner") || p.contains("bank") || p.contains("kahan") || p.contains("where") ||
                    p.contains("vikas") || p.contains("branch") || p.contains("sca") || p.contains("कहाँ") || p.contains("बैंक") || p.contains("आवेदन") -> {
                if (isHindi) {
                    """
                    🏛️ **आवेदन कहाँ और कैसे करें? (अधिकृत चैनल पार्टनर)**

                    NSFDC ऋण सीधे ऑनलाइन पास नहीं होते, इन्हें राज्य सरकार की अधिकृत एजेंसियों के माध्यम से स्वीकृत किया जाता है:

                    **1. राज्य चैनलising एजेंसी (SCA Office):**
                    • **पता**: उत्तर प्रदेश अनुसूचित जाति वित्त एवं विकास निगम (UPSCFDC), विकास भवन (जिला मुख्यालय)।
                    • यह नोडल एजेंसी है जो आपके आवेदन की स्क्रूटनी करके ऋण स्वीकृत करती है।

                    **2. अधिकृत क्षेत्रीय ग्रामीण बैंक (RRB):**
                    • **बड़ौदा यू.पी. बैंक** (Baroda U.P. Bank) — ग्रामीण शाखाएं।
                    • प्राथमिकता क्षेत्र ऋण (Priority Sector Lending) के तहत त्वरित ऋण वितरण।

                    **3. लीड डिस्ट्रिक्ट बैंक (LDB):**
                    • भारतीय स्टेट बैंक (SBI) कृषि एवं एमएसएमई विशेष शाखा।
                    • पंजाब नेशनल बैंक (PNB) एमएसएमई सेंटर।

                    **आवेदन के 4 चरण:**
                    1. विकास भवन स्थित SCA कार्यालय अथवा लीड बैंक से निःशुल्क आवेदन फॉर्म लें।
                    2. जाति प्रमाण पत्र, आय प्रमाण पत्र, आधार एवं कोटेशन संलग्न करें।
                    3. जिला स्तरीय टास्क फोर्स (DLTF) द्वारा सत्यापन साक्षात्कार।
                    4. स्वीकृति के 15 दिनों में सीधे बैंक खाते अथवा डीलर को ऋण राशि का अंतरण।

                    💡 **Partners टैब:** आप इस ऐप के **Partners** टैब में जाकर अपने निकटतम अधिकृत बैंक शाखा की दूरी, फोन नंबर और जीपीएस मैप देख सकते हैं।
                    """.trimIndent()
                } else {
                    """
                    🏛️ **Where & How to Apply: Authorized Channel Partners**

                    NSFDC and state empowerment loans are processed through authorized district implementing bodies:

                    **1. State Channelising Agency (SCA - Vikas Bhavan):**
                    • **Agency**: State Scheduled Castes Finance & Development Corporation (e.g., UPSCFDC in UP, DSCFDC in Delhi).
                    • **Office**: Room 102, Vikas Bhavan, District Headquarters.
                    • Acts as the primary nodal authority verifying caste and income documentation.

                    **2. Designated Regional Rural Banks (RRBs):**
                    • **Baroda U.P. Bank / Aryavart Bank**: Specialized in concessional agro-allied loans with minimum documentation.

                    **3. Public Sector Lead Banks:**
                    • **State Bank of India (SBI)**: Agri & MSME Intensive Branch.
                    • **Punjab National Bank (PNB)**: MSME Sulabh Kendra.

                    **4-Step Application Walkthrough:**
                    1. Obtain the official application form free of cost from the Vikas Bhavan SCA counter or Lead Bank.
                    2. Attach self-attested SC Certificate, Income Certificate, Vendor Quotations, and Aadhaar.
                    3. District Level Task Force Committee (DLTFC) reviews and recommends the proposal.
                    4. Sanction letter issued within 15–21 days, and funds are disbursed directly to your Aadhaar-seeded bank account.

                    💡 **Direct Route:** Open the **Partners** tab in Saksham to view exact distance, phone numbers, and one-tap turn-by-turn navigation.
                    """.trimIndent()
                }
            }

            // 8. WOMEN'S SCHEMES (MAHILA SAMRIDDHI)
            p.contains("mahila") || p.contains("woman") || p.contains("women") || p.contains("samriddhi") ||
                    p.contains("महिला") || p.contains("shg") || p.contains("aurat") -> {
                if (isHindi) {
                    """
                    🌸 **महिला समृद्धि योजना (Mahila Samriddhi Yojana - MSY)**

                    अनुसूचित जाति की महिला उद्यमियों एवं स्वयं सहायता समूहों (SHGs) के लिए विशेष रियायती योजना:

                    **1. ऋण सीमा एवं ब्याज दर:**
                    • **ऋण राशि**: ₹1,40,000 प्रति लाभार्थी महिला।
                    • **रियायती ब्याज दर**: मात्र **4.0% वार्षिक** (संपूर्ण भारत में सबसे सस्ती दर)।
                    • **शून्य प्रमोटर मार्जिन**: 100% राशि सरकार एवं बैंक द्वारा दी जाती है, अपनी जेब से पूंजी नहीं लगानी होती।

                    **2. किन व्यवसायों के लिए उपयुक्त:**
                    • सिलाई-कढ़ाई, बुटीक, ब्यूटी पार्लर, किराना दुकान।
                    • 1-2 दुधारू गाय/भैंस पालन, मसाला व पापड़ निर्माण, हस्तशिल्प।

                    **3. पुनर्भुगतान एवं छूट:**
                    • 3 वर्ष की आसान मासिक किस्तों में भुगतान।
                    • समय पर किस्त भरने वाली महिलाओं को अतिरिक्त 0.5% की इंसेंटिव छूट!

                    💡 **टर्म लोन में छूट:** यदि महिलाएं ₹50 लाख तक का NSFDC टर्म लोन लेती हैं, तब भी उन्हें पुरुषों की तुलना में 1% कम (6% की जगह मात्र 5%) ब्याज देना होता है।
                    """.trimIndent()
                } else {
                    """
                    🌸 **Mahila Samriddhi Yojana (MSY) for Women Entrepreneurs**

                    Dedicated concessional scheme specifically formulated for Scheduled Caste women and Self-Help Group (SHG) members:

                    **1. Key Financial Highlights:**
                    • **Loan Quantum**: Up to **₹1,40,000** per individual woman beneficiary.
                    • **Subsidized Interest Rate**: Flat **4.0% per annum** (lowest across all public lending schemes).
                    • **Zero Promoter Contribution**: 100% project funding is provided with no mandatory personal margin money required.

                    **2. Eligible Micro-Enterprises:**
                    • Dairy & Goat rearing (1–2 milch animals).
                    • Tailoring, apparel manufacturing, beauty parlors, grocery retail.
                    • Food processing (spices, papad, pickle packaging) and artisanal handicrafts.

                    **3. Repayment Horizon:**
                    • Amortized across **3 years** in manageable monthly installments.
                    • Additional 1.0% interest discount (5% instead of 6%) on regular NSFDC Term Loans up to ₹50 Lakh.

                    💡 **Application:** Apply directly through the District Vikas Bhavan SCA office or authorized SHG Federations.
                    """.trimIndent()
                }
            }

            // 9. EDUCATION LOANS
            p.contains("education") || p.contains("padhai") || p.contains("study") || p.contains("college") ||
                    p.contains("school") || p.contains("btech") || p.contains("mbbs") || p.contains("शिक्षा") || p.contains("पढ़ाई") -> {
                if (isHindi) {
                    """
                    🎓 **NSFDC रियायती शिक्षा ऋण योजना (Education Loan)**

                    अनुसूचित जाति के छात्रों को उच्च व्यावसायिक एवं तकनीकी शिक्षा हेतु रियायती ऋण:

                    **1. ऋण सीमा:**
                    • **भारत में अध्ययन हेतु**: ₹20.00 लाख तक।
                    • **विदेश में अध्ययन हेतु**: ₹30.00 लाख तक।

                    **2. अत्यंत रियायती ब्याज दर:**
                    • **छात्रों हेतु**: मात्र **4.0% वार्षिक**।
                    • **छात्राओं (Girls) हेतु**: विशेष प्रोत्साहन के साथ मात्र **3.5% वार्षिक**।

                    **3. मोरेटोरियम (अध्ययन अवधि में कोई किस्त नहीं):**
                    • कोर्स की पूरी अवधि (3 से 5 वर्ष) + कोर्स पूरा होने के **1 वर्ष बाद** या नौकरी मिलने के 6 महीने बाद किस्त शुरू होती है।
                    • पुनर्भुगतान अवधि: कोर्स पूरा होने के बाद 5 से 10 वर्ष तक।

                    💡 **पात्र कोर्स:** बीटेक, एमबीबीएस, एमबीए, विधि (Law), पीएचडी और मान्यता प्राप्त व्यावसायिक डिप्लोमा।
                    """.trimIndent()
                } else {
                    """
                    🎓 **NSFDC Concessional Education Loan Scheme**

                    Subsidized higher education loans for Scheduled Caste students pursuing professional and technical degrees:

                    **1. Maximum Funding Limit:**
                    • **Studies in India**: Up to **₹20.00 Lakh**.
                    • **Studies Abroad**: Up to **₹30.00 Lakh**.

                    **2. Subsidized Interest Rates:**
                    • **Male Students**: **4.0% per annum**.
                    • **Female Students**: **3.5% per annum** (special 0.5% concession).

                    **3. Extensive Moratorium & Repayment:**
                    • Full moratorium spanning the **entire duration of the course + 1 additional year** after graduation (or 6 months after securing employment).
                    • 5 to 10 years repayment period after the moratorium concludes.

                    💡 **Eligible Courses:** Engineering (B.Tech), Medicine (MBBS), Management (MBA), Law, Biotechnology, and recognized foreign degrees.
                    """.trimIndent()
                }
            }

            // 10. DEFAULT / GENERAL BUSINESS SETUP ADVICE
            else -> {
                val bType = profile?.businessType ?: "Small Business"
                if (isHindi) {
                    """
                    🇮🇳 **सक्षम साथी - $bType व्यवसाय एवं ऋण मार्गदर्शन**

                    आपके द्वारा पूछे गए प्रश्न के आधार पर आधिकारिक विवरण:

                    **1. वित्तीय योजना एवं पैमाना:**
                    • कुल अनुमानित निवेश: **₹$fmtTotal**
                    • आवेदक की पूंजी: **₹$fmtOwn** (10%)
                    • आवश्यक सरकारी ऋण: **₹$fmtLoan** (90%)

                    **2. उपयुक्त सरकारी योजनाएं:**
                    • **NSFDC टर्म लोन योजना**: ₹50 लाख तक 6% रियायती ब्याज पर (12 महीने की मोरेटोरियम छूट)।
                    • **PMEGP योजना**: ग्रामीण क्षेत्र में 35% तक पूंजीगत सरकारी सब्सिडी।
                    • **मुद्रा ऋण (Mudra Loan)**: ₹50,000 से ₹10 लाख तक बिना गारंटी ऋण।

                    **3. त्वरित अगला कदम:**
                    1. व्यवसाय के लिए आवश्यक मशीनरी या माल का अधिकृत डीलर से कोटेशन लें।
                    2. जाति व आय प्रमाण पत्र के साथ अपने जिले के **विकास भवन स्थित SCA कार्यालय** में संपर्क करें।
                    3. सटीक किस्तों की गणना हेतु ऐप में **Calculator** टैब का उपयोग करें।
                    """.trimIndent()
                } else {
                    """
                    🇮🇳 **Saksham Advisor - Comprehensive Business Guidance for $bType**

                    Key structural recommendations aligned with official government lending norms:

                    **1. Capital & Financial Architecture:**
                    • Total Estimated Capital: **₹$fmtTotal**
                    • Promoter Equity (10%): **₹$fmtOwn**
                    • Required Concessional Debt (90%): **₹$fmtLoan**

                    **2. Optimal Government Financing Programs:**
                    • **NSFDC Term Loan**: Up to ₹50 Lakh at **6.0% interest p.a.** with a **12-month gestation moratorium**.
                    • **PMEGP Scheme**: Grants up to **35% capital subsidy** for rural SC entrepreneurs.
                    • **Pradhan Mantri Mudra Yojana**: Collateral-free working capital up to ₹10 Lakh.

                    **3. Action Plan to Proceed:**
                    1. Secure a formal proforma invoice / quotation from approved equipment vendors.
                    2. Compile SC Certificate, Family Income Certificate, and Aadhaar Card.
                    3. Submit the docket at your District **Vikas Bhavan SCA Office** or designated Lead Bank.
                    4. Check the **Partners** and **Calculator** tabs in this app to locate branches and plan EMIs.
                    """.trimIndent()
                }
            }
        }
    }
}
