package com.example.data.repository

import com.example.data.model.BusinessProfile
import com.example.data.model.ChannelPartner
import com.example.data.model.GovernmentScheme

object GovernmentDataRepository {

    val officialSchemes: List<GovernmentScheme> = listOf(
        GovernmentScheme(
            id = "nsfdc_term_loan",
            name = "NSFDC Term Loan Scheme",
            shortName = "Term Loan",
            department = "National Scheduled Castes Finance and Development Corp. (NSFDC), Ministry of Social Justice & Empowerment, Govt. of India",
            category = "Business Loan",
            maxLoanAmount = 5000000L,
            maxLoanAmountDisplay = "Up to ₹50.00 Lakh",
            interestRateMin = 5.0,
            interestRateMax = 6.0,
            interestRateDisplay = "6% p.a. (5% for Women)",
            moratoriumMonths = 12,
            moratoriumDisplay = "Up to 12 Months gestation period",
            repaymentPeriodYears = 10,
            repaymentDisplay = "Up to 10 Years in quarterly EMIs",
            subsidyMargin = "NSFDC funds up to 95% of project cost. Promoter equity only 5%",
            suitablePurpose = "Setting up or expanding dairy farms, agricultural units, small-scale manufacturing, commercial transport, and service enterprises.",
            eligibilitySummary = "SC entrepreneurs, annual family income up to ₹3.00 Lakh (priority for BPL/under ₹1.5L), age 18-50 years.",
            eligibilityDetails = listOf(
                "Applicant must belong to the Scheduled Caste (SC) community.",
                "Annual family income should be up to ₹3,00,000/- for both rural and urban areas.",
                "Applicant should be between 18 and 50 years of age.",
                "Should possess relevant experience or technical skills in the chosen business area.",
                "Must not be a defaulter with any financial institution or bank."
            ),
            requiredDocuments = listOf(
                "Aadhaar Card & PAN Card",
                "Caste Certificate issued by competent Revenue Authority (Tehsildar/SDM)",
                "Income Certificate / Family Income Declaration",
                "Detailed Project Report (DPR) or Quotation for machinery/livestock",
                "Land Records / Lease Agreement for business site / shed",
                "Bank Account Passbook / 6 Months Bank Statement",
                "Two Passport Size Photographs"
            ),
            howToApplySteps = listOf(
                "Step 1: Click 'Apply Now' below to visit the official NSFDC portal (nsfdc.nic.in) or submit at your District State Channelising Agency (SCA) office.",
                "Step 2: Submit the project estimate along with your verified caste and income certificates.",
                "Step 3: Joint inspection & techno-economic feasibility review by District Level Task Force.",
                "Step 4: Loan sanctioned and funds disbursed directly to your bank account / vendor in phases."
            ),
            channelPartnersInfo = "Disbursed through State Channelising Agencies (SCAs), Regional Rural Banks (RRBs), and designated Public Sector Banks.",
            officialApplyUrl = "https://nsfdc.nic.in",
            officialPortalName = "NSFDC Official Portal (nsfdc.nic.in)",
            isRecommended = true,
            matchPercentage = 96,
            targetBusinessTypes = listOf("dairy", "manufacturing", "agriculture", "service", "retail", "transport")
        ),
        GovernmentScheme(
            id = "nsfdc_mahila_samriddhi",
            name = "Mahila Samriddhi Yojana (MSY)",
            shortName = "Mahila Samriddhi",
            department = "NSFDC, Ministry of Social Justice & Empowerment, Govt. of India",
            category = "Women Entrepreneurship",
            maxLoanAmount = 140000L,
            maxLoanAmountDisplay = "Up to ₹1.40 Lakh",
            interestRateMin = 4.0,
            interestRateMax = 4.0,
            interestRateDisplay = "4% p.a. (Highly Concessional)",
            moratoriumMonths = 4,
            moratoriumDisplay = "4 Months grace period",
            repaymentPeriodYears = 3,
            repaymentDisplay = "3 Years in easy monthly installments",
            subsidyMargin = "100% of project cost funded with zero promoter margin required",
            suitablePurpose = "Micro-enterprises, dairy animal unit (1-2 cows/buffaloes), tailoring shop, small grocery/kirana, handicrafts, beauty parlor.",
            eligibilitySummary = "Targeted exclusively at women entrepreneurs from SC community with family income up to ₹3.00 Lakh.",
            eligibilityDetails = listOf(
                "Exclusively for women entrepreneurs / Self Help Groups (SHGs).",
                "Belonging to the Scheduled Caste community.",
                "Annual family income up to ₹3.00 Lakh.",
                "No prior financial default with SCAs or banks."
            ),
            requiredDocuments = listOf(
                "Aadhaar Card",
                "SC Certificate of applicant or spouse/father",
                "Income Certificate / BPL Ration Card",
                "Quotation for tools / stock / livestock",
                "Bank Account Details (DBT enabled)",
                "Passport size photographs"
            ),
            howToApplySteps = listOf(
                "Step 1: Click 'Apply Now' to visit the official NSFDC application portal (nsfdc.nic.in) or contact your District SCA field office.",
                "Step 2: Fill simple single-page MSY application format.",
                "Step 3: Verification by Women Development Officer / SCA field officer.",
                "Step 4: Direct disbursement into beneficiary bank account with prompt repayment incentives."
            ),
            channelPartnersInfo = "Available via UPSCFDC, DSCFDC, and designated Regional Rural Banks across all districts.",
            officialApplyUrl = "https://nsfdc.nic.in",
            officialPortalName = "NSFDC Official Portal (nsfdc.nic.in)",
            isRecommended = false,
            matchPercentage = 92,
            targetBusinessTypes = listOf("dairy", "tailoring", "retail", "food", "handicrafts")
        ),
        GovernmentScheme(
            id = "nsfdc_micro_credit",
            name = "Micro Credit Finance (MCF) Scheme",
            shortName = "Micro Credit (MCF)",
            department = "NSFDC, Ministry of Social Justice & Empowerment, Govt. of India",
            category = "Business Loan",
            maxLoanAmount = 140000L,
            maxLoanAmountDisplay = "Up to ₹1.40 Lakh",
            interestRateMin = 5.0,
            interestRateMax = 5.0,
            interestRateDisplay = "5% p.a.",
            moratoriumMonths = 3,
            moratoriumDisplay = "3 Months gestation",
            repaymentPeriodYears = 3,
            repaymentDisplay = "3 Years in quarterly payments",
            subsidyMargin = "Up to 100% project cost covered by NSFDC",
            suitablePurpose = "Small businesses, tea stalls, vegetable vending, cycle repair, mobile repair, artisan tools, village cottage industries.",
            eligibilitySummary = "SC individuals or members of Self-Help Groups with income under ₹3 Lakh seeking quick micro loans.",
            eligibilityDetails = listOf(
                "Applicant must be from SC community.",
                "Family income below ₹3.00 Lakh per annum.",
                "Age between 18 and 55 years.",
                "Viable micro business proposal."
            ),
            requiredDocuments = listOf(
                "Aadhaar Card & Voter ID",
                "Caste & Income Certificate",
                "Bank Passbook copy",
                "Simple business activity declaration"
            ),
            howToApplySteps = listOf(
                "Step 1: Click 'Apply Now' to access official NSFDC guidelines and portal (nsfdc.nic.in) or visit local SCA branch.",
                "Step 2: Quick verification within 14 days by local channel partner.",
                "Step 3: Direct benefit transfer to beneficiary account."
            ),
            channelPartnersInfo = "State Channelising Agencies & Accredited Micro-finance NGOs.",
            officialApplyUrl = "https://nsfdc.nic.in",
            officialPortalName = "NSFDC Official Portal (nsfdc.nic.in)",
            isRecommended = false,
            matchPercentage = 88,
            targetBusinessTypes = listOf("retail", "food", "service", "handicrafts")
        ),
        GovernmentScheme(
            id = "nsfdc_education_loan",
            name = "NSFDC Education Loan Scheme (EL)",
            shortName = "Education Loan",
            department = "NSFDC, Ministry of Social Justice & Empowerment, Govt. of India",
            category = "Education Loan",
            maxLoanAmount = 3000000L,
            maxLoanAmountDisplay = "Up to ₹20L (India) / ₹30L (Abroad)",
            interestRateMin = 3.5,
            interestRateMax = 4.0,
            interestRateDisplay = "4% p.a. (3.5% for Female Students)",
            moratoriumMonths = 12,
            moratoriumDisplay = "Course Duration + 1 Year Moratorium",
            repaymentPeriodYears = 5,
            repaymentDisplay = "5 Years after course completion + moratorium",
            subsidyMargin = "Covers 100% of admission fees, hostel, books, equipment, and laptop.",
            suitablePurpose = "Professional & technical degrees: Engineering, Medical, MBA, Nursing, Agriculture, Poly-technic, and overseas studies.",
            eligibilitySummary = "SC students who have secured admission to recognized colleges/universities, family income up to ₹3.00 Lakh.",
            eligibilityDetails = listOf(
                "Must belong to the Scheduled Caste community.",
                "Family income up to ₹3,00,000/- per annum.",
                "Confirmed admission in recognized degree/diploma course in India or abroad.",
                "Qualifying entrance test score where applicable."
            ),
            requiredDocuments = listOf(
                "Aadhaar Card of Student & Parent/Guardian",
                "SC Certificate and Income Certificate",
                "Admission Letter with fee schedule from College/Institution",
                "Marksheets of 10th, 12th, and Graduation (if PG)",
                "Passport & Visa (for abroad studies)",
                "Co-obligation / Guarantee of Parent"
            ),
            howToApplySteps = listOf(
                "Step 1: Click 'Apply Now' to apply directly via Vidya Lakshmi Official Education Loan Portal (vidyalakshmi.co.in) or NSFDC portal.",
                "Step 2: Upload college admission letter and approved fee structure.",
                "Step 3: Direct sanction and disbursement to institution account."
            ),
            channelPartnersInfo = "State Channelising Agencies and Nationalized Bank education desks.",
            officialApplyUrl = "https://www.vidyalakshmi.co.in",
            officialPortalName = "Vidya Lakshmi Portal (vidyalakshmi.co.in)",
            isRecommended = false,
            matchPercentage = 85,
            targetBusinessTypes = listOf("education")
        ),
        GovernmentScheme(
            id = "pmegp_scheme",
            name = "Prime Minister's Employment Generation Programme (PMEGP)",
            shortName = "PMEGP Loan & Subsidy",
            department = "Khadi and Village Industries Commission (KVIC), Ministry of MSME, Govt. of India",
            category = "Business Loan",
            maxLoanAmount = 5000000L,
            maxLoanAmountDisplay = "Up to ₹50 Lakh (Mfg) / ₹20 Lakh (Service)",
            interestRateMin = 8.5,
            interestRateMax = 11.0,
            interestRateDisplay = "Bank Linked (With 25% - 35% Govt Subsidy)",
            moratoriumMonths = 6,
            moratoriumDisplay = "6 Months gestation period",
            repaymentPeriodYears = 7,
            repaymentDisplay = "3 to 7 Years repayment tenure",
            subsidyMargin = "Special Category (SC/ST/Women/Rural): 35% Govt Capital Subsidy. Own contribution only 5%",
            suitablePurpose = "Starting manufacturing plants, processing units, dairy cold chains, modern tailoring factories, packaging units.",
            eligibilitySummary = "Any individual above 18 years, 8th pass for projects above ₹10L in mfg or ₹5L in service. Special 35% subsidy for SC/ST/Rural.",
            eligibilityDetails = listOf(
                "Age 18+ years.",
                "At least 8th standard pass for manufacturing projects above ₹10 Lakh.",
                "Only for new projects (Greenfield).",
                "SC/ST/OBC/Women/Rural applicants receive highest capital subsidy of 35%."
            ),
            requiredDocuments = listOf(
                "Aadhaar Card, PAN Card",
                "Detailed Project Report (DPR)",
                "Education / Skill Certificate (8th pass or ITI/diploma)",
                "Caste & Rural Area Certificate",
                "EDP Training Certificate (provided after sanction)"
            ),
            howToApplySteps = listOf(
                "Step 1: Click 'Apply Now' to submit online application on the official PMEGP e-Portal (kviconline.gov.in).",
                "Step 2: District Level Task Force Committee (DLTFC) evaluates application.",
                "Step 3: Forwarded to selected financing bank for credit appraisal and margin money deposit."
            ),
            channelPartnersInfo = "Implemented through KVIC, State KVIB, DIC, and all Nationalized Public Sector Banks.",
            officialApplyUrl = "https://www.kviconline.gov.in/pmegpeportal/pmegpweb/index.jsp",
            officialPortalName = "PMEGP e-Portal (kviconline.gov.in)",
            isRecommended = false,
            matchPercentage = 91,
            targetBusinessTypes = listOf("manufacturing", "dairy", "food", "agriculture", "service")
        ),
        GovernmentScheme(
            id = "nsfdc_green_business",
            name = "NSFDC Green Business Scheme",
            shortName = "Green Business",
            department = "NSFDC, Ministry of Social Justice & Empowerment, Govt. of India",
            category = "Green Business",
            maxLoanAmount = 3000000L,
            maxLoanAmountDisplay = "Up to ₹30.00 Lakh",
            interestRateMin = 5.0,
            interestRateMax = 6.0,
            interestRateDisplay = "6% p.a. (5% for Women)",
            moratoriumMonths = 6,
            moratoriumDisplay = "6 Months moratorium",
            repaymentPeriodYears = 8,
            repaymentDisplay = "Up to 8 Years",
            subsidyMargin = "Up to 90% funding with low 10% promoter equity",
            suitablePurpose = "Eco-friendly activities: Battery e-rickshaws, solar pumps, bio-gas plants for dairy farms, solar home lighting, solid waste composting.",
            eligibilitySummary = "SC entrepreneurs targeting climate-resilient green projects, family income up to ₹3 Lakh.",
            eligibilityDetails = listOf(
                "Applicant belongs to SC community.",
                "Annual family income up to ₹3.00 Lakh.",
                "Unit must adopt renewable energy, solar power, or bio-waste solutions."
            ),
            requiredDocuments = listOf(
                "Aadhaar, Caste & Income Certificate",
                "Quotation for solar panel / e-rickshaw / bio-digester",
                "Technical clearance / feasibility proof"
            ),
            howToApplySteps = listOf(
                "Step 1: Click 'Apply Now' to visit official NSFDC Green Business portal (nsfdc.nic.in) or local SCA office.",
                "Step 2: Technical validation by Renewable Energy Nodal Agency.",
                "Step 3: Sanction and release of funds."
            ),
            channelPartnersInfo = "State Channelising Agencies and participating RRBs.",
            officialApplyUrl = "https://nsfdc.nic.in",
            officialPortalName = "NSFDC Official Portal (nsfdc.nic.in)",
            isRecommended = false,
            matchPercentage = 84,
            targetBusinessTypes = listOf("dairy", "agriculture", "transport", "service")
        ),
        GovernmentScheme(
            id = "pm_mudra_yojana",
            name = "Pradhan Mantri MUDRA Yojana (PMMY)",
            shortName = "Mudra Loan",
            department = "Department of Financial Services, Ministry of Finance, Govt. of India",
            category = "Business Loan",
            maxLoanAmount = 2000000L,
            maxLoanAmountDisplay = "Up to ₹20.00 Lakh (Shishu/Kishore/Tarun)",
            interestRateMin = 8.0,
            interestRateMax = 11.5,
            interestRateDisplay = "8% - 11.5% p.a. (Collateral-free)",
            moratoriumMonths = 6,
            moratoriumDisplay = "Up to 6 Months",
            repaymentPeriodYears = 5,
            repaymentDisplay = "3 to 5 Years tenure",
            subsidyMargin = "Zero collateral security required. Credit guarantee backed by CGTMSE.",
            suitablePurpose = "Working capital, purchase of tools, retail store expansion, vehicles for business delivery, restaurant setup.",
            eligibilitySummary = "Non-corporate, non-farm small/micro enterprises across manufacturing, trading, and services.",
            eligibilityDetails = listOf(
                "Any citizen having viable business proposal.",
                "Shishu: Loans up to ₹50,000 for early starters.",
                "Kishore: ₹50,000 to ₹5 Lakh for expanding units.",
                "Tarun: ₹5 Lakh to ₹20 Lakh for mature small businesses.",
                "No processing fee for Shishu and Kishore categories."
            ),
            requiredDocuments = listOf(
                "Proof of Identity (Aadhaar/Voter ID)",
                "Proof of Residence",
                "Quotation for machinery or items to be purchased",
                "Existing business license / registration (if applicable)",
                "Last 6 months bank statement"
            ),
            howToApplySteps = listOf(
                "Step 1: Click 'Apply Now' to apply on JanSamarth National Credit Portal (jansamarth.in) or approach nearest bank branch.",
                "Step 2: Submit 1-page Mudra loan application format with quotations.",
                "Step 3: Branch manager processes collateral-free sanction within 7-10 days."
            ),
            channelPartnersInfo = "Available at all Commercial Banks, RRBs, Small Finance Banks, and MFIs.",
            officialApplyUrl = "https://www.jansamarth.in",
            officialPortalName = "JanSamarth Portal (jansamarth.in)",
            isRecommended = false,
            matchPercentage = 89,
            targetBusinessTypes = listOf("retail", "service", "tailoring", "food", "manufacturing")
        ),
        GovernmentScheme(
            id = "stand_up_india",
            name = "Stand-Up India Scheme",
            shortName = "Stand-Up India",
            department = "Department of Financial Services & SIDBI, Govt. of India",
            category = "Business Loan",
            maxLoanAmount = 10000000L,
            maxLoanAmountDisplay = "₹10.00 Lakh to ₹1.00 Crore",
            interestRateMin = 7.5,
            interestRateMax = 9.5,
            interestRateDisplay = "MCLR + 3% (Tenor premium)",
            moratoriumMonths = 18,
            moratoriumDisplay = "Up to 18 Months moratorium",
            repaymentPeriodYears = 7,
            repaymentDisplay = "Up to 7 Years",
            subsidyMargin = "Margin money can be combined with state subsidies to reduce own equity to 10%",
            suitablePurpose = "Greenfield enterprise in manufacturing, services, agri-allied sector (dairy, poultry, fisheries), or trading sector.",
            eligibilitySummary = "Exclusively for SC, ST, and Women entrepreneurs setting up a greenfield enterprise.",
            eligibilityDetails = listOf(
                "SC/ST and/or woman entrepreneur, above 18 years of age.",
                "Loans under the scheme are available only for Greenfield projects.",
                "In case of non-individual enterprises, 51% shareholding must be held by SC/ST or woman.",
                "Borrower should not be in default to any bank."
            ),
            requiredDocuments = listOf(
                "Identity, Address, and Caste/Women Certificate",
                "Comprehensive Project Report with Cashflow Projections",
                "Land/Shed lease deed or purchase deed",
                "Pollution board clearance (if applicable)",
                "Promoters' contribution statement"
            ),
            howToApplySteps = listOf(
                "Step 1: Click 'Apply Now' to register on official Stand-Up Mitra portal (standupmitra.in) or approach Lead District Bank.",
                "Step 2: Connect with Handholding Agency (SIDBI/NABARD) if guidance is needed.",
                "Step 3: Bank branch sanctions composite loan (term loan + working capital)."
            ),
            channelPartnersInfo = "All Scheduled Commercial Banks (every branch has a mandate to support SC/ST and women).",
            officialApplyUrl = "https://www.standupmitra.in",
            officialPortalName = "Stand-Up Mitra Portal (standupmitra.in)",
            isRecommended = false,
            matchPercentage = 86,
            targetBusinessTypes = listOf("dairy", "manufacturing", "agriculture", "service")
        )
    )

