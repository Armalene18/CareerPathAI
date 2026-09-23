package com.example.careerpathai.api

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val message: String,
    val userId: Int?,
    val fullName: String?,
    val email: String?
)