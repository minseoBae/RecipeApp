package com.example.recipeapp

import java.io.Serializable

data class ExtractionRecipe (
    val id: Int,
    val title: String,
    val image: String,
    val imageType: String
): Serializable