package com.example.foodie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodie.ui.theme.FoodieTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove


import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberImagePainter
import com.example.foodie.model.CartItem
import com.example.foodie.model.ItemModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class FoodItemActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("cost")
        val description = intent.getStringExtra("description")
        val image_url = intent.getStringExtra("image_url")
        val data = ItemModels(name = name ?: "", description = description ?: "", image_url = image_url ?: "", price = price?.toInt() ?: 0)
        setContent {
            FoodieTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FoodDetailScreen(data)
                }
            }
        }
    }

    @Composable
    fun FoodDetailScreen(foodItem: ItemModels) {
        var quantity by remember { mutableStateOf(1) }
        var totalPrice by remember { mutableStateOf(foodItem.price) }

        var isSnackbarVisible by remember { mutableStateOf(false) }
        val context = LocalContext.current

        val uriHandler = LocalUriHandler.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Food Image
            Image(
                painter = rememberImagePainter(foodItem.image_url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )

            // Food Name
            Text(
                text = foodItem.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Food Description
            Text(
                text = foodItem.description,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Food Price
            Text(
                text = foodItem.price.toString(),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Quantity Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        if (quantity > 1) {
                            quantity--
                            totalPrice = totalPrice + foodItem.price
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove, // Use the appropriate icon
                        contentDescription = "Remove"
                    )

                }

                Text(
                    text = "Quantity: $quantity",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(top = 4.dp)
                )

                IconButton(
                    onClick = {
                        quantity++
                        totalPrice = totalPrice + foodItem.price
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus), // Use the appropriate icon
                        contentDescription = "Remove"
                    )

                }
            }

            // Total Price
            Text(
                text = totalPrice.toString(),
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Add to Cart Button
            Button(
                onClick = {
                    Log.e("manan","hello world")
                    var uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid()
                    if (uid != null) {
                        val timestamp = System.currentTimeMillis()
                        val data = CartItem(name = foodItem.name, description = foodItem.description, quatity = quantity, price = foodItem.price, image_url = foodItem.image_url,
                            item_id = timestamp.toString())
                        FirebaseFirestore
                            .getInstance()
                            .collection("User")
                            .document(uid)
                            .collection("Cart")
                            .document(timestamp.toString())
                            .set(data).addOnCompleteListener {
                                val intent = Intent(this@FoodItemActivity, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                    }

                    val intent = Intent(this@FoodItemActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {

                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add to Cart")
            }

            // Snackbar
            if (isSnackbarVisible) {

            }
        }
    }

}

