package com.example.recipeapp

data class GuideLine(
    val name: String,
    val steps: List<Step>
)

data class Step(
    val number: Int,
    val step: String,
)