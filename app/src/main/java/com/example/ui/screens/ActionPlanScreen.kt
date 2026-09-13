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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.BusinessActionPlan
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
import com.example.ui.theme.SlateMedium

@Composable
fun ActionPlanScreen(
    plan: BusinessActionPlan,
    onSavePlan: () -> Unit,
    onNavigate: (Screen) -> Unit,
    language: String = "English",
    isDarkMode: Boolean = false
) {
    var isPlanSaved by remember { mutableStateOf(false) }

    val cardBorder = androidx.compose.foundation.BorderStroke(
        1.dp,
        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("action_plan_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Plan Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF1E293B) else GovBlueDark
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(GovBlueContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = GovBluePrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "AI Business Action Plan",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "व्यापार कार्ययोजना एवं मार्गदर्शन",
                                    color = Color.White.copy(alpha = 0.75f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Save Button
                        Button(
                            onClick = {
                                onSavePlan()
                                isPlanSaved = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPlanSaved) GrowthGreen else Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlanSaved) Icons.Default.CheckCircle else Icons.Default.Bookmark,
                                contentDescription = "Save Plan",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isPlanSaved) "Saved" else "Save Plan",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = plan.businessType,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = plan.summary,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Section 1 & 2: Suitable Scale & Basic Setup Requirements
        item {
            ActionModuleCard(
                title = "1. Suitable Scale of Operation & Strategy",
                hindiSubtitle = "उपयुक्त पैमाना एवं व्यवसाय रणनीति",
                isDarkMode = isDarkMode
            ) {
                Text(
                    text = plan.suitableScale,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Section 3: Approximate Investment Breakdown
        item {
            ActionModuleCard(
                title = "2. Approximate Investment Areas",
                hindiSubtitle = "अनुमानित निवेश एवं पूंजी आवंटन",
                isDarkMode = isDarkMode
            ) {
                plan.investmentBreakdown.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isDarkMode) GovBluePrimaryDark else GovBluePrimary)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = item,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Section 4: Basic Setup & Infrastructure
        item {
            ActionModuleCard(
                title = "3. Setup & Infrastructure Requirements",
                hindiSubtitle = "आवश्यक बुनियादी ढांचा एवं उपकरण",
                isDarkMode = isDarkMode
            ) {
                plan.setupAndInfrastructure.forEach { infra ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GrowthGreen,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = infra,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Section 5: Expenses & Revenue Projections
        item {
            ActionModuleCard(
                title = "4. Expenses & Revenue Estimation",
                hindiSubtitle = "अनुमानित मासिक खर्च, आय एवं लाभ",
                isDarkMode = isDarkMode
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight)
                        .padding(12.dp)
                ) {
                    Text(
                        text = plan.expenseRevenueProjection,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkMode) GrowthGreenLight else Color(0xFF14532D)
                    )
                }
            }
        }

        // Section 6: Business Growth Plan
        item {
            ActionModuleCard(
                title = "5. How to Plan for Growth",
                hindiSubtitle = "व्यवसाय विस्तार एवं चरणबद्ध वृद्धि",
                isDarkMode = isDarkMode
            ) {
                plan.growthPlan.forEach { phase ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = phase,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Section 7: Recommended Scheme & Financing
        item {
            ActionModuleCard(
                title = "6. Recommended Financing & Loan Scheme",
                hindiSubtitle = "अनुशंसित सरकारी ऋण योजना",
                isDarkMode = isDarkMode
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueLight)
                        .border(
                            1.dp,
                            if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else GovBluePrimary.copy(alpha = 0.2f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = plan.recommendedFinancing,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = plan.estimatedLoanNeeded,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Section 8: Required Documents Checklist
        item {
            ActionModuleCard(
                title = "7. Required Documents & Checklist",
                hindiSubtitle = "आवेदन हेतु आवश्यक आधिकारिक दस्तावेज",
                isDarkMode = isDarkMode
            ) {
                plan.requiredDocuments.forEach { doc ->
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

        // Section 9: Step-by-Step Action Plan to Proceed
        item {
            ActionModuleCard(
                title = "8. Step-by-Step Action Plan to Apply",
                hindiSubtitle = "अगला कदम: ऋण प्राप्ति की चरणबद्ध प्रक्रिया",
                isDarkMode = isDarkMode
            ) {
                plan.stepByStepSteps.forEach { step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = step,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Section 10: Where to Find Channel Partner
        item {
            ActionModuleCard(
                title = "9. Authorized Channel Partner",
                hindiSubtitle = "निकटतम अधिकृत चैनेलाइजिंग एजेंसी व बैंक",
                isDarkMode = isDarkMode
            ) {
                Text(
                    text = plan.partnerRecommendation,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onNavigate(Screen.Partners) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = GrowthGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Find Partner", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onNavigate(Screen.AIChat) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ask AI Advisor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ActionModuleCard(
    title: String,
    hindiSubtitle: String,
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = hindiSubtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            content()
        }
    }
}
