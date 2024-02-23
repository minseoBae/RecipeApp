package com.example.recipeapp

import java.io.Serializable

data class SearchedRecipe (
    val id: Int,
    val title: String,
    val image: String,
    val usedIngredientCount: Int,
    val missedIngredientCount: Int,
    val likes: Int
): Serializable