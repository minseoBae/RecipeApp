package com.example.recipeapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ActivityRecipeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecipeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private val recipesList = mutableListOf<ExtractionRecipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.input.setBackgroundColor(Color.parseColor("#8C1E22"))

        setViewsVisibility(
            View.GONE,
            binding.query,
            binding.cuisine,
            binding.includeIngredients,
            binding.diet,
            binding.type,
            binding.intolerances,
            binding.maxReadyTime,
            binding.sort
        )

        binding.option.setOnCheckedChangeListener { buttonView, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.GONE
            setViewsVisibility(
                visibility,
                binding.query,
                binding.cuisine,
                binding.includeIngredients,
                binding.diet,
                binding.type,
                binding.intolerances,
                binding.maxReadyTime,
                binding.sort
            )
        }

        setRadioGroupVisibility(binding.query, binding.queryItem)
        setRadioGroupVisibility(binding.includeIngredients, binding.includeIngredientsItem)
        setRadioGroupVisibility(binding.cuisine, binding.cuisineItem)
        setRadioGroupVisibility(binding.diet, binding.dietItem)
        setRadioGroupVisibility(binding.type, binding.typeItem)
        setRadioGroupVisibility(binding.intolerances, binding.intolerancesItem)
        setRadioGroupVisibility(binding.maxReadyTime, binding.maxReadyTimeItem)
        setRadioGroupVisibility(binding.sort, binding.sortItem)

        val inputButton = findViewById<Button>(R.id.input)
        inputButton.setOnClickListener {
            if (binding.option.isChecked) {
                val queryItemValue =
                    if (binding.query.isChecked) binding.queryItem.text.toString() else null

                val includeIngredientsItemValue =
                    if (binding.includeIngredients.isChecked) binding.includeIngredientsItem.text.toString() else null
                val ingredientsList = includeIngredientsItemValue?.split(",")?.map { it.trim() }

                val maxReadyTime =
                    if (binding.maxReadyTime.isChecked) binding.maxReadyTimeItem.progress.toString() else null

                val cuisineId = binding.cuisineItem.checkedRadioButtonId
                val cuisineValue = if (binding.cuisine.isChecked && cuisineId != -1) {
                    findViewById<RadioButton>(cuisineId)?.text.toString()
                } else null

                val dietId = binding.dietItem.checkedRadioButtonId
                val dietValue = if (binding.diet.isChecked && cuisineId != -1) {
                    findViewById<RadioButton>(dietId)?.text.toString()
                } else null

                val typeId = binding.typeItem.checkedRadioButtonId
                val typeValue = if (binding.type.isChecked && cuisineId != -1) {
                    findViewById<RadioButton>(typeId)?.text.toString()
                } else null

                val intoleranceId = binding.intolerancesItem.checkedRadioButtonId
                val intoleranceValue = if (binding.intolerances.isChecked && intoleranceId != -1) {
                    findViewById<RadioButton>(intoleranceId)?.text.toString()
                } else null

                val sortId = binding.sortItem.checkedRadioButtonId
                val sortValue = if (binding.sort.isChecked && sortId != -1) {
                    findViewById<RadioButton>(sortId)?.text.toString()
                } else null


                val options = mutableListOf<String?>()
                options.add(queryItemValue)
                options.add(cuisineValue)
                options.add(dietValue)
                options.add(intoleranceValue)
                options.add(includeIngredientsItemValue)
                options.add(typeValue)
                options.add(maxReadyTime)
                options.add(sortValue)

                // Fetch recipes based on ingredients list
                fetchRecipes(options)
            }
        }
    }
    private fun setViewsVisibility(visibility: Int, vararg views: View) {
        views.forEach { it.visibility = visibility }
    }

    private fun setRadioGroupVisibility(checkBox: CheckBox, view: View) {
        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val visibility = if (isChecked) View.VISIBLE else View.GONE
            view.visibility = visibility
        }
    }

    private fun fetchRecipes(options: MutableList<String?>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val recipesResponse = ApiClient.getExtractionRecipes(options)

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
            val adapter = ExRecipeAdapter(this, recipesList)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter

            adapter.notifyDataSetChanged() // Notify adapter about data changes
        }
    }
}