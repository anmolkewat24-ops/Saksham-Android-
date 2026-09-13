package com.example.data.model

data class GovernmentScheme(
    val id: String,
    val name: String,
    val shortName: String,
    val department: String,
    val category: String, // "Business", "Education", "Women", "Agriculture", "Green"
    val maxLoanAmount: Long,
    val maxLoanAmountDisplay: String,
    val interestRateMin: Double,
    val interestRateMax: Double,
    val interestRateDisplay: String,
    val moratoriumMonths: Int,
    val moratoriumDisplay: String,
    val repaymentPeriodYears: Int,
    val repaymentDisplay: String,
    val subsidyMargin: String,
    val suitablePurpose: String,
    val eligibilitySummary: String,
    val eligibilityDetails: List<String>,
    val requiredDocuments: List<String>,
    val howToApplySteps: List<String>,
    val channelPartnersInfo: String,
    val isRecommended: Boolean = false,
    val matchPercentage: Int = 90,
    val targetBusinessTypes: List<String> = emptyList()
)

data class ChannelPartner(
    val id: String,
    val name: String,
    val type: String, // State Channelising Agency (SCA), RRB, Public Sector Bank
    val categoryBadge: String,
    val address: String,
    val district: String,
    val state: String,
    val distanceKm: Double,
    val phone: String,
    val email: String,
    val timing: String,
    val availableLoanCategories: List<String>,
    val status: String,
    val latitude: Double,
    val longitude: Double,
    val isNearest: Boolean = false
)

data class BusinessProfile(
    val businessType: String = "Dairy Business",
    val isExistingBusiness: Boolean = false,
    val locationType: String = "Rural", // Rural, Semi-Urban, Urban
    val state: String = "Uttar Pradesh",
    val district: String = "Varanasi",
    val totalInvestment: Long = 500000,
    val ownCapital: Long = 50000,
    val loanRequired: Long = 450000,
    val annualFamilyIncome: String = "₹1.5 Lakh - ₹3.0 Lakh",
    val monthlyExpenses: Long = 25000,
    val monthlyRevenue: Long = 45000,
    val businessExperience: String = "Under 2 Years",
    val plannedEmployees: String = "1 - 2 Persons",
    // Dairy specific
    val dairyAnimalCount: Int = 4,
    val dairyLandAvailable: String = "Own Land Available",
    val dairyWaterElectricity: Boolean = true,
    val dairyMilkCollectionCenter: Boolean = true
)

data class BusinessActionPlan(
    val id: String,
    val businessType: String,
    val summary: String,
    val investmentBreakdown: List<String>,
    val suitableScale: String,
    val setupAndInfrastructure: List<String>,
    val expenseRevenueProjection: String,
    val growthPlan: List<String>,
    val recommendedFinancing: String,
    val estimatedLoanNeeded: String,
    val requiredDocuments: List<String>,
    val stepByStepSteps: List<String>,
    val partnerRecommendation: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val suggestedPrompts: List<String> = emptyList(),
    val relatedSchemeId: String? = null
)
