package com.example.careerpathai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.careerpathai.api.LoginRequest
import com.example.careerpathai.api.RegisterRequest
import com.example.careerpathai.api.RetrofitClient
import com.example.careerpathai.ui.theme.CareerPathAITheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CareerPathAITheme {
                CareerPathAIApp()
            }
        }
    }
}

@Composable
fun CareerPathAIApp() {

    var currentScreen by remember {
        mutableStateOf("login")
    }

    var careerResults by remember {
        mutableStateOf(
            listOf(
                "Software Developer" to 0,
                "Business Analyst" to 0,
                "Data Analyst" to 0
            )
        )
    }

    when (currentScreen) {

        // =========================
        // LOGIN
        // =========================
        "login" -> {
            LoginScreen(
                onLoginSuccess = {
                    currentScreen = "dashboard"
                },
                onRegisterClick = {
                    currentScreen = "register"
                }
            )
        }

        // =========================
        // REGISTER
        // =========================
        "register" -> {
            RegisterScreen(
                onRegistrationSuccess = {
                    currentScreen = "login"
                },
                onBackToLogin = {
                    currentScreen = "login"
                }
            )
        }

        // =========================
        // DASHBOARD
        // =========================
        "dashboard" -> {
            DashboardScreen(
                onStartAssessment = {
                    currentScreen = "assessment"
                },
                onSettingsClick = {
                    currentScreen = "settings"
                }
            )
        }

        // =========================
        // ASSESSMENT
        // =========================
        "assessment" -> {
            CareerAssessmentScreen(
                onFinished = { answers ->

                    careerResults = calculateCareerResults(answers)

                    currentScreen = "results"
                }
            )
        }

        // =========================
        // RESULTS
        // =========================
        "results" -> {
            CareerResultsScreen(
                results = careerResults,
                onBackToDashboard = {
                    currentScreen = "dashboard"
                }
            )
        }

        // =========================
        // SETTINGS
        // =========================
        "settings" -> {
            SettingsScreen(
                onLogout = {
                    currentScreen = "login"
                },
                onBack = {
                    currentScreen = "dashboard"
                }
            )
        }
    }
}


// ============================================================
// LOGIN SCREEN
// ============================================================

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    val careerBlue = Color(0xFF2563EB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Sign in to continue your career journey",
            fontSize = 16.sp,
            color = Color(0xFF64748B)
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = ""
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TextButton(
            onClick = {
                errorMessage =
                    "Password recovery will be available after authentication is connected."
            }
        ) {

            Text(
                text = "Forgot Password?",
                color = careerBlue
            )
        }

        if (errorMessage.isNotEmpty()) {

            Text(
                text = errorMessage,
                color = Color(0xFFDC2626),
                fontSize = 14.sp,
                modifier = Modifier.padding(
                    bottom = 12.dp
                )
            )
        }

        Button(
            onClick = {

                when {

                    email.isBlank() -> {
                        errorMessage =
                            "Please enter your email address."
                    }

                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches() -> {

                        errorMessage =
                            "Please enter a valid email address."
                    }

                    password.isBlank() -> {
                        errorMessage =
                            "Please enter your password."
                    }

                    else -> {

                        errorMessage = ""
                        isLoading = true

                        coroutineScope.launch {

                            try {

                                Log.d(
                                    "CareerPathAI",
                                    "Attempting login for: $email"
                                )

                                val response =
                                    RetrofitClient.authApi.login(
                                        LoginRequest(
                                            email = email.trim(),
                                            password = password
                                        )
                                    )

                                if (response.isSuccessful) {

                                    Log.d(
                                        "CareerPathAI",
                                        "Login successful"
                                    )

                                    isLoading = false

                                    onLoginSuccess()

                                } else {

                                    isLoading = false

                                    errorMessage =
                                        when (response.code()) {

                                            401 ->
                                                "Invalid email or password."

                                            400 ->
                                                "Please check your login details."

                                            else ->
                                                "Login failed. Please try again."
                                        }

                                    Log.e(
                                        "CareerPathAI",
                                        "Login failed: ${response.code()}"
                                    )
                                }

                            } catch (e: Exception) {

                                isLoading = false

                                errorMessage =
                                    "Unable to connect to the server. Make sure the API is running."

                                Log.e(
                                    "CareerPathAI",
                                    "Login error",
                                    e
                                )
                            }
                        }
                    }
                }
            },

            enabled = !isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = careerBlue
            )
        ) {

            Text(
                text = if (isLoading) "LOGGING IN..." else "LOGIN",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Don't have an account? Register",
                color = careerBlue
            )
        }
    }
}


// ============================================================
// REGISTRATION SCREEN
// ============================================================

