package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.SakshamStrings
import com.example.ui.navigation.Screen
import com.example.ui.theme.GovBlueContainer
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBlueLight
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SaffronLight
import com.example.ui.theme.SlateBorder

@Composable
fun HomeScreen(
    onNavigate: (Screen) -> Unit,
    onSelectSchemeCategory: (String) -> Unit,
    language: String = "English",
    isDarkMode: Boolean = false
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Government Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = if (isDarkMode) {
                                listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                            } else {
                                listOf(GovBluePrimary, GovBlueDark)
                            }
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Official Portal",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = SakshamStrings.get("official_portal", language),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = SakshamStrings.get("welcome_hero", language),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = SakshamStrings.get("welcome_hero_sub", language),
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Key Highlights row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickMetricPill(
                            label = SakshamStrings.get("metric_interest_label", language),
                            value = SakshamStrings.get("metric_interest", language),
                            modifier = Modifier.weight(1f)
                        )
                        QuickMetricPill(
                            label = SakshamStrings.get("metric_limit_label", language),
                            value = SakshamStrings.get("metric_limit", language),
                            modifier = Modifier.weight(1f)
                        )
                        QuickMetricPill(
                            label = SakshamStrings.get("metric_moratorium_label", language),
                            value = SakshamStrings.get("metric_moratorium", language),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Section Title: Main Services
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Text(
                    text = SakshamStrings.get("services_heading", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp
                )
                Text(
                    text = SakshamStrings.get("services_sub", language),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Main 7 Options Cards
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Find Best Scheme (Primary Feature)
                MainOptionCard(
                    title = SakshamStrings.get("card_find_scheme_title", language),
                    hindiSubtitle = SakshamStrings.get("card_find_scheme_sub", language),
                    description = SakshamStrings.get("card_find_scheme_desc", language),
                    icon = Icons.Default.FindInPage,
                    badgeText = "Most Popular",
                    badgeColor = GrowthGreen,
                    containerColor = if (isDarkMode) Color(0xFF132A1C) else GovBlueLight,
                    accentColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                    isDarkMode = isDarkMode,
                    testTag = "home_card_find_best_scheme",
                    onClick = { onNavigate(Screen.BusinessForm) }
                )

                // 2. Education Loan
                MainOptionCard(
                    title = SakshamStrings.get("card_education_title", language),
                    hindiSubtitle = SakshamStrings.get("card_education_sub", language),
                    description = SakshamStrings.get("card_education_desc", language),
                    icon = Icons.Default.School,
                    badgeText = "3.5% - 4% Interest",
                    badgeColor = GovBluePrimary,
                    containerColor = if (isDarkMode) Color(0xFF132A1C) else Color(0xFFF0FDF4),
                    accentColor = GrowthGreen,
                    isDarkMode = isDarkMode,
                    testTag = "home_card_education_loan",
                    onClick = {
                        onSelectSchemeCategory("Education")
                        onNavigate(Screen.Schemes)
                    }
                )

                // 3. Business Loan
                MainOptionCard(
                    title = SakshamStrings.get("card_business_title", language),
                    hindiSubtitle = SakshamStrings.get("card_business_sub", language),
                    description = SakshamStrings.get("card_business_desc", language),
                    icon = Icons.Default.Business,
                    badgeText = "Up to ₹50 Lakh",
                    badgeColor = SaffronAccent,
                    containerColor = if (isDarkMode) Color(0xFF261D10) else SaffronLight.copy(alpha = 0.4f),
                    accentColor = SaffronAccent,
                    isDarkMode = isDarkMode,
                    testTag = "home_card_business_loan",
                    onClick = {
                        onSelectSchemeCategory("Business")
                        onNavigate(Screen.Schemes)
                    }
                )

                // 4. EMI Calculator
                MainOptionCard(
                    title = SakshamStrings.get("card_calculator_title", language),
                    hindiSubtitle = SakshamStrings.get("card_calculator_sub", language),
                    description = SakshamStrings.get("card_calculator_desc", language),
                    icon = Icons.Default.Calculate,
                    badgeText = "Moratorium Friendly",
                    badgeColor = GovBluePrimary,
                    containerColor = if (isDarkMode) Color(0xFF1F1B2E) else Color(0xFFFAF5FF),
                    accentColor = Color(0xFF8B5CF6),
                    isDarkMode = isDarkMode,
                    testTag = "home_card_emi_calculator",
                    onClick = { onNavigate(Screen.Calculator) }
                )

                // 5. Find Nearest Partner
                MainOptionCard(
                    title = SakshamStrings.get("card_partners_title", language),
                    hindiSubtitle = SakshamStrings.get("card_partners_sub", language),
                    description = SakshamStrings.get("card_partners_desc", language),
                    icon = Icons.Default.LocationOn,
                    badgeText = "Map & Directions",
                    badgeColor = GrowthGreen,
                    containerColor = if (isDarkMode) Color(0xFF132A1C) else Color(0xFFF0FDF4),
                    accentColor = GrowthGreen,
                    isDarkMode = isDarkMode,
                    testTag = "home_card_find_nearest_partner",
                    onClick = { onNavigate(Screen.Partners) }
                )

                // 6. AI Assistant
                MainOptionCard(
                    title = SakshamStrings.get("card_ai_title", language),
                    hindiSubtitle = SakshamStrings.get("card_ai_sub", language),
                    description = SakshamStrings.get("card_ai_desc", language),
                    icon = Icons.Default.AutoAwesome,
                    badgeText = "AI Powered 24/7",
                    badgeColor = GovBluePrimary,
                    containerColor = if (isDarkMode) Color(0xFF1E293B) else GovBlueContainer.copy(alpha = 0.5f),
                    accentColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                    isDarkMode = isDarkMode,
                    testTag = "home_card_ai_assistant",
                    onClick = { onNavigate(Screen.AIChat) }
                )

                // 7. Help & Contact Support
                MainOptionCard(
                    title = SakshamStrings.get("card_help_title", language),
                    hindiSubtitle = SakshamStrings.get("card_help_sub", language),
                    description = SakshamStrings.get("card_help_desc", language),
                    icon = Icons.Default.SupportAgent,
                    badgeText = "Help & Helpline",
                    badgeColor = SaffronAccent,
                    containerColor = if (isDarkMode) Color(0xFF261D10) else SaffronLight.copy(alpha = 0.35f),
                    accentColor = SaffronAccent,
                    isDarkMode = isDarkMode,
                    testTag = "home_card_help_contact",
                    onClick = { onNavigate(Screen.HelpContact) }
                )
            }
        }

        // Trust Banner / Authoritative Data Source
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = GrowthGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = SakshamStrings.get("trust_title", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
                        )
                        Text(
                            text = SakshamStrings.get("trust_desc", language),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickMetricPill(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .padding(vertical = 8.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun MainOptionCard(
    title: String,
    hindiSubtitle: String,
    description: String,
    icon: ImageVector,
    badgeText: String,
    badgeColor: Color,
    containerColor: Color,
    accentColor: Color,
    isDarkMode: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, hoveredElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon in tinted box
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(containerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(badgeColor.copy(alpha = 0.15f))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = badgeColor
                            )
                        }
                    }

                    Text(
                        text = hindiSubtitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = description,
                fontSize = 12.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Open Service",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
