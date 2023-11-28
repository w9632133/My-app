package com.example.foodie.screens


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodie.R
import com.google.firebase.auth.FirebaseAuth
import coil.compose.rememberImagePainter
import com.example.foodie.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.content.ContextCompat

import java.io.File
import java.text.SimpleDateFormat


@Composable
fun Accounts() {
    var profilePicture by remember { mutableStateOf(R.drawable.profile_picture) }
    var name by remember { mutableStateOf(TextFieldValue("John Doe")) }
    var email by remember { mutableStateOf(TextFieldValue("john.doe@example.com")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("+1 (555) 123-4567")) }
    var notifications by remember { mutableStateOf(TextFieldValue("Notification")) }
    var privacy by remember { mutableStateOf(TextFieldValue("Privacy")) }
    var help by remember { mutableStateOf(TextFieldValue("Help Center")) }
    var about by remember { mutableStateOf(TextFieldValue("About Us")) }
    var image_url by remember {
        mutableStateOf(TextFieldValue("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"))
    }
    val firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser
    LaunchedEffect(user?.displayName) {
       val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid()
        if (uid != null) {
            FirebaseFirestore
                .getInstance()
                .collection("User")
                .document(uid)
                .get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        // DocumentSnapshot data may be null if the document doesn't exist
                        val userData = document.toObject(UserModel::class.java)
                        if (userData != null) {
                            name = TextFieldValue(userData.name)
                            phoneNumber = TextFieldValue(userData.phone)
                            email = TextFieldValue(userData.email)
                            image_url = TextFieldValue(userData.image_url)
                        }
                    } else {
                        // The document does not exist
                    }
                }
        }
    }

    val painter = rememberImagePainter("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Section with Profile Picture and Edit Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable {

                    },
                contentScale = ContentScale.Crop

            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .clickable { /* Handle edit profile picture */ }
                    .padding(8.dp)
            )
        }

        // User Information
        UserInfo(
            name = name.text,
            email = email.text,
            phoneNumber = phoneNumber.text
        )

        // Divider
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f))

        // Profile Items
        val profileItems = listOf(
            ProfileItem(R.drawable.user,"Name",name.text),
            ProfileItem(R.drawable.email, "Email", email.text),
            ProfileItem(R.drawable.phone, "Phone", phoneNumber.text),
        )

        LazyColumn {
            items(profileItems) { item ->
                ProfileItemRow(item)
            }
        }


        // Divider
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f))

        val profileItems2 = listOf(
            ProfileItem(R.drawable.lock, "Privacy", privacy.text),
            ProfileItem(R.drawable.question, "Help Center", help.text),
            ProfileItem(R.drawable.information, "About Us", about.text),
        )

        LazyColumn {
            items(profileItems2) { item ->
                ProfileBottomRow(item)
            }
        }

        Button(onClick = { /*TODO*/ },

            Modifier
                .fillMaxWidth()
                .height(66.dp)
                .padding(start = 68.dp, end = 62.dp, top = 20.dp, bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#CF471E"))),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Logout",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

data class ProfileItem(val iconResId: Int, val title: String, val value: String)



@Composable
fun ProfileItemRow(profileItem: ProfileItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomIcon(iconResId = profileItem.iconResId)
            Text(text = profileItem.title)
        }
        Text(text = profileItem.value, color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))
    }
}


@Composable
fun UserInfo(name: String, email: String, phoneNumber: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(text = email, style = MaterialTheme.typography.body1)
        Text(text = phoneNumber, style = MaterialTheme.typography.body1)
    }
}


@Composable
fun CustomIcon(iconResId: Int) {
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        tint = MaterialTheme.colors.onSurface,
        modifier = Modifier.size(24.dp)
    )
}


@Composable
fun ProfileBottomRow(profileItem2: ProfileItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomIcon(iconResId = profileItem2.iconResId)
            Text(text = profileItem2.value)
        }
    }
}

