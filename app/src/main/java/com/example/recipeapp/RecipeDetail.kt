package com.example.recipeapp

data class RecipeDetail(
    val ingredients: List<Ingredient>
)

data class Ingredient(
    val amount: Map<String, Measurement>,
    val image: String,
    val name: String
)

data class Measurement(
    val unit: String,
    val value: Double
)