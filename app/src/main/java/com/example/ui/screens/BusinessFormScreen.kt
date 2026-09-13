package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BusinessProfile
import com.example.ui.navigation.Screen
import com.example.ui.theme.GovBlueContainer
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBlueLight
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateMedium

val businessTypesList = listOf(
    "Dairy Business" to "🐄 डेयरी फार्मिंग",
    "Retail Grocery Shop" to "🛒 किराना एवं खुदरा दुकान",
    "Tailoring & Garments" to "🧵 सिलाई व कपड़ा निर्माण",
    "Food Processing / Eatery" to "🍲 खाद्य प्रसंस्करण व खान-पान",
    "Small Manufacturing" to "⚙️ लघु निर्माण व कार्यशाला",
    "Agriculture & Allied" to "🌾 कृषि एवं संबद्ध गतिविधियां",
    "Repair & Service Center" to "🔧 वाहन/उपकरण मरम्मत केंद्र",
    "Handicrafts & Artisans" to "🎨 हस्तशिल्प व कारीगरी",
    "Transportation (E-Rickshaw)" to "🛺 ई-रिक्शा / व्यावसायिक वाहन"
)

val familyIncomeOptions = listOf(
    "Below ₹1.5 Lakh" to "BPL / ₹1.5 लाख से कम (उच्चतम प्राथमिकता)",
    "₹1.5 Lakh - ₹3.0 Lakh" to "₹1.50 - 3.00 लाख (NSFDC पूर्ण पात्र)",
    "₹3.0 Lakh - ₹6.0 Lakh" to "₹3.00 - 6.00 लाख",
    "Above ₹6.0 Lakh" to "₹6.00 लाख से अधिक"
)

val experienceOptions = listOf(
    "No Experience (Beginner)",
    "Under 2 Years",
    "2 to 5 Years",
    "5+ Years Expert"
)

