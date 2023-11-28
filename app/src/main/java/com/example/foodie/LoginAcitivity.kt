package com.example.foodie

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodie.components.*
import com.example.foodie.data.LoginViewModel
import com.example.foodie.ui.theme.FoodieTheme

class LoginAcitivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodieTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginScreen()
                }
            }
        }
    }

    @Composable
    fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(28.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    NormalTextComponent(value = stringResource(id = R.string.login))
                    HeadingTextComponent(value = stringResource(id = R.string.welcome))
                    Spacer(modifier = Modifier.height(20.dp))

                    MyTextFieldComponent(labelValue = stringResource(id = R.string.email),
                        painterResource(id = R.drawable.message),
                        onTextChanged = {
                            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.emailError
                    )

                    passwordFieldComponent(
                        labelValue = stringResource(id = R.string.password),
                        painterResource(id = R.drawable.lock),
                        onTextChanged = {
                            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
                        },
                        errorStatus = loginViewModel.loginUIState.value.passwordError
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Spacer(modifier = Modifier.height(40.dp))

                    ButtonComponent(
                        value = stringResource(id = R.string.login),
                        onButtonClicked = {
                            loginViewModel.initialiseContext(this@LoginAcitivity)
                            loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                        },
                        isEnabled = loginViewModel.allValidationsPassed.value
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent()

                    ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                        val intent = Intent(this@LoginAcitivity, SignUpAcitivity::class.java)
                        startActivity(intent)
                        finish()
                    })
                }
            }

            if(loginViewModel.loginInProgress.value) {
                CircularProgressIndicator()
            }
        }


    }

    @Preview
    @Composable
    fun LoginScreenPreview() {
        LoginScreen()
    }
}

