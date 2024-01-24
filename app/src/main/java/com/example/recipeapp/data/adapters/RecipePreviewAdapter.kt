package com.example.recipeapp.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.recipeapp.R
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.data.models.RecipePreview
import com.example.recipeapp.databinding.ItemRecipePreviewBinding
/**
 * The `RecipePreviewAdapter` class is a custom RecyclerView adapter designed to display a list
 * of `RecipePreview` items. It supports item click events and highlights the selected item.
 *
 * @property listener A listener for item click events.
 */
class RecipePreviewAdapter(
    private val listener: ClickRecipePreviewListener
) : ListAdapter<RecipePreview, RecipePreviewAdapter.RecipePreviewHolder>(RecipePreviewCallback) {
    /**
     * ViewHolder for displaying recipe items.
     *
     * @param binding View binding for the recipe item layout.
     */
    inner class RecipePreviewHolder(
        private val binding: ItemRecipePreviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the data of a recipe to the corresponding UI elements.
         *
         * @param recipe The recipe to bind.
         */
        fun bind(recipe: RecipePreview) {
            // Bind recipe title to UI title
            binding.homeRecipeTitle.text = recipe.title
            // Load recipe image using Glide library and setting it to UI
            bindImage(recipe)
        }
        /**
         * Binds the image at the specified position to the ViewHolder.
         *
         * @param recipe The recipe to bind.
         */
        private fun bindImage(recipe: RecipePreview) {
            Glide.with(binding.homeRecipeImage.context)
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
                .into(binding.homeRecipeImage)
        }
    }
    /**
     * Companion object for [RecipePreviewAdapter] responsible for implementing DiffUtil.
     */
    companion object RecipePreviewCallback : DiffUtil.ItemCallback<RecipePreview>() {
        override fun areItemsTheSame(oldItem: RecipePreview, newItem: RecipePreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecipePreview, newItem: RecipePreview): Boolean {
            return oldItem == newItem
        }

    }
    /**
     * Creates a new ViewHolder by inflating the recipe item layout.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of the new View.
     * @return A new RecipePreviewHolder instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipePreviewHolder {
        return RecipePreviewHolder(
            ItemRecipePreviewBinding.inflate(
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
    override fun onBindViewHolder(holder: RecipePreviewHolder, position: Int) {
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
    class ClickRecipePreviewListener(val clickListener: (recipe: RecipePreview) -> Unit) {
        /**
         * Handles the item click event by invoking the provided lambda function.
         *
         * @param recipe The clicked recipe.
         */
        fun onClick(recipe: RecipePreview) = clickListener(recipe)
    }
}