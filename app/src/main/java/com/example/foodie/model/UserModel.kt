package com.example.foodie.model

data class UserModel(
    val name: String,
    val phone : String,
    var image_url: String,
    val user_id : String,
    val email : String
){
    constructor() : this("", "", "", "", "")
    fun displayInfo() {
        println("Name: $name, Phone: $phone, Image URL: $image_url")
    }
}