    val authorizedPartners: List<ChannelPartner> = listOf(
        ChannelPartner(
            id = "partner_sca_1",
            name = "Uttar Pradesh Scheduled Castes Finance & Dev. Corp. (UPSCFDC)",
            type = "State Channelising Agency (SCA)",
            categoryBadge = "Primary Official Nodal Agency",
            address = "Vikas Bhavan, 2nd Floor, Kachehri Road, Varanasi",
            district = "Varanasi",
            state = "Uttar Pradesh",
            distanceKm = 1.8,
            phone = "+91 542 2508912",
            email = "dm-varanasi.sc@up.gov.in",
            timing = "10:00 AM - 5:30 PM (Mon-Fri)",
            availableLoanCategories = listOf("Dairy & Livestock", "NSFDC Term Loan", "Mahila Samriddhi", "Micro Credit", "Education Loan"),
            status = "Authorized Government Nodal Center",
            latitude = 25.3216,
            longitude = 82.9873,
            isNearest = true
        ),
        ChannelPartner(
            id = "partner_rrb_1",
            name = "Baroda U.P. Bank (Regional Rural Bank)",
            type = "Regional Rural Bank (RRB)",
            categoryBadge = "Designated Rural Banking Partner",
            address = "Shivpur Branch, Main Market, Varanasi",
            district = "Varanasi",
            state = "Uttar Pradesh",
            distanceKm = 3.4,
            phone = "+91 542 2280145",
            email = "shivpur@barodauprrb.co.in",
            timing = "10:00 AM - 4:00 PM (Mon-Sat)",
            availableLoanCategories = listOf("Dairy Entrepreneurship", "Mudra Loans", "PMEGP", "Term Loans"),
            status = "Designated Channel Branch",
            latitude = 25.3490,
            longitude = 82.9712,
            isNearest = false
        ),
        ChannelPartner(
            id = "partner_psb_1",
            name = "State Bank of India - Agriculture & MSME Intensive Branch",
            type = "Public Sector Bank",
            categoryBadge = "Lead District Bank (LDB)",
            address = "SBI Main Complex, Sigra, Varanasi",
            district = "Varanasi",
            state = "Uttar Pradesh",
            distanceKm = 4.2,
            phone = "+91 542 2221080",
            email = "sbi.01234@sbi.co.in",
            timing = "10:00 AM - 4:30 PM (Mon-Sat)",
            availableLoanCategories = listOf("Stand-Up India", "PMEGP", "PMMY Mudra", "NSFDC Channel Financing"),
            status = "Authorized Nodal Branch",
            latitude = 25.3176,
            longitude = 82.9890,
            isNearest = false
        ),
        ChannelPartner(
            id = "partner_psb_2",
            name = "Punjab National Bank - MSME Specialized Center",
            type = "Public Sector Bank",
            categoryBadge = "Priority Sector Hub",
            address = "Rathyatra Crossing, Mahmoorganj, Varanasi",
            district = "Varanasi",
            state = "Uttar Pradesh",
            distanceKm = 5.1,
            phone = "+91 542 2361289",
            email = "bo3456@pnb.co.in",
            timing = "10:00 AM - 4:00 PM (Mon-Sat)",
            availableLoanCategories = listOf("NSFDC Term Loan", "Dairy Unit Financing", "Micro Credit"),
            status = "Active Partner Branch",
            latitude = 25.3090,
            longitude = 82.9912,
            isNearest = false
        ),
        ChannelPartner(
            id = "partner_sca_2",
            name = "District Industries Center (DIC) - MSME Facilitation Desk",
            type = "State Channelising Agency (SCA)",
            categoryBadge = "PMEGP Facilitation Center",
            address = "Industrial Estate, Chandpur, Varanasi",
            district = "Varanasi",
            state = "Uttar Pradesh",
            distanceKm = 6.8,
            phone = "+91 542 2370911",
            email = "dic.varanasi@up.gov.in",
            timing = "9:30 AM - 5:00 PM (Mon-Fri)",
            availableLoanCategories = listOf("PMEGP", "Green Business", "Small Manufacturing"),
            status = "Government Facilitation Desk",
            latitude = 25.2980,
            longitude = 82.9420,
            isNearest = false
        )
    )

