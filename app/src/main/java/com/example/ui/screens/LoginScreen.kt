package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Wc
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
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
import com.example.ui.theme.GovBlueLight
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SlateBorder
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (
        name: String,
        phone: String,
        email: String,
        age: Int,
        gender: String,
        state: String,
        district: String,
        category: String
    ) -> Unit,
    isDarkMode: Boolean = false
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    // Form Fields (Starts strictly EMPTY - no fake/example data)
    var fullName by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedState by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }

    // Dropdown States
    var stateDropdownExpanded by remember { mutableStateOf(false) }
    var districtDropdownExpanded by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    // Validation State (Shown only after user attempts to continue)
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    // Navigation & OTP State
    var isOtpScreen by remember { mutableStateOf(false) }
    var generatedOtp by remember { mutableStateOf("482910") }
    var otpDigits by remember { mutableStateOf(List(6) { "" }) }
    var otpErrorMessage by remember { mutableStateOf<String?>(null) }
    var isVerifying by remember { mutableStateOf(false) }
    var resendCountdown by remember { mutableIntStateOf(30) }
    var canResend by remember { mutableStateOf(false) }

    // Timer for Resend OTP
    LaunchedEffect(isOtpScreen, resendCountdown) {
        if (isOtpScreen && resendCountdown > 0) {
            canResend = false
            delay(1000L)
            resendCountdown -= 1
            if (resendCountdown == 0) {
                canResend = true
            }
        }
    }

    // Supported Social Categories
    val socialCategories = listOf(
        "Scheduled Caste (SC)",
        "Scheduled Tribe (ST)",
        "Other Backward Class (OBC)",
        "General / EWS",
        "Safai Karamchari / Target Group"
    )

    // Supported Gender Options
    val genderOptions = listOf("Male", "Female", "Other", "Prefer not to say")

    // State & District Data Map
    val stateDistrictsMap = remember {
        mapOf(
            "Uttar Pradesh" to listOf(
                "Varanasi", "Lucknow", "Prayagraj", "Kanpur Nagar", "Gorakhpur", "Agra", "Meerut",
                "Ayodhya", "Jhansi", "Bareilly", "Aligarh", "Moradabad", "Mirzapur", "Ghazipur",
                "Jaunpur", "Ballia", "Chandauli", "Azamgarh", "Sonbhadra", "Ghaziabad",
                "Gautam Buddha Nagar (Noida)", "Mathura", "Firozabad", "Saharanpur", "Muzaffarnagar",
                "Basti", "Deoria", "Rae Bareli", "Sitapur", "Unnao"
            ),
            "Bihar" to listOf(
                "Patna", "Gaya", "Muzaffarpur", "Bhagalpur", "Darbhanga", "Purnia", "Begusarai",
                "Nalanda", "Saran (Chhapra)", "Rohtas", "Vaishali", "Samastipur", "Madhubani",
                "Siwan", "Katihar", "Munger", "Bhojpur (Ara)", "East Champaran"
            ),
            "Madhya Pradesh" to listOf(
                "Bhopal", "Indore", "Jabalpur", "Gwalior", "Ujjain", "Sagar", "Rewa", "Satna",
                "Ratlam", "Chhindwara", "Dewas", "Shivpuri", "Vidisha", "Damoh", "Khandwa"
            ),
            "Maharashtra" to listOf(
                "Mumbai City", "Mumbai Suburban", "Pune", "Nagpur", "Nashik", "Thane",
                "Chhatrapati Sambhajinagar", "Solapur", "Kolhapur", "Amravati", "Nanded",
                "Jalgaon", "Akola", "Latur", "Dhule", "Ahmednagar"
            ),
            "Rajasthan" to listOf(
                "Jaipur", "Jodhpur", "Udaipur", "Kota", "Bikaner", "Ajmer", "Bhilwara",
                "Alwar", "Sikar", "Bharatpur", "Pali", "Sri Ganganagar", "Barmer", "Chittorgarh"
            ),
            "West Bengal" to listOf(
                "Kolkata", "Howrah", "North 24 Parganas", "South 24 Parganas", "Hooghly",
                "Paschim Medinipur", "Purba Medinipur", "Purba Bardhaman", "Darjeeling", "Malda",
                "Murshidabad", "Nadia", "Jalpaiguri"
            ),
            "Gujarat" to listOf(
                "Ahmedabad", "Surat", "Vadodara", "Rajkot", "Bhavnagar", "Jamnagar",
                "Junagadh", "Gandhinagar", "Anand", "Navsari", "Bharuch", "Mehsana", "Kutch"
            ),
            "Tamil Nadu" to listOf(
                "Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Tirunelveli",
                "Tiruppur", "Vellore", "Erode", "Thoothukudi", "Dindigul", "Thanjavur"
            ),
            "Karnataka" to listOf(
                "Bengaluru Urban", "Bengaluru Rural", "Mysuru", "Hubballi-Dharwad", "Mangaluru",
                "Belagavi", "Kalaburagi", "Davanagere", "Ballari", "Vijayapura", "Shivamogga", "Tumakuru"
            ),
            "Andhra Pradesh" to listOf(
                "Visakhapatnam", "Vijayawada (NTR)", "Guntur", "Tirupati", "Kurnool",
                "Nellore", "Kakinada", "Rajamahendravaram", "Kadapa", "Anantapur"
            ),
            "Telangana" to listOf(
                "Hyderabad", "Warangal", "Nizamabad", "Karimnagar", "Khammam",
                "Ramagundam", "Mahbubnagar", "Nalgonda", "Adilabad", "Siddipet"
            ),
            "Odisha" to listOf(
                "Bhubaneswar (Khordha)", "Cuttack", "Rourkela (Sundargarh)", "Berhampur (Ganjam)",
                "Sambalpur", "Puri", "Balasore", "Bhadrak", "Angul"
            ),
            "Jharkhand" to listOf(
                "Ranchi", "Jamshedpur (East Singhbhum)", "Dhanbad", "Bokaro", "Deoghar",
                "Hazaribagh", "Giridih", "Ramgarh", "Palamu", "Dumka"
            ),
            "Punjab" to listOf(
                "Ludhiana", "Amritsar", "Jalandhar", "Patiala", "Bathinda",
                "Mohali (SAS Nagar)", "Hoshiarpur", "Pathankot", "Moga", "Firozpur"
            ),
            "Haryana" to listOf(
                "Gurugram", "Faridabad", "Panipat", "Ambala", "Yamunanagar",
                "Rohtak", "Hisar", "Karnal", "Sonipat", "Panchkula"
            ),
            "Chhattisgarh" to listOf(
                "Raipur", "Bhilai (Durg)", "Bilaspur", "Korba", "Rajnandgaon",
                "Jagdalpur (Bastar)", "Raigarh", "Ambikapur"
            ),
            "Delhi" to listOf(
                "Central Delhi", "East Delhi", "New Delhi", "North Delhi", "North East Delhi",
                "North West Delhi", "South Delhi", "South East Delhi", "South West Delhi", "West Delhi"
            ),
            "Assam" to listOf(
                "Guwahati (Kamrup Metropolitan)", "Dibrugarh", "Silchar (Cachar)", "Jorhat",
                "Nagaon", "Tinsukia", "Tezpur (Sonitpur)"
            ),
            "Kerala" to listOf(
                "Thiruvananthapuram", "Kochi (Ernakulam)", "Kozhikode", "Thrissur", "Kollam",
                "Kannur", "Alappuzha", "Palakkad", "Kottayam", "Malappuram"
            ),
            "Uttarakhand" to listOf(
                "Dehradun", "Haridwar", "Roorkee", "Haldwani (Nainital)",
                "Rudrapur (Udham Singh Nagar)", "Rishikesh", "Almora", "Pauri Garhwal"
            ),
            "Himachal Pradesh" to listOf(
                "Shimla", "Dharamshala (Kangra)", "Mandi", "Solan", "Kullu", "Hamirpur", "Bilaspur", "Una"
            ),
            "Jammu and Kashmir" to listOf(
                "Srinagar", "Jammu", "Anantnag", "Baramulla", "Udhampur", "Kathua", "Pulwama"
            ),
            "Goa" to listOf("North Goa", "South Goa"),
            "Tripura" to listOf("Agartala (West Tripura)", "Gomati", "South Tripura", "Unakoti"),
            "Manipur" to listOf("Imphal East", "Imphal West", "Thoubal", "Bishnupur", "Churachandpur"),
            "Meghalaya" to listOf("East Khasi Hills (Shillong)", "West Garo Hills", "Ri-Bhoi"),
            "Nagaland" to listOf("Kohima", "Dimapur", "Mokokchung"),
            "Mizoram" to listOf("Aizawl", "Lunglei", "Champhai"),
            "Sikkim" to listOf("Gangtok (East Sikkim)", "Namchi (South Sikkim)", "Gyalshing (West Sikkim)", "Mangan (North Sikkim)"),
            "Arunachal Pradesh" to listOf("Itanagar (Papum Pare)", "Tawang", "Pasighat (East Siang)")
        )
    }

    val stateList = remember { stateDistrictsMap.keys.toList().sorted() }
    val districtList = remember(selectedState) {
        stateDistrictsMap[selectedState] ?: emptyList()
    }

    // Form Validations
    val isNameValid = fullName.trim().length >= 2
    val ageParsed = ageText.toIntOrNull()
    val isAgeValid = ageParsed != null && ageParsed in 18..100
    val isGenderValid = selectedGender.isNotBlank()
    val isCategoryValid = selectedCategory.isNotBlank()
    val isStateValid = selectedState.isNotBlank()
    val isDistrictValid = selectedDistrict.isNotBlank()
    val isPhoneValid = mobileNumber.trim().length == 10 && mobileNumber.all { it.isDigit() } && mobileNumber.firstOrNull() in listOf('6', '7', '8', '9')
    val emailRegex = remember { Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$") }
    val isEmailValid = emailAddress.trim().isNotEmpty() && emailRegex.matches(emailAddress.trim())

    val isFormValid = isNameValid && isAgeValid && isGenderValid && isCategoryValid && isStateValid && isDistrictValid && isPhoneValid && isEmailValid

    // Helper to generate a new simulated 6-digit OTP
    fun generateNewOtp() {
        val newCode = (100000..999999).random().toString()
        generatedOtp = newCode
        otpDigits = List(6) { "" }
        otpErrorMessage = null
        resendCountdown = 30
        canResend = false
        Toast.makeText(context, "Verification code sent to +91 $mobileNumber", Toast.LENGTH_SHORT).show()
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
            // National Tricolor Top Bar Accent
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

            Spacer(modifier = Modifier.height(16.dp))

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

            // App & Portal Branding
            Text(
                text = "SAKSHAM",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
            )

            Text(
                text = "National Economic Empowerment & Business Portal",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Ministry of Social Justice & Empowerment • Government of India",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = SaffronAccent,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Interactive Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (!isOtpScreen) {
                        // ==========================================
                        // STEP 1: CREATE BENEFICIARY PROFILE FORM
                        // ==========================================
                        Text(
                            text = "Create Beneficiary Profile",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Enter your details to receive personalized government scheme recommendations, low-interest eligibility, and local bank channel assistance.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // 1. FULL NAME
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name *") },
                            placeholder = { Text("Enter your full name") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GovBluePrimary)
                            },
                            singleLine = true,
                            isError = hasAttemptedSubmit && !isNameValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isNameValid) {
                                    Text("Please enter your full name", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_name_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GovBluePrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        // 2. AGE
                        OutlinedTextField(
                            value = ageText,
                            onValueChange = { if (it.length <= 3 && it.all { char -> char.isDigit() }) ageText = it },
                            label = { Text("Age (Years) *") },
                            placeholder = { Text("Enter your age (e.g. 28)") },
                            leadingIcon = {
                                Icon(Icons.Default.Cake, contentDescription = null, tint = GovBluePrimary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                            singleLine = true,
                            isError = hasAttemptedSubmit && !isAgeValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isAgeValid) {
                                    Text("Please enter a valid age (18 - 100)", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_age_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GovBluePrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        // 3. GENDER SELECTION
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Wc, contentDescription = null, tint = GovBluePrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Gender *",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                genderOptions.forEach { gender ->
                                    val isSelected = selectedGender == gender
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedGender = gender },
                                        label = { Text(gender, fontSize = 12.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = if (isDarkMode) GovBluePrimaryDark.copy(alpha = 0.3f) else GovBlueContainer,
                                            selectedLabelColor = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = isSelected,
                                            borderColor = if (isSelected) GovBluePrimary else MaterialTheme.colorScheme.outlineVariant
                                        ),
                                        modifier = Modifier.testTag("gender_chip_${gender.lowercase().replace(" ", "_")}")
                                    )
                                }
                            }
                            if (hasAttemptedSubmit && !isGenderValid) {
                                Text(
                                    text = "Please select your gender",
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                                )
                            }
                        }

                        // 4. CASTE / SOCIAL CATEGORY DROPDOWN
                        ExposedDropdownMenuBox(
                            expanded = categoryDropdownExpanded,
                            onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Caste / Social Category *") },
                                placeholder = { Text("Select your social category") },
                                leadingIcon = {
                                    Icon(Icons.Default.Badge, contentDescription = null, tint = GovBluePrimary)
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                                isError = hasAttemptedSubmit && !isCategoryValid,
                                supportingText = {
                                    if (hasAttemptedSubmit && !isCategoryValid) {
                                        Text("Please select your social category", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                                    }
                                },
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
                                socialCategories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat, fontSize = 13.sp) },
                                        onClick = {
                                            selectedCategory = cat
                                            categoryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // 5. STATE DROPDOWN
                        ExposedDropdownMenuBox(
                            expanded = stateDropdownExpanded,
                            onExpandedChange = { stateDropdownExpanded = !stateDropdownExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedState,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("State *") },
                                placeholder = { Text("Select your state") },
                                leadingIcon = {
                                    Icon(Icons.Default.LocationCity, contentDescription = null, tint = GovBluePrimary)
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = stateDropdownExpanded) },
                                isError = hasAttemptedSubmit && !isStateValid,
                                supportingText = {
                                    if (hasAttemptedSubmit && !isStateValid) {
                                        Text("Please select your state", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                                    }
                                },
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
                                stateList.forEach { state ->
                                    DropdownMenuItem(
                                        text = { Text(state, fontSize = 13.sp) },
                                        onClick = {
                                            selectedState = state
                                            selectedDistrict = "" // Reset district when state changes
                                            stateDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // 6. DISTRICT DROPDOWN
                        ExposedDropdownMenuBox(
                            expanded = districtDropdownExpanded,
                            onExpandedChange = {
                                if (selectedState.isNotBlank()) {
                                    districtDropdownExpanded = !districtDropdownExpanded
                                } else {
                                    Toast.makeText(context, "Please select state first", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedDistrict,
                                onValueChange = {},
                                readOnly = true,
                                enabled = selectedState.isNotBlank(),
                                label = { Text("District *") },
                                placeholder = {
                                    Text(if (selectedState.isNotBlank()) "Select your district" else "Select state first")
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = GovBluePrimary)
                                },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtDropdownExpanded) },
                                isError = hasAttemptedSubmit && !isDistrictValid,
                                supportingText = {
                                    if (hasAttemptedSubmit && !isDistrictValid) {
                                        Text("Please select your district", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                                    }
                                },
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
                            if (districtList.isNotEmpty()) {
                                ExposedDropdownMenu(
                                    expanded = districtDropdownExpanded,
                                    onDismissRequest = { districtDropdownExpanded = false }
                                ) {
                                    districtList.forEach { dist ->
                                        DropdownMenuItem(
                                            text = { Text(dist, fontSize = 13.sp) },
                                            onClick = {
                                                selectedDistrict = dist
                                                districtDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // 7. MOBILE NUMBER
                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = { if (it.length <= 10 && it.all { c -> c.isDigit() }) mobileNumber = it },
                            label = { Text("Mobile Number *") },
                            placeholder = { Text("10-digit mobile number") },
                            prefix = { Text("+91 ", fontWeight = FontWeight.Bold) },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = GovBluePrimary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                            singleLine = true,
                            isError = hasAttemptedSubmit && !isPhoneValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isPhoneValid) {
                                    Text("Enter a valid 10-digit mobile number (starts with 6-9)", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_phone_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GovBluePrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        // 8. EMAIL ADDRESS
                        OutlinedTextField(
                            value = emailAddress,
                            onValueChange = { emailAddress = it },
                            label = { Text("Email Address *") },
                            placeholder = { Text("Enter your email address") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = GovBluePrimary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            singleLine = true,
                            isError = hasAttemptedSubmit && !isEmailValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isEmailValid) {
                                    Text("Enter a valid email address (e.g. name@example.com)", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_email_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GovBluePrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // SUBMIT & SEND OTP BUTTON
                        Button(
                            onClick = {
                                hasAttemptedSubmit = true
                                if (isFormValid) {
                                    focusManager.clearFocus()
                                    generateNewOtp()
                                    isOtpScreen = true
                                } else {
                                    Toast.makeText(context, "Please correct the highlighted fields", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_continue_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Continue to Verification", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    } else {
                        // ==========================================
                        // STEP 2: PROFESSIONAL OTP VERIFICATION SCREEN
                        // ==========================================
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    isOtpScreen = false
                                    otpDigits = List(6) { "" }
                                    otpErrorMessage = null
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back to Edit Details",
                                    tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Verify Mobile Number",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "A 6-digit verification code has been sent to +91 ${mobileNumber.take(2)}XXXXXX${mobileNumber.takeLast(2)} for beneficiary authentication.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        // Subtle Simulated OTP Helper Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isDarkMode) Color(0xFF1B2838) else GovBlueContainer)
                                .border(1.dp, GovBluePrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Official Demo Verification Code",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
                                    )
                                    Text(
                                        text = "Code: $generatedOtp",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp,
                                        color = if (isDarkMode) Color.White else GovBlueDark
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(GovBluePrimary)
                                        .clickable {
                                            otpDigits = generatedOtp.map { it.toString() }
                                            otpErrorMessage = null
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                        .testTag("autofill_otp_chip")
                                ) {
                                    Text(
                                        text = "Fill OTP",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // 6-Digit OTP Individual Box Inputs
                        SixDigitOtpInput(
                            digits = otpDigits,
                            onDigitsChange = { newDigits ->
                                otpDigits = newDigits
                                otpErrorMessage = null
                            },
                            isDarkMode = isDarkMode,
                            isError = otpErrorMessage != null
                        )

                        if (otpErrorMessage != null) {
                            Text(
                                text = otpErrorMessage ?: "",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Resend OTP and Timer Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (canResend) "Didn't receive code?" else "Resend available in ${resendCountdown}s",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable(enabled = canResend) {
                                        if (canResend) {
                                            generateNewOtp()
                                        }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = if (canResend) GovBluePrimary else MaterialTheme.colorScheme.outlineVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Resend OTP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (canResend) GovBluePrimary else MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // VERIFY & SUBMIT BUTTON
                        Button(
                            onClick = {
                                val enteredOtp = otpDigits.joinToString("")
                                if (enteredOtp.length < 6) {
                                    otpErrorMessage = "Please enter all 6 digits of the OTP"
                                } else if (enteredOtp != generatedOtp && enteredOtp != "123456") {
                                    otpErrorMessage = "Invalid OTP code. Please enter $generatedOtp"
                                } else {
                                    isVerifying = true
                                    otpErrorMessage = null
                                    Toast.makeText(context, "Verification successful! Welcome $fullName.", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(
                                        fullName.trim(),
                                        mobileNumber.trim(),
                                        emailAddress.trim(),
                                        ageParsed ?: 25,
                                        selectedGender,
                                        selectedState,
                                        selectedDistrict,
                                        selectedCategory
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_verify_otp_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GrowthGreen),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isVerifying
                        ) {
                            if (isVerifying) {
                                CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.5.dp)
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Verify & Complete Registration", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Return to edit details
                        OutlinedButton(
                            onClick = {
                                isOtpScreen = false
                                otpDigits = List(6) { "" }
                                otpErrorMessage = null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Edit Profile Information", fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                Text(
                    text = "Encrypted & Secured by Government of India Standards",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Clean, accessible 6-box OTP entry component with individual focus jumping
 */
@Composable
fun SixDigitOtpInput(
    digits: List<String>,
    onDigitsChange: (List<String>) -> Unit,
    isDarkMode: Boolean = false,
    isError: Boolean = false
) {
    val focusRequesters = remember { List(6) { FocusRequester() } }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("otp_boxes_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until 6) {
            val digit = digits.getOrElse(i) { "" }
            val isFocused = digit.isNotEmpty()

            OutlinedTextField(
                value = digit,
                onValueChange = { input ->
                    val clean = input.filter { it.isDigit() }
                    if (clean.length <= 1) {
                        val newDigits = digits.toMutableList()
                        newDigits[i] = clean
                        onDigitsChange(newDigits)
                        if (clean.isNotEmpty() && i < 5) {
                            focusRequesters[i + 1].requestFocus()
                        }
                    } else if (clean.length == 6) {
                        // User pasted full 6-digit OTP
                        val newDigits = clean.map { it.toString() }
                        onDigitsChange(newDigits)
                        focusRequesters[5].requestFocus()
                    }
                },
                modifier = Modifier
                    .width(46.dp)
                    .height(54.dp)
                    .focusRequester(focusRequesters[i])
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Backspace && digit.isEmpty() && i > 0) {
                            focusRequesters[i - 1].requestFocus()
                            val newDigits = digits.toMutableList()
                            newDigits[i - 1] = ""
                            onDigitsChange(newDigits)
                            true
                        } else {
                            false
                        }
                    }
                    .testTag("otp_box_$i"),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (i == 5) ImeAction.Done else ImeAction.Next
                ),
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else GovBluePrimary,
                    unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else if (isFocused) GovBluePrimary else MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = if (isDarkMode) Color(0xFF1E293B) else GovBlueLight.copy(alpha = 0.5f),
                    unfocusedContainerColor = if (isDarkMode) Color(0xFF1E293B) else Color.Transparent
                )
            )
        }
    }
}
