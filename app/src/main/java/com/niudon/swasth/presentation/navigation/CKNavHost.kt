package com.niudon.swasth.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.niudon.swasth.presentation.RegisterScreen
import com.niudon.swasth.presentation.contacts.ContactsScreen
import com.niudon.swasth.presentation.home.HomeScreen
import com.niudon.swasth.presentation.login.ForgotPassword
import com.niudon.swasth.presentation.login.LoginScreen
import com.niudon.swasth.presentation.login.RegisterMethod
import com.niudon.swasth.presentation.login.SignInMethod
import com.niudon.swasth.presentation.main.MainScreen
import com.niudon.swasth.presentation.onboarding.OnboardingScreen
import com.niudon.swasth.presentation.onboarding.Review
import com.niudon.swasth.presentation.onboarding.Signature
import com.niudon.swasth.presentation.onboarding.WelcomeScreen
import com.niudon.swasth.presentation.profile.ProfileScreen
import com.niudon.swasth.presentation.profile.ReviewConsent
import com.niudon.swasth.presentation.tasks.TasksScreen
import com.niudon.swasth.presentation.welcome.JoinStudyScreen

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun CKNavHost(navController: NavHostController, startDestination: String) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(Screens.OnboardingScreen.route) {
            OnboardingScreen(navController = navController)
        }
        composable(Screens.WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Screens.Review.route) {
            Review(navController = navController)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(Screens.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screens.RegisterMethod.route) {
            RegisterMethod(navController = navController)
        }
        composable(Screens.MainScreen.route) {
            MainScreen()
        }
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.TasksScreen.route) {
            TasksScreen()
        }
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screens.ContactsScreen.route) {
            ContactsScreen()
        }
        composable(Screens.ReviewConsent.route) {
            ReviewConsent(navController = navController)
        }
        composable(Screens.SignatureScreen.route) {
            Signature(navController = navController)
        }
        composable(Screens.SignInMethod.route) {
            SignInMethod(navController = navController)
        }
        composable(Screens.JoinStudyScreen.route) {
            JoinStudyScreen(navController = navController)
        }
        composable(Screens.ForgotPasswordScreen.route) {
            ForgotPassword(navController = navController)
        }
    }
}
