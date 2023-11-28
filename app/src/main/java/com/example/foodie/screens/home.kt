package com.example.foodie.screens

import android.content.Context
import android.content.Intent

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import coil.compose.rememberImagePainter
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.foodie.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

import androidx.navigation.NavController
import com.example.foodie.FoodItemActivity
import com.example.foodie.SignUpAcitivity

@Composable
fun Home(context : Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(enabled = true, state = rememberScrollState()),
    ) {

        Text(
            text = "Categories",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier
                .padding(13.dp)
                .fillMaxWidth()
                .align(Alignment.Start)
        )
        val listState = rememberLazyListState()
        val imagesWithNames = listOf(
            Pair(painterResource(id = R.drawable.pizza), "Pizza"),
            Pair(painterResource(id = R.drawable.burger), "Burger"),
            Pair(painterResource(id = R.drawable.sandwich), "SandWich"),
            Pair(painterResource(id = R.drawable.drinks), "Drinks"),
            Pair(painterResource(id = R.drawable.salad), "Salad"),
        )
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
        ) {
            items(imagesWithNames) { (painter, name) ->
                ImageWithName(imagePainter = painter, name = name)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(13.dp)
                    .width(120.dp)
            )
            Text(
                text = "All",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Right
                ),
                modifier = Modifier
                    .padding(13.dp)
                    .width(40.dp)
            )
        }

        // Featured Categories Row
        val featuredCategories = listOf(
            FeaturedCategory2(
                "beef Pizza",
                "5",
                "17",
                "No",
                "https://hebbarskitchen.com/wp-content/uploads/2020/11/chole-bhature-recipe-chhole-bhature-chana-bhatura-chola-batura-1-1024x682.jpeg"
            ),
            FeaturedCategory2(
                "Steak Pizza",
                "4",
                "12",
                "No",
                "https://hebbarskitchen.com/wp-content/uploads/2020/11/chole-bhature-recipe-chhole-bhature-chana-bhatura-chola-batura-1-1024x682.jpeg"
            ),
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(featuredCategories) { category ->
                FeaturedCategoryCard2(category = category,context = context)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recomended",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(13.dp)
                    .width(120.dp)
            )
            Text(
                text = "All",
                style = MaterialTheme.typography.subtitle1.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    textAlign = TextAlign.Right
                ),
                modifier = Modifier
                    .padding(13.dp)
                    .width(40.dp)
            )
        }

        val featuredCategories2 = remember {
            mutableStateListOf<FeaturedCategory2>()
        }
        FirebaseFirestore.getInstance().collection("Popular").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val name = document.getString("name") ?: ""
                    val cost = document.get("price")?.toString()
                    val imageUrl = document.getString("image_url") ?: ""
                    val description = document.getString("description") ?: ""
                    val rating = Random.nextInt(1, 6)
                    val category = FeaturedCategory2(
                        categoryName = name,
                        rating = rating.toString(),
                        cost = cost.toString(),
                        description = description,
                        image_url = imageUrl
                    )
                    featuredCategories2.add(category)
                }
            } else {
                // Handle the error if the data retrieval is not successful
                Log.e("Firestore", "Error getting documents: ", task.exception)
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(featuredCategories2) { category ->
                FeaturedCategoryCard2(category = category,context = context)
            }
        }

    }










}

@Composable
fun ImageWithName(imagePainter: Painter, name: String) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = name,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 2.dp),
        )
    }
}

data class FeaturedCategory2(
    val categoryName: String,
    val rating: String,
    val cost: String,
    val description: String,
    val image_url: String
)

data class FeaturedCategory(
    val imagePainter: Painter,
    val categoryName: String,
    val rating: String,
    val cost: String
)

@Composable
fun FeaturedCategoryCard2(category: FeaturedCategory2,context: Context) {
    val localContext = LocalContext.current
    Card(
        modifier = Modifier
            .width(155.dp)
            .height(190.dp)
            .shadow(4.dp, shape = RoundedCornerShape(12.dp))
            .clickable {


            }, // Add shadow here
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp // Set elevation to 0.dp as shadow is handled separately
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(android.graphics.Color.parseColor("#FBE1D9")))
                .clickable {
                    val intent = Intent(context, FoodItemActivity::class.java)
                    intent.putExtra("name",category.categoryName)
                    intent.putExtra("cost",category.cost)
                    intent.putExtra("description",category.description)
                    intent.putExtra("image_url",category.image_url)
                    localContext.startActivity(intent)
                           },
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = rememberImagePainter(category.image_url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(start = 18.dp, end = 18.dp, top = 18.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.categoryName.take(8),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .width(70.dp)
                )

                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp), // Adjust spacing as needed
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.rating,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            textAlign = TextAlign.Right
                        ),
                        modifier = Modifier.width(1.dp)
                    )

                    // Replace Icons.Default.Star with the desired icon
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Star Icon",
                        tint = Color.Yellow,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.cost,
                        style = MaterialTheme.typography.subtitle1.copy(
                            fontSize = 15.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier
                            .width(50.dp)
                            .padding(start = 15.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.dollar),
                        contentDescription = "Dollar Icon",
                        tint = Color.Red,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
