package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Wc
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GovBlueContainer
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SlateBorder
import kotlinx.coroutines.delay

enum class LoginAuthStep {
    PROFILE_FORM,
    OTP_VERIFICATION
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (
        fullName: String,
        phone: String,
        email: String,
        age: String,
        gender: String,
        state: String,
        district: String,
        category: String,
        familyIncome: String
    ) -> Unit,
    onAdminLogin: ((email: String, pass: String) -> Boolean)? = null,
    isDarkMode: Boolean = false
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Mode Tab: 0 = Beneficiary Citizen Login, 1 = Admin Portal Login
    var loginTypeTab by remember { mutableIntStateOf(0) }

    // Admin Credentials State
    var adminEmailInput by remember { mutableStateOf("") }
    var adminPasswordInput by remember { mutableStateOf("") }
    var adminCaptchaInput by remember { mutableStateOf("") }
    var captchaCode by remember { mutableStateOf("7K9P") }
    var adminLoginError by remember { mutableStateOf<String?>(null) }

    // Current Step for Citizen Login
    var currentStep by remember { mutableStateOf(LoginAuthStep.PROFILE_FORM) }

    // Form Fields (Initialized strictly EMPTY - No fake pre-filled values)
    var fullName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var selectedFamilyIncome by remember { mutableStateOf("Below ₹1.50 Lakh") }

    // Dropdown Expansion States
    var stateDropdownExpanded by remember { mutableStateOf(false) }
    var districtDropdownExpanded by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var familyIncomeDropdownExpanded by remember { mutableStateOf(false) }

