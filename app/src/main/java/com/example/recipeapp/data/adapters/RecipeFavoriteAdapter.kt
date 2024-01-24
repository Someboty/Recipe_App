package com.example.recipeapp.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.recipeapp.R
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.databinding.ItemRecipeDetailedBinding
/**
 * The `RecipeFavoriteAdapter` class is a custom RecyclerView adapter designed to display a list
 * of `RecipeDetailed` items, marked as favorite. It supports item click events and highlights the selected item.
 *
 * @property listener A listener for item click events.
 */
class RecipeFavoriteAdapter(
    private val listener: ClickRecipeFavoriteListener
) : ListAdapter<RecipeDetailed, RecipeFavoriteAdapter.RecipeFavoriteHolder>(RecipeFavoriteCallback) {
    /**
     * ViewHolder for displaying favorite recipe items.
     *
     * @param binding View binding for the recipe item layout.
     */
    inner class RecipeFavoriteHolder(
        private val binding: ItemRecipeDetailedBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the data of a recipe to the corresponding UI elements.
         *
         * @param recipe The recipe to bind.
         */
        fun bind(recipe: RecipeDetailed) {
            // Bind recipe title to UI title
            binding.favoriteRecipeTitle.text = recipe.title
            // Load recipe image using Glide library and setting it to UI
            bindImage(recipe)
            // Bind recipe details to UI elements
            binding.favoriteRecipeSummary.text = recipe.summary.parseAsHtml()
            binding.favoriteLikes.text = recipe.likes.toString()
            binding.favoriteReadyInMinutes.text = recipe.readyInMinutes.toString()
            // Set text color and icon based on whether the recipe is vegan or not
            binding.favoriteVegan.setTextColor(
                ContextCompat.getColor(
                    binding.favoriteVegan.context,
                    if (recipe.isVegan) R.color.characteristic_true_green
                    else R.color.characteristic_false_grey
                )
            )
            binding.favoriteVegan.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0,
                if (recipe.isVegan) R.drawable.ic_vegan_true else R.drawable.ic_vegan_false,
                0,
                0
            )
        }
        /**
         * Binds the image at the specified position to the ViewHolder.
         *
         * @param recipe The recipe to bind.
         */
        private fun bindImage(recipe: RecipeDetailed) {
            Glide.with(binding.favoriteRecipeImage.context)
                .load(
                    recipe.image
                        .toUri()
                        .buildUpon()
                        .scheme("https")
                        .build()
                ).apply(
                    RequestOptions()
                        .override(240, 240)
                        .placeholder(R.drawable.ic_loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(binding.favoriteRecipeImage)
        }
    }
    /**
     * Companion object for [RecipeFavoriteAdapter] responsible for implementing DiffUtil.
     */
    companion object RecipeFavoriteCallback : DiffUtil.ItemCallback<RecipeDetailed>() {
        override fun areItemsTheSame(oldItem: RecipeDetailed, newItem: RecipeDetailed): Boolean {
            return oldItem.recipeId == newItem.recipeId
        }

        override fun areContentsTheSame(oldItem: RecipeDetailed, newItem: RecipeDetailed): Boolean {
            return oldItem == newItem
        }

    }
    /**
     * Creates a new ViewHolder by inflating the recipe item layout.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of the new View.
     * @return A new RecipeFavoriteHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeFavoriteHolder {
        return RecipeFavoriteHolder(
            ItemRecipeDetailedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }
    /**
     * Binds the data at the specified position to the ViewHolder.
     *
     * @param holder The ViewHolder.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RecipeFavoriteHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
        // Set click listener for the item
        holder.itemView.setOnClickListener {
            listener.onClick(recipe)
        }
    }
    /**
     * Click listener class for handling recipe item clicks.
     *
     * @param clickListener Lambda function to be executed on item click.
     */
    class ClickRecipeFavoriteListener(val clickListener: (recipe: RecipeDetailed) -> Unit) {
        /**
         * Handles the item click event by invoking the provided lambda function.
         *
         * @param recipe The clicked recipe.
         */
        fun onClick(recipe: RecipeDetailed) = clickListener(recipe)
    }
}
