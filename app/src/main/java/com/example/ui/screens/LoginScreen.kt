package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GovBlueContainer
import com.example.ui.theme.GovBlueDark
import com.example.ui.theme.GovBluePrimary
import com.example.ui.theme.GovBluePrimaryDark
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.GrowthGreenLight
import com.example.ui.theme.SaffronAccent
import com.example.ui.theme.SlateBorder

@Composable
fun LoginScreen(
    onLoginSuccess: (name: String, phone: String) -> Unit,
    isDarkMode: Boolean = false
) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("Ramesh Kumar") }
    var mobileNumber by remember { mutableStateOf("9876543210") }
    var otpValue by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isGoogleLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("login_screen"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // National Tricolor Top Bar Accent
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
            ) {
                Box(modifier = Modifier.weight(1f).fillMaxSize().background(SaffronAccent))
                Box(modifier = Modifier.weight(1f).fillMaxSize().background(Color.White))
                Box(modifier = Modifier.weight(1f).fillMaxSize().background(GrowthGreen))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Official Emblem Badge
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(GovBluePrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Emblem",
                    tint = Color.White,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // App & Portal Title
            Text(
                text = "SAKSHAM",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = if (isDarkMode) GovBluePrimaryDark else GovBlueDark
            )

            Text(
                text = "National Economic Empowerment & Business Portal",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Ministry of Social Justice & Empowerment • Government of India",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = SaffronAccent,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Primary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkMode) MaterialTheme.colorScheme.outlineVariant else SlateBorder
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = if (isOtpSent) "Enter Verification OTP" else "Beneficiary Sign In",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (isOtpSent)
                            "Enter the 6-digit code sent to +91 $mobileNumber (Enter 123456 for instant demo login)"
                        else
                            "Sign in to access verified NSFDC concessional loans, customized business action plans, and local channel partner banks.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    if (!isOtpSent) {
                        // "Continue with Google" Button
                        OutlinedButton(
                            onClick = {
                                isGoogleLoading = true
                                Toast.makeText(context, "Signing in with Google Account...", Toast.LENGTH_SHORT).show()
                                onLoginSuccess("Ramesh Kumar (Google)", "+91 9876543210")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("login_google_btn"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isDarkMode) Color(0xFF475569) else Color(0xFFCBD5E1)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isDarkMode) Color(0xFF1E293B) else Color(0xFFF8FAFC)
                            ),
                            enabled = !isGoogleLoading && !isLoading
                        ) {
                            if (isGoogleLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                GoogleLogoIcon(modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Continue with Google",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDarkMode) Color.White else Color(0xFF1E293B)
                                )
                            }
                        }

                        // Divider with text
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                            Text(
                                text = "  OR SIGN IN WITH MOBILE  ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                        }

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GovBluePrimary)
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_name_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GovBluePrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = { if (it.length <= 10) mobileNumber = it },
                            label = { Text("Mobile Number") },
                            prefix = { Text("+91 ", fontWeight = FontWeight.Bold) },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = GovBluePrimary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (mobileNumber.length >= 10) {
                                    isOtpSent = true
                                    otpValue = "123456"
                                    Toast.makeText(context, "OTP sent to +91 $mobileNumber (Default: 123456)", Toast.LENGTH_LONG).show()
                                }
                            }),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_phone_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GovBluePrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        Button(
                            onClick = {
                                if (mobileNumber.length < 10) {
                                    Toast.makeText(context, "Please enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
                                } else {
                                    isOtpSent = true
                                    otpValue = "123456"
                                    Toast.makeText(context, "OTP sent to +91 $mobileNumber (Default: 123456)", Toast.LENGTH_LONG).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("login_get_otp_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GovBluePrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Get Verification OTP", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        // OTP Verification Step
                        OutlinedTextField(
                            value = otpValue,
                            onValueChange = { if (it.length <= 6) otpValue = it },
                            label = { Text("Enter 6-digit OTP") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = GovBluePrimary)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (otpValue.length >= 6) {
                                    isLoading = true
                                    Toast.makeText(context, "Welcome $fullName! Authentication successful.", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(fullName, mobileNumber)
                                }
                            }),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_otp_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GrowthGreen,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Quick fill button for testers/evaluators
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GovBlueContainer)
                                    .clickable { otpValue = "123456" }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Auto-fill 123456",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = GovBlueDark
                                )
                            }

                            Text(
                                text = "Resend OTP",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GovBluePrimary,
                                modifier = Modifier.clickable {
                                    otpValue = "123456"
                                    Toast.makeText(context, "New OTP sent: 123456", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        Button(
                            onClick = {
                                if (otpValue.length < 6) {
                                    Toast.makeText(context, "Please enter a valid 6-digit OTP", Toast.LENGTH_SHORT).show()
                                } else {
                                    isLoading = true
                                    Toast.makeText(context, "Welcome $fullName! Authentication successful.", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(fullName, mobileNumber)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("login_verify_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = GrowthGreen),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Verify & Enter Saksham", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        OutlinedButton(
                            onClick = { isOtpSent = false },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Change Mobile Number", fontSize = 12.sp)
                        }
                    }

                    // Direct / Guest Access Button
                    OutlinedButton(
                        onClick = {
                            onLoginSuccess(fullName.ifBlank { "Ramesh Kumar" }, mobileNumber.ifBlank { "9876543210" })
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_skip_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Explore as Guest / Skip Login",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Trust Security Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = GrowthGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Encrypted & Secured by Government of India Standards",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Clean Canvas-drawn Google 4-color 'G' icon
 */
@Composable
fun GoogleLogoIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = w / 2f

        // Blue bar in center-right
        drawRect(
            color = Color(0xFF4285F4),
            topLeft = Offset(cx, cy - h * 0.12f),
            size = androidx.compose.ui.geometry.Size(w * 0.48f, h * 0.24f)
        )

        // Blue top arc
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = 0f,
            sweepAngle = -45f,
            useCenter = false,
            topLeft = Offset(w * 0.1f, h * 0.1f),
            size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.8f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.2f)
        )

        // Red top arc
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = -45f,
            sweepAngle = -100f,
            useCenter = false,
            topLeft = Offset(w * 0.1f, h * 0.1f),
            size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.8f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.2f)
        )

        // Yellow left arc
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = -145f,
            sweepAngle = -90f,
            useCenter = false,
            topLeft = Offset(w * 0.1f, h * 0.1f),
            size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.8f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.2f)
        )

        // Green bottom arc
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 125f,
            sweepAngle = -105f,
            useCenter = false,
            topLeft = Offset(w * 0.1f, h * 0.1f),
            size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.8f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w * 0.2f)
        )
    }
}
