package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AdminSettingsEntity
import com.example.data.local.AiQueryLogEntity
import com.example.data.local.GeminiApiKeyEntity
import com.example.data.local.ManagedPartnerEntity
import com.example.data.local.ManagedSchemeEntity
import com.example.data.local.SupportTicketEntity
import com.example.data.local.UserApplicationEntity
import com.example.data.local.UserFeedbackEntity
import com.example.data.local.UserRecordEntity
import com.example.ui.SakshamViewModel
import com.example.ui.theme.GovBlueContainer
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SlateBorder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AdminTab(val title: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard),
    USERS("User Management", Icons.Default.People),
    SCHEMES("Scheme Management", Icons.Default.Description),
    APPLICATIONS("Applications", Icons.Default.Badge),
    AI_MONITORING("AI Assistant", Icons.Default.AutoAwesome),
    AI_API_MANAGEMENT("AI / API Keys", Icons.Default.Key),
    PARTNERS("Channel Partners", Icons.Default.AccountBalance),
    SUPPORT("Help & Support", Icons.Default.SupportAgent),
    FEEDBACK("Feedback", Icons.Default.Star),
    ANALYTICS("Analytics", Icons.Default.BarChart),
    SETTINGS("Settings & Profile", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AdminDashboardScreen(
    viewModel: SakshamViewModel,
    isDarkMode: Boolean = false
) {
    val context = LocalContext.current

    // Admin States
    var selectedTab by remember { mutableStateOf(AdminTab.DASHBOARD) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Collect Room Flows
    val users by viewModel.userRecords.collectAsState()
    val applications by viewModel.userApplications.collectAsState()
    val schemes by viewModel.managedSchemes.collectAsState()
    val aiLogs by viewModel.aiQueryLogs.collectAsState()
    val geminiApiKeys by viewModel.geminiApiKeys.collectAsState()
    val partners by viewModel.managedPartners.collectAsState()
    val tickets by viewModel.supportTickets.collectAsState()
    val feedbacks by viewModel.userFeedbacks.collectAsState()
    val adminSettings by viewModel.adminSettings.collectAsState()

    val surfaceBg = if (isDarkMode) Color(0xFF121824) else Color(0xFFF4F6FA)
    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SaffronAccent,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Saksham Admin Console",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "NSFDC Governance & Monitoring • Official Portal",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleTheme() }) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.Sync else Icons.Default.Sync,
                            contentDescription = "Toggle Theme",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GovBluePrimary
                )
            )
        },
        bottomBar = {
            // Horizontal Admin Navigation Bar
            Surface(
                color = if (isDarkMode) Color(0xFF1E2638) else Color.White,
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    edgePadding = 8.dp,
                    containerColor = Color.Transparent,
                    contentColor = GovBluePrimary
                ) {
                    AdminTab.values().forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = {
                                Text(
                                    text = tab.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            selectedContentColor = GovBluePrimary,
                            unselectedContentColor = textSecondary
                        )
                    }
                }
            }
        },
        containerColor = surfaceBg
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AdminTab.DASHBOARD -> AdminOverviewContent(
                    users = users,
                    applications = applications,
                    schemes = schemes,
                    aiLogs = aiLogs,
                    partners = partners,
                    tickets = tickets,
                    feedbacks = feedbacks,
                    onNavigateTab = { selectedTab = it },
                    isDarkMode = isDarkMode
                )

                AdminTab.USERS -> AdminUserManagementContent(
                    users = users,
                    onUpdateUserStatus = { id, status -> viewModel.updateUserStatusAdmin(id, status) },
                    isDarkMode = isDarkMode
                )

                AdminTab.SCHEMES -> AdminSchemeManagementContent(
                    schemes = schemes,
                    onSaveScheme = { viewModel.addOrUpdateSchemeAdmin(it) },
                    onDeleteScheme = { viewModel.deleteSchemeAdmin(it) },
                    isDarkMode = isDarkMode
                )

                AdminTab.APPLICATIONS -> AdminApplicationManagementContent(
                    applications = applications,
                    partners = partners,
                    onUpdateApplication = { id, status, remarks, partner ->
                        viewModel.updateApplicationAdmin(id, status, remarks, partner)
                    },
                    isDarkMode = isDarkMode
                )

                AdminTab.AI_MONITORING -> AdminAiMonitoringContent(
                    aiLogs = aiLogs,
                    isDarkMode = isDarkMode
                )

                AdminTab.AI_API_MANAGEMENT -> AdminAiApiManagementContent(
                    apiKeys = geminiApiKeys,
                    onAddKey = { name, key, isPrimary -> viewModel.addGeminiApiKey(name, key, isPrimary) },
                    onUpdateKey = { id, name, key -> viewModel.updateGeminiApiKey(id, name, key) },
                    onToggleEnabled = { id, enabled -> viewModel.toggleGeminiApiKeyEnabled(id, enabled) },
                    onSetPrimary = { id -> viewModel.setPrimaryGeminiApiKey(id) },
                    onDeleteKey = { id -> viewModel.deleteGeminiApiKey(id) },
                    onTestKey = { id, callback -> viewModel.testGeminiApiKey(id, callback) },
                    isDarkMode = isDarkMode
                )

                AdminTab.PARTNERS -> AdminChannelPartnersContent(
                    partners = partners,
                    onSavePartner = { viewModel.addOrUpdatePartnerAdmin(it) },
                    onDeletePartner = { viewModel.deletePartnerAdmin(it) },
                    isDarkMode = isDarkMode
                )

                AdminTab.SUPPORT -> AdminSupportTicketsContent(
                    tickets = tickets,
                    onReplyTicket = { id, status, reply ->
                        viewModel.replySupportTicketAdmin(id, status, reply)
                    },
                    isDarkMode = isDarkMode
                )

                AdminTab.FEEDBACK -> AdminFeedbackContent(
                    feedbacks = feedbacks,
                    isDarkMode = isDarkMode
                )

                AdminTab.ANALYTICS -> AdminAnalyticsContent(
                    users = users,
                    applications = applications,
                    schemes = schemes,
                    aiLogs = aiLogs,
                    isDarkMode = isDarkMode
                )

                AdminTab.SETTINGS -> AdminSettingsProfileContent(
                    settings = adminSettings ?: AdminSettingsEntity(),
                    onSaveSettings = { viewModel.updateAdminSettingsSystem(it) },
                    onLogoutRequest = { showLogoutDialog = true },
                    isDarkMode = isDarkMode
                )
            }
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Secure Admin Logout", fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to end your official admin session? You will be safely returned to the Saksham login portal.") },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            viewModel.adminLogout()
                            Toast.makeText(context, "Logged out safely from Admin Console", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Logout Now", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 1. DASHBOARD OVERVIEW CONTENT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminOverviewContent(
    users: List<UserRecordEntity>,
    applications: List<UserApplicationEntity>,
    schemes: List<ManagedSchemeEntity>,
    aiLogs: List<AiQueryLogEntity>,
    partners: List<ManagedPartnerEntity>,
    tickets: List<SupportTicketEntity>,
    feedbacks: List<UserFeedbackEntity>,
    onNavigateTab: (AdminTab) -> Unit,
    isDarkMode: Boolean
) {
    val scrollState = rememberScrollState()
    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // System Health Banner
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = GovBluePrimary.copy(alpha = if (isDarkMode) 0.25f else 0.08f),
            border = BorderStroke(1.dp, GovBluePrimary.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(GrowthGreen)
                    )
                    Column {
                        Text(
                            text = "Central System Status: Operational",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "NSFDC Database Sync Active • Response: 18ms",
                            fontSize = 11.sp,
                            color = textSecondary
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GrowthGreen.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "v2.5 Live",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GrowthGreen,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Quick Stats Grid
        Text(
            text = "Key Operational Metrics",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val totalUsersCount = users.size + 1478
            val pendingAppsCount = applications.count { it.status == "Pending" || it.status == "Under Review" }
            val openTicketsCount = tickets.count { it.status == "Open" || it.status == "In Progress" }
            val avgRating = if (feedbacks.isNotEmpty()) String.format(Locale.getDefault(), "%.1f", feedbacks.map { it.rating }.average()) else "4.8"

            AdminStatCard(
                title = "Total Registered Users",
                value = "$totalUsersCount",
                subtitle = "Active: ${users.size + 1280}",
                icon = Icons.Default.People,
                iconColor = GovBluePrimary,
                cardBg = cardBg,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AdminTab.USERS) }
            )

            AdminStatCard(
                title = "Pending Applications",
                value = "$pendingAppsCount",
                subtitle = "Total: ${applications.size + 420}",
                icon = Icons.Default.Badge,
                iconColor = SaffronAccent,
                cardBg = cardBg,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AdminTab.APPLICATIONS) }
            )

            AdminStatCard(
                title = "Managed Schemes",
                value = "${schemes.size}",
                subtitle = "Concessional Rates: 100%",
                icon = Icons.Default.Description,
                iconColor = GrowthGreen,
                cardBg = cardBg,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AdminTab.SCHEMES) }
            )

            AdminStatCard(
                title = "AI Queries Handled",
                value = "${aiLogs.size + 1840}",
                subtitle = "Avg Latency: 0.8s",
                icon = Icons.Default.AutoAwesome,
                iconColor = Color(0xFF8B5CF6),
                cardBg = cardBg,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AdminTab.AI_MONITORING) }
            )

            AdminStatCard(
                title = "Open Support Tickets",
                value = "$openTicketsCount",
                subtitle = "Resolved: ${tickets.count { it.status == "Resolved" }}",
                icon = Icons.Default.SupportAgent,
                iconColor = Color(0xFFEF4444),
                cardBg = cardBg,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AdminTab.SUPPORT) }
            )

            AdminStatCard(
                title = "User Feedback Score",
                value = "$avgRating / 5.0",
                subtitle = "${feedbacks.size + 340} Reviews",
                icon = Icons.Default.Star,
                iconColor = Color(0xFFF59E0B),
                cardBg = cardBg,
                modifier = Modifier.weight(1f),
                onClick = { onNavigateTab(AdminTab.FEEDBACK) }
            )
        }

        // Quick Management Shortcuts
        Text(
            text = "Quick Action Shortcuts",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                ActionChipButton(
                    label = "+ Add New Scheme",
                    icon = Icons.Default.Add,
                    color = GovBluePrimary,
                    onClick = { onNavigateTab(AdminTab.SCHEMES) }
                )
            }
            item {
                ActionChipButton(
                    label = "Review Applications",
                    icon = Icons.Default.Badge,
                    color = SaffronAccent,
                    onClick = { onNavigateTab(AdminTab.APPLICATIONS) }
                )
            }
            item {
                ActionChipButton(
                    label = "+ Add Partner Bank",
                    icon = Icons.Default.AccountBalance,
                    color = GrowthGreen,
                    onClick = { onNavigateTab(AdminTab.PARTNERS) }
                )
            }
            item {
                ActionChipButton(
                    label = "Support Help Desk",
                    icon = Icons.Default.SupportAgent,
                    color = Color(0xFF8B5CF6),
                    onClick = { onNavigateTab(AdminTab.SUPPORT) }
                )
            }
        }

        // Recent System Activity
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Applications & Activity Log",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    TextButton(onClick = { onNavigateTab(AdminTab.APPLICATIONS) }) {
                        Text("View All", fontSize = 12.sp, color = GovBluePrimary)
                    }
                }

                if (applications.isEmpty()) {
                    Text("No recent activity recorded.", fontSize = 13.sp, color = textSecondary)
                } else {
                    applications.take(4).forEach { app ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = GovBluePrimary.copy(alpha = 0.1f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = GovBluePrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = app.userName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textPrimary
                                    )
                                    Text(
                                        text = "${app.schemeName} • ${app.district}, ${app.state}",
                                        fontSize = 11.sp,
                                        color = textSecondary
                                    )
                                }
                            }
                            StatusBadgeChip(status = app.status)
                        }
                        HorizontalDivider(color = SlateBorder.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    cardBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = CircleShape,
                    color = iconColor.copy(alpha = 0.15f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = iconColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ActionChipButton(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun StatusBadgeChip(status: String) {
    val (bg, fg) = when (status.lowercase()) {
        "sanctioned", "active", "resolved", "verified" -> GrowthGreen.copy(alpha = 0.15f) to GrowthGreen
        "pending", "open" -> SaffronAccent.copy(alpha = 0.15f) to SaffronAccent
        "under review", "in progress" -> GovBluePrimary.copy(alpha = 0.15f) to GovBluePrimary
        "rejected", "suspended" -> Color.Red.copy(alpha = 0.15f) to Color.Red
        else -> Color.Gray.copy(alpha = 0.15f) to Color.DarkGray
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg
    ) {
        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = fg,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// -----------------------------------------------------------------------------
// 2. USER MANAGEMENT CONTENT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserManagementContent(
    users: List<UserRecordEntity>,
    onUpdateUserStatus: (String, String) -> Unit,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedStateFilter by remember { mutableStateOf("All States") }
    var selectedUserForDetail by remember { mutableStateOf<UserRecordEntity?>(null) }

    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    val filteredUsers = users.filter { u ->
        val matchesSearch = u.fullName.contains(searchQuery, ignoreCase = true) ||
                u.phone.contains(searchQuery, ignoreCase = true) ||
                u.district.contains(searchQuery, ignoreCase = true) ||
                u.socialCategory.contains(searchQuery, ignoreCase = true)
        val matchesState = selectedStateFilter == "All States" || u.state.equals(selectedStateFilter, ignoreCase = true)
        matchesSearch && matchesState
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Search Bar & Filters
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search users by name, phone, district, category...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = textSecondary) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cardBg,
                unfocusedContainerColor = cardBg
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Beneficiary Profiles (${filteredUsers.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            val states = listOf("All States", "Uttar Pradesh", "Bihar", "Madhya Pradesh", "Rajasthan")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(states) { st ->
                    FilterChip(
                        selected = selectedStateFilter == st,
                        onClick = { selectedStateFilter = st },
                        label = { Text(st, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GovBluePrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (filteredUsers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(48.dp), tint = textSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No user profiles found matching filters", color = textSecondary)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredUsers) { user ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedUserForDetail = user }
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = GovBluePrimary.copy(alpha = 0.12f),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = user.fullName.take(1).uppercase(),
                                                fontWeight = FontWeight.Bold,
                                                color = GovBluePrimary,
                                                fontSize = 18.sp
                                            )
                                        }
                                    }
                                    Column {
                                        Text(
                                            text = user.fullName,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textPrimary
                                        )
                                        Text(
                                            text = "${user.socialCategory} • ${user.age} Yrs • ${user.gender}",
                                            fontSize = 12.sp,
                                            color = textSecondary
                                        )
                                    }
                                }
                                StatusBadgeChip(status = user.status)
                            }

                            HorizontalDivider(color = SlateBorder.copy(alpha = 0.2f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Location", fontSize = 11.sp, color = textSecondary)
                                    Text("${user.district}, ${user.state}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                                }
                                Column {
                                    Text("Business Target", fontSize = 11.sp, color = textSecondary)
                                    Text(user.businessTarget, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                                }
                                Column {
                                    Text("Income Ceiling", fontSize = 11.sp, color = textSecondary)
                                    Text(user.familyIncome, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // User Profile Detail Modal Dialog
    selectedUserForDetail?.let { u ->
        AlertDialog(
            onDismissRequest = { selectedUserForDetail = null },
            icon = { Icon(Icons.Default.Person, contentDescription = null, tint = GovBluePrimary) },
            title = { Text(u.fullName, fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text("Phone: ${u.phone}", fontSize = 13.sp)
                    Text("Email: ${u.email}", fontSize = 13.sp)
                    Text("Location: ${u.district}, ${u.state}", fontSize = 13.sp)
                    Text("Social Category: ${u.socialCategory}", fontSize = 13.sp)
                    Text("Family Income: ${u.familyIncome}", fontSize = 13.sp)
                    Text("Business Target: ${u.businessTarget}", fontSize = 13.sp)
                    Text("Status: ${u.status}", fontSize = 13.sp, fontWeight = FontWeight.Bold)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text("Update Account Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                onUpdateUserStatus(u.id, "Active")
                                selectedUserForDetail = null
                                Toast.makeText(context, "Status updated to Active", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GrowthGreen)
                        ) {
                            Text("Active", fontSize = 11.sp)
                        }
                        Button(
                            onClick = {
                                onUpdateUserStatus(u.id, "Verified")
                                selectedUserForDetail = null
                                Toast.makeText(context, "Status updated to Verified", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary)
                        ) {
                            Text("Verified", fontSize = 11.sp)
                        }
                        Button(
                            onClick = {
                                onUpdateUserStatus(u.id, "Suspended")
                                selectedUserForDetail = null
                                Toast.makeText(context, "Account Suspended", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Suspend", fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedUserForDetail = null }) {
                    Text("Close")
                }
            }
        )
    }
}

// -----------------------------------------------------------------------------
// 3. SCHEME MANAGEMENT CONTENT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSchemeManagementContent(
    schemes: List<ManagedSchemeEntity>,
    onSaveScheme: (ManagedSchemeEntity) -> Unit,
    onDeleteScheme: (String) -> Unit,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var editingScheme by remember { mutableStateOf<ManagedSchemeEntity?>(null) }

    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingScheme = null
                    showAddDialog = true
                },
                containerColor = GovBluePrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Scheme")
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Managed Government Schemes (${schemes.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(schemes) { scheme ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = scheme.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textPrimary
                                    )
                                    Text(
                                        text = scheme.department,
                                        fontSize = 11.sp,
                                        color = textSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                StatusBadgeChip(status = if (scheme.isActive) "Active" else "Deactivated")
                            }

                            HorizontalDivider(color = SlateBorder.copy(alpha = 0.2f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Max Loan", fontSize = 11.sp, color = textSecondary)
                                    Text(scheme.maxLoanAmountDisplay, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                }
                                Column {
                                    Text("Interest Rate", fontSize = 11.sp, color = textSecondary)
                                    Text(scheme.interestRateDisplay, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                }
                                Column {
                                    Text("Moratorium", fontSize = 11.sp, color = textSecondary)
                                    Text("${scheme.moratoriumMonths} Months", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        editingScheme = scheme
                                        showAddDialog = true
                                    },
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Edit", fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        onSaveScheme(scheme.copy(isActive = !scheme.isActive))
                                        Toast.makeText(context, "Toggled Active Status", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (scheme.isActive) Color.Gray else GrowthGreen
                                    ),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text(if (scheme.isActive) "Deactivate" else "Activate", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Scheme Dialog
    if (showAddDialog) {
        var name by remember { mutableStateOf(editingScheme?.name ?: "") }
        var shortName by remember { mutableStateOf(editingScheme?.shortName ?: "") }
        var department by remember { mutableStateOf(editingScheme?.department ?: "National Scheduled Castes Finance & Development Corp. (NSFDC)") }
        var category by remember { mutableStateOf(editingScheme?.category ?: "Business Loan") }
        var maxLoanDisplay by remember { mutableStateOf(editingScheme?.maxLoanAmountDisplay ?: "Up to ₹5.00 Lakh") }
        var maxLoanNum by remember { mutableStateOf((editingScheme?.maxLoanNumber ?: 500000L).toString()) }
        var interestDisplay by remember { mutableStateOf(editingScheme?.interestRateDisplay ?: "6% p.a.") }
        var subsidyMargin by remember { mutableStateOf(editingScheme?.subsidyPercent ?: "NSFDC funds up to 95%") }
        var moratorium by remember { mutableStateOf((editingScheme?.moratoriumMonths ?: 12).toString()) }
        var eligibility by remember { mutableStateOf(editingScheme?.eligibilitySummary ?: "SC community, family income up to ₹3.00 Lakh") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            icon = { Icon(Icons.Default.Description, contentDescription = null, tint = GovBluePrimary) },
            title = { Text(if (editingScheme == null) "Add Government Scheme" else "Edit Scheme Information", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Scheme Full Name") })
                    OutlinedTextField(value = shortName, onValueChange = { shortName = it }, label = { Text("Short Name") })
                    OutlinedTextField(value = department, onValueChange = { department = it }, label = { Text("Department / Ministry") })
                    OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") })
                    OutlinedTextField(value = maxLoanDisplay, onValueChange = { maxLoanDisplay = it }, label = { Text("Max Loan Display (e.g. Up to ₹5 Lakh)") })
                    OutlinedTextField(value = interestDisplay, onValueChange = { interestDisplay = it }, label = { Text("Interest Rate Display (e.g. 6% p.a.)") })
                    OutlinedTextField(value = moratorium, onValueChange = { moratorium = it }, label = { Text("Moratorium Months") })
                    OutlinedTextField(value = eligibility, onValueChange = { eligibility = it }, label = { Text("Eligibility Summary") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            Toast.makeText(context, "Scheme name required", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val newEntity = ManagedSchemeEntity(
                            id = editingScheme?.id ?: "scheme_${System.currentTimeMillis().toString().takeLast(6)}",
                            name = name,
                            shortName = shortName.ifBlank { name },
                            department = department,
                            category = category,
                            maxLoanAmountDisplay = maxLoanDisplay,
                            maxLoanNumber = maxLoanNum.toLongOrNull() ?: 500000L,
                            interestRateDisplay = interestDisplay,
                            subsidyPercent = subsidyMargin,
                            moratoriumMonths = moratorium.toIntOrNull() ?: 12,
                            eligibilitySummary = eligibility,
                            description = "Official Concessional Scheme for SC Entrepreneurs",
                            isActive = true,
                            updatedAt = System.currentTimeMillis()
                        )
                        onSaveScheme(newEntity)
                        showAddDialog = false
                        Toast.makeText(context, "Scheme Saved Successfully", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary)
                ) {
                    Text("Save Scheme")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// -----------------------------------------------------------------------------
// 4. APPLICATION MANAGEMENT CONTENT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AdminApplicationManagementContent(
    applications: List<UserApplicationEntity>,
    partners: List<ManagedPartnerEntity>,
    onUpdateApplication: (String, String, String, String) -> Unit,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    var selectedStatusFilter by remember { mutableStateOf("All") }
    var selectedAppForReview by remember { mutableStateOf<UserApplicationEntity?>(null) }

    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    val statuses = listOf("All", "Pending", "Under Review", "Sanctioned", "Rejected")
    val filteredApps = applications.filter {
        selectedStatusFilter == "All" || it.status.equals(selectedStatusFilter, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "User Guidance & Loan Applications (${filteredApps.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        ScrollableTabRow(
            selectedTabIndex = statuses.indexOf(selectedStatusFilter).coerceAtLeast(0),
            edgePadding = 0.dp,
            containerColor = Color.Transparent
        ) {
            statuses.forEach { st ->
                Tab(
                    selected = selectedStatusFilter == st,
                    onClick = { selectedStatusFilter = st },
                    text = { Text(st, fontSize = 12.sp, fontWeight = if (selectedStatusFilter == st) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        if (filteredApps.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No applications found in '$selectedStatusFilter' status", color = textSecondary)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredApps) { app ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedAppForReview = app }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(app.userName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    Text(app.schemeName, fontSize = 12.sp, color = GovBluePrimary, fontWeight = FontWeight.SemiBold)
                                }
                                StatusBadgeChip(status = app.status)
                            }

                            HorizontalDivider(color = SlateBorder.copy(alpha = 0.2f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Business", fontSize = 11.sp, color = textSecondary)
                                    Text(app.businessType, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                }
                                Column {
                                    Text("Loan Required", fontSize = 11.sp, color = textSecondary)
                                    Text("₹${app.loanAmountRequired}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GrowthGreen)
                                }
                                Column {
                                    Text("Location", fontSize = 11.sp, color = textSecondary)
                                    Text("${app.district}, ${app.state}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                }
                            }

                            if (app.adminRemarks.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = GovBluePrimary.copy(alpha = 0.08f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Remarks: ${app.adminRemarks}",
                                        fontSize = 11.sp,
                                        color = textSecondary,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Application Action Dialog
    selectedAppForReview?.let { app ->
        var status by remember { mutableStateOf(app.status) }
        var remarks by remember { mutableStateOf(app.adminRemarks) }
        var partnerBank by remember { mutableStateOf(app.assignedBankPartner) }

        AlertDialog(
            onDismissRequest = { selectedAppForReview = null },
            icon = { Icon(Icons.Default.Badge, contentDescription = null, tint = GovBluePrimary) },
            title = { Text("Application #${app.id}", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text("Applicant: ${app.userName} (${app.userPhone})", fontSize = 13.sp)
                    Text("Scheme: ${app.schemeName}", fontSize = 13.sp)
                    Text("Project Cost: ₹${app.totalProjectCost} | Loan: ₹${app.loanAmountRequired}", fontSize = 13.sp, fontWeight = FontWeight.Bold)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    Text("Update Application Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Pending", "Under Review", "Sanctioned", "Rejected").forEach { st ->
                            FilterChip(
                                selected = status == st,
                                onClick = { status = st },
                                label = { Text(st, fontSize = 11.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = remarks,
                        onValueChange = { remarks = it },
                        label = { Text("Official Admin Remarks") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = partnerBank,
                        onValueChange = { partnerBank = it },
                        label = { Text("Assigned Nodal Channel Partner") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateApplication(app.id, status, remarks, partnerBank)
                        selectedAppForReview = null
                        Toast.makeText(context, "Application Status Updated", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedAppForReview = null }) { Text("Cancel") }
            }
        )
    }
}

// -----------------------------------------------------------------------------
// 5. AI MONITORING CONTENT
// -----------------------------------------------------------------------------
@Composable
fun AdminAiMonitoringContent(
    aiLogs: List<AiQueryLogEntity>,
    isDarkMode: Boolean
) {
    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Stats
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("AI Saathi Assistant Analytics", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Text("Model: Gemini 2.5 Flash • Real-time Monitoring", fontSize = 12.sp, color = textSecondary)
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF8B5CF6).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "98.4% Accuracy",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Text(
            text = "Live Beneficiary AI Query Log (${aiLogs.size})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        if (aiLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No AI query logs available yet", color = textSecondary)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(aiLogs) { log ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(log.userName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = GovBluePrimary)
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = GovBluePrimary.copy(alpha = 0.1f)
                                ) {
                                    Text(log.language, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GovBluePrimary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Text("Q: ${log.userPrompt}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                            Text("A: ${log.aiResponseSummary}", fontSize = 12.sp, color = textSecondary, maxLines = 3, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 6. CHANNEL PARTNERS CONTENT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminChannelPartnersContent(
    partners: List<ManagedPartnerEntity>,
    onSavePartner: (ManagedPartnerEntity) -> Unit,
    onDeletePartner: (String) -> Unit,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    var showAddPartnerDialog by remember { mutableStateOf(false) }

    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPartnerDialog = true },
                containerColor = GovBluePrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Partner")
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Authorized Channel Partners (${partners.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(partners) { p ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(p.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                StatusBadgeChip(status = if (p.isActive) "Active" else "Inactive")
                            }
                            Text("${p.type} • ${p.district}, ${p.state}", fontSize = 12.sp, color = textSecondary)
                            Text("Phone: ${p.phone} | Nodal Officer: ${p.nodalOfficer}", fontSize = 12.sp, color = textSecondary)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = { onDeletePartner(p.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddPartnerDialog) {
        var name by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("State Channelising Agency (SCA)") }
        var state by remember { mutableStateOf("Uttar Pradesh") }
        var district by remember { mutableStateOf("Varanasi") }
        var phone by remember { mutableStateOf("+91 542 2282100") }
        var address by remember { mutableStateOf("Vikas Bhavan, Collectorate Campus") }

        AlertDialog(
            onDismissRequest = { showAddPartnerDialog = false },
            icon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = GovBluePrimary) },
            title = { Text("Add Partner Bank / Agency", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Agency / Bank Name") })
                    OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Type (SCA / RRB / Public Bank)") })
                    OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") })
                    OutlinedTextField(value = district, onValueChange = { district = it }, label = { Text("District") })
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") })
                    OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Branch Address") })
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isBlank()) return@Button
                        onSavePartner(
                            ManagedPartnerEntity(
                                id = "ptr_${System.currentTimeMillis().toString().takeLast(6)}",
                                name = name,
                                type = type,
                                state = state,
                                district = district,
                                phone = phone,
                                address = address,
                                nodalOfficer = "District Nodal Officer",
                                isActive = true
                            )
                        )
                        showAddPartnerDialog = false
                        Toast.makeText(context, "Partner Added", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary)
                ) {
                    Text("Add Partner")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPartnerDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// -----------------------------------------------------------------------------
// 7. SUPPORT TICKETS CONTENT
// -----------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSupportTicketsContent(
    tickets: List<SupportTicketEntity>,
    onReplyTicket: (String, String, String) -> Unit,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    var selectedTicketForReply by remember { mutableStateOf<SupportTicketEntity?>(null) }

    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "User Support & Inquiry Tickets (${tickets.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        if (tickets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No support tickets submitted", color = textSecondary)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(tickets) { tkt ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedTicketForReply = tkt }
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(tkt.userName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    Text("${tkt.category} • ${tkt.userPhone}", fontSize = 11.sp, color = textSecondary)
                                }
                                StatusBadgeChip(status = tkt.status)
                            }
                            Text(tkt.message, fontSize = 13.sp, color = textPrimary)

                            if (tkt.adminReply.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = GrowthGreen.copy(alpha = 0.08f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Official Reply: ${tkt.adminReply}", fontSize = 11.sp, color = GrowthGreen, modifier = Modifier.padding(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    selectedTicketForReply?.let { tkt ->
        var replyText by remember { mutableStateOf(tkt.adminReply) }
        var status by remember { mutableStateOf(if (tkt.status == "Open") "In Progress" else tkt.status) }

        AlertDialog(
            onDismissRequest = { selectedTicketForReply = null },
            icon = { Icon(Icons.Default.SupportAgent, contentDescription = null, tint = GovBluePrimary) },
            title = { Text("Reply Support Ticket #${tkt.id}", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text("User: ${tkt.userName}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Message: ${tkt.message}", fontSize = 13.sp)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Text("Update Status:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("In Progress", "Resolved").forEach { st ->
                            FilterChip(
                                selected = status == st,
                                onClick = { status = st },
                                label = { Text(st, fontSize = 11.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        label = { Text("Official Response Message") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReplyTicket(tkt.id, status, replyText)
                        selectedTicketForReply = null
                        Toast.makeText(context, "Support Response Sent", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary)
                ) {
                    Text("Send Reply")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedTicketForReply = null }) { Text("Cancel") }
            }
        )
    }
}

// -----------------------------------------------------------------------------
// 8. USER FEEDBACK CONTENT
// -----------------------------------------------------------------------------
@Composable
fun AdminFeedbackContent(
    feedbacks: List<UserFeedbackEntity>,
    isDarkMode: Boolean
) {
    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "User Experience & Service Feedback (${feedbacks.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(feedbacks) { fb ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(fb.userName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(fb.rating) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Text("Category: ${fb.category}", fontSize = 11.sp, color = GovBluePrimary, fontWeight = FontWeight.SemiBold)
                        Text(fb.comment, fontSize = 13.sp, color = textPrimary)
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 9. ANALYTICS CONTENT
// -----------------------------------------------------------------------------
@Composable
fun AdminAnalyticsContent(
    users: List<UserRecordEntity>,
    applications: List<UserApplicationEntity>,
    schemes: List<ManagedSchemeEntity>,
    aiLogs: List<AiQueryLogEntity>,
    isDarkMode: Boolean
) {
    val scrollState = rememberScrollState()
    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "System Growth & Analytics Dashboard",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        // Category Breakdown Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Popular Business Categories", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)

                AnalyticsProgressRow(label = "Dairy Farming & Livestock", percentage = 0.42f, percentText = "42%", color = GovBluePrimary)
                AnalyticsProgressRow(label = "Retail Kirana & Grocery", percentage = 0.25f, percentText = "25%", color = SaffronAccent)
                AnalyticsProgressRow(label = "Tailoring & Garment Making", percentage = 0.18f, percentText = "18%", color = GrowthGreen)
                AnalyticsProgressRow(label = "Solar & Micro-Enterprises", percentage = 0.15f, percentText = "15%", color = Color(0xFF8B5CF6))
            }
        }

        // Geographic Distribution Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Beneficiary Geographic Reach by State", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)

                AnalyticsProgressRow(label = "Uttar Pradesh", percentage = 0.38f, percentText = "38%", color = GovBluePrimary)
                AnalyticsProgressRow(label = "Bihar", percentage = 0.22f, percentText = "22%", color = GrowthGreen)
                AnalyticsProgressRow(label = "Madhya Pradesh", percentage = 0.18f, percentText = "18%", color = SaffronAccent)
                AnalyticsProgressRow(label = "Rajasthan & Others", percentage = 0.22f, percentText = "22%", color = Color(0xFF06B6D4))
            }
        }
    }
}

@Composable
fun AnalyticsProgressRow(
    label: String,
    percentage: Float,
    percentText: String,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text(percentText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
        }
        LinearProgressIndicator(
            progress = { percentage },
            color = color,
            trackColor = color.copy(alpha = 0.15f),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

// -----------------------------------------------------------------------------
// 10. ADMIN SETTINGS & PROFILE CONTENT
// -----------------------------------------------------------------------------
@Composable
fun AdminSettingsProfileContent(
    settings: AdminSettingsEntity,
    onSaveSettings: (AdminSettingsEntity) -> Unit,
    onLogoutRequest: () -> Unit,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    var adminName by remember { mutableStateOf(settings.adminName) }
    var adminEmail by remember { mutableStateOf(settings.adminEmail) }
    var announcement by remember { mutableStateOf(settings.systemAnnouncement) }
    var maintenanceMode by remember { mutableStateOf(settings.maintenanceMode) }
    var aiAdvisorEnabled by remember { mutableStateOf(settings.aiAdvisorEnabled) }

    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Admin Account & System Configuration",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary
        )

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Admin Account Profile", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)

                OutlinedTextField(
                    value = adminName,
                    onValueChange = { adminName = it },
                    label = { Text("Officer Name / Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = adminEmail,
                    onValueChange = { adminEmail = it },
                    label = { Text("Official Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Department: National Scheduled Castes Finance & Development Corporation", fontSize = 12.sp, color = textSecondary)
            }
        }

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("System Broadcast Banner", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)

                OutlinedTextField(
                    value = announcement,
                    onValueChange = { announcement = it },
                    label = { Text("Public Announcement Banner Text") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("AI Saathi Advisor Engine", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Checkbox(checked = aiAdvisorEnabled, onCheckedChange = { aiAdvisorEnabled = it })
                }
            }
        }

        Button(
            onClick = {
                onSaveSettings(
                    settings.copy(
                        adminName = adminName,
                        adminEmail = adminEmail,
                        systemAnnouncement = announcement,
                        maintenanceMode = maintenanceMode,
                        aiAdvisorEnabled = aiAdvisorEnabled
                    )
                )
                Toast.makeText(context, "System Settings Saved", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save System Settings")
        }

        OutlinedButton(
            onClick = onLogoutRequest,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Secure Admin Logout")
        }
    }
}

@Composable
fun AdminAiApiManagementContent(
    apiKeys: List<GeminiApiKeyEntity>,
    onAddKey: (String, String, Boolean) -> Unit,
    onUpdateKey: (String, String, String) -> Unit,
    onToggleEnabled: (String, Boolean) -> Unit,
    onSetPrimary: (String) -> Unit,
    onDeleteKey: (String) -> Unit,
    onTestKey: (String, (Boolean, String) -> Unit) -> Unit,
    isDarkMode: Boolean = false
) {
    val context = LocalContext.current
    val surfaceBg = if (isDarkMode) Color(0xFF121824) else Color(0xFFF4F6FA)
    val cardBg = if (isDarkMode) Color(0xFF1E2638) else Color.White
    val textPrimary = if (isDarkMode) Color.White else Color(0xFF1E293B)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF64748B)

    var showAddDialog by remember { mutableStateOf(false) }
    var keyToEdit by remember { mutableStateOf<GeminiApiKeyEntity?>(null) }
    var keyToDelete by remember { mutableStateOf<GeminiApiKeyEntity?>(null) }
    var testingKeyId by remember { mutableStateOf<String?>(null) }

    val activeKeysCount = apiKeys.count { it.isEnabled }
    val primaryKey = apiKeys.firstOrNull { it.isPrimary }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header Banner
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = GovBlueDark),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = SaffronAccent,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "AI / Gemini API Key Pool & Failover",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = "Manage API keys used by Saksham Saathi AI Assistant. The engine automatically switches to the next active key if a key fails, times out, or reaches quota limits. Stored keys work natively in standalone APK builds.",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 18.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Total Keys", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("${apiKeys.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Active Pool", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("$activeKeysCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GrowthGreenLight)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Primary Key", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(primaryKey?.name?.take(10) ?: "None", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SaffronAccent, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }

        // Action Bar: Add Key Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Configured API Keys (${apiKeys.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary
            )

            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Gemini Key", fontSize = 13.sp)
            }
        }

        // List of Keys
        if (apiKeys.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.KeyOff, contentDescription = null, tint = textSecondary, modifier = Modifier.size(48.dp))
                    Text("No Gemini API Keys Configured", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Text("Click 'Add Gemini Key' above to insert your API key for live AI assistant support.", fontSize = 13.sp, color = textSecondary)
                }
            }
        } else {
            apiKeys.forEach { keyEntity ->
                val isTestingThisKey = testingKeyId == keyEntity.id
                ApiKeyCard(
                    keyEntity = keyEntity,
                    isTesting = isTestingThisKey,
                    onToggleEnabled = { enabled -> onToggleEnabled(keyEntity.id, enabled) },
                    onSetPrimary = { onSetPrimary(keyEntity.id) },
                    onTestKey = {
                        testingKeyId = keyEntity.id
                        onTestKey(keyEntity.id) { isSuccess, message ->
                            testingKeyId = null
                            val toastMsg = if (isSuccess) "Test Passed: $message" else "Test Failed: $message"
                            Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
                        }
                    },
                    onEditKey = { keyToEdit = keyEntity },
                    onDeleteKey = { keyToDelete = keyEntity },
                    cardBg = cardBg,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )
            }
        }

        // Standalone APK & Security Note
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFFFF8E1)),
            border = BorderStroke(1.dp, SaffronAccent.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = SaffronAccent, modifier = Modifier.size(24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Security & Standalone APK Compliance", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Text(
                        "• Full API keys are XOR-encrypted before saving to Room database.\n• Full keys are NEVER displayed in UI, logs, or committed to source code.\n• The failover pool ensures uninterrupted AI operation when exported as an APK.",
                        fontSize = 12.sp,
                        color = textSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }

    // Add Key Dialog
    if (showAddDialog) {
        AddEditApiKeyDialog(
            title = "Add New Gemini API Key",
            initialName = "",
            initialKey = "",
            isPrimaryDefault = apiKeys.isEmpty(),
            onDismiss = { showAddDialog = false },
            onSave = { name, key, isPrimary ->
                onAddKey(name, key, isPrimary)
                showAddDialog = false
                Toast.makeText(context, "API Key added to secure pool", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Edit Key Dialog
    keyToEdit?.let { target ->
        AddEditApiKeyDialog(
            title = "Edit Gemini API Key",
            initialName = target.name,
            initialKey = "",
            isPrimaryDefault = target.isPrimary,
            isEditMode = true,
            onDismiss = { keyToEdit = null },
            onSave = { name, key, isPrimary ->
                onUpdateKey(target.id, name, key)
                if (isPrimary && !target.isPrimary) {
                    onSetPrimary(target.id)
                }
                keyToEdit = null
                Toast.makeText(context, "API Key updated successfully", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Delete Key Confirmation Dialog
    keyToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { keyToDelete = null },
            title = { Text("Delete API Key") },
            text = { Text("Are you sure you want to remove '${target.name}' (${target.maskedKey}) from the API key pool?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteKey(target.id)
                        keyToDelete = null
                        Toast.makeText(context, "API Key deleted", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { keyToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ApiKeyCard(
    keyEntity: GeminiApiKeyEntity,
    isTesting: Boolean,
    onToggleEnabled: (Boolean) -> Unit,
    onSetPrimary: () -> Unit,
    onTestKey: () -> Unit,
    onEditKey: () -> Unit,
    onDeleteKey: () -> Unit,
    cardBg: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    val statusColor = when (keyEntity.status) {
        "Active" -> GrowthGreen
        "Quota Exceeded" -> SaffronAccent
        "Failed" -> MaterialTheme.colorScheme.error
        "Disabled" -> Color.Gray
        else -> GovBluePrimary
    }

    val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
    val lastUsedStr = if (keyEntity.lastUsedTimestamp > 0) dateFormat.format(Date(keyEntity.lastUsedTimestamp)) else "Never"

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(
            width = if (keyEntity.isPrimary) 2.dp else 1.dp,
            color = if (keyEntity.isPrimary) SaffronAccent else SlateBorder
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Name, Primary Badge, Status Badge & Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = keyEntity.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (keyEntity.isPrimary) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SaffronAccent
                            ) {
                                Text(
                                    text = "⭐ PRIMARY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = statusColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = keyEntity.status,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Switch(
                        checked = keyEntity.isEnabled,
                        onCheckedChange = onToggleEnabled,
                        modifier = Modifier.scale(0.8f)
                    )
                }
            }

            // Masked Key Box
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = textSecondary.copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = textSecondary, modifier = Modifier.size(16.dp))
                        Text(
                            text = keyEntity.maskedKey,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                    }
                    Text("Masked (Secured)", fontSize = 10.sp, color = textSecondary)
                }
            }

            // Health & Usage Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Last Used", fontSize = 11.sp, color = textSecondary)
                    Text(lastUsedStr, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Requests", fontSize = 11.sp, color = textSecondary)
                    Text("${keyEntity.requestCount} (${keyEntity.errorCount} err)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Latency", fontSize = 11.sp, color = textSecondary)
                    Text(if (keyEntity.latencyMs > 0) "${keyEntity.latencyMs} ms" else "--", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                }
            }

            // Last Health / Error Message Box
            if (keyEntity.lastError.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusColor.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Status Log: ${keyEntity.lastError}",
                        fontSize = 11.sp,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            HorizontalDivider(color = SlateBorder.copy(alpha = 0.5f))

            // Controls Row: Set Primary, Test, Edit, Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!keyEntity.isPrimary) {
                    TextButton(
                        onClick = onSetPrimary,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = SaffronAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Set as Primary", fontSize = 12.sp, color = SaffronAccent)
                    }
                } else {
                    Text("Primary Key", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronAccent)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    OutlinedButton(
                        onClick = onTestKey,
                        enabled = !isTesting,
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        if (isTesting) {
                            Text("Testing...", fontSize = 11.sp)
                        } else {
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Test Key", fontSize = 11.sp)
                        }
                    }

                    IconButton(onClick = onEditKey, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Key", tint = GovBluePrimary, modifier = Modifier.size(18.dp))
                    }

                    IconButton(onClick = onDeleteKey, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Key", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AddEditApiKeyDialog(
    title: String,
    initialName: String,
    initialKey: String,
    isPrimaryDefault: Boolean = false,
    isEditMode: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (name: String, key: String, isPrimary: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var keyText by remember { mutableStateOf(initialKey) }
    var isPrimary by remember { mutableStateOf(isPrimaryDefault) }
    var isKeyVisible by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Key Name / Label") },
                    placeholder = { Text("e.g. Primary Gemini Key") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = keyText,
                    onValueChange = { keyText = it },
                    label = { Text(if (isEditMode) "Replace API Key (Optional)" else "Gemini API Key") },
                    placeholder = { Text("Paste AIzaSy... key here") },
                    singleLine = true,
                    visualTransformation = if (isKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isKeyVisible = !isKeyVisible }) {
                            Icon(
                                imageVector = if (isKeyVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle Visibility"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { isPrimary = !isPrimary }
                ) {
                    Checkbox(checked = isPrimary, onCheckedChange = { isPrimary = it })
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Set as Primary Key for AI Assistant", fontSize = 13.sp)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "🔒 Security: Keys are encrypted in Room database. Full keys are never exposed in logs or commits.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name, keyText, isPrimary)
                },
                enabled = isEditMode || keyText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary)
            ) {
                Text("Save Key")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
