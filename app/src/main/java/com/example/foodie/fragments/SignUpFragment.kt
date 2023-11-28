package com.example.foodie.fragments

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.foodie.LoginFragment
import com.example.foodie.R


class SignUpFragment : Fragment(R.layout.sign_up_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.sign_up_compose_view).setContent {
            SignUpScreen()
        }
    }
    @Composable
    fun SignUpScreen() {
        // State variables for name, email, and password
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input fields for name, email, and password
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign-up button
            Button(
                onClick = {
                    println(name)
                    println(email)
                    println(password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Already have an account? Login button
            Text(
                text = "Already have an account? Log in",
                color = Color.Blue,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        fragmentManager?.beginTransaction()
                            ?.add(R.id.main_activity_content, LoginFragment())
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                    .padding(8.dp)
            )
        }
    }

    fun createUserInFirebase(email:String,password:String){

    }
}