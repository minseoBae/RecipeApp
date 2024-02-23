package com.example.recipeapp

import okhttp3.OkHttpClient

object ApiClient {
    // OkHttpClient 인스턴스를 초기화합니다.
    val client = OkHttpClient()

    fun getExtractionRecipes(options: MutableList<String?>): List<ExtractionRecipe> {
        val baseUrl = "https://api.spoonacular.com/recipes/complexSearch?number=10"

        val url = buildString {
            append(baseUrl)

            val queryParams = listOf(
                "query" to options[0],
                "cuisine" to options[1],
                "diet" to options[2],
                "intolerances" to options[3],
                "includeIngredients" to options[4],
                "type" to options[5],
                "maxReadyTime" to options[6],
                "sort" to options[7]
            )

            queryParams.forEach { (param, value) ->
                value?.let {
                    append("&$param=$it")
                }
            }

            append("&apiKey=85a5a3b0a42045babce34beb6bb76fa2")
        }

        val responseBody = ApiGateway.getRecipeDataFromApi(url)

        return RecipeDataParser.parseExtrationRecipes(responseBody)
    }

    fun getSearchedRecipes(ingredients: List<String>): List<SearchedRecipe> {
        // "%2C" 구분자로 재료들을 결합합니다.
        val joinedIngredients = ingredients.joinToString("%,+")

        val url =
            "https://api.spoonacular.com/recipes/findByIngredients?ingredients=${joinedIngredients}&number=10&apiKey=85a5a3b0a42045babce34beb6bb76fa2"

        val responseBody = ApiGateway.getRecipeDataFromApi(url)

        return RecipeDataParser.parseRecipes(responseBody)
    }

    fun getRecipeInformation(recipeId: Int): RecipeInformation? {
        val url =
            "https://api.spoonacular.com/recipes/${recipeId}/information?includeNutrition=false&apiKey=85a5a3b0a42045babce34beb6bb76fa2"

        val responseBody = ApiGateway.getRecipeDataFromApi(url)

        return RecipeDataParser.parseInformation(responseBody)
    }

    fun getDetailById(recipeId: Int): List<RecipeDetail> {
        val url =
            "https://api.spoonacular.com/recipes/${recipeId}/ingredientWidget.json?apiKey=85a5a3b0a42045babce34beb6bb76fa2"
        val responseBody = ApiGateway.getRecipeDataFromApi(url)

        return RecipeDataParser.parseIngredients(responseBody)
    }

    fun getSimilarRecipeById(recipeId: Int): List<SimilarRecipe> {
        val url =
            "https://api.spoonacular.com/recipes/${recipeId}/similar?number=5&apiKey=85a5a3b0a42045babce34beb6bb76fa2"

        val responseBody = ApiGateway.getRecipeDataFromApi(url)

        return RecipeDataParser.parseSimilarRecipe(responseBody)
    }

    fun getGuideLineById(recipeId: Int): List<GuideLine> {
        val url =
            "https://api.spoonacular.com/recipes/${recipeId}/analyzedInstructions?apiKey=85a5a3b0a42045babce34beb6bb76fa2"

        val responseBody = ApiGateway.getRecipeDataFromApi(url)

        return RecipeDataParser.parseGuideLine(responseBody)
    }
}