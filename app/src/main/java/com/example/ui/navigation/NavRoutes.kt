package com.example.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Schemes : Screen("schemes", "Schemes")
    object Calculator : Screen("calculator", "EMI Calculator")
    object Partners : Screen("partners", "Partners")
    object Profile : Screen("profile", "Profile")
    object BusinessForm : Screen("business_form", "Business Details")
    object ActionPlan : Screen("action_plan", "AI Action Plan")
    object SchemeDetail : Screen("scheme_detail", "Scheme Details")
    object AIChat : Screen("ai_chat", "AI Assistant")
    object Login : Screen("login", "Login")
    object HelpContact : Screen("help_contact", "Help & Contact")
}
