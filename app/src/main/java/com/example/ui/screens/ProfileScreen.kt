package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.SavedPartnerEntity
import com.example.data.local.SavedPlanEntity
import com.example.data.local.SavedSchemeEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.BusinessProfile
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
import com.example.ui.theme.SlateBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    businessProfile: BusinessProfile,
    savedSchemes: List<SavedSchemeEntity>,
    savedPlans: List<SavedPlanEntity>,
    savedPartners: List<SavedPartnerEntity>,
    onSelectSchemeById: (String) -> Unit,
    onNavigate: (Screen) -> Unit,
    onUpdateProfile: (name: String, phone: String, state: String, district: String, category: String, income: String, target: String) -> Unit,
    onUpdatePhoto: (photoUri: String) -> Unit = {},
    onRemoveScheme: (String) -> Unit,
    onLogout: () -> Unit = {},
    language: String = "English",
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }

    val name = userProfile?.fullName ?: "Ramesh Kumar"
    val phone = userProfile?.phone ?: "+91 98765 43210"
    val state = userProfile?.state ?: "Uttar Pradesh"
    val district = userProfile?.district ?: "Varanasi"
    val category = userProfile?.socialCategory ?: "Scheduled Caste (SC)"
    val income = userProfile?.familyIncome ?: "₹1.50 - 3.00 Lakh"
    val activeTarget = userProfile?.activeBusinessTarget ?: "Dairy Farming & Milk Production"
    val photoUri = userProfile?.photoUri

    // Zero-permission Android Photo Picker for profile photo
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onUpdatePhoto(uri.toString())
            Toast.makeText(context, "Profile photo updated successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Avatar and Profile Header Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Profile Avatar with Photo Picker Badge
                        Box(
                            modifier = Modifier.size(68.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(if (isDarkMode) Color(0xFF1E293B) else GovBluePrimary)
                                    .border(2.dp, if (isDarkMode) GovBluePrimaryDark else Color.White, CircleShape)
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (!photoUri.isNullOrBlank()) {
                                    AsyncImage(
                                        model = photoUri,
                                        contentDescription = "Profile Photo",
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Default Avatar",
                                        tint = Color.White,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }

                            // Camera / Edit Photo floating badge
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(GovBluePrimary)
                                    .border(1.5.dp, Color.White, CircleShape)
                                    .clickable {
                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                    .testTag("change_profile_photo_badge"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Change Photo",
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 17.sp
                            )
                            Text(
                                text = phone,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = category,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GrowthGreen
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = { showEditDialog = true },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueContainer.copy(alpha = 0.6f))
                                .testTag("edit_profile_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Location & Family Income Pills (Editable via Edit Dialog)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProfileInfoBox(
                            label = "Location",
                            value = "$district, $state",
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f)
                        )
                        ProfileInfoBox(
                            label = SakshamStrings.get("profile_income", language),
                            value = income,
                            isDarkMode = isDarkMode,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Active Business Target (Editable)
                    ProfileInfoBox(
                        label = SakshamStrings.get("profile_business_target", language),
                        value = activeTarget,
                        isDarkMode = isDarkMode,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Quick Action: Help, Support & Contact
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(Screen.HelpContact) }
                    .testTag("profile_help_card")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (isDarkMode) Color(0xFF261D10) else GovBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = SaffronAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = SakshamStrings.get("help_faq_tab", language),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Guidelines, document checklist, FAQs & direct contact",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Saved Schemes Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${SakshamStrings.get("saved_schemes", language)} (${savedSchemes.size})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    if (savedSchemes.isEmpty()) {
                        Text(
                            text = "No saved schemes yet. Bookmark schemes from the Schemes tab to review them later.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        savedSchemes.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueLight)
                                    .clickable {
                                        onSelectSchemeById(item.id)
                                        onNavigate(Screen.SchemeDetail)
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${item.category} • ${item.maxLoan} • ${item.interestRate}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = { onRemoveScheme(item.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Official Links Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Official National Portals",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    GovernmentLinkItem("NSFDC Official Website", "https://nsfdc.nic.in", isDarkMode)
                    GovernmentLinkItem("Ministry of Social Justice & Empowerment", "https://socialjustice.gov.in", isDarkMode)
                    GovernmentLinkItem("Jan Samarth National Portal", "https://www.jansamarth.in", isDarkMode)
                    GovernmentLinkItem("Stand-Up India Scheme Portal", "https://www.standupmitra.in", isDarkMode)
                    GovernmentLinkItem("Udyam MSME Registration Portal", "https://udyamregistration.gov.in", isDarkMode)
                }
            }
        }

        // Account / Session Actions
        item {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_logout_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign Out / Switch User",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        }

        // Legal Disclaimer Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFF8FAFC)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = SaffronAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Saksham is a guidance platform and does not guarantee loan approval. Final eligibility, sanction, and disbursement are determined by the concerned government scheme and authorized channel partner.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }

    // Comprehensive Edit Profile Modal Dialog with Editable Income & Active Business Target
    if (showEditDialog) {
        var editName by remember { mutableStateOf(name) }
        var editPhone by remember { mutableStateOf(phone) }
        var editState by remember { mutableStateOf(state) }
        var editDistrict by remember { mutableStateOf(district) }
        var editCategory by remember { mutableStateOf(category) }
        var editIncome by remember { mutableStateOf(income) }
        var editTarget by remember { mutableStateOf(activeTarget) }

        var incomeDropdownExpanded by remember { mutableStateOf(false) }
        val incomeOptions = listOf(
            "Below ₹1.50 Lakh",
            "₹1.50 - 3.00 Lakh",
            "₹3.00 - 5.00 Lakh",
            "₹5.00 - 8.00 Lakh",
            "Above ₹8.00 Lakh"
        )

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "Update User Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_name_field")
                    )

                    OutlinedTextField(
                        value = editPhone,
                        onValueChange = { editPhone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_phone_field")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = editDistrict,
                            onValueChange = { editDistrict = it },
                            label = { Text("District") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = editState,
                            onValueChange = { editState = it },
                            label = { Text("State") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Editable Family Income
                    ExposedDropdownMenuBox(
                        expanded = incomeDropdownExpanded,
                        onExpandedChange = { incomeDropdownExpanded = !incomeDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = editIncome,
                            onValueChange = { editIncome = it },
                            label = { Text("Family Income (Annual)") },
                            readOnly = false,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = incomeDropdownExpanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor().testTag("edit_income_field")
                        )
                        ExposedDropdownMenu(
                            expanded = incomeDropdownExpanded,
                            onDismissRequest = { incomeDropdownExpanded = false }
                        ) {
                            incomeOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        editIncome = option
                                        incomeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Editable Active Business Target
                    OutlinedTextField(
                        value = editTarget,
                        onValueChange = { editTarget = it },
                        label = { Text("Active Business Target") },
                        placeholder = { Text("e.g. Dairy Farm, Grocery Store, Apparel") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("edit_target_field")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(
                            editName.ifBlank { name },
                            editPhone.ifBlank { phone },
                            editState.ifBlank { state },
                            editDistrict.ifBlank { district },
                            editCategory,
                            editIncome.ifBlank { income },
                            editTarget.ifBlank { activeTarget }
                        )
                        showEditDialog = false
                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                    modifier = Modifier.testTag("save_profile_button")
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileInfoBox(
    label: String,
    value: String,
    isDarkMode: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueLight)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
            )
        }
    }
}

@Composable
fun GovernmentLinkItem(title: String, url: String, isDarkMode: Boolean = false) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
            modifier = Modifier.size(14.dp)
        )
    }
}
