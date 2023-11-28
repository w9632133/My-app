package com.example.foodie.model


data class CartItem(
    val name: String,
    val price: Int,
    val description: String,
    val image_url: String,
    val quatity : Int,
    val item_id : String
){
    fun displayInfo() {
        println("Name: $name, Price: $price, Description: $description, Image URL: $image_url")
    }
}

