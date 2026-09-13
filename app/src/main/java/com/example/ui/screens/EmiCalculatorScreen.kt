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
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.ui.i18n.SakshamStrings
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SaffronLight
import com.example.ui.theme.SlateBorder
import kotlin.math.pow

@Composable
fun EmiCalculatorScreen(
    initialLoan: Double,
    initialRate: Double,
    initialTenure: Int,
    initialMoratorium: Int,
    onParametersChanged: (Double, Double, Int, Int) -> Unit,
    language: String = "English",
    isDarkMode: Boolean = false
) {
    var loanAmount by remember { mutableDoubleStateOf(initialLoan) }
    var interestRate by remember { mutableDoubleStateOf(initialRate) }
    var tenureYears by remember { mutableIntStateOf(initialTenure) }
    var moratoriumMonths by remember { mutableIntStateOf(initialMoratorium) }

    // EMI Math Calculation
    val totalMonths = tenureYears * 12
    val activeMonths = (totalMonths - moratoriumMonths).coerceAtLeast(1)
    val monthlyRate = (interestRate / 12.0) / 100.0

    val monthlyEmi = if (monthlyRate == 0.0) {
        loanAmount / activeMonths
    } else {
        (loanAmount * monthlyRate * (1 + monthlyRate).pow(activeMonths)) / ((1 + monthlyRate).pow(activeMonths) - 1)
    }

    val totalRepayment = monthlyEmi * activeMonths
    val totalInterest = (totalRepayment - loanAmount).coerceAtLeast(0.0)

    val cardBorder = androidx.compose.foundation.BorderStroke(
        1.dp,
        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("emi_calculator_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Result Highlight Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF1E293B) else GovBlueDark
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = com.example.ui.i18n.SakshamStrings.get("estimated_monthly_emi_मासिक_किस्त"),
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "₹%,d".format(monthlyEmi.toLong()),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Payable for $activeMonths months (After $moratoriumMonths mo. moratorium)",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp, bottom = 16.dp)
                    )

                    // Breakdown Summary
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(com.example.ui.i18n.SakshamStrings.get("principal_loan"), fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Text("₹%,d".format(loanAmount.toLong()), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(com.example.ui.i18n.SakshamStrings.get("total_interest"), fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Text("₹%,d".format(totalInterest.toLong()), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GrowthGreenLight)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(com.example.ui.i18n.SakshamStrings.get("total_payable"), fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                            Text("₹%,d".format(totalRepayment.toLong()), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Quick Preset Schemes
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = cardBorder
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = com.example.ui.i18n.SakshamStrings.get("official_concessional_scheme_presets"),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                loanAmount = 140000.0
                                interestRate = 4.0
                                tenureYears = 3
                                moratoriumMonths = 4
                                onParametersChanged(loanAmount, interestRate, tenureYears, moratoriumMonths)
                            },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text("MSY (4%)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                loanAmount = 500000.0
                                interestRate = 6.0
                                tenureYears = 5
                                moratoriumMonths = 12
                                onParametersChanged(loanAmount, interestRate, tenureYears, moratoriumMonths)
                            },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text("Dairy (6%)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                loanAmount = 1000000.0
                                interestRate = 6.0
                                tenureYears = 7
                                moratoriumMonths = 12
                                onParametersChanged(loanAmount, interestRate, tenureYears, moratoriumMonths)
                            },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(com.example.ui.i18n.SakshamStrings.get("term_loan"), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                loanAmount = 1500000.0
                                interestRate = 4.0
                                tenureYears = 5
                                moratoriumMonths = 12
                                onParametersChanged(loanAmount, interestRate, tenureYears, moratoriumMonths)
                            },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(com.example.ui.i18n.SakshamStrings.get("edu_loan"), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Sliders & Inputs Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = cardBorder,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // 1. Loan Amount Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(com.example.ui.i18n.SakshamStrings.get("loan_amount_ऋण_राशि"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "₹%,d".format(loanAmount.toLong()),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                        )
                    }
                    Slider(
                        value = loanAmount.toFloat(),
                        onValueChange = {
                            loanAmount = it.toDouble()
                            onParametersChanged(loanAmount, interestRate, tenureYears, moratoriumMonths)
                        },
                        valueRange = 25000f..5000000f,
                        steps = 99,
                        colors = SliderDefaults.colors(
                            thumbColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            activeTrackColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. Interest Rate Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Interest Rate (% वार्षिक ब्याज):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "%.1f%% p.a.".format(interestRate),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = GrowthGreen
                        )
                    }
                    Slider(
                        value = interestRate.toFloat(),
                        onValueChange = {
                            interestRate = it.toDouble()
                            onParametersChanged(loanAmount, interestRate, tenureYears, moratoriumMonths)
                        },
                        valueRange = 3.5f..14.0f,
                        steps = 21,
                        colors = SliderDefaults.colors(thumbColor = GrowthGreen, activeTrackColor = GrowthGreen)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Loan Tenure Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(com.example.ui.i18n.SakshamStrings.get("repayment_tenure_पुनर्भुगतान_अवधि"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "$tenureYears Years (${tenureYears * 12} Months)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Slider(
                        value = tenureYears.toFloat(),
                        onValueChange = {
                            tenureYears = it.toInt()
                            onParametersChanged(loanAmount, interestRate, tenureYears, moratoriumMonths)
                        },
                        valueRange = 1f..10f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            activeTrackColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Moratorium Period Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(com.example.ui.i18n.SakshamStrings.get("moratorium_gestation_छूट_अवधि"),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "$moratoriumMonths Months",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaffronAccent
                        )
                    }
                    Slider(
                        value = moratoriumMonths.toFloat(),
                        onValueChange = {
                            moratoriumMonths = it.toInt()
                            onParametersChanged(loanAmount, interestRate, tenureYears, moratoriumMonths)
                        },
                        valueRange = 0f..24f,
                        steps = 23,
                        colors = SliderDefaults.colors(thumbColor = SaffronAccent, activeTrackColor = SaffronAccent)
                    )
                }
            }
        }

        // Educational Section on Moratorium Benefits
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF261D10) else SaffronLight.copy(alpha = 0.5f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, SaffronAccent.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SaffronAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassBottom,
                            contentDescription = null,
                            tint = SaffronAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(text = com.example.ui.i18n.SakshamStrings.get("how_the_moratorium_period_helps_you"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isDarkMode) SaffronAccent else GovBlueDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = com.example.ui.i18n.SakshamStrings.get("in_schemes_like_nsfdc_term_loan_and_dairy_entrepreneurship_you_receive_up_to_12_months_moratorium_gestation_period_during_these_first_12_months_you_do_not_have_to_pay_loan_emis\n\nthis_gives_you_breathing_space_to_purchase_milch_animals_construct_cattle_sheds_establish_daily_milk_procurement_and_generate_healthy_cash_flows_before_your_repayment_schedule_begins"),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}
