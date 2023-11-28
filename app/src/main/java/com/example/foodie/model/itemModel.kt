package com.example.foodie.model


data class ItemModels(
    val name: String,
    val price: Int,
    val description: String,
    val image_url: String
){
    fun displayInfo() {
        println("Name: $name, Price: $price, Description: $description, Image URL: $image_url")
    }
}

