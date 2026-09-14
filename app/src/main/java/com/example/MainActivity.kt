package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.repository.GovernmentDataRepository
import com.example.ui.SakshamViewModel
import com.example.ui.components.SakshamBottomBar
import com.example.ui.components.SakshamTopBar
import com.example.ui.i18n.LocalLanguage
import com.example.ui.i18n.SakshamStrings
import com.example.ui.navigation.Screen
import com.example.ui.screens.AIChatScreen
import com.example.ui.screens.ActionPlanScreen
import com.example.ui.screens.BusinessFormScreen
import com.example.ui.screens.ChannelPartnerLocatorScreen
import com.example.ui.screens.EmiCalculatorScreen
import com.example.ui.screens.HelpContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SchemeDetailScreen
import com.example.ui.screens.SchemeRecommendationScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: SakshamViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            MyApplicationTheme(darkTheme = isDarkMode, dynamicColor = false) {
                SakshamApp(viewModel = viewModel, isDarkMode = isDarkMode)
            }
        }
    }
}

@Composable
fun SakshamApp(viewModel: SakshamViewModel = viewModel(), isDarkMode: Boolean = false) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val businessProfile by viewModel.businessProfile.collectAsState()
    val selectedScheme by viewModel.selectedScheme.collectAsState()
    val actionPlan by viewModel.actionPlan.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val isAiThinking by viewModel.isAiThinking.collectAsState()

    val savedSchemes by viewModel.savedSchemes.collectAsState()
    val savedPlans by viewModel.savedPlans.collectAsState()
    val savedPartners by viewModel.savedPartners.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val emiLoan by viewModel.emiLoanAmount.collectAsState()
    val emiRate by viewModel.emiInterestRate.collectAsState()
    val emiTenure by viewModel.emiTenureYears.collectAsState()
    val emiMoratorium by viewModel.emiMoratoriumMonths.collectAsState()

    // Check if user is logged in (strictly true only when authenticated)
    val isLoggedIn = userProfile?.isLoggedIn == true
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()

    CompositionLocalProvider(LocalLanguage provides selectedLanguage) {
        if (isAdminLoggedIn) {
            com.example.ui.screens.AdminDashboardScreen(
                viewModel = viewModel,
                isDarkMode = isDarkMode
            )
            return@CompositionLocalProvider
        }

        if (!isLoggedIn) {
            LoginScreen(
                onLoginSuccess = { name, phone, email, age, gender, state, district, category, familyIncome ->
                    viewModel.registerAndLoginUser(
                        fullName = name,
                        phone = phone,
                        email = email,
                        age = age,
                        gender = gender,
                        state = state,
                        district = district,
                        socialCategory = category,
                        familyIncome = familyIncome
                    )
                },
                onAdminLogin = { email, pass ->
                    viewModel.adminLogin(email, pass)
                },
                isDarkMode = isDarkMode
            )
            return@CompositionLocalProvider
        }

        val rootTabs = listOf(
            Screen.Home.route,
            Screen.Schemes.route,
            Screen.Calculator.route,
            Screen.Partners.route,
            Screen.Profile.route
        )
        val isRootTab = currentRoute in rootTabs

        val currentScreenTitle: String? = when (currentRoute) {
            Screen.Home.route -> null
            Screen.Schemes.route -> SakshamStrings.get("schemes_heading", selectedLanguage)
            Screen.Calculator.route -> SakshamStrings.get("calculator_heading", selectedLanguage)
            Screen.Partners.route -> SakshamStrings.get("partners_heading", selectedLanguage)
            Screen.Profile.route -> SakshamStrings.get("profile_title", selectedLanguage)
            Screen.BusinessForm.route -> SakshamStrings.get("card_find_scheme_title", selectedLanguage)
            Screen.ActionPlan.route -> SakshamStrings.get("ai_business_action_plan", selectedLanguage)
            Screen.SchemeDetail.route -> selectedScheme?.shortName ?: SakshamStrings.get("schemes_heading", selectedLanguage)
            Screen.AIChat.route -> SakshamStrings.get("saksham_saathi_सक्षम_साथी", selectedLanguage)
            Screen.HelpContact.route -> SakshamStrings.get("help_title", selectedLanguage)
            else -> null
        }

        Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SakshamTopBar(
                title = currentScreenTitle,
                canNavigateBack = !isRootTab,
                onNavigateBack = { navController.popBackStack() },
                currentLanguage = selectedLanguage,
                onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
                isDarkMode = isDarkMode,
                onToggleDarkMode = { viewModel.toggleTheme() }
            )
        },
        bottomBar = {
            SakshamBottomBar(
                currentRoute = currentRoute,
                onNavigate = { screen ->
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                language = selectedLanguage
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    onSelectSchemeCategory = { _ ->
                        navController.navigate(Screen.Schemes.route)
                    },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }

            composable(Screen.Schemes.route) {
                val matchingSchemes = GovernmentDataRepository.getRecommendedSchemes(userProfile, businessProfile)
                val savedIds = savedSchemes.map { it.id }.toSet()

                SchemeRecommendationScreen(
                    schemes = matchingSchemes,
                    profile = businessProfile,
                    savedSchemeIds = savedIds,
                    onSelectScheme = { scheme -> viewModel.selectScheme(scheme) },
                    onToggleSave = { scheme -> viewModel.toggleSaveScheme(scheme) },
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }

            composable(Screen.Calculator.route) {
                EmiCalculatorScreen(
                    initialLoan = emiLoan,
                    initialRate = emiRate,
                    initialTenure = emiTenure,
                    initialMoratorium = emiMoratorium,
                    onParametersChanged = { l, r, t, m ->
                        viewModel.updateEmiParameters(l, r, t, m)
                    },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }

            composable(Screen.Partners.route) {
                val partners = GovernmentDataRepository.authorizedPartners
                val savedIds = savedPartners.map { it.id }.toSet()

                ChannelPartnerLocatorScreen(
                    partners = partners,
                    savedPartnerIds = savedIds,
                    onToggleSave = { partner -> viewModel.toggleSavePartner(partner) },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    userProfile = userProfile,
                    businessProfile = businessProfile,
                    savedSchemes = savedSchemes,
                    savedPlans = savedPlans,
                    savedPartners = savedPartners,
                    onSelectSchemeById = { id -> viewModel.selectSchemeById(id) },
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    onUpdateProfile = { name, phone, state, district, category, income, target ->
                        viewModel.updateProfileInfo(name, phone, state, district, category, income, target)
                    },
                    onUpdatePhoto = { photoUri ->
                        viewModel.updateProfilePhoto(photoUri)
                    },
                    onRemoveScheme = { id ->
                        val scheme = GovernmentDataRepository.getSchemeById(id)
                        if (scheme != null) viewModel.toggleSaveScheme(scheme)
                    },
                    onLogout = {
                        viewModel.logoutUser()
                    },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { viewModel.toggleTheme() }
                )
            }

            composable(Screen.BusinessForm.route) {
                BusinessFormScreen(
                    currentProfile = businessProfile,
                    onSaveProfile = { profile -> viewModel.updateBusinessProfile(profile) },
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }

            composable(Screen.ActionPlan.route) {
                ActionPlanScreen(
                    plan = actionPlan,
                    onSavePlan = { viewModel.saveCurrentPlan() },
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }

            composable(Screen.SchemeDetail.route) {
                val isSaved = savedSchemes.any { it.id == selectedScheme?.id }
                SchemeDetailScreen(
                    scheme = selectedScheme,
                    isSaved = isSaved,
                    onToggleSave = {
                        selectedScheme?.let { viewModel.toggleSaveScheme(it) }
                    },
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }

            composable(Screen.AIChat.route) {
                AIChatScreen(
                    messages = chatMessages,
                    isAiThinking = isAiThinking,
                    onSendMessage = { prompt -> viewModel.sendMessage(prompt) },
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }

            composable(Screen.HelpContact.route) {
                HelpContactScreen(
                    language = selectedLanguage,
                    isDarkMode = isDarkMode
                )
            }
        }
    }
}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Android") }
}