val employeeOptions = listOf(
    "Self Only",
    "1 - 2 Persons",
    "3 - 5 Persons",
    "6 - 10 Persons",
    "10+ Persons"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BusinessFormScreen(
    currentProfile: BusinessProfile,
    onSaveProfile: (BusinessProfile) -> Unit,
    onNavigate: (Screen) -> Unit,
    language: String = "English",
    isDarkMode: Boolean = false
) {
    var step by remember { mutableIntStateOf(1) }
    val totalSteps = 4

    // Local form states initialized from current profile
    var businessType by remember { mutableStateOf(currentProfile.businessType) }
    var isExistingBusiness by remember { mutableStateOf(currentProfile.isExistingBusiness) }
    var locationType by remember { mutableStateOf(currentProfile.locationType) }
    var state by remember { mutableStateOf(currentProfile.state) }
    var district by remember { mutableStateOf(currentProfile.district) }

    var totalInvestment by remember { mutableFloatStateOf(currentProfile.totalInvestment.toFloat()) }
    var ownCapital by remember { mutableFloatStateOf(currentProfile.ownCapital.toFloat()) }
    var annualFamilyIncome by remember { mutableStateOf(currentProfile.annualFamilyIncome) }
    var monthlyExpenses by remember { mutableFloatStateOf(currentProfile.monthlyExpenses.toFloat()) }
    var monthlyRevenue by remember { mutableFloatStateOf(currentProfile.monthlyRevenue.toFloat()) }

    var businessExperience by remember { mutableStateOf(currentProfile.businessExperience) }
    var plannedEmployees by remember { mutableStateOf(currentProfile.plannedEmployees) }

    // Dairy specific
    var dairyAnimalCount by remember { mutableIntStateOf(currentProfile.dairyAnimalCount) }
    var dairyLandAvailable by remember { mutableStateOf(currentProfile.dairyLandAvailable) }
    var dairyWaterElectricity by remember { mutableStateOf(currentProfile.dairyWaterElectricity) }
    var dairyMilkCollectionCenter by remember { mutableStateOf(currentProfile.dairyMilkCollectionCenter) }

    val loanRequired = (totalInvestment - ownCapital).coerceAtLeast(10000f).toLong()

    val isDairy = businessType.contains("Dairy", ignoreCase = true)

    val cardBorder = androidx.compose.foundation.BorderStroke(
        1.dp,
        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
    )

    fun submitAndProceed() {
        val updated = BusinessProfile(
            businessType = businessType,
            isExistingBusiness = isExistingBusiness,
            locationType = locationType,
            state = state,
            district = district,
            totalInvestment = totalInvestment.toLong(),
            ownCapital = ownCapital.toLong(),
            loanRequired = loanRequired,
            annualFamilyIncome = annualFamilyIncome,
            monthlyExpenses = monthlyExpenses.toLong(),
            monthlyRevenue = monthlyRevenue.toLong(),
            businessExperience = businessExperience,
            plannedEmployees = plannedEmployees,
            dairyAnimalCount = dairyAnimalCount,
            dairyLandAvailable = dairyLandAvailable,
            dairyWaterElectricity = dairyWaterElectricity,
            dairyMilkCollectionCenter = dairyMilkCollectionCenter
        )
        onSaveProfile(updated)
        onNavigate(Screen.Schemes)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("business_form_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Step Indicator Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = cardBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Step $step of $totalSteps",
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = when (step) {
                                1 -> "Business Basics"
                                2 -> if (isDairy) "Dairy Details" else "Experience & Scale"
                                3 -> "Investment & Financing"
                                else -> "Review & Match"
                            },
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { step / totalSteps.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                        trackColor = if (isDarkMode) Color(0xFF1E293B) else GovBlueLight
                    )
                }
            }
        }

        // STEP 1: Business Basics
        if (step == 1) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = cardBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "What type of business do you want to start or grow?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "आप किस प्रकार का व्यवसाय शुरू या विस्तारित करना चाहते हैं?",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        businessTypesList.forEach { (typeKey, typeLabel) ->
                            val isSelected = businessType == typeKey
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(
                                        if (isSelected) {
                                            if (isDarkMode) Color(0xFF1E293B) else GovBlueLight
                                        } else MaterialTheme.colorScheme.surface
                                    )
                                    .clickable { businessType = typeKey }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, if (isSelected) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else SlateLight, CircleShape)
                                        .background(if (isSelected) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = if (isDarkMode) Color.Black else Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "$typeKey ($typeLabel)",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (isSelected) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // New vs Existing Business
                        Text(
                            text = "Is this a new or existing business?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SelectionChip(
                                label = "🌱 New Startup (नई शुरुआत)",
                                isSelected = !isExistingBusiness,
                                isDarkMode = isDarkMode,
                                modifier = Modifier.weight(1f),
                                onClick = { isExistingBusiness = false }
                            )
                            SelectionChip(
                                label = "📈 Existing Business (विस्तार)",
                                isSelected = isExistingBusiness,
                                isDarkMode = isDarkMode,
                                modifier = Modifier.weight(1f),
                                onClick = { isExistingBusiness = true }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Location Type
                        Text(
                            text = "Business Location Type",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Rural (ग्रामीण)", "Semi-Urban (कस्बा)", "Urban (शहरी)").forEach { loc ->
                                val key = loc.split(" ").first()
                                val isSelected = locationType == key
                                SelectionChip(
                                    label = loc,
                                    isSelected = isSelected,
                                    isDarkMode = isDarkMode,
                                    modifier = Modifier.weight(1f),
                                    onClick = { locationType = key }
                                )
                            }
                        }
                    }
                }
            }
        }

        // STEP 2: Specific Domain Questions (Dairy or General)
        if (step == 2) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = cardBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (isDairy) {
                            Text(
                                text = "Dairy Business Setup Plan 🐄",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "विशिष्ट डेयरी योजना: पशुओं की संख्या व बुनियादी ढांचा",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 14.dp)
                            )

                            // Animal Count Selection
                            Text(
                                text = "How many milch animals (cows/buffaloes) are you planning for?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(2, 4, 6, 10).forEach { count ->
                                    val isSelected = dairyAnimalCount == count
                                    SelectionChip(
                                        label = "$count Animals",
                                        isSelected = isSelected,
                                        isDarkMode = isDarkMode,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            dairyAnimalCount = count
                                            totalInvestment = (count * 125000f).coerceIn(100000f, 2500000f)
                                            ownCapital = (totalInvestment * 0.10f)
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Land / Shed Status
                            Text(
                                text = "Available Land & Shed Status",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            listOf(
                                "Own Land Available (खुद की जमीन/शेड)",
                                "Rented or Leased Land (किराए की जमीन)",
                                "Need funds for land/shed construction"
                            ).forEach { option ->
                                val isSelected = dairyLandAvailable == option
                                SelectionChip(
                                    label = option,
                                    isSelected = isSelected,
                                    isDarkMode = isDarkMode,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    onClick = { dairyLandAvailable = option }
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Resources Checklist
                            Text(
                                text = "Essential Infrastructure Checklist",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Water & Electricity Available?", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                    Text("80-100 L per animal / day", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Switch(
                                    checked = dairyWaterElectricity,
                                    onCheckedChange = { dairyWaterElectricity = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = GovBluePrimary)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Milk Collection / Cooperative Nearby?", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Within 3-5 km for easy daily sale", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Switch(
                                    checked = dairyMilkCollectionCenter,
                                    onCheckedChange = { dairyMilkCollectionCenter = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = GrowthGreen)
                                )
                            }
                        } else {
                            // Non-dairy specific questions
                            Text(
                                text = "Experience & Planned Employees",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "कार्य अनुभव एवं अनुमानित रोजगार सृजन",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 14.dp)
                            )

                            Text(
                                text = "Your Experience in this Business Field",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            experienceOptions.forEach { exp ->
                                val isSelected = businessExperience == exp
                                SelectionChip(
                                    label = exp,
                                    isSelected = isSelected,
                                    isDarkMode = isDarkMode,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    onClick = { businessExperience = exp }
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "How many people do you plan to employ?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                employeeOptions.forEach { emp ->
                                    val isSelected = plannedEmployees == emp
                                    SelectionChip(
                                        label = emp,
                                        isSelected = isSelected,
                                        isDarkMode = isDarkMode,
                                        onClick = { plannedEmployees = emp }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // STEP 3: Financials & Investment
        if (step == 3) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = cardBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Investment & Capital Requirements",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "परियोजना लागत, स्वयं का निवेश एवं आवश्यक ऋण",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 14.dp)
                        )

                        // Total Investment Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Total Project Cost:", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                "₹%,d".format(totalInvestment.toLong()),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                            )
                        }
                        Slider(
                            value = totalInvestment,
                            onValueChange = {
                                totalInvestment = it
                                if (ownCapital > totalInvestment * 0.5f) {
                                    ownCapital = totalInvestment * 0.1f
                                }
                            },
                            valueRange = 50000f..3000000f,
                            steps = 58,
                            colors = SliderDefaults.colors(
                                thumbColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                                activeTrackColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                            )
                        )

                        // Quick buttons for total investment
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(150000f, 300000f, 500000f, 1000000f).forEach { amount ->
                                OutlinedButton(
                                    onClick = {
                                        totalInvestment = amount
                                        ownCapital = amount * 0.10f
                                    },
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text("₹%.0fL".format(amount / 100000f), fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // User's Own Capital
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Your Own Investment (Capital):", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                "₹%,d (%.0f%%)".format(ownCapital.toLong(), (ownCapital / totalInvestment * 100)),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GrowthGreen
                            )
                        }
                        Slider(
                            value = ownCapital,
                            onValueChange = { ownCapital = it },
                            valueRange = 0f..(totalInvestment * 0.4f),
                            colors = SliderDefaults.colors(thumbColor = GrowthGreen, activeTrackColor = GrowthGreen)
                        )

                        // Auto Computed Loan Requirement Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueLight)
                                .border(
                                    1.dp,
                                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else GovBluePrimary.copy(alpha = 0.3f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Government Loan Required:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Eligible for up to 90-95% NSFDC funding",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "₹%,d".format(loanRequired),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Annual Family Income
                        Text(
                            text = "Annual Family Income (वार्षिक पारिवारिक आय)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        familyIncomeOptions.forEach { (incKey, incDesc) ->
                            val isSelected = annualFamilyIncome == incKey
                            SelectionChip(
                                label = incDesc,
                                isSelected = isSelected,
                                isDarkMode = isDarkMode,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                onClick = { annualFamilyIncome = incKey }
                            )
                        }
                    }
                }
            }
        }

        // STEP 4: Review & Summary
        if (step == 4) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = cardBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Ready for Scheme Matchmaking",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Review your details. Our AI will analyze your plan against official NSFDC & Government scheme rules.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                        )

                        SummaryRow("Proposed Business:", businessType)
                        SummaryRow("Category:", if (isExistingBusiness) "Expansion" else "Greenfield Startup")
                        SummaryRow("Location:", "$locationType ($district, $state)")
                        if (isDairy) {
                            SummaryRow("Dairy Scale:", "$dairyAnimalCount Milch Animals")
                            SummaryRow("Water & Power:", if (dairyWaterElectricity) "Available" else "Pending")
                        }
                        SummaryRow("Total Project Cost:", "₹%,d".format(totalInvestment.toLong()))
                        SummaryRow("Own Margin Equity:", "₹%,d".format(ownCapital.toLong()))
                        SummaryRow("Government Loan Needed:", "₹%,d".format(loanRequired))
                        SummaryRow("Annual Family Income:", annualFamilyIncome)
                        SummaryRow("Experience Level:", businessExperience)

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight)
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = GrowthGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "High eligibility detected for NSFDC Term Loan (6% interest, 12 months moratorium).",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDarkMode) GrowthGreenLight else GrowthGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // Navigation Buttons (Back & Next / Submit)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = { step-- },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("form_back_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Back (पिछला)")
                    }
                }

                Button(
                    onClick = {
                        if (step < totalSteps) {
                            step++
                        } else {
                            submitAndProceed()
                        }
                    },
                    modifier = Modifier
                        .weight(if (step == 1) 2f else 1.2f)
                        .height(50.dp)
                        .testTag("form_next_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary)
                ) {
                    Text(
                        text = if (step < totalSteps) "Next Step (अगला)" else "View Best Schemes ➔",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    if (step < totalSteps) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SelectionChip(
    label: String,
    isSelected: Boolean,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                if (isSelected) {
                    if (isDarkMode) Color(0xFF1E293B) else GovBlueContainer.copy(alpha = 0.6f)
                } else MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}
