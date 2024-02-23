package com.example.recipeapp

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object RecipeDataParser {
    fun parseRecipes(responseBody: String?): List<SearchedRecipe> {
        // 응답 본문이 null이면 빈 목록을 반환합니다.
        val recipes = responseBody?.let {
            // JSON 문자열을 JSON 배열로 변환합니다.
            val jsonArray = JSONArray(it)

            // JSON 배열에서 각 레시피를 추출합니다.
            val mutableListOfRecipes = mutableListOf<SearchedRecipe>()
            for (i in 0 until jsonArray.length()) {
                val recipeObject = jsonArray.getJSONObject(i)

                // 레시피의 개별 세부 정보를 추출합니다.
                val id = recipeObject.getInt("id")
                val title = recipeObject.getString("title")
                val image = recipeObject.getString("image")
                val usedIngredientCount = recipeObject.getInt("usedIngredientCount")
                val missedIngredientCount = recipeObject.getInt("missedIngredientCount")
                val likes = recipeObject.getInt("likes")

                // SearchedRecipe 객체를 생성하고 목록에 추가합니다.
                val searchedRecipe = SearchedRecipe(
                    id,
                    title,
                    image,
                    usedIngredientCount,
                    missedIngredientCount,
                    likes
                )
                mutableListOfRecipes.add(searchedRecipe)
            }

            mutableListOfRecipes
        } ?: emptyList()

        return recipes
    }

    fun parseExtrationRecipes(responseBody: String?): List<ExtractionRecipe> {
        val recipes = mutableListOf<ExtractionRecipe>()

        // If the response body is null or empty, return an empty list of recipes
        if (responseBody.isNullOrEmpty()) {
            return recipes
        }

        try {
            val jsonObject = JSONObject(responseBody)
            val resultsArray = jsonObject.getJSONArray("results")

            for (i in 0 until resultsArray.length()) {
                val recipeObject = resultsArray.getJSONObject(i)

                val id = recipeObject.getInt("id")
                val title = recipeObject.getString("title")
                val image = recipeObject.getString("image")
                val imageType = recipeObject.getString("imageType")

                val extractionRecipe = ExtractionRecipe(id, title, image, imageType)
                recipes.add(extractionRecipe)
            }
        } catch (e: JSONException) {
            // Log or handle the exception here
            e.printStackTrace()
        }

        return recipes
    }

    fun parseIngredients(responseBody: String?): List<RecipeDetail> {
        val recipes = mutableListOf<RecipeDetail>()

        responseBody?.let {
            try {
                val recipeObject = JSONObject(it)
                val ingredientsArray = recipeObject.getJSONArray("ingredients")

                val ingredientList = mutableListOf<Ingredient>()

                for (i in 0 until ingredientsArray.length()) {
                    val ingredientObject = ingredientsArray.getJSONObject(i)

                    val amountObject = ingredientObject.getJSONObject("amount")
                    val metricObject = amountObject.getJSONObject("metric")
                    val usObject = amountObject.getJSONObject("us")

                    val metricUnit = metricObject.getString("unit")
                    val metricValue = metricObject.getDouble("value")
                    val usUnit = usObject.getString("unit")
                    val usValue = usObject.getDouble("value")

                    val amountMap = mapOf(
                        "metric" to Measurement(metricUnit, metricValue),
                        "us" to Measurement(usUnit, usValue)
                    )

                    val ingredientImage = ingredientObject.getString("image")
                    val ingredientName = ingredientObject.getString("name")

                    val ingredient = Ingredient(amountMap, ingredientImage, ingredientName)
                    ingredientList.add(ingredient)
                }
                val recipeDetail = RecipeDetail(ingredientList)
                recipes.add(recipeDetail)
            } catch (e: JSONException) {
                // Log or handle the exception here
                e.printStackTrace()
            }
        }

        return recipes
    }

    fun parseSimilarRecipe(responseBody: String?): List<SimilarRecipe> {
        val similarRecipes = mutableListOf<SimilarRecipe>()

        responseBody?.let {
            try {
                val similarRecipesArray = JSONArray(it)

                for (i in 0 until similarRecipesArray.length()) {
                    val recipeObject = similarRecipesArray.getJSONObject(i)

                    val id = recipeObject.getInt("id")
                    val title = recipeObject.getString("title")
                    val imageType = recipeObject.getString("imageType")
                    val readyInMinutes = recipeObject.getInt("readyInMinutes")
                    val servings = recipeObject.getInt("servings")
                    val sourceUrl = recipeObject.getString("sourceUrl")

                    val similarRecipe = SimilarRecipe(id, title, imageType, readyInMinutes, servings, sourceUrl)
                    similarRecipes.add(similarRecipe)
                }
            } catch (e: JSONException) {
                // Log or handle the exception here
                e.printStackTrace()
            }
        }
        return similarRecipes
    }

    fun parseGuideLine(responseBody: String?): List<GuideLine> {
        val guidelines = mutableListOf<GuideLine>()

        responseBody?.let {
            try {
                val guideLineRecipesArray = JSONArray(it)

                for (i in 0 until guideLineRecipesArray.length()) {
                    val guideLineObject = guideLineRecipesArray.getJSONObject(i)

                    val guideLineName = guideLineObject.getString("name")
                    val stepsArray = guideLineObject.getJSONArray("steps")

                    val stepList = mutableListOf<Step>()

                    for (j in 0 until stepsArray.length()) {
                        val stepObject = stepsArray.getJSONObject(j)

                        val number = stepObject.getInt("number")
                        val step = stepObject.getString("step")

                        val stepData = Step(number, step)
                        stepList.add(stepData)
                    }

                    val guideLine = GuideLine(guideLineName, stepList)
                    guidelines.add(guideLine)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        return guidelines
    }

    fun parseInformation(responseBody: String?): RecipeInformation? {
        var recipeInformation: RecipeInformation? = null

        responseBody?.let {
            try {
                val recipeObject  = JSONObject(it)


                    val id = recipeObject.getInt("id")
                    val title = recipeObject.getString("title")
                    val image = recipeObject.getString("image")


                recipeInformation = RecipeInformation(id, title, image)

            } catch (e: JSONException) {
                // Log or handle the exception here
                e.printStackTrace()
            }
        }
        return recipeInformation
    }

}