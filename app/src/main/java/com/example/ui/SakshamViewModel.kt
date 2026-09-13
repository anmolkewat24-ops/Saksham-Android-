package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiAdvisorService
import com.example.data.local.SakshamDatabase
import com.example.data.local.SavedPartnerEntity
import com.example.data.local.SavedPlanEntity
import com.example.data.local.SavedSchemeEntity
import com.example.data.local.UserProfileEntity
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.pow

class SakshamViewModel(application: Application) : AndroidViewModel(application) {

    private val db = SakshamDatabase.getDatabase(application)
    private val dao = db.sakshamDao()
    private val aiService = GeminiAdvisorService()

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

    // Room Database Observables
    val savedSchemes: StateFlow<List<SavedSchemeEntity>> = dao.getSavedSchemes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPlans: StateFlow<List<SavedPlanEntity>> = dao.getSavedPlans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPartners: StateFlow<List<SavedPartnerEntity>> = dao.getSavedPartners()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = dao.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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
        val emi = (p * r * (1 + r).pow(activeMonths)) / ((1 + r).pow(activeMonths) - 1)
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

    // AI Chat Messaging
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

            val p = promptText.lowercase()
            val suggestedPrompts = when {
                p.contains("document") || p.contains("dastavez") || p.contains("paper") || p.contains("कागज") -> listOf(
                    "How to get digital SC caste certificate?",
                    "What is the family income ceiling?",
                    "Where is the nearest Vikas Bhavan office?"
                )
                p.contains("interest") || p.contains("byaj") || p.contains("rate") || p.contains("subsidy") || p.contains("ब्याज") -> listOf(
                    "Tell me about Mahila Samriddhi 4% rate",
                    "How does the 35% PMEGP capital subsidy work?",
                    "How does the 12-month moratorium work?"
                )
                p.contains("moratorium") || p.contains("chhut") || p.contains("gestation") || p.contains("किस्त") -> listOf(
                    "When does my first principal EMI start?",
                    "Calculate exact monthly installment in Calculator",
                    "What are the interest rates for women?"
                )
                p.contains("dairy") || p.contains("cow") || p.contains("buffalo") || p.contains("गाय") || p.contains("भैंस") -> listOf(
                    "How to sell milk to cooperatives at best price?",
                    "What is the daily green fodder requirement?",
                    "What documents do I need for a 4-cow unit?"
                )
                p.contains("market") || p.contains("sell") || p.contains("bechna") || p.contains("customer") -> listOf(
                    "How much profit in making paneer & ghee?",
                    "How to tie up with Amul or local cooperative?",
                    "What is the required investment for equipment?"
                )
                p.contains("woman") || p.contains("women") || p.contains("mahila") || p.contains("महिला") -> listOf(
                    "Can self-help groups (SHGs) apply together?",
                    "Is promoter equity zero in Mahila Samriddhi?",
                    "What documents do women entrepreneurs need?"
                )
                p.contains("education") || p.contains("study") || p.contains("college") || p.contains("पढ़ाई") -> listOf(
                    "What is the loan limit for studies abroad?",
                    "Is interest 3.5% for girl students?",
                    "When does repayment start after graduation?"
                )
                else -> listOf(
                    "What documents do I need for NSFDC loan?",
                    "Which scheme offers lowest interest rate?",
                    "Where is the nearest State Channel Partner?"
                )
            }

            val botMsg = ChatMessage(
                text = responseText,
                isUser = false,
                suggestedPrompts = suggestedPrompts
            )
            _chatMessages.value = _chatMessages.value + botMsg
            _isAiThinking.value = false
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

    fun updateProfileInfo(
        name: String,
        phone: String,
        state: String,
        district: String,
        category: String,
        income: String,
        activeBusinessTarget: String = "",
        photoUri: String? = null,
        email: String = "",
        age: Int = 0,
        gender: String = ""
    ) {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = (current ?: UserProfileEntity(id = 1)).copy(
                fullName = name,
                phone = phone,
                email = if (email.isNotBlank()) email else (current?.email ?: ""),
                age = if (age > 0) age else (current?.age ?: 0),
                gender = if (gender.isNotBlank()) gender else (current?.gender ?: ""),
                state = state,
                district = district,
                socialCategory = category,
                familyIncome = income,
                activeBusinessTarget = activeBusinessTarget.ifBlank { current?.activeBusinessTarget ?: "" },
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

    fun loginUser(
        name: String,
        phone: String,
        email: String = "",
        age: Int = 0,
        gender: String = "",
        state: String = "Uttar Pradesh",
        district: String = "Varanasi",
        category: String = "Scheduled Caste (SC)",
        income: String = ""
    ) {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = (current ?: UserProfileEntity(id = 1)).copy(
                fullName = name,
                phone = phone,
                email = email,
                age = age,
                gender = gender,
                state = state,
                district = district,
                socialCategory = if (category.isNotBlank()) category else (current?.socialCategory ?: "Scheduled Caste (SC)"),
                familyIncome = income,
                activeBusinessTarget = current?.activeBusinessTarget ?: "",
                selectedLanguage = _selectedLanguage.value,
                isLoggedIn = true
            )
            dao.updateUserProfile(updated)
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = (current ?: UserProfileEntity(id = 1)).copy(isLoggedIn = false)
            dao.updateUserProfile(updated)
        }
    }
}
