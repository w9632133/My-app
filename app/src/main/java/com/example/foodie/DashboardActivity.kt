package com.example.foodie

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.fragment.app.FragmentActivity
import com.example.foodie.ui.theme.FoodieTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodie.model.ItemModels
import com.example.foodie.model.UserModel
import com.example.foodie.screens.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*

import androidx.compose.ui.text.input.TextFieldValue
import coil.compose.rememberImagePainter
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foodie.screens.Cart
import com.example.foodie.screens.CartScreen
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*


class DashboardActivity : FragmentActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var temp_image_url: TextFieldValue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodieTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ){
                    Navigation()
                }
            }
        }
    }

    private fun openCamera() {
        if (checkPermissionsCamera()) {
            if (isCameraPermissionEnabled()) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                } catch (e: ActivityNotFoundException) {
                    // display error state to the user
                }}
        }
        else{
            requestCameraPermission()
        }
    }
    private val CAMERA_PERMISSION_REQUEST_CODE = 123
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun checkPermissionsCamera(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isCameraPermissionEnabled(): Boolean {
        val permission = Manifest.permission.CAMERA
        val result = ContextCompat.checkSelfPermission(this, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val storage = FirebaseStorage.getInstance()
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //binding.image.setImageBitmap(imageBitmap)
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child("images").child(fileName)
            // Convert the Bitmap to a byte array
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            // Upload the image to Firebase Storage
            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()

                        Log.d("MainActivity", "Download URL: $downloadUrl")
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
                                            userData.image_url = downloadUrl
                                            FirebaseFirestore.getInstance().collection("User").document(uid).set(userData)
                                        }
                                    } else {
                                        // The document does not exist
                                    }
                                }

                        }
                        temp_image_url = TextFieldValue(downloadUrl)
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                        // You can save the downloadUrl or use it to display the image later
                    } } else {
                    // Image upload failed
                    val exception = task.exception
                    // Handle the exception
                }
            }
        }
    }


    @Composable
    fun NavigationController(navController: NavHostController) {
        NavHost(navController = navController, startDestination = NavigationItem.Home.route) {

            composable(NavigationItem.Home.route) {
                Home(this@DashboardActivity)
            }

            composable(NavigationItem.Notifications.route) {
                CartScreen(this@DashboardActivity)
            }

            composable(NavigationItem.Settings.route) {
                Settings()
            }

            composable(NavigationItem.Account.route) {
                Accounts()
            }

        }

    }



    @Composable
    fun ProfileTopBar(profilePicture: Painter, name: String) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            elevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    Image(
                        painter = profilePicture,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Spacer
                Spacer(modifier = Modifier.width(8.dp))

                // User Name
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.subtitle1.copy(fontSize = 16.sp),
                        color = Color.Black
                    )
                }
            }
        }

    }

    @Composable
    fun Navigation() {

        val navController = rememberNavController()

        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Settings,
            NavigationItem.Notifications,
            NavigationItem.Account
        )
        val profilePicture: Painter = painterResource(id = R.drawable.profile_picture)
        val name = "Hi, " +"John Doe"


        Scaffold(topBar = {
            ProfileTopBar(profilePicture = profilePicture, name = name)
        },
            bottomBar = {
                BottomNavigation(backgroundColor = MaterialTheme.colors.background) {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route


                    items.forEach {
                        BottomNavigationItem(selected = currentRoute == it.route,
                            label = {
                                Text(
                                    text = it.label,
                                    color = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = it.icons, contentDescription = null,
                                    tint = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                                )

                            },

                            onClick = {
                                if(currentRoute!=it.route){

                                    navController.graph?.startDestinationRoute?.let {
                                        navController.popBackStack(it,true)
                                    }

                                    navController.navigate(it.route){
                                        launchSingleTop = true
                                    }

                                }

                            })

                    }


                }


            }) {

            NavigationController(navController = navController)

        }

    }


    @Composable
    fun Notifications() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Notifications")

            }
        }
    }

    @Composable
    fun Settings() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Settings")

            }
        }
    }




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
        temp_image_url = TextFieldValue("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2")
        var image_url by remember {
            mutableStateOf(temp_image_url)
        }
        fun resetProfileImage(uri:String) {
            image_url = TextFieldValue(uri)
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

        val painter = rememberImagePainter(image_url.text)

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
                            openCamera()
                        },
                    contentScale = ContentScale.Crop

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





}