@Composable
fun RegisterScreen(
    onRegistrationSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {

    var fullName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    val careerBlue = Color(0xFF2563EB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Create Account",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Start your career journey with CareerPath AI",
            fontSize = 16.sp,
            color = Color(0xFF64748B)
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = {
                fullName = it
                errorMessage = ""
            },
            label = {
                Text("Full Name")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = ""
            },
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = ""
            },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                errorMessage = ""
            },
            label = {
                Text("Confirm Password")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (errorMessage.isNotEmpty()) {

            Text(
                text = errorMessage,
                color = Color(0xFFDC2626),
                fontSize = 14.sp,
                modifier = Modifier.padding(
                    bottom = 12.dp
                )
            )
        }

        Button(
            onClick = {

                when {

                    fullName.isBlank() -> {
                        errorMessage =
                            "Please enter your full name."
                    }

                    email.isBlank() -> {
                        errorMessage =
                            "Please enter your email address."
                    }

                    !android.util.Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches() -> {

                        errorMessage =
                            "Please enter a valid email address."
                    }

                    password.length < 8 -> {
                        errorMessage =
                            "Password must contain at least 8 characters."
                    }

                    password != confirmPassword -> {
                        errorMessage =
                            "Passwords do not match."
                    }

                    else -> {

                        errorMessage = ""
                        isLoading = true

                        coroutineScope.launch {

                            try {

                                Log.d(
                                    "CareerPathAI",
                                    "Attempting registration for: $email"
                                )

                                val response =
                                    RetrofitClient.authApi.register(
                                        RegisterRequest(
                                            fullName = fullName.trim(),
                                            email = email.trim(),
                                            password = password
                                        )
                                    )

                                if (response.isSuccessful) {

                                    Log.d(
                                        "CareerPathAI",
                                        "Registration successful"
                                    )

                                    isLoading = false

                                    onRegistrationSuccess()

                                } else {

                                    isLoading = false

                                    errorMessage =
                                        when (response.code()) {

                                            409 ->
                                                "An account with this email already exists."

                                            400 ->
                                                "Please check your registration details."

                                            else ->
                                                "Registration failed. Please try again."
                                        }

                                    Log.e(
                                        "CareerPathAI",
                                        "Registration failed: ${response.code()}"
                                    )
                                }

                            } catch (e: Exception) {

                                isLoading = false

                                errorMessage =
                                    "Unable to connect to the server. Make sure the API is running."

                                Log.e(
                                    "CareerPathAI",
                                    "Registration error",
                                    e
                                )
                            }
                        }
                    }
                }
            },

            enabled = !isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = careerBlue
            )
        ) {

            Text(
                text =
                    if (isLoading)
                        "CREATING ACCOUNT..."
                    else
                        "CREATE ACCOUNT",

                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Already have an account? Login",
                color = careerBlue
            )
        }
    }
}


// ============================================================
// DASHBOARD SCREEN
// ============================================================

@Composable
fun DashboardScreen(
    onStartAssessment: () -> Unit,
    onSettingsClick: () -> Unit
) {

    val careerBlue = Color(0xFF2563EB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp),

        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Welcome to CareerPath AI",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Discover career paths that match your interests, skills and goals.",
            fontSize = 16.sp,
            color = Color(0xFF64748B)
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Button(
            onClick = onStartAssessment,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = careerBlue
            )
        ) {

            Text(
                text = "START CAREER ASSESSMENT",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = onSettingsClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF475569)
            )
        ) {

            Text(
                text = "SETTINGS",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ============================================================
// SETTINGS SCREEN
// ============================================================

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    onBack: () -> Unit
) {

    val careerBlue = Color(0xFF2563EB)

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    var darkModeEnabled by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {

        Text(
            text = "Settings",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Manage your CareerPath AI preferences.",
            fontSize = 16.sp,
            color = Color(0xFF64748B)
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = "Notifications",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Receive career notifications",
                fontSize = 16.sp,
                color = Color(0xFF334155)
            )

            Switch(
                checked = notificationsEnabled,

                onCheckedChange = {
                    notificationsEnabled = it
                }
            )
        }

        Text(
            text = "Appearance",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Dark Mode",
                fontSize = 16.sp,
                color = Color(0xFF334155)
            )

            Switch(
                checked = darkModeEnabled,

                onCheckedChange = {
                    darkModeEnabled = it
                }
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onLogout,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDC2626)
            )
        ) {

            Text(
                text = "LOGOUT",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Back to Dashboard",
                color = careerBlue
            )
        }
    }
}


// ============================================================
// CAREER ASSESSMENT SCREEN
// ============================================================

