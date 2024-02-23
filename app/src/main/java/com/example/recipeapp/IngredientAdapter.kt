package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class IngredientAdapter(private val ingredients: List<RecipeDetail>) :
    RecyclerView.Adapter<IngredientAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textViewName)
        val mecAmount: TextView = itemView.findViewById(R.id.textViewAmountMec)
        val usAmount: TextView = itemView.findViewById(R.id.textViewAmountUs)
        val ingredientImage: ImageView = itemView.findViewById(R.id.ingredientImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_ingredient_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipeDetail = ingredients[0]

        // 레시피의 인덱스와 일치하는 재료에 접근합니다.
        val ingredient = recipeDetail.ingredients[position]

        holder.name.text = ingredient.name

        val metricUnit = ingredient.amount["metric"]?.unit ?: ""
        val metricValue = ingredient.amount["metric"]?.value ?: 0.0
        holder.mecAmount.text = "$metricValue $metricUnit"

        val usUnit = ingredient.amount["us"]?.unit ?: ""
        val usValue = ingredient.amount["us"]?.value ?: 0.0
        holder.usAmount.text = "$usValue $usUnit"

        val baseUrl = "https://spoonacular.com/cdn/ingredients_100x100/"
        val imageUrl = baseUrl + ingredient.image

        Picasso.get().load(imageUrl).into(holder.ingredientImage)
    }

    override fun getItemCount(): Int {
        return ingredients.sumOf { it.ingredients.size }
    }
}