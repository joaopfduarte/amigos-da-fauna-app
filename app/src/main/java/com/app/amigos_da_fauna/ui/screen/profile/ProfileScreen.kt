package com.app.amigos_da_fauna.ui.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.amigos_da_fauna.ui.components.ThemeSelector
import com.app.amigos_da_fauna.ui.theme.FaunaTheme

@Composable
fun ProfileScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onThemeChange: (com.app.amigos_da_fauna.domain.model.ThemePreference) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = FaunaTheme.colors

    if (uiState.isLoading) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        ) {
            CircularProgressIndicator(color = colors.primary)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.card),
        ) {
            Column(modifier = Modifier.padding(32.dp)) {
                Text("🌿", fontSize = 40.sp, modifier = Modifier.align(Alignment.CenterHorizontally))

                if (uiState.isAuthenticated && uiState.user != null) {
                    Text(
                        text = "Olá, ${uiState.user!!.name}!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.text,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                    Text(
                        text = uiState.user!!.email,
                        color = colors.textSecondary,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Preferência de tema", fontWeight = FontWeight.SemiBold, color = colors.text)
                    ThemeSelector(
                        selected = uiState.themePreference,
                        onSelected = {
                            viewModel.setThemePreference(it)
                            onThemeChange(it)
                        },
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                    Button(
                        onClick = { viewModel.logout() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Sair")
                    }
                } else {
                    Text(
                        text = "Entrar",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.text,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                    Text(
                        text = "Bem-vindo de volta!",
                        color = colors.textSecondary,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )

                    (uiState.validationError ?: uiState.apiError)?.let { error ->
                        Text(
                            text = error,
                            color = colors.error,
                            modifier = Modifier.padding(vertical = 12.dp),
                        )
                    }

                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("E-mail") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("Senha") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                    )
                    Button(
                        onClick = { viewModel.login(onLoginSuccess) },
                        enabled = !uiState.isSubmitting && uiState.email.isNotBlank() && uiState.password.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator()
                        } else {
                            Text("Entrar")
                        }
                    }
                    TextButton(
                        onClick = onRegisterClick,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    ) {
                        Text("Não tem uma conta? Cadastrar", color = colors.textSecondary)
                    }
                }
            }
        }
    }
}