    // Inline Validation Error States (Initially false, shown only upon submission)
    var fullNameError by remember { mutableStateOf<String?>(null) }
    var ageError by remember { mutableStateOf<String?>(null) }
    var genderError by remember { mutableStateOf<String?>(null) }
    var categoryError by remember { mutableStateOf<String?>(null) }
    var stateError by remember { mutableStateOf<String?>(null) }
    var districtError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    // OTP State
    var otpValue by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }
    var isVerifyingOtp by remember { mutableStateOf(false) }
    var resendCountdown by remember { mutableIntStateOf(30) }
    var canResendOtp by remember { mutableStateOf(false) }

    // Countdown Timer for OTP Resend
    LaunchedEffect(currentStep, resendCountdown) {
        if (currentStep == LoginAuthStep.OTP_VERIFICATION && resendCountdown > 0) {
            delay(1000L)
            resendCountdown--
            if (resendCountdown == 0) {
                canResendOtp = true
            }
        }
    }

    // Comprehensive State to District Data
    val stateDistrictMap = remember {
        mapOf(
            "Uttar Pradesh" to listOf("Varanasi", "Lucknow", "Kanpur Nagar", "Prayagraj", "Gorakhpur", "Agra", "Meerut", "Ghaziabad", "Bareilly", "Aligarh", "Ayodhya", "Jhansi", "Moradabad", "Mathura", "Saharanpur"),
            "Bihar" to listOf("Patna", "Gaya", "Bhagalpur", "Muzaffarpur", "Darbhanga", "Purnia", "Begusarai", "Nalanda", "Vaishali", "Arrah", "Siwan"),
            "Madhya Pradesh" to listOf("Bhopal", "Indore", "Jabalpur", "Gwalior", "Ujjain", "Sagar", "Rewa", "Satna", "Ratlam", "Singrauli"),
            "Rajasthan" to listOf("Jaipur", "Jodhpur", "Udaipur", "Kota", "Bikaner", "Ajmer", "Bhilwara", "Alwar", "Sikar", "Bharatpur"),
            "Maharashtra" to listOf("Mumbai", "Pune", "Nagpur", "Nashik", "Chhatrapati Sambhajinagar", "Thane", "Solapur", "Kolhapur", "Amravati", "Nanded"),
            "Gujarat" to listOf("Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar", "Jamnagar", "Gandhinagar", "Junagadh", "Anand", "Navsari"),
            "Delhi (NCT)" to listOf("New Delhi", "Central Delhi", "East Delhi", "North Delhi", "South Delhi", "West Delhi", "North East Delhi", "North West Delhi"),
            "Karnataka" to listOf("Bengaluru Urban", "Mysuru", "Hubballi-Dharwad", "Mangaluru", "Belagavi", "Kalaburagi", "Ballari", "Vijayapura", "Shivamogga"),
            "Tamil Nadu" to listOf("Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Tirunelveli", "Erode", "Vellore", "Thanjavur"),
            "West Bengal" to listOf("Kolkata", "Howrah", "North 24 Parganas", "South 24 Parganas", "Durgapur", "Siliguri", "Malda", "Murshidabad"),
            "Punjab" to listOf("Amritsar", "Ludhiana", "Jalandhar", "Patiala", "Bathinda", "SAS Nagar (Mohali)", "Hoshiarpur", "Pathankot"),
            "Haryana" to listOf("Gurugram", "Faridabad", "Panipat", "Ambala", "Rohtak", "Karnal", "Hisar", "Sonipat", "Panchkula"),
            "Odisha" to listOf("Bhubaneswar", "Cuttack", "Rourkela", "Berhampur", "Sambalpur", "Puri", "Balasore"),
            "Assam" to listOf("Guwahati", "Dibrugarh", "Silchar", "Jorhat", "Nagaon", "Tinsukia", "Tezpur"),
            "Jharkhand" to listOf("Ranchi", "Jamshedpur", "Dhanbad", "Bokaro", "Deoghar", "Hazaribagh"),
            "Chhattisgarh" to listOf("Raipur", "Bhilai-Durg", "Bilaspur", "Korba", "Rajnandgaon", "Jagdalpur"),
            "Kerala" to listOf("Thiruvananthapuram", "Kochi", "Kozhikode", "Thrissur", "Kollam", "Kannur", "Palakkad"),
            "Telangana" to listOf("Hyderabad", "Warangal", "Nizamabad", "Karimnagar", "Khammam", "Ramagundam"),
            "Andhra Pradesh" to listOf("Visakhapatnam", "Vijayawada", "Guntur", "Tirupati", "Kurnool", "Nellore", "Kakinada"),
            "Uttarakhand" to listOf("Dehradun", "Haridwar", "Nainital", "Udham Singh Nagar", "Rishikesh", "Roorkee"),
            "Himachal Pradesh" to listOf("Shimla", "Dharamshala", "Mandi", "Solan", "Kullu", "Hamirpur")
        )
    }

    val stateList = remember { stateDistrictMap.keys.toList().sorted() }
    val availableDistricts = remember(selectedState) {
        stateDistrictMap[selectedState] ?: listOf("Select State First")
    }

    val categoryList = remember { com.example.util.ProfileFormatter.CASTE_OPTIONS }
    val genderList = remember { com.example.util.ProfileFormatter.GENDER_OPTIONS }
    val familyIncomeList = remember { com.example.util.ProfileFormatter.FAMILY_INCOME_OPTIONS }

    // Validation Function
    fun validateAndProceed(): Boolean {
        var isValid = true

        // Full Name Validation
        if (fullName.trim().isBlank()) {
            fullNameError = "Please enter your full name"
            isValid = false
        } else if (fullName.trim().length < 2) {
            fullNameError = "Name must be at least 2 characters"
            isValid = false
        } else {
            fullNameError = null
        }

        // Age Validation
        val ageNum = age.trim().toIntOrNull()
        if (age.trim().isBlank()) {
            ageError = "Please enter your age"
            isValid = false
        } else if (ageNum == null || ageNum < 18 || ageNum > 99) {
            ageError = "Age must be between 18 and 99 years"
            isValid = false
        } else {
            ageError = null
        }

        // Gender Validation
        if (selectedGender.isBlank()) {
            genderError = "Please select your gender"
            isValid = false
        } else {
            genderError = null
        }

        // Category Validation
        if (selectedCategory.isBlank()) {
            categoryError = "Please select your social category"
            isValid = false
        } else {
            categoryError = null
        }

        // State Validation
        if (selectedState.isBlank()) {
            stateError = "Please select your state"
            isValid = false
        } else {
            stateError = null
        }

        // District Validation
        if (selectedDistrict.isBlank() || selectedDistrict == "Select State First") {
            districtError = "Please select your district"
            isValid = false
        } else {
            districtError = null
        }

        // Mobile Number Validation
        val cleanPhone = mobileNumber.trim()
        if (cleanPhone.isBlank()) {
            phoneError = "Please enter your 10-digit mobile number"
            isValid = false
        } else if (cleanPhone.length != 10 || !cleanPhone.all { it.isDigit() }) {
            phoneError = "Enter a valid 10-digit mobile number"
            isValid = false
        } else if (!cleanPhone.startsWith("6") && !cleanPhone.startsWith("7") && !cleanPhone.startsWith("8") && !cleanPhone.startsWith("9")) {
            phoneError = "Mobile number must start with 6, 7, 8, or 9"
            isValid = false
        } else {
            phoneError = null
        }

        // Email Address Validation
        val cleanEmail = emailAddress.trim()
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (cleanEmail.isBlank()) {
            emailError = "Please enter your email address"
            isValid = false
        } else if (!cleanEmail.matches(emailRegex)) {
            emailError = "Enter a valid email address (e.g. name@example.com)"
            isValid = false
        } else {
            emailError = null
        }

        return isValid
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("login_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // National Tricolor Top Accent Line
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
            ) {
                Box(modifier = Modifier.weight(1f).fillMaxSize().background(SaffronAccent))
                Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color.White))
                Box(modifier = Modifier.weight(1f).fillMaxSize().background(GrowthGreen))
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Official Emblem Badge
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(GovBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Emblem",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Branding Title
            Text(text = com.example.ui.i18n.SakshamStrings.get("saksham"),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
            )

            Text(text = com.example.ui.i18n.SakshamStrings.get("national_economic_empowerment_&_beneficiary_portal"),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Text(text = com.example.ui.i18n.SakshamStrings.get("ministry_of_social_justice_&_empowerment_•_government_of_india"),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = SaffronAccent,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Login Mode Switcher (Beneficiary vs Admin Login)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isDarkMode) Color(0xFF1E2638) else Color(0xFFE2E8F0),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        onClick = { loginTypeTab = 0 },
                        shape = RoundedCornerShape(10.dp),
                        color = if (loginTypeTab == 0) GovBluePrimary else Color.Transparent,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = if (loginTypeTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Citizen Login",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (loginTypeTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        onClick = { loginTypeTab = 1 },
                        shape = RoundedCornerShape(10.dp),
                        color = if (loginTypeTab == 1) SaffronAccent else Color.Transparent,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = if (loginTypeTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Admin Portal",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (loginTypeTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (loginTypeTab == 1) {
                // ==========================================
                // OFFICIAL ADMIN PORTAL LOGIN FORM
                // ==========================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        1.dp,
                        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SaffronAccent.copy(alpha = 0.15f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = SaffronAccent,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "Official Admin Authentication",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "NSFDC Governance & Monitoring Console",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // Admin Email / ID
                        OutlinedTextField(
                            value = adminEmailInput,
                            onValueChange = {
                                adminEmailInput = it
                                adminLoginError = null
                            },
                            label = { Text("Admin Email / Official ID") },
                            placeholder = { Text("e.g. admin@saksham.gov.in") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = SaffronAccent) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Admin Password
                        OutlinedTextField(
                            value = adminPasswordInput,
                            onValueChange = {
                                adminPasswordInput = it
                                adminLoginError = null
                            },
                            label = { Text("Admin Password") },
                            placeholder = { Text("Enter official password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = SaffronAccent) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        // Captcha Box
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SaffronAccent.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, SaffronAccent),
                                modifier = Modifier
                                    .weight(0.4f)
                                    .height(52.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = captchaCode,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 4.sp,
                                        color = SaffronAccent
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = adminCaptchaInput,
                                onValueChange = {
                                    adminCaptchaInput = it
                                    adminLoginError = null
                                },
                                label = { Text("Enter Captcha") },
                                singleLine = true,
                                modifier = Modifier.weight(0.6f),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }

                        // Demo Quick Auto-Fill Chip
                        Surface(
                            onClick = {
                                adminEmailInput = "admin@saksham.gov.in"
                                adminPasswordInput = "admin123"
                                adminCaptchaInput = captchaCode
                                adminLoginError = null
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = GovBluePrimary.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, GovBluePrimary.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GovBluePrimary, modifier = Modifier.size(16.dp))
                                Column {
                                    Text("Tap to Auto-fill Admin Demo Credentials", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GovBluePrimary)
                                    Text("admin@saksham.gov.in / admin123", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }

                        if (adminLoginError != null) {
                            Text(
                                text = adminLoginError ?: "",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (adminEmailInput.isBlank() || adminPasswordInput.isBlank()) {
                                    adminLoginError = "Please enter Admin Email & Password"
                                    return@Button
                                }
                                if (adminCaptchaInput.trim().uppercase() != captchaCode.uppercase()) {
                                    adminLoginError = "Security Captcha mismatched! Try again."
                                    return@Button
                                }

                                val success = onAdminLogin?.invoke(adminEmailInput.trim(), adminPasswordInput.trim()) ?: false
                                if (!success) {
                                    adminLoginError = "Invalid Admin Credentials! (Use admin@saksham.gov.in / admin123)"
                                } else {
                                    Toast.makeText(context, "Admin Authentication Successful!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SaffronAccent),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White)
                                Text("Login to Admin Console", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            } else {
                // Animated Screen Step Switcher
                AnimatedVisibility(
                    visible = currentStep == LoginAuthStep.PROFILE_FORM,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                // ==========================================
                // STEP 1: BENEFICIARY PROFILE CREATION FORM
                // ==========================================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        1.dp,
                        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Section Header
                        Column {
                            Text(text = com.example.ui.i18n.SakshamStrings.get("create_your_profile"),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(text = com.example.ui.i18n.SakshamStrings.get("enter_your_details_to_receive_personalized_government_schemes_loan_subsidies_and_channel_partner_assistance"),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        // 1. FULL NAME
                        Column {
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = {
                                    fullName = it
                                    if (fullNameError != null) fullNameError = null
                                },
                                label = { Text(com.example.ui.i18n.SakshamStrings.get("full_name")) },
                                placeholder = { Text(com.example.ui.i18n.SakshamStrings.get("enter_your_full_name_as_per_aadhaar")) },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = GovBluePrimary)
                                },
                                isError = fullNameError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_name_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GovBluePrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                )
                            )
                            if (fullNameError != null) {
                                Text(
                                    text = fullNameError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        // 2. AGE & 3. GENDER ROW
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Age Field
                            Column(modifier = Modifier.weight(0.45f)) {
                                OutlinedTextField(
                                    value = age,
                                    onValueChange = {
                                        if (it.length <= 2 && it.all { char -> char.isDigit() }) {
                                            age = it
                                            if (ageError != null) ageError = null
                                        }
                                    },
                                    label = { Text(com.example.ui.i18n.SakshamStrings.get("age")) },
                                    placeholder = { Text(com.example.ui.i18n.SakshamStrings.get("eg_28")) },
                                    leadingIcon = {
                                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = GovBluePrimary)
                                    },
                                    isError = ageError != null,
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("login_age_input"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GovBluePrimary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                    )
                                )
                                if (ageError != null) {
                                    Text(
                                        text = ageError ?: "",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                                    )
                                }
                            }

                            // Gender Quick Info / Display
                            Column(modifier = Modifier.weight(0.55f)) {
                                Text(text = com.example.ui.i18n.SakshamStrings.get("gender"),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(
                                            1.dp,
                                            if (genderError != null) MaterialTheme.colorScheme.error
                                            else MaterialTheme.colorScheme.outlineVariant,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueContainer.copy(alpha = 0.3f))
                                        .padding(horizontal = 8.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Wc, contentDescription = null, tint = GovBluePrimary, modifier = Modifier.size(18.dp))
                                        Text(
                                            text = selectedGender.ifBlank { "Select below" },
                                            fontSize = 13.sp,
                                            fontWeight = if (selectedGender.isNotBlank()) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedGender.isNotBlank()) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        // GENDER SELECTION CHIPS
                        Column {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                genderList.forEach { g ->
                                    val isSelected = selectedGender == g
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            selectedGender = g
                                            if (genderError != null) genderError = null
                                        },
                                        label = { Text(g, fontSize = 12.sp) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                        } else null,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = GovBluePrimary,
                                            selectedLabelColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("gender_chip_${g.lowercase().replace(" ", "_")}")
                                    )
                                }
                            }
                            if (genderError != null) {
                                Text(
                                    text = genderError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                                )
                            }
                        }

                        // 4. CASTE / SOCIAL CATEGORY DROPDOWN
                        Column {
                            ExposedDropdownMenuBox(
                                expanded = categoryDropdownExpanded,
                                onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = selectedCategory,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(com.example.ui.i18n.SakshamStrings.get("social_category_caste_group")) },
                                    placeholder = { Text(com.example.ui.i18n.SakshamStrings.get("select_your_eligible_category")) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Badge, contentDescription = null, tint = GovBluePrimary)
                                    },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                                    isError = categoryError != null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                        .testTag("login_category_dropdown"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = GovBluePrimary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = categoryDropdownExpanded,
                                    onDismissRequest = { categoryDropdownExpanded = false }
                                ) {
                                    categoryList.forEach { cat ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = cat,
                                                    fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            onClick = {
                                                selectedCategory = cat
                                                categoryDropdownExpanded = false
                                                if (categoryError != null) categoryError = null
                                            }
                                        )
                                    }
                                }
                            }
                            if (categoryError != null) {
                                Text(
                                    text = categoryError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        // 5. STATE & 6. DISTRICT DROPDOWNS (Linked)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // State Dropdown
                            Column(modifier = Modifier.weight(1f)) {
                                ExposedDropdownMenuBox(
                                    expanded = stateDropdownExpanded,
                                    onExpandedChange = { stateDropdownExpanded = !stateDropdownExpanded }
                                ) {
                                    OutlinedTextField(
                                        value = selectedState,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text(com.example.ui.i18n.SakshamStrings.get("state")) },
                                        placeholder = { Text(com.example.ui.i18n.SakshamStrings.get("select_state")) },
                                        leadingIcon = {
                                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = GovBluePrimary)
                                        },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateDropdownExpanded) },
                                        isError = stateError != null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                            .testTag("login_state_dropdown"),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = GovBluePrimary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                        )
                                    )
                                    ExposedDropdownMenu(
                                        expanded = stateDropdownExpanded,
                                        onDismissRequest = { stateDropdownExpanded = false }
                                    ) {
                                        stateList.forEach { stateItem ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = stateItem,
                                                        fontWeight = if (selectedState == stateItem) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                },
                                                onClick = {
                                                    selectedState = stateItem
                                                    // Reset district when state changes
                                                    selectedDistrict = stateDistrictMap[stateItem]?.firstOrNull() ?: ""
                                                    stateDropdownExpanded = false
                                                    if (stateError != null) stateError = null
                                                    if (districtError != null) districtError = null
                                                }
                                            )
                                        }
                                    }
                                }
                                if (stateError != null) {
                                    Text(
                                        text = stateError ?: "",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                                    )
                                }
                            }

                            // District Dropdown
                            Column(modifier = Modifier.weight(1f)) {
                                ExposedDropdownMenuBox(
                                    expanded = districtDropdownExpanded,
                                    onExpandedChange = {
                                        if (selectedState.isNotBlank()) {
                                            districtDropdownExpanded = !districtDropdownExpanded
                                        } else {
                                            Toast.makeText(context, "Please select a state first", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                ) {
                                    OutlinedTextField(
                                        value = selectedDistrict,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text(com.example.ui.i18n.SakshamStrings.get("district")) },
                                        placeholder = { Text(if (selectedState.isBlank()) "Select State 1st" else "Select District") },
                                        leadingIcon = {
                                            Icon(Icons.Default.LocationCity, contentDescription = null, tint = GovBluePrimary)
                                        },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtDropdownExpanded) },
                                        isError = districtError != null,
                                        enabled = selectedState.isNotBlank(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor()
                                            .testTag("login_district_dropdown"),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = GovBluePrimary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                        )
                                    )
                                    ExposedDropdownMenu(
                                        expanded = districtDropdownExpanded,
                                        onDismissRequest = { districtDropdownExpanded = false }
                                    ) {
                                        availableDistricts.forEach { distItem ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = distItem,
                                                        fontWeight = if (selectedDistrict == distItem) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                },
                                                onClick = {
                                                    selectedDistrict = distItem
                                                    districtDropdownExpanded = false
                                                    if (districtError != null) districtError = null
                                                }
                                            )
                                        }
                                    }
                                }
                                if (districtError != null) {
                                    Text(
                                        text = districtError ?: "",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                                    )
                                }
                            }
                        }

                        // 7. MOBILE NUMBER
                        Column {
                            OutlinedTextField(
                                value = mobileNumber,
                                onValueChange = {
                                    if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                                        mobileNumber = it
                                        if (phoneError != null) phoneError = null
                                    }
                                },
                                label = { Text(com.example.ui.i18n.SakshamStrings.get("mobile_number_for_otp_verification")) },
                                placeholder = { Text(com.example.ui.i18n.SakshamStrings.get("enter_10_digit_mobile_number")) },
                                prefix = { Text(com.example.ui.i18n.SakshamStrings.get("+91"), fontWeight = FontWeight.Bold) },
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = GovBluePrimary)
                                },
                                isError = phoneError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Phone,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_phone_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GovBluePrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                )
                            )
                            if (phoneError != null) {
                                Text(
                                    text = phoneError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        // 8. EMAIL ADDRESS
                        Column {
                            OutlinedTextField(
                                value = emailAddress,
                                onValueChange = {
                                    emailAddress = it
                                    if (emailError != null) emailError = null
                                },
                                label = { Text(com.example.ui.i18n.SakshamStrings.get("email_address")) },
                                placeholder = { Text(com.example.ui.i18n.SakshamStrings.get("enter_your_email_address_eg_name@examplecom")) },
                                leadingIcon = {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = GovBluePrimary)
                                },
                                isError = emailError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    focusManager.clearFocus()
                                    if (validateAndProceed()) {
                                        otpValue = ""
                                        otpError = null
                                        resendCountdown = 30
                                        canResendOtp = false
                                        currentStep = LoginAuthStep.OTP_VERIFICATION
                                        Toast.makeText(context, "Verification code sent to +91 $mobileNumber", Toast.LENGTH_SHORT).show()
                                    }
                                }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_email_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GovBluePrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                )
                            )
                            if (emailError != null) {
                                Text(
                                    text = emailError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // SUBMIT / SEND OTP BUTTON
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                if (validateAndProceed()) {
                                    otpValue = ""
                                    otpError = null
                                    resendCountdown = 30
                                    canResendOtp = false
                                    currentStep = LoginAuthStep.OTP_VERIFICATION
                                    Toast.makeText(context, "Verification code sent to +91 $mobileNumber", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please fill all required fields correctly", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_continue_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(com.example.ui.i18n.SakshamStrings.get("continue_to_verification"), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = currentStep == LoginAuthStep.OTP_VERIFICATION,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                // ==========================================
                // STEP 2: PROFESSIONAL OTP VERIFICATION
                // ==========================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("otp_screen"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        1.dp,
                        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Back navigation row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { currentStep = LoginAuthStep.PROFILE_FORM }
                                .padding(vertical = 4.dp)
                                .testTag("login_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = GovBluePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = com.example.ui.i18n.SakshamStrings.get("edit_profile_details"),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = GovBluePrimary
                            )
                        }

                        // Header
                        Column {
                            Text(text = com.example.ui.i18n.SakshamStrings.get("verify_your_mobile_number"),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val maskedPhone = if (mobileNumber.length == 10) {
                                "+91 ${mobileNumber.take(2)}•••••${mobileNumber.takeLast(3)}"
                            } else {
                                "+91 $mobileNumber"
                            }
                            Text(
                                text = "Enter the 6-digit one-time password (OTP) sent to $maskedPhone",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // Simulation / Official Notice Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueContainer)
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = GovBluePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(text = com.example.ui.i18n.SakshamStrings.get("sms_verification_code"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkMode) Color.White else GovBlueDark
                                    )
                                    Text(text = com.example.ui.i18n.SakshamStrings.get("for_instant_verification_enter_otp_code_123456"),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
                                    )
                                }
                            }
                        }

                        // 6-DIGIT OTP INPUT DISPLAY
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // High-contrast 6-digit display boxes
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                (0..5).forEach { index ->
                                    val digit = otpValue.getOrNull(index)?.toString() ?: ""
                                    val isFocused = otpValue.length == index || (otpValue.length == 6 && index == 5)
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                width = if (isFocused) 2.dp else 1.dp,
                                                color = when {
                                                    otpError != null -> MaterialTheme.colorScheme.error
                                                    isFocused -> GovBluePrimary
                                                    digit.isNotEmpty() -> GrowthGreen
                                                    else -> MaterialTheme.colorScheme.outlineVariant
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .background(
                                                if (digit.isNotEmpty()) {
                                                    if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight.copy(alpha = 0.4f)
                                                } else {
                                                    if (isDarkMode) Color(0xFF1E293B) else Color.White
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = digit,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            // Real Outlined Text Field for typing 6 digits
                            OutlinedTextField(
                                value = otpValue,
                                onValueChange = {
                                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                                        otpValue = it
                                        if (otpError != null) otpError = null
                                    }
                                },
                                label = { Text(com.example.ui.i18n.SakshamStrings.get("enter_6_digit_otp_code")) },
                                placeholder = { Text(com.example.ui.i18n.SakshamStrings.get("123456")) },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = GovBluePrimary)
                                },
                                isError = otpError != null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(onDone = {
                                    focusManager.clearFocus()
                                    if (otpValue.length == 6) {
                                        isVerifyingOtp = true
                                        Toast.makeText(context, "Verifying OTP...", Toast.LENGTH_SHORT).show()
                                        onLoginSuccess(
                                            fullName,
                                            mobileNumber,
                                            emailAddress,
                                            age,
                                            selectedGender,
                                            selectedState,
                                            selectedDistrict,
                                            selectedCategory,
                                            selectedFamilyIncome
                                        )
                                    } else {
                                        otpError = "Please enter complete 6-digit OTP"
                                    }
                                }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_otp_input"),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = GovBluePrimary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                )
                            )

                            if (otpError != null) {
                                Text(
                                    text = otpError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        // Resend OTP Row with Timer
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (!canResendOtp) "Resend code in 00:${resendCountdown.toString().padStart(2, '0')}"
                                else "Didn't receive the OTP?",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(text = com.example.ui.i18n.SakshamStrings.get("resend_otp"),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (canResendOtp) GovBluePrimary else MaterialTheme.colorScheme.outline,
                                modifier = Modifier
                                    .clickable(enabled = canResendOtp) {
                                        resendCountdown = 30
                                        canResendOtp = false
                                        otpValue = ""
                                        Toast.makeText(context, "New OTP sent to +91 $mobileNumber: 123456", Toast.LENGTH_SHORT).show()
                                    }
                                    .testTag("login_resend_otp_btn")
                            )
                        }

                        // VERIFY & COMPLETE SIGNUP BUTTON
                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                if (otpValue.length == 6) {
                                    isVerifyingOtp = true
                                    Toast.makeText(context, "Authentication successful! Welcome $fullName", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(
                                        fullName,
                                        mobileNumber,
                                        emailAddress,
                                        age,
                                        selectedGender,
                                        selectedState,
                                        selectedDistrict,
                                        selectedCategory,
                                        selectedFamilyIncome
                                    )
                                } else {
                                    otpError = "Please enter complete 6-digit OTP (e.g. 123456)"
                                    Toast.makeText(context, "Please enter 6-digit OTP", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_verify_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GrowthGreen),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isVerifyingOtp
                        ) {
                            if (isVerifyingOtp) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(com.example.ui.i18n.SakshamStrings.get("verifying"), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(com.example.ui.i18n.SakshamStrings.get("verify_otp_&_access_saksham"), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Change Details Button
                        OutlinedButton(
                            onClick = { currentStep = LoginAuthStep.PROFILE_FORM },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(com.example.ui.i18n.SakshamStrings.get("change_mobile_number_or_details"), fontSize = 12.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Trust & Security Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = GrowthGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = com.example.ui.i18n.SakshamStrings.get("encrypted_&_secured_under_government_of_india_standards"),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
}
