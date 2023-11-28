
package com.example.foodie
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.foodie.R

class LoginFragment : Fragment(R.layout.login_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            LoginScreen()
        }
    }
    @Composable
    fun LoginScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Log In",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input fields for email and password
            OutlinedTextField(
                value = "johndoe@example.com", // Replace with state
                onValueChange = { /* Handle email input */ },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

            OutlinedTextField(
                value = "********", // Replace with state
                onValueChange = { /* Handle password input */ },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login button
            Button(
                onClick = { /* Handle login button click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Log In")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot Password? Link
            Text(
                text = "Forgot your password?",
                color = Color.Blue,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* Handle "Forgot Password?" link click */ }
                    .padding(8.dp)
            )
        }
    }

}