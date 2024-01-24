package com.example.recipeapp.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.RECIPE_AMOUNT_BY_DEFAULT
import com.example.recipeapp.data.RECIPE_AMOUNT_KEY
import com.example.recipeapp.data.SETTINGS_KEY
import com.example.recipeapp.data.adapters.RecipeFavoriteAdapter
import com.example.recipeapp.databinding.FragmentFavoritesBinding
import com.example.recipeapp.ui.home.HomeFragmentDirections
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Favorites fragment of application for displaying favorites recipes.
 */
@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private val viewModel : FavoritesViewModel by viewModels<FavoritesViewModel>()
    /**
     * Overrides the default [Fragment.onCreateView] method.
     * Sets up the UI components, observes LiveData, and handles user interactions.
     *
     * @param inflater The layout inflater.
     * @param container The container view.
     * @param savedInstanceState The saved instance state.
     * @return The inflated view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        // Update the recipes amount from SharedPreferences
        updateRecipesAmount()
        // Observe recipes LiveData and update the adapter when the data changes
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            (binding.favoritesRecipesList.adapter as RecipeFavoriteAdapter).submitList(recipes)
        }
        // Set up the RecipePreviewAdapter and handle item clicks
        binding.favoritesRecipesList.adapter = RecipeFavoriteAdapter(RecipeFavoriteAdapter.ClickRecipeFavoriteListener {
            Timber.d("Recipe ${it.recipeId} was clicked")
            viewModel.navigateToRecipeDetails(it.recipeId)
        })
        // Observe navigateToRecipeDetails LiveData and navigate when an item is clicked
        viewModel.navigateToRecipeDetails.observe(viewLifecycleOwner) {
            it?.let { id ->
                Timber.d("Id $id was sent to Details fragment")
                findNavController().navigate(HomeFragmentDirections.homeShowDetails(id))
                viewModel.navigateToRecipeDetailsCompleted()
            }
        }
        // Observe recipesAmount LiveData and trigger fetching recipes when it changes
        viewModel.recipesAmount.observe(viewLifecycleOwner) {
            Timber.d("Amount recipes in fragment was changed to $it")
            viewModel.getFavoriteRecipes()
        }

        return binding.root
    }
    /**
     * Updates the recipes amount in the ViewModel from SharedPreferences.
     */
    private fun updateRecipesAmount() {
        //Receive amount from SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences(
            SETTINGS_KEY,
            Context.MODE_PRIVATE
        )
        //Update amount in the ViewModel
        viewModel.updateAmount(
            sharedPreferences.getInt(
                RECIPE_AMOUNT_KEY,
                RECIPE_AMOUNT_BY_DEFAULT
            )
        )
    }
}
