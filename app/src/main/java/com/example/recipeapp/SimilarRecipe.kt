package com.example.recipeapp

data class SimilarRecipe (
    val id: Int,
    val title: String,
    val imageType: String,
    val readyInMinutes: Int,
    val servings: Int,
    val sourceUrl: String
)