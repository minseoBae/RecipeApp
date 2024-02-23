package com.example.recipeapp

import java.io.File

data class UserAccount(
    var idToken: String? = null,
    var emailId: String? = null,
    var password: String? = null,
    var name: String? = null,
    var address: String? = null,
    var age: Int? = null,
    var phone: String? = null,
    var imageProfile: File? = null,
    var lastLoginDate: String? = null,
    var favoriteRecipesId: MutableList<Int>? = mutableListOf()
)