    fun getRecommendedSchemes(
        userProfile: com.example.data.local.UserProfileEntity?,
        profile: BusinessProfile
    ): List<GovernmentScheme> {
        val bTypeLower = profile.businessType.lowercase()
        val userCategory = com.example.util.ProfileFormatter.formatCaste(userProfile?.socialCategory)
        val userGender = com.example.util.ProfileFormatter.formatGender(userProfile?.gender)
        val userAge = com.example.util.ProfileFormatter.formatAge(userProfile?.age ?: 25)
        val userIncomeStr = userProfile?.familyIncome?.ifBlank { profile.annualFamilyIncome } ?: profile.annualFamilyIncome
        val userIncomeNum = com.example.util.ProfileFormatter.parseIncomeToNumeric(userIncomeStr)
        val userState = userProfile?.state?.ifBlank { profile.state } ?: profile.state
        val userDistrict = userProfile?.district?.ifBlank { profile.district } ?: profile.district
        val isFemale = userGender.equals("Female", ignoreCase = true)
        val isSC = userCategory.contains("Scheduled Caste", ignoreCase = true) || userCategory.contains("(SC)", ignoreCase = true)
        val isST = userCategory.contains("Scheduled Tribe", ignoreCase = true) || userCategory.contains("(ST)", ignoreCase = true)
        val isOBC = userCategory.contains("OBC", ignoreCase = true) || userCategory.contains("Other Backward", ignoreCase = true)

        val evaluatedSchemes = officialSchemes.map { scheme ->
            var score = 50
            val matchedList = mutableListOf<String>()
            val unmetList = mutableListOf<String>()

            // 1. Social Category Evaluation
            val isNsfdcScheme = scheme.id.startsWith("nsfdc_")
            if (isNsfdcScheme) {
                if (isSC) {
                    score += 25
                    matchedList.add("Social Category: User category ($userCategory) matches NSFDC mandate")
                } else {
                    score -= 30
                    unmetList.add("Social Category: NSFDC schemes target SC community (User is $userCategory)")
                }
            } else if (scheme.id == "stand_up_india") {
                if (isSC || isST || isFemale) {
                    score += 25
                    val matchedRole = if (isFemale) "Women Entrepreneur" else userCategory
                    matchedList.add("Category/Gender: Stand-Up India targets SC/ST & Women ($matchedRole)")
                } else {
                    score -= 25
                    unmetList.add("Category/Gender: Stand-Up India requires SC/ST or Female applicant (User is $userCategory, $userGender)")
                }
            } else if (scheme.id == "pmegp_scheme") {
                if (isSC || isST || isOBC || isFemale) {
                    score += 20
                    matchedList.add("Subsidy Rate: Qualifies for maximum 35% Govt capital subsidy (Special Category)")
                } else {
                    score += 10
                    matchedList.add("Social Category: Open to General category (15-25% subsidy)")
                }
            } else if (scheme.id == "pm_mudra_yojana") {
                score += 15
                matchedList.add("Social Category: Open to all social categories collateral-free")
            }

            // 2. Gender Criteria Evaluation
            if (scheme.id == "nsfdc_mahila_samriddhi") {
                if (isFemale) {
                    score += 25
                    matchedList.add("Gender Criteria: Reserved exclusively for female entrepreneurs")
                } else {
                    score -= 40
                    unmetList.add("Gender Criteria: Mahila Samriddhi is exclusively for female applicants")
                }
            } else if (scheme.id == "nsfdc_education_loan" && isFemale) {
                matchedList.add("Concessional Rate: Special 3.5% p.a. interest rate for female students")
            }

            // 3. Family Income Evaluation
            if (isNsfdcScheme) {
                if (userIncomeNum <= 300000L) {
                    score += 15
                    matchedList.add("Annual Family Income: ₹%,d is within NSFDC ₹3.00 Lakh threshold".format(userIncomeNum))
                } else {
                    score -= 20
                    unmetList.add("Annual Family Income: ₹%,d exceeds NSFDC ceiling of ₹3.00 Lakh/year".format(userIncomeNum))
                }
            } else {
                matchedList.add("Income Limit: Commercial growth project (flexible income criteria)")
            }

            // 4. Loan Amount / Project Cost Evaluation
            if (profile.loanRequired <= scheme.maxLoanAmount) {
                score += 15
                matchedList.add("Loan Requirement: Requested ₹%,d fits within scheme limit (%s)".format(profile.loanRequired, scheme.maxLoanAmountDisplay))
            } else {
                score -= 20
                unmetList.add("Loan Requirement: Requested ₹%,d exceeds scheme maximum cap of %s".format(profile.loanRequired, scheme.maxLoanAmountDisplay))
            }

            // 5. User Age Criteria Evaluation
            if (scheme.id == "nsfdc_term_loan") {
                if (userAge in 18..50) {
                    matchedList.add("Age Limit: Applicant age ($userAge yrs) is within 18-50 year limit")
                } else {
                    unmetList.add("Age Limit: Applicant age ($userAge yrs) is outside 18-50 year limit")
                }
            } else if (scheme.id == "nsfdc_micro_credit" || scheme.id == "nsfdc_mahila_samriddhi") {
                if (userAge in 18..55) {
                    matchedList.add("Age Limit: Applicant age ($userAge yrs) is within 18-55 year limit")
                } else {
                    unmetList.add("Age Limit: Applicant age ($userAge yrs) is outside 18-55 year limit")
                }
            }

            // 6. Business Type / Purpose Matching
            if (scheme.targetBusinessTypes.any { bTypeLower.contains(it) }) {
                score += 15
                matchedList.add("Business Focus: '${profile.businessType}' matches scheme priority sector")
            } else if (scheme.id == "nsfdc_education_loan" && !bTypeLower.contains("education")) {
                score -= 25
                unmetList.add("Business Focus: Education scheme requires professional degree/diploma purpose")
            }

            // 7. Location Support
            matchedList.add("Nodal Center: Authorized State Channelising Agency active in $userDistrict, $userState")

            val finalScore = score.coerceIn(30, 98)

            scheme.copy(
                matchPercentage = finalScore,
                isRecommended = false,
                matchedCriteria = matchedList,
                unmetCriteria = unmetList
            )
        }.sortedByDescending { it.matchPercentage }

        val topScore = evaluatedSchemes.firstOrNull()?.matchPercentage ?: 0
        return evaluatedSchemes.mapIndexed { index, scheme ->
            scheme.copy(
                isRecommended = (index == 0 && scheme.matchPercentage >= 70 && scheme.unmetCriteria.size <= 1)
            )
        }
    }

