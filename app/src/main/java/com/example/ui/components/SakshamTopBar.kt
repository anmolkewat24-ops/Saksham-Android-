package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GovBlueContainer
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateMedium
import kotlinx.coroutines.launch

val supportedLanguages = listOf(
    "English" to "English",
    "हिन्दी" to "Hindi",
    "मराठी" to "Marathi",
    "বাংলা" to "Bengali",
    "தமிழ்" to "Tamil",
    "తెలుగు" to "Telugu"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SakshamTopBar(
    title: String? = null,
    canNavigateBack: Boolean = false,
    onNavigateBack: () -> Unit = {},
    currentLanguage: String = "English",
    onLanguageSelected: (String) -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    var showLanguageSheet by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            // Official Tri-color Micro Accent Stripe (Saffron, White, Green)
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFFF9933))
                        .size(height = 3.5.dp, width = 0.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isDarkMode) Color(0xFFCCCCCC) else Color(0xFFFFFFFF))
                        .size(height = 3.5.dp, width = 0.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF138808))
                        .size(height = 3.5.dp, width = 0.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (canNavigateBack) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                            .testTag("top_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                } else {
                    // Official Emblem Logo
                    Image(
                        painter = painterResource(id = R.drawable.saksham_logo),
                        contentDescription = "Saksham Logo",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // Title and Subtitle with high contrast
                Column(modifier = Modifier.weight(1f)) {
                    if (title != null && canNavigateBack) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 17.sp,
                            maxLines = 1
                        )
                        Text(text = com.example.ui.i18n.SakshamStrings.get("saksham_portal_•_government_of_india"),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = com.example.ui.i18n.SakshamStrings.get("saksham"),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                                letterSpacing = 1.3.sp,
                                fontSize = 18.sp
                            )
                        }
                        Text(text = com.example.ui.i18n.SakshamStrings.get("government_loan_&_business_advisor"),
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Theme Mode Toggle Button
                IconButton(
                    onClick = onToggleDarkMode,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isDarkMode) Color(0xFF1F2937) else GovBlueContainer.copy(alpha = 0.7f))
                        .border(1.dp, if (isDarkMode) Color(0xFF374151) else SlateBorder, CircleShape)
                        .testTag("theme_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                        tint = if (isDarkMode) Color(0xFFFBBF24) else GovBluePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Language Selection pill
                Row(
                    modifier = Modifier
                        .testTag("language_selector_button")
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isDarkMode) Color(0xFF1F2937) else GovBlueContainer)
                        .border(1.dp, if (isDarkMode) Color(0xFF374151) else GovBluePrimary.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                        .clickable { showLanguageSheet = true }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Select Language",
                        tint = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentLanguage,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) Color.White else GovBluePrimary
                    )
                }
            }
        }
    }

    if (showLanguageSheet) {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        ModalBottomSheet(
            onDismissRequest = { showLanguageSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(text = com.example.ui.i18n.SakshamStrings.get("select_preferred_language_भाषा_चुनें"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp
                )
                Text(text = com.example.ui.i18n.SakshamStrings.get("choose_the_language_for_application_guidance_&_ai_advisory"),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                supportedLanguages.forEach { (nativeName, engName) ->
                    val isSelected = currentLanguage == nativeName || currentLanguage == engName
                    val selectedItemBg = if (isSelected) {
                        if (isDarkMode) Color(0xFF1E3A5F) else GovBlueContainer.copy(alpha = 0.6f)
                    } else Color.Transparent
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(selectedItemBg)
                            .clickable {
                                onLanguageSelected(nativeName)
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    showLanguageSheet = false
                                }
                            }
                            .padding(vertical = 12.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = {
                                onLanguageSelected(nativeName)
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    showLanguageSheet = false
                                }
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = nativeName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (isSelected) {
                                    if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                                } else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = engName,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
