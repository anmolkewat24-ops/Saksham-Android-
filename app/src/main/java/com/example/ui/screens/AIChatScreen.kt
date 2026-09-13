package com.example.ui.screens

import android.widget.Toast
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.IconButtonDefaults

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
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
import com.example.ui.theme.SlateLight

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime

@Composable
fun AIChatScreen(
    messages: List<ChatMessage>,
    isAiThinking: Boolean,
    onSendMessage: (String) -> Unit,
    onNavigate: (Screen) -> Unit,
    language: String = "English",
    isDarkMode: Boolean = false
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickTopicChips = remember {
        listOf(
            "🐄 Dairy Farm Setup",
            "📑 Document Checklist",
            "💰 4% MSY Interest",
            "⏳ 12-Mo Moratorium",
            "🏛️ Nearest Partner Bank",
            "🎓 Education Loan"
        )
    }

    val imeBottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
    val isKeyboardOpen = imeBottom > 0.dp

    LaunchedEffect(messages.size, isAiThinking, isKeyboardOpen) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .testTag("ai_chat_screen")
    ) {
        // Modern AI Header Banner
        Surface(
            color = if (isDarkMode) Color(0xFF1E293B) else GovBlueDark,
            shadowElevation = 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = if (isKeyboardOpen) 6.dp else 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isKeyboardOpen) 30.dp else 38.dp)
                        .clip(CircleShape)
                        .background(if (isDarkMode) Color(0xFF334155) else GovBlueContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (isDarkMode) SaffronAccent else GovBluePrimary,
                        modifier = Modifier.size(if (isKeyboardOpen) 16.dp else 20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = com.example.ui.i18n.SakshamStrings.get("saksham_saathi_सक्षम_साथी", language),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = if (isKeyboardOpen) 13.sp else 15.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = GrowthGreen,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    if (!isKeyboardOpen) {
                        Text(text = com.example.ui.i18n.SakshamStrings.get("official_nsfdc_&_govt_concessional_advisor_•_247_ai", language),
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }
                }

                // National Flag tricolor pill
                Row(
                    modifier = Modifier
                        .height(12.dp)
                        .width(24.dp)
                        .clip(RoundedCornerShape(3.dp))
                ) {
                    Box(modifier = Modifier.weight(1f).fillMaxSize().background(SaffronAccent))
                    Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color.White))
                    Box(modifier = Modifier.weight(1f).fillMaxSize().background(GrowthGreen))
                }
            }
        }

        // Chat Message Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { msg ->
                ModernChatMessageItem(
                    message = msg,
                    isDarkMode = isDarkMode,
                    onPromptClick = { prompt -> onSendMessage(prompt) },
                    onNavigate = onNavigate
                )
            }

            if (isAiThinking) {
                item {
                    ModernTypingIndicator(isDarkMode = isDarkMode)
                }
            }
        }

        // Suggested Contextual Prompts Carousel (Hide when keyboard is open to maximize message space)
        AnimatedVisibility(
            visible = !isKeyboardOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickTopicChips) { chipText ->
                    val promptQuery = when {
                        chipText.contains("Dairy") -> "I want to start a dairy farm with 4 cows"
                        chipText.contains("Document") -> "What documents do I need for NSFDC loan?"
                        chipText.contains("MSY") -> "Tell me about Mahila Samriddhi 4% interest rate"
                        chipText.contains("Moratorium") -> "How does the 12-month moratorium work?"
                        chipText.contains("Partner") -> "Where is the nearest State Channel Partner bank?"
                        chipText.contains("Education") -> "What are the rules for NSFDC education loan?"
                        else -> chipText
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isDarkMode) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                            .border(
                                1.dp,
                                if (isDarkMode) Color(0xFF334155) else GovBluePrimary.copy(alpha = 0.25f),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { onSendMessage(promptQuery) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = chipText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                        )
                    }
                }
            }
        }

        // Modern Input Bar & Send Button
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(text = com.example.ui.i18n.SakshamStrings.get("ask_about_loans_cattle_rates_documents", language),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_chat_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GovBluePrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFF8FAFC),
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFF8FAFC)
                    ),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank()) {
                                onSendMessage(inputText)
                                inputText = ""
                            }
                        }
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    enabled = inputText.isNotBlank() && !isAiThinking,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = GovBluePrimary,
                        contentColor = Color.White,
                        disabledContainerColor = if (isDarkMode) Color(0xFF334155) else SlateBorder,
                        disabledContentColor = SlateLight
                    ),
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("ai_chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Modern Chat Bubble with Markdown-like bullet and heading rendering
 */
@Composable
fun ModernChatMessageItem(
    message: ChatMessage,
    isDarkMode: Boolean = false,
    onPromptClick: (String) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(GovBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.85f else 0.92f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            if (!isUser) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 3.dp, start = 2.dp)
                ) {
                    Text(text = com.example.ui.i18n.SakshamStrings.get("saksham_saathi"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = com.example.ui.i18n.SakshamStrings.get("•_official_advisor"),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Chat Bubble Container
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .background(
                        if (isUser) (if (isDarkMode) Color(0xFF2563EB) else GovBluePrimary)
                        else MaterialTheme.colorScheme.surface
                    )
                    .border(
                        1.dp,
                        if (isUser) Color.Transparent
                        else if (isDarkMode) Color(0xFF334155) else SlateBorder,
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                if (isUser) {
                    Text(
                        text = message.text,
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                } else {
                    Column {
                        FormattedAiResponse(
                            rawText = message.text,
                            isDarkMode = isDarkMode
                        )
                        
                        val clipboardManager = LocalClipboardManager.current
                        val context = androidx.compose.ui.platform.LocalContext.current
                        
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { 
                                clipboardManager.setText(buildAnnotatedString { append(message.text) })
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = GovBluePrimary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // Action Buttons for relevant topics
            if (!isUser) {
                val t = message.text.lowercase()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (t.contains("calculator") || t.contains("emi") || t.contains("interest") || t.contains("ब्याज")) {
                        ActionNavChip(
                            label = "Open EMI Calculator",
                            icon = Icons.Default.Calculate,
                            isDarkMode = isDarkMode,
                            onClick = { onNavigate(Screen.Calculator) }
                        )
                    }
                    if (t.contains("partner") || t.contains("bank") || t.contains("vikas bhavan") || t.contains("शाखा")) {
                        ActionNavChip(
                            label = "Find Partner Banks",
                            icon = Icons.Default.LocationOn,
                            isDarkMode = isDarkMode,
                            onClick = { onNavigate(Screen.Partners) }
                        )
                    }
                    if (t.contains("plan") || t.contains("dairy") || t.contains("setup") || t.contains("एक्शन प्लान")) {
                        ActionNavChip(
                            label = "View Action Plan",
                            icon = Icons.Default.Description,
                            isDarkMode = isDarkMode,
                            onClick = { onNavigate(Screen.ActionPlan) }
                        )
                    }
                }
            }

            // Attached Suggested Follow-up Prompts
            if (!isUser && message.suggestedPrompts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    message.suggestedPrompts.forEach { prompt ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkMode) Color(0xFF1E293B) else GovBlueLight)
                                .border(
                                    1.dp,
                                    if (isDarkMode) Color(0xFF334155) else GovBluePrimary.copy(alpha = 0.2f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onPromptClick(prompt) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = com.example.ui.i18n.SakshamStrings.get("➔"),
                                    color = SaffronAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = prompt,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDarkMode) GovBluePrimaryDark else GovBluePrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isDarkMode) Color(0xFF334155) else SlateBorder),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isDarkMode) Color.White else GovBlueDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Formats AI text with clear headings, short bullets, and highlighted callouts
 */
@Composable
fun FormattedAiResponse(
    rawText: String,
    isDarkMode: Boolean
) {
    val lines = rawText.lines()

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        lines.forEach { line ->
            val trimmed = line.trim()
            when {
                trimmed.isBlank() -> {
                    Spacer(modifier = Modifier.height(2.dp))
                }
                trimmed == "---" || trimmed == "***" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    )
                }
                // Headings (starts with ** or emojis or #)
                trimmed.startsWith("**") && trimmed.endsWith("**") -> {
                    val headingText = trimmed.removeSurrounding("**")
                    Text(
                        text = headingText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }
                trimmed.startsWith("###") || trimmed.startsWith("##") -> {
                    val headingText = trimmed.removePrefix("###").removePrefix("##").trim().replace("**", "")
                    Text(
                        text = headingText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark,
                        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                    )
                }
                trimmed.startsWith("💡") || trimmed.startsWith("Tip:") || trimmed.startsWith("सलाह:") -> {
                    // Callout tip box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDarkMode) Color(0xFF1E3A5F) else SaffronLight)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = trimmed.replace("**", ""),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) Color.White else Color(0xFF9A3412),
                            lineHeight = 16.sp
                        )
                    }
                }
                // Bullets (starts with •, -, *)
                trimmed.startsWith("•") || trimmed.startsWith("-") || trimmed.startsWith("* ") -> {
                    val bulletContent = trimmed.removePrefix("•").removePrefix("-").removePrefix("*").trim()
                    Row(
                        modifier = Modifier.padding(start = 2.dp, top = 2.dp, bottom = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp, end = 6.dp)
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(if (isDarkMode) SaffronAccent else GovBluePrimary)
                        )
                        Text(
                            text = bulletContent.replace("**", ""),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )
                    }
                }
                // Numbered lists (starts with digit and dot)
                trimmed.matches(Regex("^\\d+\\..*")) -> {
                    Row(
                        modifier = Modifier.padding(start = 2.dp, top = 2.dp, bottom = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = trimmed.replace("**", ""),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        )
                    }
                }
                // Regular body text
                else -> {
                    Text(
                        text = trimmed.replace("**", ""),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

/**
 * Animated Typing Indicator Bubble
 */
@Composable
fun ModernTypingIndicator(isDarkMode: Boolean) {
    val transition = rememberInfiniteTransition(label = "dots")
    val alpha1 by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(600), repeatMode = RepeatMode.Reverse),
        label = "dot1"
    )
    val alpha2 by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(600, delayMillis = 200), repeatMode = RepeatMode.Reverse),
        label = "dot2"
    )
    val alpha3 by transition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(600, delayMillis = 400), repeatMode = RepeatMode.Reverse),
        label = "dot3"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(GovBluePrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 4.dp))
                .background(if (isDarkMode) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                .border(1.dp, if (isDarkMode) Color(0xFF334155) else SlateBorder, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = com.example.ui.i18n.SakshamStrings.get("saksham_saathi_is_analyzing"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GovBluePrimary.copy(alpha = alpha1)))
                Spacer(modifier = Modifier.width(3.dp))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GovBluePrimary.copy(alpha = alpha2)))
                Spacer(modifier = Modifier.width(3.dp))
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GovBluePrimary.copy(alpha = alpha3)))
            }
        }
    }
}

@Composable
fun ActionNavChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isDarkMode: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isDarkMode) Color(0xFF0F172A) else Color(0xFFE2E8F0))
            .border(1.dp, if (isDarkMode) Color(0xFF334155) else SlateBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isDarkMode) GovBluePrimaryDark else GovBlueDark,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
            )
        }
    }
}
