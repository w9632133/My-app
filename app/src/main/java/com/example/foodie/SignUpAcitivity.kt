package com.example.foodie

import android.app.Activity
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
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodie.components.*
import com.example.foodie.ui.theme.FoodieTheme

class SignUpAcitivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SignUpScreen()
                }
            }
        }
    }
    @Composable
    fun SignUpScreen(signupViewModel: SignupViewModel = viewModel()) {

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
                Column(modifier = Modifier.fillMaxSize()) {

                    NormalTextComponent(value = stringResource(id = R.string.hello))
                    HeadingTextComponent(value = stringResource(id = R.string.create_account))
                    Spacer(modifier = Modifier.height(20.dp))

                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.first_name),
                        painterResource(id = R.drawable.profile),
                        onTextChanged = {
                            signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.firstNameError
                    )

                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.last_name),
                        painterResource = painterResource(id = R.drawable.profile),
                        onTextChanged = {
                            signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.lastNameError
                    )

                    MyTextFieldComponent(
                        labelValue = stringResource(id = R.string.email),
                        painterResource = painterResource(id = R.drawable.message),
                        onTextChanged = {
                            signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
                        },
                        errorStatus = signupViewModel.registrationUIState.value.emailError
                    )

                    passwordFieldComponent(
                        labelValue = stringResource(id = R.string.password),
                        painterResource = painterResource(id = R.drawable.ic_lock),
                        onTextChanged = {
                            signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
                        },

                        errorStatus = signupViewModel.registrationUIState.value.passwordError
                    )

                    CheckboxComponent(value = stringResource(id = R.string.terms_and_conditions),
                        onTextSelected = {

                        },
                        onCheckedChange = {
                            signupViewModel.onEvent(SignupUIEvent.PrivacyPolicyCheckBoxClicked(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    ButtonComponent(
                        value = stringResource(id = R.string.register),
                        onButtonClicked = {
                            signupViewModel.initialiseContext(this@SignUpAcitivity)
                            signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
                        },
                        isEnabled = signupViewModel.allValidationsPassed.value
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent()

                    ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                                val intent = Intent(this@SignUpAcitivity, LoginAcitivity::class.java)
                                startActivity(intent)
                                // Finish the current activity if needed
                                finish()
                    })
                }

            }

            if(signupViewModel.signUpInProgress.value) {
                CircularProgressIndicator()
            }
        }


    }


}
