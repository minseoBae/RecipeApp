package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class GuidelineAdapter(private val guidelines: List<GuideLine>) :
    RecyclerView.Adapter<GuidelineAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stepNumber: TextView = itemView.findViewById(R.id.textViewStepNumber)
        val stepDescription: TextView = itemView.findViewById(R.id.textViewStepDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.detail_guideline_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        var cumulativeStepCount = 0

        for (guideLine in guidelines) {
            val stepCount = guideLine.steps.size

            if (position < cumulativeStepCount + stepCount) {
                val stepIndex = position - cumulativeStepCount
                val step = guideLine.steps[stepIndex]

                holder.stepNumber.text = step.number.toString()
                holder.stepDescription.text = step.step
                return
            }

            cumulativeStepCount += stepCount
        }
    }

    override fun getItemCount(): Int {
        return guidelines.sumBy { it.steps.size }
    }
}