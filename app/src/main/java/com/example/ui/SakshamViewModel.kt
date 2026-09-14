package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiAdvisorService
import com.example.data.local.AdminSettingsEntity
import com.example.data.local.AiQueryLogEntity
import com.example.data.local.ManagedPartnerEntity
import com.example.data.local.ManagedSchemeEntity
import com.example.data.local.SakshamDatabase
import com.example.data.local.SavedPartnerEntity
import com.example.data.local.SavedPlanEntity
import com.example.data.local.SavedSchemeEntity
import com.example.data.local.SupportTicketEntity
import com.example.data.local.UserApplicationEntity
import com.example.data.local.UserFeedbackEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.UserRecordEntity
import com.example.data.model.BusinessActionPlan
import com.example.data.model.BusinessProfile
import com.example.data.model.ChannelPartner
import com.example.data.model.ChatMessage
import com.example.data.model.GovernmentScheme
import com.example.data.repository.GovernmentDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.pow

class SakshamViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SakshamDatabase.getDatabase(application)
    private val dao = db.sakshamDao()
    private val aiService = GeminiAdvisorService()

    // Authentication session state (User)
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Admin Authentication session state
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _adminRole = MutableStateFlow("NSFDC System Administrator")
    val adminRole: StateFlow<String> = _adminRole.asStateFlow()

    // Language state
    private val _selectedLanguage = MutableStateFlow("English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    // Active business profile form
    private val _businessProfile = MutableStateFlow(BusinessProfile())
    val businessProfile: StateFlow<BusinessProfile> = _businessProfile.asStateFlow()

    // Selected Scheme for details
    private val _selectedScheme = MutableStateFlow<GovernmentScheme?>(GovernmentDataRepository.officialSchemes.first())
    val selectedScheme: StateFlow<GovernmentScheme?> = _selectedScheme.asStateFlow()

    // Action plan
    private val _actionPlan = MutableStateFlow<BusinessActionPlan>(
        GovernmentDataRepository.generateCompleteActionPlan(BusinessProfile())
    )
    val actionPlan: StateFlow<BusinessActionPlan> = _actionPlan.asStateFlow()

    // AI Chat state
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Namaste! I am Saksham Saathi, your official Government Scheme & Business Advisor. How can I assist you today? You can ask me about starting a dairy farm, loan eligibility, required documents, or finding your nearest authorized State Channel Partner.",
                isUser = false,
                suggestedPrompts = listOf(
                    "I want to start a dairy business",
                    "Which scheme has lowest interest rate?",
                    "What documents do I need for NSFDC loan?",
                    "How does the 12-month moratorium work?",
                    "Where is the nearest State Channel Partner?"
                )
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Partner search & filter
    private val _partnerFilter = MutableStateFlow("All")
    val partnerFilter: StateFlow<String> = _partnerFilter.asStateFlow()

    // EMI Calculator state
    private val _emiLoanAmount = MutableStateFlow(500000.0)
    val emiLoanAmount: StateFlow<Double> = _emiLoanAmount.asStateFlow()

    private val _emiInterestRate = MutableStateFlow(6.0)
    val emiInterestRate: StateFlow<Double> = _emiInterestRate.asStateFlow()

    private val _emiTenureYears = MutableStateFlow(5)
    val emiTenureYears: StateFlow<Int> = _emiTenureYears.asStateFlow()

    private val _emiMoratoriumMonths = MutableStateFlow(12)
    val emiMoratoriumMonths: StateFlow<Int> = _emiMoratoriumMonths.asStateFlow()

    // Room Database Observables (User)
    val savedSchemes: StateFlow<List<SavedSchemeEntity>> = dao.getSavedSchemes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPlans: StateFlow<List<SavedPlanEntity>> = dao.getSavedPlans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPartners: StateFlow<List<SavedPartnerEntity>> = dao.getSavedPartners()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = dao.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserProfileEntity(isLoggedIn = false))

    // === ADMIN ROOM DATABASE OBSERVABLES ===
    val managedSchemes: StateFlow<List<ManagedSchemeEntity>> = dao.getManagedSchemes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userRecords: StateFlow<List<UserRecordEntity>> = dao.getAllUserRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userApplications: StateFlow<List<UserApplicationEntity>> = dao.getAllApplications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val aiQueryLogs: StateFlow<List<AiQueryLogEntity>> = dao.getAiQueryLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val managedPartners: StateFlow<List<ManagedPartnerEntity>> = dao.getManagedPartners()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val supportTickets: StateFlow<List<SupportTicketEntity>> = dao.getSupportTickets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userFeedbacks: StateFlow<List<UserFeedbackEntity>> = dao.getUserFeedbacks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminSettings: StateFlow<AdminSettingsEntity?> = dao.getAdminSettings()
        .stateIn(viewModelScope, SharingStarted.Eagerly, AdminSettingsEntity())

    init {
        viewModelScope.launch {
            seedAdminDemoDataIfEmpty()
        }
    }

    private suspend fun seedAdminDemoDataIfEmpty() {
        // Seed User Records if empty
        val existingUsers = dao.getAllUserRecords().first()
        if (existingUsers.isEmpty()) {
            val defaultUsers = listOf(
                UserRecordEntity("usr_101", "Ramesh Kumar", "+91 9876543210", "ramesh.k@gmail.com", 32, "Male", "Uttar Pradesh", "Varanasi", "Scheduled Caste (SC)", "₹1.50 - 3.00 Lakh", "Dairy Farming & Milk Production", System.currentTimeMillis() - 86400000L * 15, "Active", System.currentTimeMillis() - 3600000L),
                UserRecordEntity("usr_102", "Sunita Devi", "+91 9812345678", "sunita.d@gmail.com", 29, "Female", "Uttar Pradesh", "Lucknow", "Scheduled Caste (SC)", "Below ₹1.50 Lakh", "Tailoring & Garments Unit", System.currentTimeMillis() - 86400000L * 10, "Active", System.currentTimeMillis() - 7200000L),
                UserRecordEntity("usr_103", "Amit Gautam", "+91 9765432109", "amit.g@gmail.com", 35, "Male", "Bihar", "Patna", "Scheduled Caste (SC)", "₹1.50 - 3.00 Lakh", "Retail Grocery / Kirana Store", System.currentTimeMillis() - 86400000L * 25, "Verified", System.currentTimeMillis() - 14400000L),
                UserRecordEntity("usr_104", "Priya Paswan", "+91 9654321098", "priya.p@gmail.com", 26, "Female", "Madhya Pradesh", "Bhopal", "Scheduled Caste (SC)", "Below ₹1.50 Lakh", "Beauty Parlor & Cosmetics", System.currentTimeMillis() - 86400000L * 5, "Active", System.currentTimeMillis() - 1800000L),
                UserRecordEntity("usr_105", "Rajesh Ahirwar", "+91 9543210987", "rajesh.a@gmail.com", 41, "Male", "Rajasthan", "Jaipur", "Scheduled Caste (SC)", "₹1.50 - 3.00 Lakh", "Solar Water Pump Installation", System.currentTimeMillis() - 86400000L * 30, "Active", System.currentTimeMillis() - 86400000L)
            )
            defaultUsers.forEach { dao.insertUserRecord(it) }
        }

        // Seed Managed Schemes if empty
        val existingManagedSchemes = dao.getManagedSchemes().first()
        if (existingManagedSchemes.isEmpty()) {
            GovernmentDataRepository.officialSchemes.forEach { s ->
                dao.insertManagedScheme(
                    ManagedSchemeEntity(
                        id = s.id,
                        name = s.name,
                        shortName = s.shortName,
                        department = s.department,
                        category = s.category,
                        maxLoanAmountDisplay = s.maxLoanAmountDisplay,
                        maxLoanNumber = s.maxLoanAmount,
                        interestRateDisplay = s.interestRateDisplay,
                        subsidyPercent = s.subsidyMargin,
                        moratoriumMonths = s.moratoriumMonths,
                        eligibilitySummary = s.eligibilitySummary,
                        description = s.suitablePurpose,
                        isActive = true,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }

        // Seed User Applications if empty
        val existingApps = dao.getAllApplications().first()
        if (existingApps.isEmpty()) {
            val defaultApps = listOf(
                UserApplicationEntity("app_501", "usr_101", "Ramesh Kumar", "+91 9876543210", "nsfdc_term_loan", "NSFDC Term Loan Scheme", "Dairy Farming & Milk Production", 500000L, 475000L, 25000L, "Uttar Pradesh", "Varanasi", "Under Review", "Document verification in progress at SCA Varanasi office.", "State Bank of India (Varanasi Main Branch)", System.currentTimeMillis() - 86400000L * 3),
                UserApplicationEntity("app_502", "usr_102", "Sunita Devi", "+91 9812345678", "nsfdc_mahila_samriddhi", "Mahila Samriddhi Yojana (MSY)", "Tailoring & Garments Unit", 140000L, 140000L, 0L, "Uttar Pradesh", "Lucknow", "Sanctioned", "Sanction letter issued. Disbursal scheduled via Lucknow RRB.", "Aryavart Bank (Lucknow Main Branch)", System.currentTimeMillis() - 86400000L * 8),
                UserApplicationEntity("app_503", "usr_103", "Amit Gautam", "+91 9765432109", "nsfdc_micro_credit", "Micro Credit Finance (MCF) Scheme", "Retail Grocery / Kirana Store", 100000L, 95000L, 5000L, "Bihar", "Patna", "Pending", "Awaiting Caste Certificate upload.", "Dakshin Bihar Gramin Bank", System.currentTimeMillis() - 86400000L * 1),
                UserApplicationEntity("app_504", "usr_104", "Priya Paswan", "+91 9654321098", "nsfdc_mahila_samriddhi", "Mahila Samriddhi Yojana (MSY)", "Beauty Parlor & Cosmetics", 120000L, 120000L, 0L, "Madhya Pradesh", "Bhopal", "Pending", "Application received via Saksham Portal.", "Madhya Pradesh Gramin Bank", System.currentTimeMillis() - 3600000L * 5)
            )
            defaultApps.forEach { dao.insertApplication(it) }
        }

        // Seed AI Query Logs if empty
        val existingLogs = dao.getAiQueryLogs().first()
        if (existingLogs.isEmpty()) {
            val defaultLogs = listOf(
                AiQueryLogEntity("log_201", "Ramesh Kumar", "+91 9876543210", "What documents are required for 4 cows dairy loan under NSFDC Term Loan?", "Explained 7 required documents including Caste Certificate, DPR, and 6-month bank statement with 12-month moratorium rules.", "English", System.currentTimeMillis() - 3600000L * 2),
                AiQueryLogEntity("log_202", "Sunita Devi", "+91 9812345678", "महिला समृद्धि योजना में कितना ब्याज दर लगता है?", "4% प्रति वर्ष रियायती ब्याज दर और बिना किसी प्रमोटर मार्जिन के ₹1.40 लाख तक की सीमा की जानकारी दी गई।", "Hindi", System.currentTimeMillis() - 3600000L * 6),
                AiQueryLogEntity("log_203", "Amit Gautam", "+91 9765432109", "Where is the nearest channel partner bank in Patna?", "Listed Dakshin Bihar Gramin Bank and Punjab National Bank main branch details in Patna.", "English", System.currentTimeMillis() - 3600000L * 12)
            )
            defaultLogs.forEach { dao.insertAiQueryLog(it) }
        }

        // Seed Managed Channel Partners if empty
        val existingPartners = dao.getManagedPartners().first()
        if (existingPartners.isEmpty()) {
            GovernmentDataRepository.authorizedPartners.forEach { p ->
                dao.insertManagedPartner(
                    ManagedPartnerEntity(
                        id = p.id,
                        name = p.name,
                        type = p.type,
                        state = p.state,
                        district = p.district,
                        phone = p.phone,
                        address = p.address,
                        nodalOfficer = "Nodal Officer",
                        isActive = true,
                        addedAt = System.currentTimeMillis()
                    )
                )
            }
        }

        // Seed Support Tickets if empty
        val existingTickets = dao.getSupportTickets().first()
        if (existingTickets.isEmpty()) {
            val defaultTickets = listOf(
                SupportTicketEntity("tkt_301", "Ramesh Kumar", "+91 9876543210", "ramesh.k@gmail.com", "Loan Processing", "My Term Loan application status shows document pending, but I uploaded my Caste certificate yesterday. Please check.", "High", "In Progress", "Verification officer assigned at Varanasi District SCA.", System.currentTimeMillis() - 86400000L * 2),
                SupportTicketEntity("tkt_302", "Sunita Devi", "+91 9812345678", "sunita.d@gmail.com", "Scheme Eligibility", "Can self help group women apply for Mahila Samriddhi together?", "Medium", "Resolved", "Yes, SHGs can apply under MSY scheme through District SCA.", System.currentTimeMillis() - 86400000L * 5)
            )
            defaultTickets.forEach { dao.insertSupportTicket(it) }
        }

        // Seed Feedbacks if empty
        val existingFeedbacks = dao.getUserFeedbacks().first()
        if (existingFeedbacks.isEmpty()) {
            val defaultFeedbacks = listOf(
                UserFeedbackEntity("fb_401", "Ramesh Kumar", "+91 9876543210", 5, "AI Assistance", "Saksham Saathi AI answered my dairy loan query instantly in Hindi! Great tool for farmers.", System.currentTimeMillis() - 86400000L * 3),
                UserFeedbackEntity("fb_402", "Sunita Devi", "+91 9812345678", 5, "App Ease", "Very simple app to calculate EMI with 12 months moratorium period.", System.currentTimeMillis() - 86400000L * 6),
                UserFeedbackEntity("fb_403", "Amit Gautam", "+91 9765432109", 4, "Scheme Guidance", "Good list of channel partner banks. Found nearest branch in Patna easily.", System.currentTimeMillis() - 86400000L * 10)
            )
            defaultFeedbacks.forEach { dao.insertUserFeedback(it) }
        }

        // Seed Admin Settings if null
        val settings = dao.getAdminSettings().first()
        if (settings == null) {
            dao.updateAdminSettings(AdminSettingsEntity())
        }
    }

    // === ADMIN ACTIONS ===
    fun adminLogin(emailInput: String, passwordInput: String): Boolean {
        val cleanEmail = emailInput.trim().lowercase()
        val cleanPass = passwordInput.trim()

        val isAuthorized = (cleanEmail == "admin@saksham.gov.in" || cleanEmail == "admin") &&
                (cleanPass == "admin123" || cleanPass == "GovAdmin@2026" || cleanPass == "admin")

        if (isAuthorized) {
            _isAdminLoggedIn.value = true
            _adminRole.value = "NSFDC Senior System Administrator"
        }
        return isAuthorized
    }

    fun adminLogout() {
        _isAdminLoggedIn.value = false
    }

    fun addOrUpdateSchemeAdmin(scheme: ManagedSchemeEntity) {
        viewModelScope.launch {
            dao.insertManagedScheme(scheme)
        }
    }

    fun deleteSchemeAdmin(schemeId: String) {
        viewModelScope.launch {
            dao.deleteManagedScheme(schemeId)
        }
    }

    fun updateUserStatusAdmin(userId: String, newStatus: String) {
        viewModelScope.launch {
            dao.updateUserStatus(userId, newStatus)
        }
    }

    fun updateApplicationAdmin(appId: String, status: String, remarks: String, partnerBank: String) {
        viewModelScope.launch {
            dao.updateApplicationStatus(appId, status, remarks, partnerBank)
        }
    }

    fun addOrUpdatePartnerAdmin(partner: ManagedPartnerEntity) {
        viewModelScope.launch {
            dao.insertManagedPartner(partner)
        }
    }

    fun deletePartnerAdmin(partnerId: String) {
        viewModelScope.launch {
            dao.deleteManagedPartner(partnerId)
        }
    }

    fun replySupportTicketAdmin(ticketId: String, status: String, replyText: String) {
        viewModelScope.launch {
            dao.updateSupportTicket(ticketId, status, replyText)
        }
    }

    fun updateAdminSettingsSystem(settings: AdminSettingsEntity) {
        viewModelScope.launch {
            dao.updateAdminSettings(settings)
        }
    }

    // === USER ACTION INTEGRATIONS FOR ADMIN LOGS ===
    fun submitUserSupportTicket(category: String, message: String) {
        viewModelScope.launch {
            val currUser = userProfile.value
            val userName = currUser?.fullName?.ifBlank { "Registered Beneficiary" } ?: "Beneficiary"
            val userPhone = currUser?.phone?.ifBlank { "+91 9876543210" } ?: "+91 9876543210"
            val userEmail = currUser?.email?.ifBlank { "user@saksham.gov.in" } ?: "user@saksham.gov.in"

            val ticket = SupportTicketEntity(
                id = "tkt_" + System.currentTimeMillis().toString().takeLast(6),
                userName = userName,
                userPhone = userPhone,
                userEmail = userEmail,
                category = category,
                message = message,
                priority = "Medium",
                status = "Open",
                submittedAt = System.currentTimeMillis()
            )
            dao.insertSupportTicket(ticket)
        }
    }

    fun submitUserFeedbackRating(rating: Int, category: String, comment: String) {
        viewModelScope.launch {
            val currUser = userProfile.value
            val userName = currUser?.fullName?.ifBlank { "Registered Beneficiary" } ?: "Beneficiary"
            val userPhone = currUser?.phone?.ifBlank { "+91 9876543210" } ?: "+91 9876543210"

            val fb = UserFeedbackEntity(
                id = "fb_" + System.currentTimeMillis().toString().takeLast(6),
                userName = userName,
                userPhone = userPhone,
                rating = rating,
                category = category,
                comment = comment,
                submittedAt = System.currentTimeMillis()
            )
            dao.insertUserFeedback(fb)
        }
    }

    fun submitApplicationFromUser(
        schemeId: String,
        schemeName: String,
        businessType: String,
        totalCost: Long,
        loanRequired: Long,
        ownCapital: Long
    ) {
        viewModelScope.launch {
            val currUser = userProfile.value
            val userName = currUser?.fullName?.ifBlank { "Beneficiary Entrepreneur" } ?: "Beneficiary Entrepreneur"
            val userPhone = currUser?.phone?.ifBlank { "+91 9876543210" } ?: "+91 9876543210"
            val state = currUser?.state?.ifBlank { "Uttar Pradesh" } ?: "Uttar Pradesh"
            val district = currUser?.district?.ifBlank { "Varanasi" } ?: "Varanasi"

            val app = UserApplicationEntity(
                id = "app_" + System.currentTimeMillis().toString().takeLast(6),
                userId = "usr_101",
                userName = userName,
                userPhone = userPhone,
                schemeId = schemeId,
                schemeName = schemeName,
                businessType = businessType,
                totalProjectCost = totalCost,
                loanAmountRequired = loanRequired,
                ownCapital = ownCapital,
                state = state,
                district = district,
                status = "Pending",
                adminRemarks = "Submitted via Saksham Portal",
                assignedBankPartner = "State Bank of India ($district Main Branch)",
                submittedAt = System.currentTimeMillis()
            )
            dao.insertApplication(app)
        }
    }

    fun setLanguage(lang: String) {
        _selectedLanguage.value = lang
        viewModelScope.launch {
            val curr = userProfile.value
            if (curr != null) {
                dao.updateUserProfile(curr.copy(selectedLanguage = lang))
            }
        }
    }

    fun updateBusinessProfile(profile: BusinessProfile) {
        _businessProfile.value = profile
        _actionPlan.value = GovernmentDataRepository.generateCompleteActionPlan(profile)
    }

    fun selectScheme(scheme: GovernmentScheme) {
        _selectedScheme.value = scheme
    }

    fun selectSchemeById(id: String) {
        val found = GovernmentDataRepository.getSchemeById(id)
        if (found != null) {
            _selectedScheme.value = found
        }
    }

    fun setPartnerFilter(filter: String) {
        _partnerFilter.value = filter
    }

    // EMI Calculator updates
    fun updateEmiParameters(loan: Double, rate: Double, tenureYears: Int, moratoriumMonths: Int) {
        _emiLoanAmount.value = loan
        _emiInterestRate.value = rate
        _emiTenureYears.value = tenureYears
        _emiMoratoriumMonths.value = moratoriumMonths
    }

    fun calculateMonthlyEmi(): Double {
        val p = _emiLoanAmount.value
        val annualRate = _emiInterestRate.value
        val r = (annualRate / 12) / 100.0
        val totalMonths = _emiTenureYears.value * 12
        val activeMonths = (totalMonths - _emiMoratoriumMonths.value).coerceAtLeast(1)

        if (r == 0.0) return p / activeMonths
        val emi = (p * r * (1 + r).pow(activeMonths.toDouble())) / ((1 + r).pow(activeMonths.toDouble()) - 1)
        return emi
    }

    fun calculateTotalRepayment(): Double {
        val emi = calculateMonthlyEmi()
        val totalMonths = _emiTenureYears.value * 12
        val activeMonths = (totalMonths - _emiMoratoriumMonths.value).coerceAtLeast(1)
        return emi * activeMonths
    }

    fun calculateTotalInterest(): Double {
        return (calculateTotalRepayment() - _emiLoanAmount.value).coerceAtLeast(0.0)
    }

    // AI Chat Messaging with Admin Logging
    fun sendMessage(promptText: String) {
        if (promptText.isBlank()) return

        val userMsg = ChatMessage(text = promptText, isUser = true)
        val currentHistory = _chatMessages.value
        _chatMessages.value = currentHistory + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val responseText = aiService.getAdvice(
                userPrompt = promptText,
                profile = _businessProfile.value,
                language = _selectedLanguage.value,
                chatHistory = currentHistory
            )

            val suggestionsIndex = responseText.indexOf("SUGGESTIONS:")
            val cleanText: String
            val dynamicPrompts: List<String>

            if (suggestionsIndex != -1) {
                cleanText = responseText.substring(0, suggestionsIndex).trim()
                val rawSuggestions = responseText.substring(suggestionsIndex + "SUGGESTIONS:".length).trim()
                dynamicPrompts = rawSuggestions.lines()
                    .map { it.trim().removePrefix("-").removePrefix("•").removePrefix("*").trim() }
                    .filter { it.isNotBlank() }
            } else {
                cleanText = responseText
                dynamicPrompts = emptyList()
            }

            val p = promptText.lowercase()
            val fallbackPrompts = when {
                p.contains("document") || p.contains("dastavez") || p.contains("paper") -> listOf(
                    "How to get digital SC caste certificate?",
                    "What is the family income ceiling?",
                    "Where is the nearest Vikas Bhavan office?"
                )
                p.contains("interest") || p.contains("byaj") || p.contains("rate") || p.contains("subsidy") -> listOf(
                    "Tell me about Mahila Samriddhi 4% rate",
                    "How does the 35% PMEGP capital subsidy work?",
                    "How does the 12-month moratorium work?"
                )
                else -> listOf(
                    "What documents do I need for NSFDC loan?",
                    "Which scheme offers lowest interest rate?",
                    "Where is the nearest State Channel Partner?"
                )
            }

            val finalPrompts = if (dynamicPrompts.isNotEmpty()) dynamicPrompts else fallbackPrompts

            val botMsg = ChatMessage(
                text = cleanText,
                isUser = false,
                suggestedPrompts = finalPrompts
            )
            _chatMessages.value = _chatMessages.value + botMsg
            _isAiThinking.value = false

            // Log AI Query for Admin Monitoring
            val currUser = userProfile.value
            dao.insertAiQueryLog(
                AiQueryLogEntity(
                    id = "log_" + System.currentTimeMillis().toString().takeLast(6),
                    userName = currUser?.fullName?.ifBlank { "Beneficiary" } ?: "Beneficiary",
                    userPhone = currUser?.phone?.ifBlank { "+91 9876543210" } ?: "+91 9876543210",
                    userPrompt = promptText,
                    aiResponseSummary = cleanText.take(150) + "...",
                    language = _selectedLanguage.value,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    // Room Save / Unsave
    fun toggleSaveScheme(scheme: GovernmentScheme) {
        viewModelScope.launch {
            val isAlreadySaved = savedSchemes.value.any { it.id == scheme.id }
            if (isAlreadySaved) {
                dao.removeScheme(scheme.id)
            } else {
                dao.saveScheme(
                    SavedSchemeEntity(
                        id = scheme.id,
                        name = scheme.name,
                        department = scheme.department,
                        category = scheme.category,
                        maxLoan = scheme.maxLoanAmountDisplay,
                        interestRate = scheme.interestRateDisplay
                    )
                )
            }
        }
    }

    fun toggleSavePartner(partner: ChannelPartner) {
        viewModelScope.launch {
            val isAlreadySaved = savedPartners.value.any { it.id == partner.id }
            if (isAlreadySaved) {
                dao.deletePartner(partner.id)
            } else {
                dao.savePartner(
                    SavedPartnerEntity(
                        id = partner.id,
                        name = partner.name,
                        type = partner.type,
                        phone = partner.phone,
                        address = partner.address,
                        distance = partner.distanceKm
                    )
                )
            }
        }
    }

    fun saveCurrentPlan() {
        viewModelScope.launch {
            val plan = _actionPlan.value
            dao.savePlan(
                SavedPlanEntity(
                    id = plan.id,
                    businessType = plan.businessType,
                    summary = plan.summary,
                    totalInvestment = _businessProfile.value.totalInvestment,
                    loanRequired = _businessProfile.value.loanRequired
                )
            )
        }
    }

    // Theme Mode state (Light / Dark)
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleTheme() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun registerAndLoginUser(
        fullName: String,
        phone: String,
        email: String,
        age: String,
        gender: String,
        state: String,
        district: String,
        socialCategory: String,
        familyIncome: String = "₹1.50 - 3.00 Lakh"
    ) {
        _isLoggedIn.value = true
        viewModelScope.launch {
            val current = userProfile.value
            val updated = (current ?: UserProfileEntity(id = 1)).copy(
                id = 1,
                fullName = fullName.trim(),
                phone = if (phone.trim().startsWith("+91")) phone.trim() else "+91 ${phone.trim()}",
                email = email.trim(),
                age = age.trim().toIntOrNull() ?: 0,
                gender = gender.trim(),
                state = state.trim(),
                district = district.trim(),
                socialCategory = socialCategory.trim(),
                familyIncome = familyIncome.trim(),
                selectedLanguage = _selectedLanguage.value,
                activeBusinessTarget = current?.activeBusinessTarget?.ifBlank { "Dairy Farming & Milk Production" } ?: "Dairy Farming & Milk Production",
                isLoggedIn = true
            )
            dao.updateUserProfile(updated)

            // Also register in Admin User Records table!
            dao.insertUserRecord(
                UserRecordEntity(
                    id = "usr_" + System.currentTimeMillis().toString().takeLast(6),
                    fullName = fullName.trim(),
                    phone = if (phone.trim().startsWith("+91")) phone.trim() else "+91 ${phone.trim()}",
                    email = email.trim(),
                    age = age.trim().toIntOrNull() ?: 0,
                    gender = gender.trim(),
                    state = state.trim(),
                    district = district.trim(),
                    socialCategory = socialCategory.trim(),
                    familyIncome = familyIncome.trim(),
                    businessTarget = "Dairy Farming & Milk Production",
                    registeredAt = System.currentTimeMillis(),
                    status = "Active",
                    lastActive = System.currentTimeMillis()
                )
            )
        }
    }

    fun updateProfileInfo(
        name: String,
        phone: String,
        state: String,
        district: String,
        category: String,
        income: String,
        activeBusinessTarget: String = "",
        photoUri: String? = null
    ) {
        _isLoggedIn.value = true
        viewModelScope.launch {
            val current = userProfile.value
            val updated = (current ?: UserProfileEntity(id = 1)).copy(
                fullName = name.trim(),
                phone = phone.trim(),
                state = state.trim(),
                district = district.trim(),
                socialCategory = category.trim(),
                familyIncome = income.trim(),
                activeBusinessTarget = activeBusinessTarget.ifBlank { current?.activeBusinessTarget ?: "Dairy Farming & Milk Production" },
                photoUri = photoUri ?: current?.photoUri,
                selectedLanguage = _selectedLanguage.value,
                isLoggedIn = true
            )
            dao.updateUserProfile(updated)
        }
    }

    fun updateProfilePhoto(uriString: String) {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = (current ?: UserProfileEntity(id = 1)).copy(photoUri = uriString)
            dao.updateUserProfile(updated)
        }
    }

    fun loginUser(name: String, phone: String, state: String = "Uttar Pradesh", district: String = "Varanasi") {
        _isLoggedIn.value = true
        viewModelScope.launch {
            val current = userProfile.value
            val updated = (current ?: UserProfileEntity(id = 1)).copy(
                id = 1,
                fullName = name.trim(),
                phone = if (phone.trim().startsWith("+91")) phone.trim() else "+91 ${phone.trim()}",
                state = state.trim(),
                district = district.trim(),
                isLoggedIn = true
            )
            dao.updateUserProfile(updated)
        }
    }

    fun logoutUser() {
        _isLoggedIn.value = false
        viewModelScope.launch {
            val current = userProfile.value
            val updated = (current ?: UserProfileEntity(id = 1)).copy(isLoggedIn = false)
            dao.updateUserProfile(updated)
        }
    }
}

