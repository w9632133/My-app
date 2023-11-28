package com.example.foodie.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.foodie.R

class SplashFragment : Fragment(R.layout.splash_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            SplashScreen()
            object : Thread() {
                override fun run() {
                    try {
                        sleep(5000)
                    } catch (e: Exception) {
                    } finally {
                        fragmentManager?.beginTransaction()
                            ?.add(R.id.main_activity_content, SignUpFragment())
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                }
            }.start()
        }
    }
    @Composable
    fun SplashScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your logo
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp) // Adjust the size as needed
                    .padding(16.dp)
            )
        }
    }


}