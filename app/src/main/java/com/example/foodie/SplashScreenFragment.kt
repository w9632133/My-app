package com.example.foodie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.foodie.fragments.SignUpFragment
import com.google.firebase.auth.FirebaseAuth

class SplashScreenFragment : Fragment(R.layout.home_fragment){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.home_compose_view).setContent {
            SplashScreen()
            object : Thread() {
                override fun run() {
                    try {
                        sleep(5000)
                    } catch (e: Exception) {
                    } finally {
//                        fragmentManager?.beginTransaction()
//                            ?.add(R.id.main_activity_content, SignUpFragment())
//                            ?.addToBackStack(null)
//                            ?.commit()
                        if(isUserLoggedIn()){
                            val intent = Intent(requireContext(), DashboardActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }else{
                            val intent = Intent(requireContext(), SignUpAcitivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }


                        // Finish the current activity if needed
//                        activity?.finish()
                    }
                }
            }.start()
        }
    }


    fun isUserLoggedIn(): Boolean {
            val currentUser = FirebaseAuth.getInstance().currentUser
            return currentUser != null
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