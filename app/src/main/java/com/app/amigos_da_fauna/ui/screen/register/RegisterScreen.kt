package com.app.amigos_da_fauna.ui.screen.register

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
import com.app.amigos_da_fauna.ui.theme.FaunaTheme

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = FaunaTheme.colors

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
                Text(
                    text = "Criar conta",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.text,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Text(
                    text = "Junte-se aos Amigos da Fauna",
                    color = colors.textSecondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                if (uiState.success) {
                    Text(
                        text = "Conta criada com sucesso!",
                        fontWeight = FontWeight.Bold,
                        color = colors.text,
                        modifier = Modifier.padding(top = 24.dp),
                    )
                    Text(
                        text = "Redirecionando para login…",
                        color = colors.textSecondary,
                    )
                } else {
                    (uiState.validationError ?: uiState.apiError)?.let { error ->
                        Text(text = error, color = colors.error, modifier = Modifier.padding(top = 12.dp))
                    }

                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = viewModel::onNameChange,
                        label = { Text("Nome") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = uiState.email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("E-mail") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
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
                        onClick = { viewModel.register(onSuccess) },
                        enabled = !uiState.isSubmitting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator()
                        } else {
                            Text("Cadastrar")
                        }
                    }
                    TextButton(
                        onClick = onLoginClick,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    ) {
                        Text("Já tem conta? Entrar", color = colors.textSecondary)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
