package com.example.recipeapp

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.ApiClient.getSearchedRecipes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {
    private lateinit var editTextIngredients: EditText
    private lateinit var btnBack: ImageButton
    private val recipesList = mutableListOf<SearchedRecipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        editTextIngredients = findViewById(R.id.editTextIngredients)
        btnBack = findViewById(R.id.btn_back3)

        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        editTextIngredients.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                // Detects the Enter key press or Done button on the keyboard
                performSearch()
                true
            } else {
                false
            }
        }
    }

    private fun performSearch() {
        val userInput = editTextIngredients.text.toString()
        val ingredientsList = userInput.split(",").map { it.trim() }

        // Fetch recipes based on ingredients list
        fetchRecipes(ingredientsList)
    }

    private fun fetchRecipes(ingredients: List<String>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val recipesResponse = getSearchedRecipes(ingredients)

                recipesList.clear() // Clear previous data
                recipesList.addAll(recipesResponse)

                withContext(Dispatchers.Main) {
                    updateRecyclerView()
                }
            } catch (e: Exception) {
                // Handle errors or show a message to the user
            }
        }
    }
    private fun updateRecyclerView() {
        val recyclerViewRecipes = findViewById<RecyclerView>(R.id.recyclerViewRecipes)
        recyclerViewRecipes?.let { recyclerView ->
            val adapter = RecipeAdapter(this, recipesList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            adapter.notifyDataSetChanged() // Notify adapter about data changes
        }
    }
}
