package com.example.dreamscape.Views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dreamscape.ViewModels.AuthViewModel
import com.example.dreamscape.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var emailFocused by remember { mutableStateOf(false) }
    var passwordFocused by remember { mutableStateOf(false) }

    val currentUser by viewModel.currentUser.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            onAuthSuccess()
        }
    }

    val cardElevation by animateDpAsState(
        targetValue = if (isLoading) 4.dp else 12.dp,
        animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(
                    elevation = cardElevation,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color.Black.copy(alpha = 0.5f)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Title
                Text(
                    text = "DREAMSCAPE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = if (isLogin) "Welcome back" else "Create your account",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (emailFocused) 2.dp else 1.dp,
                            color = if (emailFocused) InputBorderFocused else InputBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = InputFieldBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = if (emailFocused) PrimaryBlue else TextTertiary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (email.isEmpty()) {
                            Text(
                                text = "Email",
                                color = TextTertiary,
                                fontSize = 15.sp
                            )
                        }
                        androidx.compose.foundation.text.BasicTextField(
                            value = email,
                            onValueChange = { email = it },
                            textStyle = TextStyle(
                                color = TextPrimary,
                                fontSize = 15.sp
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { emailFocused = it.isFocused }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (passwordFocused) 2.dp else 1.dp,
                            color = if (passwordFocused) InputBorderFocused else InputBorder,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = InputFieldBackground,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (passwordFocused) PrimaryBlue else TextTertiary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (password.isEmpty()) {
                            Text(
                                text = "Password",
                                color = TextTertiary,
                                fontSize = 15.sp
                            )
                        }
                        androidx.compose.foundation.text.BasicTextField(
                            value = password,
                            onValueChange = { password = it },
                            textStyle = TextStyle(
                                color = TextPrimary,
                                fontSize = 15.sp
                            ),
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { passwordFocused = it.isFocused }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Login/Register Button
                Button(
                    onClick = {
                        isLoading = true
                        errorMessage = null
                        scope.launch {
                            try {
                                if (isLogin) {
                                    viewModel.login(email, password)
                                } else {
                                    viewModel.register(email, password)
                                }
                            } catch (e: Exception) {
                                errorMessage = e.message
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = Color.White,
                        disabledContainerColor = ButtonDisabled,
                        disabledContentColor = TextDisabled
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp,
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = if (isLogin) "LOGIN" else "REGISTER",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Animated error message
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    errorMessage?.let {
                        Column {
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ErrorRed.copy(alpha = 0.15f))
                                    .border(
                                        1.dp,
                                        ErrorRed.copy(alpha = 0.4f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = it,
                                    color = ErrorRed,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Toggle between login and register
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isLogin) "Don't have an account?" else "Already have an account?",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isLogin) "Sign up" else "Sign in",
                        fontSize = 14.sp,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            isLogin = !isLogin
                            errorMessage = null
                        }
                    )
                }
            }
        }
    }
}