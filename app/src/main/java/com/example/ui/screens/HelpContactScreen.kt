package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.SakshamStrings
import com.example.ui.theme.GovBlueContainer
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SaffronLight
import com.example.ui.theme.SlateBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpContactScreen(
    language: String = "English",
    isDarkMode: Boolean = false
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("FAQs & Help", "Contact Us", "Support Form", "Feedback")

    // Contact details specified by user
    val contactEmail = "anmolkewat24@gmail.com"
    val contactPhone = "9630194023"
    val contactWhatsApp = "9630194023"

    val titleColor = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
    val cardBg = MaterialTheme.colorScheme.surface
    val borderStroke = androidx.compose.foundation.BorderStroke(
        1.dp,
        if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("help_contact_screen")
    ) {
        // Tab Header
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTabIndex) {
                0 -> {
                    // Help, FAQs, Guides & Checklists
                    item {
                        SectionHeader(
                            title = "How to Use Saksham Portal",
                            icon = Icons.Default.Info,
                            titleColor = titleColor
                        )
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            border = borderStroke,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                StepItem(
                                    step = "1",
                                    title = "Check Matching Schemes",
                                    desc = "Use 'Find Best Scheme' or explore the Schemes tab. Enter your business details (investment, category) to see eligibility."
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                StepItem(
                                    step = "2",
                                    title = "Calculate EMI & Moratorium",
                                    desc = "Use the EMI Calculator to see your exact monthly installment and how much you save during the 12-month moratorium."
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                StepItem(
                                    step = "3",
                                    title = "Chat with Saksham Saathi AI",
                                    desc = "Ask the AI assistant for customized project reports, cattle/machinery costs, and required documents."
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                StepItem(
                                    step = "4",
                                    title = "Locate Nearest Authorized Partner",
                                    desc = "Visit the Partners tab to find your district State Channelising Agency (SCA) or nationalized partner bank branch."
                                )
                            }
                        }
                    }

                    item {
                        SectionHeader(
                            title = "Eligibility & Document Checklist",
                            icon = Icons.Default.CheckCircle,
                            titleColor = titleColor
                        )
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            border = borderStroke,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                ChecklistItem("Aadhaar Card (linked with active mobile number & bank account)")
                                ChecklistItem("PAN Card (mandatory for business registrations and bank accounts)")
                                ChecklistItem("Caste Certificate (issued by competent revenue authority / Tehsildar)")
                                ChecklistItem("Income Certificate (family income under eligibility threshold, e.g. < ₹3 Lakh)")
                                ChecklistItem("Land Record (Khatauni / Khasra) or Registered Lease Deed for business premises")
                                ChecklistItem("Quotation / Proforma Invoice from authorized cattle/equipment supplier")
                                ChecklistItem("Last 6 Months Bank Account Statement & Cancelled Cheque")
                                ChecklistItem("Passport Size Photographs (2 recent copies)")
                            }
                        }
                    }

                    item {
                        SectionHeader(
                            title = "Frequently Asked Questions (FAQs)",
                            icon = Icons.Default.QuestionAnswer,
                            titleColor = titleColor
                        )
                    }

                    item {
                        FaqAccordionItem(
                            question = "What is the moratorium period in NSFDC schemes?",
                            answer = "A moratorium (gestation period) is a grace period — typically 6 to 12 months — during which the borrower is not required to pay principal loan installments. This allows your business (e.g. dairy setup or manufacturing unit) to generate revenue before repayments commence.",
                            isDarkMode = isDarkMode
                        )
                    }

                    item {
                        FaqAccordionItem(
                            question = "What are the interest rates for women entrepreneurs?",
                            answer = "Under NSFDC and Mahila Samriddhi schemes, women receive a 1% concessional rebate on interest rates. For example, the Term Loan rate is 5% p.a. for women versus 6% for others.",
                            isDarkMode = isDarkMode
                        )
                    }

                    item {
                        FaqAccordionItem(
                            question = "Can I apply online or do I have to visit the bank?",
                            answer = "You can review eligibility and generate your business action plan through this app. Applications are submitted via the district State Channelising Agency (SCA) office (e.g. UPSCFDC) or designated Lead District Bank branches listed in the Partners tab.",
                            isDarkMode = isDarkMode
                        )
                    }

                    item {
                        FaqAccordionItem(
                            question = "How does the AI Business Assistant help me?",
                            answer = "Saksham Saathi AI provides step-by-step guidance tailored to your trade (such as dairy farming, tailoring, or retail shops), calculates estimated budgets, lists required documents, and prepares you for bank scrutiny.",
                            isDarkMode = isDarkMode
                        )
                    }

                    item {
                        FaqAccordionItem(
                            question = "Troubleshooting: What if my application is delayed?",
                            answer = "Visit your district SCA office with your acknowledgment slip or call the toll-free national helpline at 1800-11-2001. You can also reach our support team via the Contact tab.",
                            isDarkMode = isDarkMode
                        )
                    }
                }

                1 -> {
                    // Contact Us (Direct Call, WhatsApp, Email)
                    item {
                        SectionHeader(
                            title = "Official Support & Contact Channels",
                            icon = Icons.Default.SupportAgent,
                            titleColor = titleColor
                        )
                    }

                    // Email Card
                    item {
                        ContactChannelCard(
                            icon = Icons.Default.Email,
                            iconBg = GovBlueContainer,
                            iconTint = GovBluePrimary,
                            title = "Email Support",
                            value = contactEmail,
                            actionLabel = "Send Email",
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$contactEmail")
                                    putExtra(Intent.EXTRA_SUBJECT, "Saksham Portal Support Request")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Email: $contactEmail", Toast.LENGTH_LONG).show()
                                }
                            },
                            isDarkMode = isDarkMode
                        )
                    }

                    // Phone Call Card
                    item {
                        ContactChannelCard(
                            icon = Icons.Default.Call,
                            iconBg = GrowthGreenLight,
                            iconTint = GrowthGreen,
                            title = "Phone Helpline",
                            value = "+91 $contactPhone",
                            actionLabel = "Call Now",
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactPhone"))
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Phone: $contactPhone", Toast.LENGTH_LONG).show()
                                }
                            },
                            isDarkMode = isDarkMode
                        )
                    }

                    // WhatsApp Card
                    item {
                        ContactChannelCard(
                            icon = Icons.Default.QuestionAnswer,
                            iconBg = Color(0xFFDCFCE7),
                            iconTint = Color(0xFF16A34A),
                            title = "WhatsApp Chat Support",
                            value = "+91 $contactWhatsApp",
                            actionLabel = "Open WhatsApp",
                            onClick = {
                                val url = "https://api.whatsapp.com/send?phone=91$contactWhatsApp&text=Hello%20Saksham%20Support,%20I%20need%20assistance%20with%20government%20loan%20schemes."
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "WhatsApp: $contactWhatsApp", Toast.LENGTH_LONG).show()
                                }
                            },
                            isDarkMode = isDarkMode
                        )
                    }

                    // National Toll Free Note
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color(0xFF132A1C) else GrowthGreenLight),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = GrowthGreen,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = com.example.ui.i18n.SakshamStrings.get("national_toll_free_helpline_govt_of_india"),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkMode) Color.White else Color(0xFF14532D)
                                    )
                                    Text(text = com.example.ui.i18n.SakshamStrings.get("1800_11_2001_available_930_am_600_pm_on_working_days"),
                                        fontSize = 11.sp,
                                        color = if (isDarkMode) Color(0xFFBBF7D0) else Color(0xFF166534)
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Contact Support Form
                    item {
                        SectionHeader(
                            title = "Submit Support Request",
                            icon = Icons.Default.SupportAgent,
                            titleColor = titleColor
                        )
                    }

                    item {
                        SupportFormCard(
                            contactEmail = contactEmail,
                            isDarkMode = isDarkMode
                        )
                    }
                }

                3 -> {
                    // Feedback & Rating
                    item {
                        SectionHeader(
                            title = "User Feedback & Rating",
                            icon = Icons.Default.RateReview,
                            titleColor = titleColor
                        )
                    }

                    item {
                        FeedbackCard(isDarkMode = isDarkMode)
                    }
                }
            }

            // Official Disclaimer Card (MANDATORY EXACT TEXT)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF261D10) else SaffronLight.copy(alpha = 0.5f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDarkMode) Color(0xFF78350F) else SaffronAccent.copy(alpha = 0.4f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Disclaimer",
                            tint = SaffronAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = com.example.ui.i18n.SakshamStrings.get("important_official_disclaimer"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (isDarkMode) Color(0xFFFDE68A) else Color(0xFF92400E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = com.example.ui.i18n.SakshamStrings.get("saksham_is_a_guidance_platform_and_does_not_guarantee_loan_approval_final_eligibility_sanction_and_disbursement_are_determined_by_the_concerned_government_scheme_and_authorized_channel_partner"),
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = if (isDarkMode) Color(0xFFF3F4F6) else Color(0xFF451A03)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = com.example.ui.i18n.SakshamStrings.get("official_information_note_all_scheme_data_is_synchronized_from_official_guidelines_issued_by_nsfdc_and_the_ministry_of_social_justice_&_empowerment_government_of_india_beware_of_unauthorized_intermediaries"),
                                fontSize = 10.sp,
                                color = if (isDarkMode) Color(0xFFD1D5DB) else Color(0xFF78350F)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    titleColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = titleColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun StepItem(step: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(GovBluePrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = step, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun ChecklistItem(text: String) {
    Row(verticalAlignment = Alignment.Top) {
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
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun FaqAccordionItem(
    question: String,
    answer: String,
    isDarkMode: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = answer,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactChannelCard(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    value: String,
    actionLabel: String,
    onClick: () -> Unit,
    isDarkMode: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(text = actionLabel, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupportFormCard(
    contactEmail: String,
    isDarkMode: Boolean
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Scheme Eligibility Inquiry") }
    var message by remember { mutableStateOf("") }
    var expandedDropdown by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }

    val categories = listOf(
        "Scheme Eligibility Inquiry",
        "Application Guidance Help",
        "AI Business Assistant Query",
        "Channel Partner Branch Inquiry",
        "Technical Issue or Bug Report",
        "Other General Query"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isSubmitted) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GrowthGreenLight)
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GrowthGreen,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = com.example.ui.i18n.SakshamStrings.get("thank_you!_your_support_request_has_been_registered"),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF14532D),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Our support executive will respond to you via $contactEmail shortly.",
                            fontSize = 11.sp,
                            color = Color(0xFF166534)
                        )
                    }
                }
            } else {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(com.example.ui.i18n.SakshamStrings.get("your_full_name")) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text(com.example.ui.i18n.SakshamStrings.get("your_phone_or_email_address")) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(com.example.ui.i18n.SakshamStrings.get("select_issue_category")) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    selectedCategory = cat
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text(com.example.ui.i18n.SakshamStrings.get("describe_your_query_or_problem")) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        if (name.isBlank() || contact.isBlank() || message.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields before submitting.", Toast.LENGTH_SHORT).show()
                        } else {
                            isSubmitted = true
                            Toast.makeText(context, "Support request submitted successfully!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(com.example.ui.i18n.SakshamStrings.get("submit_support_request"))
                }
            }
        }
    }
}

@Composable
private fun FeedbackCard(isDarkMode: Boolean) {
    val context = LocalContext.current
    var rating by remember { mutableIntStateOf(5) }
    var feedbackText by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (submitted) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GrowthGreenLight)
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = GrowthGreen,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = com.example.ui.i18n.SakshamStrings.get("thank_you_for_rating_saksham!"),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF14532D),
                            fontSize = 13.sp
                        )
                        Text(text = com.example.ui.i18n.SakshamStrings.get("your_feedback_helps_us_empower_more_entrepreneurs"),
                            fontSize = 11.sp,
                            color = Color(0xFF166534)
                        )
                    }
                }
            } else {
                Text(text = com.example.ui.i18n.SakshamStrings.get("how_was_your_experience_using_saksham"),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Interactive 5 Stars
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "$i Stars",
                            tint = if (i <= rating) Color(0xFFF59E0B) else Color.Gray,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { rating = i }
                                .padding(2.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    label = { Text(com.example.ui.i18n.SakshamStrings.get("what_did_you_like_or_how_can_we_improve_optional")) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 4,
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        submitted = true
                        Toast.makeText(context, "Feedback received! Thank you.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(com.example.ui.i18n.SakshamStrings.get("submit_feedback"))
                }
            }
        }
    }
}
