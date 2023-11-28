package com.example.foodie.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.foodie.R
import com.example.foodie.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


var uid = ""
@Composable
fun CartScreen(context: Context) {
    val uid = FirebaseAuth.getInstance().getCurrentUser()?.uid
    var cartItems by remember { mutableStateOf<List<CartItem>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("User").document(uid).collection("Cart")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val items = mutableListOf<CartItem>()
                        for (document in task.result!!) {
                            val name = document.getString("name") ?: ""
                            val image = document.getString("image_url") ?: ""
                            val description = document.getString("description") ?: ""
                            val price = document.getLong("price")?.toInt() ?: 0
                            val quantity = document.getLong("quatity")?.toInt() ?: 0
                            val item_id = document.getString("item_id") ?: ""

                            val item = CartItem(name, price, description, image, quantity, item_id)
                            items.add(item)
                        }
                        cartItems = items
                    }
                    loading = false
                }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (loading) {
            // Display a loading indicator while data is being fetched
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            // Data has been fetched, display the EditProfileScreen1
            // Render the UI with the loaded cart items
            Cart(cartItems,context)
        }
    }


}


@Composable
fun Cart(cartItems2: List<CartItem>,context: Context){
    var cartItems by remember { mutableStateOf<List<CartItem>>(cartItems2) }
    uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid() ?: ""
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (cartItems.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                items(cartItems) { cartItem ->
                    CartItemCard(cartItem = cartItem,onRemoveItem = {
                        FirebaseFirestore.getInstance().collection("User").document(uid).collection("Cart").document(cartItem.item_id).delete()
                        val updatedCartItems = cartItems.toMutableList()
                        updatedCartItems.remove(cartItem)
                        cartItems = updatedCartItems

                    })
                }

                // Add some spacing between the LazyColumn and the TotalAmountButton
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                val totalPrice = cartItems.sumBy { it.price * it.quatity }
                // Add the TotalAmountButton
                item {
                    TotalAmountButton(totalAmount = "$"+totalPrice, onClick = {
                        FirebaseFirestore.getInstance().collection("User").document(uid)
                        .collection("Cart").get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                for (document in task.result!!) {
                                    val docRef = FirebaseFirestore.getInstance().collection("User").document(uid)
                                        .collection("Cart").document(document.id)
                                    docRef.delete()
                                        .addOnSuccessListener {
                                            // Document successfully deleted
                                            cartItems = emptyList()
                                            Toast.makeText(context,"Items Have been Checkout",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                        .addOnFailureListener { e ->
                                            // Handle errors here
                                        }
                                }
                            } else {

                            }
                        }})
                }
            }
            Spacer(modifier = Modifier.height(100.dp))

        }else
        {
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.subtitle1
            )

        }


    }
}

@Composable
fun TotalAmountButton(totalAmount: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(start = 30.dp, end = 30.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#CF471E"))),
    ) {
        Text(
            text = "Pay Total Amount: $totalAmount",
            style = MaterialTheme.typography.button,
            color = Color.White
        )
    }
}


@Composable
fun CartItemCard(cartItem: CartItem, onRemoveItem: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color(android.graphics.Color.parseColor("#FBE1D9")),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image (Replace R.drawable.pizza with the actual resource ID for the image)
            Image(
                painter = rememberImagePainter(cartItem.image_url),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Product Details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = cartItem.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Price: ${cartItem.price}", fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))

            // Quantity Section
            Column(
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = "Quantity: ${cartItem.quatity}", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                IconButton(
                    onClick = {
                        onRemoveItem()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }
    }
}

