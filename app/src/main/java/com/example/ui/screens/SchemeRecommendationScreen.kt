package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

val schemeCategories = listOf("All Schemes", "Business Loan", "Education Loan", "Women Entrepreneurship", "Green Business")

@Composable
fun SchemeRecommendationScreen(
    schemes: List<GovernmentScheme>,
    profile: BusinessProfile,
    savedSchemeIds: Set<String>,
    onSelectScheme: (GovernmentScheme) -> Unit,
    onToggleSave: (GovernmentScheme) -> Unit,
    onNavigate: (Screen) -> Unit,
    language: String = "English",
    isDarkMode: Boolean = false
) {
    var selectedCategory by remember { mutableStateOf("All Schemes") }

    val filteredSchemes = schemes.filter {
        if (selectedCategory == "All Schemes") true
        else it.category.contains(selectedCategory.split(" ").first(), ignoreCase = true)
    }

    val recommendedScheme = schemes.firstOrNull { it.isRecommended } ?: schemes.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("scheme_recommendation_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // AI Advice Header Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF1E293B) else GovBluePrimary
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Personalized Scheme Matches",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        // Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GrowthGreen)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "NSFDC Verified",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Target: ${profile.businessType} (₹${"%,d".format(profile.loanRequired)} Loan Required)",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Matched against government concessional interest rates, moratorium criteria, and social category.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { onNavigate(Screen.ActionPlan) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDarkMode) GovBluePrimaryDark else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "View Step-by-Step AI Guidance Plan ➔",
                            color = if (isDarkMode) Color.Black else GovBluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(schemeCategories) { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) GovBluePrimary else MaterialTheme.colorScheme.surface)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) GovBluePrimary else if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // HIGHLIGHTED RECOMMENDED SCHEME SECTION
        if (recommendedScheme != null && selectedCategory == "All Schemes") {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = SaffronAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RECOMMENDED SCHEME (सर्वोत्तम अनुशंसित योजना)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SaffronAccent,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    SchemeCard(
                        scheme = recommendedScheme,
                        isHighlighted = true,
                        isSaved = savedSchemeIds.contains(recommendedScheme.id),
                        isDarkMode = isDarkMode,
                        onViewDetails = {
                            onSelectScheme(recommendedScheme)
                            onNavigate(Screen.SchemeDetail)
                        },
                        onToggleSave = { onToggleSave(recommendedScheme) }
                    )
                }
            }
        }

        // ALL MATCHING SCHEMES SECTION
        item {
            Text(
                text = "All Available Schemes (${filteredSchemes.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        items(filteredSchemes) { scheme ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                SchemeCard(
                    scheme = scheme,
                    isHighlighted = false,
                    isSaved = savedSchemeIds.contains(scheme.id),
                    isDarkMode = isDarkMode,
                    onViewDetails = {
                        onSelectScheme(scheme)
                        onNavigate(Screen.SchemeDetail)
                    },
                    onToggleSave = { onToggleSave(scheme) }
                )
            }
        }
    }
}

@Composable
fun SchemeCard(
    scheme: GovernmentScheme,
    isHighlighted: Boolean,
    isSaved: Boolean,
    isDarkMode: Boolean = false,
    onViewDetails: () -> Unit,
    onToggleSave: () -> Unit
) {
    val cardBg = if (isHighlighted) {
        if (isDarkMode) Color(0xFF16253B) else Color(0xFFF8FAFF)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 4.dp else 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isHighlighted) 2.dp else 1.dp,
            color = if (isHighlighted) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else (if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("scheme_card_${scheme.id}")
            .clickable(onClick = onViewDetails)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Match Percentage & Category Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${scheme.matchPercentage}% Match",
                            color = GrowthGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueLight)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = scheme.category,
                            color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Scheme",
                        tint = if (isSaved) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else SlateLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scheme Name
            Text(
                text = scheme.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            Text(
                text = scheme.department,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // Key Metrics Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isDarkMode) Color(0xFF1E293B)
                        else if (isHighlighted) Color.White
                        else GovBlueLight.copy(alpha = 0.5f)
                    )
                    .border(
                        1.dp,
                        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Max Loan Amount", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = scheme.maxLoanAmountDisplay,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Interest Rate", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = scheme.interestRateDisplay,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrowthGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Suitable Purpose
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "Suitable for: ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = scheme.suitablePurpose,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Basic Eligibility
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "Eligibility: ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = scheme.eligibilitySummary,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // View Details Button
            Button(
                onClick = onViewDetails,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("view_details_button_${scheme.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isHighlighted) GovBluePrimary else if (isDarkMode) Color(0xFF2563EB) else GovBlueDark
                )
            ) {
                Text(
                    text = "View Complete Details (विस्तृत जानकारी)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}