    fun getRecommendedSchemes(profile: BusinessProfile): List<GovernmentScheme> {
        return getRecommendedSchemes(null, profile)
    }

    fun getSchemeById(id: String): GovernmentScheme? {
        return officialSchemes.find { it.id == id }
    }

    fun generateCompleteActionPlan(profile: BusinessProfile): com.example.data.model.BusinessActionPlan {
        val isDairy = profile.businessType.contains("Dairy", ignoreCase = true)
        val animalCount = if (profile.dairyAnimalCount > 0) profile.dairyAnimalCount else 4
        val totalInv = profile.totalInvestment
        val ownCap = profile.ownCapital
        val loanReq = profile.loanRequired

        val summary = if (isDairy) {
            "Action Plan for establishing a high-yield commercial Dairy Farm with $animalCount crossbreed milch cows/buffaloes, modern ventilated shed, green fodder cultivation, and assured milk society tie-up."
        } else {
            "Comprehensive business startup and expansion action plan for ${profile.businessType}, optimizing working capital, equipment acquisition, and low-cost government credit."
        }

        val investmentBreakdown = if (isDairy) {
            val cattleCost = animalCount * 65000L
            val shedCost = totalInv * 25 / 100
            val equipmentCost = totalInv * 10 / 100
            val workingCap = totalInv - cattleCost - shedCost - equipmentCost
            listOf(
                "Livestock Acquisition ($animalCount Murrah / HF Cows): ₹%,d".format(cattleCost),
                "Cattle Shed & Bio-secure Flooring Setup: ₹%,d".format(shedCost),
                "Milking Machines & Chilling / Storage Cans: ₹%,d".format(equipmentCost),
                "Initial Green/Dry Fodder, Feed & Vet Reserves: ₹%,d".format(workingCap.coerceAtLeast(20000L))
            )
        } else {
            listOf(
                "Plant, Machinery & Essential Tools: ₹%,d".format(totalInv * 50 / 100),
                "Raw Material & Initial Inventory Stock: ₹%,d".format(totalInv * 25 / 100),
                "Premises Rent, Interior Setup & Electricals: ₹%,d".format(totalInv * 15 / 100),
                "Contingency Reserve & Working Capital: ₹%,d".format(totalInv * 10 / 100)
            )
        }

        val suitableScale = if (isDairy) {
            "Recommended Scale: $animalCount Animals Phase-1 unit producing approx. ${animalCount * 12} to ${animalCount * 16} Litres of milk daily. Expandable to 10 animals in Year 2 upon loan repayment."
        } else {
            "Small to Medium Enterprise scale catering to local community demand, capable of breaking even within 4 to 6 months."
        }

        val setupAndInfrastructure = if (isDairy) {
            listOf(
                "Covered shed area: Minimum 50 sq.ft per animal with concrete standing and slope dung drain.",
                "Water supply: Minimum 80-100 Litres clean drinking water per animal daily.",
                "Fodder storage room and feeding mangers (pukka cement).",
                "Chaff cutter machine (2 HP electric or manual) for feed chopping.",
                "Tie-up with local Dairy Cooperative Society / Private Milk Chilling Center."
            )
        } else {
            listOf(
                "Secure commercial / rural workshop premises (minimum 250-400 sq.ft).",
                "Power load sanction and commercial meter connection.",
                "Safe fire safety and tool storage arrangements.",
                "Billing counter, POS device, and digital payment QR code setup."
            )
        }

        val expenseRevenueProjection = if (isDairy) {
            val milkRevenue = animalCount * 14 * 30 * 45L
            val fodderExp = animalCount * 4500L
            val netSurplus = milkRevenue - fodderExp
            val estEmi = (loanReq * 6 / 100 / 12) + (loanReq / 60)
            val fmtMilkRev = "%,d".format(milkRevenue)
            val fmtFodder = "%,d".format(fodderExp)
            val fmtNet = "%,d".format(netSurplus)
            val fmtEmi = "%,d".format(estEmi)
            "Estimated Monthly Milk Yield: ${(animalCount * 14 * 30)} Litres @ ₹45/L = ₹$fmtMilkRev. Monthly Fodder & Vet Expenses: ₹$fmtFodder. Projected Net Monthly Surplus: ₹$fmtNet (Easily covers concessional EMI of ~₹$fmtEmi)."
        } else {
            val rev = "%,d".format(profile.monthlyRevenue)
            val exp = "%,d".format(profile.monthlyExpenses)
            val margin = "%,d".format((profile.monthlyRevenue - profile.monthlyExpenses).coerceAtLeast(15000L))
            "Projected Monthly Revenue: ₹$rev. Operating Costs (raw materials, wages, power): ₹$exp. Estimated Monthly Profit Margin: ~25% (₹$margin)."
        }

        val growthPlan = listOf(
            "Phase 1 (Months 1-6): Setup infrastructure, acquire certified livestock/machinery, begin regular output.",
            "Phase 2 (Months 7-12): Avail moratorium gestation period; build cash reserves and register brand/trade name.",
            "Phase 3 (Year 2): Utilize prompt repayment incentives from NSFDC to lower net interest to 5% p.a.",
            "Phase 4 (Year 3+): Apply for second tranche scaling loan up to ₹50 Lakh under NSFDC Term Loan Scheme."
        )

        val recommendedFinancing = if (isDairy || totalInv >= 200000) {
            "NSFDC Term Loan Scheme (up to ₹50 Lakh @ 6% interest, 5% for women) or Dairy Entrepreneurship Fund via RRB/SCA."
        } else {
            "NSFDC Mahila Samriddhi Yojana (4% interest) or Micro Credit Finance (5% interest)."
        }

        val fmtTotal = "%,d".format(totalInv)
        val fmtOwn = "%,d".format(ownCap)
        val fmtPct = "%.1f".format(ownCap.toDouble() / totalInv.toDouble() * 100)
        val fmtLoan = "%,d".format(loanReq)
        val estimatedLoanNeeded = "Total Project Cost: ₹$fmtTotal | Your Equity: ₹$fmtOwn ($fmtPct%) | Recommended Govt Loan: ₹$fmtLoan"

        val requiredDocuments = listOf(
            "Aadhaar Card and PAN Card of applicant",
            "Valid SC Caste Certificate from Tehsildar / Competent Authority",
            "Annual Family Income Certificate (below ₹3 Lakh)",
            "Land Proof (Khatauni / Khasra) or Registered Rent Deed for shed/premises",
            "Detailed Project Report (DPR) with quotation from certified equipment/cattle seller",
            "Active Bank Account Passbook (Aadhaar Seeded)",
            "Two Passport-size recent color photographs"
        )

        val stepByStepSteps = listOf(
            "1. Visit the Uttar Pradesh Scheduled Castes Finance & Development Corporation (UPSCFDC) District Office or authorized Regional Rural Bank branch.",
            "2. Collect and submit the NSFDC Scheme Application Form with your Caste & Income certificates.",
            "3. Present your Dairy / Business Plan and machinery/cattle vendor quotation to the District Task Force.",
            "4. Receive Field Inspection verification by the Veterinary Officer / District Industries Officer.",
            "5. Loan Sanction Letter issued; credit disbursed directly with 12 months moratorium gestation."
        )

        val partnerRecommendation = "Visit UPSCFDC District Nodal Office (1.8 km away at Vikas Bhavan) or Baroda U.P. Bank Shivpur Branch. You can view navigation routes and contact officers directly in the Partners tab."

        return com.example.data.model.BusinessActionPlan(
            id = "plan_${System.currentTimeMillis()}",
            businessType = profile.businessType,
            summary = summary,
            investmentBreakdown = investmentBreakdown,
            suitableScale = suitableScale,
            setupAndInfrastructure = setupAndInfrastructure,
            expenseRevenueProjection = expenseRevenueProjection,
            growthPlan = growthPlan,
            recommendedFinancing = recommendedFinancing,
            estimatedLoanNeeded = estimatedLoanNeeded,
            requiredDocuments = requiredDocuments,
            stepByStepSteps = stepByStepSteps,
            partnerRecommendation = partnerRecommendation
        )
    }
}