@Composable
fun CareerAssessmentScreen(
    onFinished: (List<String>) -> Unit
) {

    val careerBlue = Color(0xFF2563EB)

    var currentQuestion by remember {
        mutableStateOf(0)
    }

    var selectedAnswer by remember {
        mutableStateOf("")
    }

    var answers by remember {
        mutableStateOf(listOf<String>())
    }

    val questions = listOf(

        "Which area interests you the most?" to
                listOf(
                    "Technology",
                    "Business",
                    "Design and Creativity",
                    "Healthcare"
                ),

        "What type of work do you enjoy?" to
                listOf(
                    "Solving problems",
                    "Working with people",
                    "Working with data",
                    "Creating new ideas"
                ),

        "Which skill would you like to develop?" to
                listOf(
                    "Programming",
                    "Communication",
                    "Data Analysis",
                    "Leadership"
                ),

        "What work environment do you prefer?" to
                listOf(
                    "Office",
                    "Remote",
                    "Hybrid",
                    "Flexible"
                )
    )

    val question = questions[currentQuestion]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {

        Text(
            text = "Career Assessment",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Question ${currentQuestion + 1} of ${questions.size}",
            fontSize = 15.sp,
            color = careerBlue,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Text(
            text = question.first,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        question.second.forEach { answer ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selectedAnswer == answer,

                    onClick = {
                        selectedAnswer = answer
                    }
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = answer,
                    fontSize = 16.sp,
                    color = Color(0xFF334155)
                )
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {

                if (selectedAnswer.isNotEmpty()) {

                    val updatedAnswers =
                        answers + selectedAnswer

                    if (currentQuestion < questions.lastIndex) {

                        answers = updatedAnswers

                        currentQuestion++

                        selectedAnswer = ""

                    } else {

                        onFinished(updatedAnswers)
                    }
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = careerBlue
            )
        ) {

            Text(
                text =
                    if (currentQuestion == questions.lastIndex)
                        "VIEW RESULTS"
                    else
                        "NEXT",

                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


// ============================================================
// CAREER RESULT CALCULATION
// ============================================================

fun calculateCareerResults(
    answers: List<String>
): List<Pair<String, Int>> {

    var softwareDeveloper = 0
    var businessAnalyst = 0
    var dataAnalyst = 0

    answers.forEach { answer ->

        when (answer) {

            "Technology" -> {
                softwareDeveloper += 30
                dataAnalyst += 10
            }

            "Business" -> {
                businessAnalyst += 30
            }

            "Design and Creativity" -> {
                softwareDeveloper += 10
                businessAnalyst += 10
            }

            "Solving problems" -> {
                softwareDeveloper += 20
                businessAnalyst += 20
            }

            "Working with people" -> {
                businessAnalyst += 20
            }

            "Working with data" -> {
                dataAnalyst += 30
                businessAnalyst += 10
            }

            "Creating new ideas" -> {
                softwareDeveloper += 10
                businessAnalyst += 10
            }

            "Programming" -> {
                softwareDeveloper += 30
            }

            "Communication" -> {
                businessAnalyst += 20
            }

            "Data Analysis" -> {
                dataAnalyst += 30
            }

            "Leadership" -> {
                businessAnalyst += 20
            }

            "Remote" -> {
                softwareDeveloper += 10
                dataAnalyst += 10
            }

            "Hybrid" -> {
                softwareDeveloper += 10
                businessAnalyst += 10
                dataAnalyst += 10
            }

            "Flexible" -> {
                softwareDeveloper += 10
                dataAnalyst += 10
            }
        }
    }

    return listOf(

        "Software Developer" to softwareDeveloper,

        "Business Analyst" to businessAnalyst,

        "Data Analyst" to dataAnalyst

    ).sortedByDescending {
        it.second
    }
}


// ============================================================
// CAREER RESULTS SCREEN
// ============================================================

@Composable
fun CareerResultsScreen(
    results: List<Pair<String, Int>>,
    onBackToDashboard: () -> Unit
) {

    val careerBlue = Color(0xFF2563EB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {

        Text(
            text = "Your Career Results",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Your results are based on the answers you provided.",
            fontSize = 16.sp,
            color = Color(0xFF64748B)
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        results.forEach { result ->

            CareerResultCard(
                career = result.first,
                score = result.second,
                careerBlue = careerBlue
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "These results are guidance to help you explore possible career paths.",
            fontSize = 13.sp,
            color = Color(0xFF64748B)
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        TextButton(
            onClick = onBackToDashboard,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Back to Dashboard",
                color = careerBlue
            )
        }
    }
}


// ============================================================
// CAREER RESULT CARD
// ============================================================

@Composable
fun CareerResultCard(
    career: String,
    score: Int,
    careerBlue: Color
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White,
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {

        Text(
            text = career,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = careerBlue
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Match score: $score%",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = when (career) {

                "Software Developer" ->
                    "A technology-focused career involving programming, software design and problem solving."

                "Business Analyst" ->
                    "A career focused on understanding business needs, analysing processes and communicating solutions."

                else ->
                    "A career focused on analysing information, identifying patterns and supporting data-driven decisions."
            },

            fontSize = 14.sp,
            color = Color(0xFF475569)
        )
    }
}