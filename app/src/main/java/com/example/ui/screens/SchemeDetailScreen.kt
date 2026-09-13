package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GovernmentScheme
import com.example.ui.navigation.Screen
import com.example.ui.theme.GovBlueContainer
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBlueLight
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateMedium

@Composable
fun SchemeDetailScreen(
    scheme: GovernmentScheme?,
    isSaved: Boolean,
    onToggleSave: () -> Unit,
    onNavigate: (Screen) -> Unit,
    language: String = "English",
    isDarkMode: Boolean = false
) {
    if (scheme == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(com.example.ui.i18n.SakshamStrings.get("scheme_details_not_found"), color = MaterialTheme.colorScheme.onSurface)
        }
        return
    }

    val cardBorder = androidx.compose.foundation.BorderStroke(
        1.dp,
        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("scheme_detail_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Official Scheme Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = cardBorder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = scheme.category,
                                color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        IconButton(
                            onClick = onToggleSave,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isSaved) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else SlateLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = scheme.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = scheme.department,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Financial Parameters Grid (4 key boxes)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailParameterBox(
                        icon = Icons.Default.AccountBalance,
                        label = "Maximum Loan Limit",
                        value = scheme.maxLoanAmountDisplay,
                        accentColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                        modifier = Modifier.weight(1f)
                    )
                    DetailParameterBox(
                        icon = Icons.Default.Percent,
                        label = "Concessional Interest",
                        value = scheme.interestRateDisplay,
                        accentColor = GrowthGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailParameterBox(
                        icon = Icons.Default.HourglassBottom,
                        label = "Moratorium Period",
                        value = scheme.moratoriumDisplay,
                        accentColor = SaffronAccent,
                        modifier = Modifier.weight(1f)
                    )
                    DetailParameterBox(
                        icon = Icons.Default.Schedule,
                        label = "Repayment Tenure",
                        value = scheme.repaymentDisplay,
                        accentColor = Color(0xFF818CF8),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Subsidy / Margin Money Box
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, GrowthGreen.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GrowthGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = com.example.ui.i18n.SakshamStrings.get("subsidy_&_margin_money_support"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isDarkMode) GrowthGreenLight else Color(0xFF14532D)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = scheme.subsidyMargin,
                            fontSize = 12.sp,
                            color = if (isDarkMode) MaterialTheme.colorScheme.onSurface else Color(0xFF14532D),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Eligibility Criteria
        item {
            DetailSectionCard(title = "Eligibility Criteria (पात्रता शर्तें)") {
                scheme.eligibilityDetails.forEach { crit ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isDarkMode) GovBluePrimaryDark else GovBluePrimary)
                                .padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = crit,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Required Documents
        item {
            DetailSectionCard(title = "Required Documents (आवश्यक दस्तावेज)") {
                scheme.requiredDocuments.forEach { doc ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = SaffronAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = doc,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // How to Apply Step-by-Step
        item {
            DetailSectionCard(title = "How to Apply Step-by-Step (आवेदन की प्रक्रिया)") {
                scheme.howToApplySteps.forEach { stepText ->
                    Text(
                        text = stepText,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        // Channel Partners Info & Prominent CTA Button
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF1E293B) else GovBlueLight
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else GovBluePrimary.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = com.example.ui.i18n.SakshamStrings.get("authorized_channel_partners_&_banks"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = scheme.channelPartnersInfo,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Prominent Button: Find Eligible Partner
                    Button(
                        onClick = { onNavigate(Screen.Partners) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("find_eligible_partner_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GrowthGreen)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = com.example.ui.i18n.SakshamStrings.get("find_eligible_partner_निकटतम_बैंक_खोजें"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { onNavigate(Screen.AIChat) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = com.example.ui.i18n.SakshamStrings.get("ask_ai_assistant_about_this_scheme"),
                            color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailParameterBox(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun DetailSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

