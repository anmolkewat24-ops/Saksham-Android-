package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChannelPartner
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

val partnerFilters = listOf("All Partners", "SCA (State Agency)", "Rural Banks (RRB)", "Public Sector Banks")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChannelPartnerLocatorScreen(
    partners: List<ChannelPartner>,
    savedPartnerIds: Set<String>,
    onToggleSave: (ChannelPartner) -> Unit,
    language: String = "English",
    isDarkMode: Boolean = false
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All Partners") }

    val filteredPartners = partners.filter { partner ->
        when (selectedFilter) {
            "SCA (State Agency)" -> partner.type.contains("SCA", ignoreCase = true)
            "Rural Banks (RRB)" -> partner.type.contains("RRB", ignoreCase = true) || partner.type.contains("Rural", ignoreCase = true)
            "Public Sector Banks" -> partner.type.contains("Public", ignoreCase = true)
            else -> true
        }
    }

    val cardBorder = androidx.compose.foundation.BorderStroke(
        1.dp,
        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("channel_partner_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Map Radar Visual Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = cardBorder,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = null,
                                tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Authorized Channel Partners Map",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("5 Nearby", color = GrowthGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stylized Geographic Radar Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDarkMode) Color(0xFF0F172A) else GovBlueLight)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val centerX = size.width / 2
                            val centerY = size.height / 2

                            // Concentric distance circles
                            drawCircle(
                                color = (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary).copy(alpha = 0.15f),
                                radius = size.height * 0.45f,
                                center = Offset(centerX, centerY)
                            )
                            drawCircle(
                                color = (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary).copy(alpha = 0.25f),
                                radius = size.height * 0.28f,
                                center = Offset(centerX, centerY)
                            )
                            drawCircle(
                                color = (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary).copy(alpha = 0.4f),
                                radius = size.height * 0.12f,
                                center = Offset(centerX, centerY)
                            )

                            // User center point
                            drawCircle(
                                color = GrowthGreen,
                                radius = 7.dp.toPx(),
                                center = Offset(centerX, centerY)
                            )

                            // Partner pin points
                            drawCircle(
                                color = GovBluePrimary,
                                radius = 6.dp.toPx(),
                                center = Offset(centerX - 60f, centerY - 25f)
                            )
                            drawCircle(
                                color = SaffronAccent,
                                radius = 6.dp.toPx(),
                                center = Offset(centerX + 80f, centerY + 30f)
                            )
                            drawCircle(
                                color = if (isDarkMode) Color(0xFF60A5FA) else GovBlueDark,
                                radius = 6.dp.toPx(),
                                center = Offset(centerX - 100f, centerY + 40f)
                            )
                        }

                        // Map Legend overlay
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 6.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isDarkMode) Color(0xFF1E293B).copy(alpha = 0.92f) else Color.White.copy(alpha = 0.9f)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                "🟢 You",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "🔵 SCA Office",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "🟠 Partner Banks",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Partner Filters
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(partnerFilters) { filter ->
                    val isSelected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) GovBluePrimary else MaterialTheme.colorScheme.surface)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) GovBluePrimary else if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = filter,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Partner Count Label
        item {
            Text(
                text = "Authorized Channel Partners (${filteredPartners.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        // Partner Cards List
        items(filteredPartners) { partner ->
            val isSaved = savedPartnerIds.contains(partner.id)
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                ChannelPartnerCard(
                    partner = partner,
                    isSaved = isSaved,
                    isDarkMode = isDarkMode,
                    onToggleSave = { onToggleSave(partner) },
                    onCall = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${partner.phone}"))
                        context.startActivity(intent)
                    },
                    onGetDirections = {
                        val gmmIntentUri = Uri.parse("geo:${partner.latitude},${partner.longitude}?q=${Uri.encode(partner.name)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        context.startActivity(mapIntent)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChannelPartnerCard(
    partner: ChannelPartner,
    isSaved: Boolean,
    isDarkMode: Boolean = false,
    onToggleSave: () -> Unit,
    onCall: () -> Unit,
    onGetDirections: () -> Unit
) {
    val cardBg = if (partner.isNearest) {
        if (isDarkMode) Color(0xFF16253B) else Color(0xFFF8FAFF)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (partner.isNearest) 2.dp else 1.dp,
            color = if (partner.isNearest) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else (if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("partner_card_${partner.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Badges row: Nearest / Type / Save button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (partner.isNearest) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "⭐ Nearest (${partner.distanceKm} km)",
                                color = GrowthGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueLight)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${partner.distanceKm} km away",
                                color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isDarkMode) Color(0xFF1E293B) else Color.White)
                            .border(
                                1.dp,
                                if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = partner.type,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Partner",
                        tint = if (isSaved) (if (isDarkMode) GovBluePrimaryDark else GovBluePrimary) else SlateLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Partner Name
            Text(
                text = partner.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            // Status / Category badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = partner.categoryBadge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                )
            }

            // Address & Hours
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = SlateLight,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = partner.address,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = SlateLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = partner.timing,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Available Loans Chips
            Text(
                text = "Available Schemes at this branch:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                partner.availableLoanCategories.forEach { category ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isDarkMode) Color(0xFF1E293B) else GovBlueLight.copy(alpha = 0.7f)
                            )
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = category,
                            fontSize = 10.sp,
                            color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Call & Get Directions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onCall,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("call_partner_${partner.id}"),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = GrowthGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Call Branch",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = onGetDirections,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("directions_partner_${partner.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Navigation, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Get Directions", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
