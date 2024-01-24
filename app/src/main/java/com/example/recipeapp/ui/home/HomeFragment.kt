package com.example.recipeapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.RECIPE_AMOUNT_BY_DEFAULT
import com.example.recipeapp.data.RECIPE_AMOUNT_KEY
import com.example.recipeapp.data.SETTINGS_KEY
import com.example.recipeapp.data.adapters.RecipePreviewAdapter
import com.example.recipeapp.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
/**
 * Home fragment of application for displaying random recipes.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel : HomeViewModel by viewModels<HomeViewModel>()
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
        Timber.d("HomeFragment was created")
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Update the recipes amount from SharedPreferences
        updateRecipesAmount()
        // Observe recipes LiveData and update the adapter when the data changes
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            Timber.d("${recipes.size} recipes were send to adapter")
            (binding.homeRecipesList.adapter as RecipePreviewAdapter).submitList(recipes)
        }
        // Set up the RecipePreviewAdapter and handle item clicks
        binding.homeRecipesList.adapter = RecipePreviewAdapter(RecipePreviewAdapter.ClickRecipePreviewListener {
            Timber.d("Recipe ${it.id} was clicked")
            viewModel.navigateToRecipeDetails(it.id)
        })
        // Observe navigateToRecipeDetails LiveData and navigate when an item is clicked
        viewModel.navigateToRecipeDetails.observe(viewLifecycleOwner) {
            it?.let { id ->
                Timber.d("Id $id was sent to Details fragment")
                findNavController().navigate(HomeFragmentDirections.homeShowDetails(id))
                viewModel.navigateToRecipeDetailsCompleted()
            }
        }
        // Handle search button click to show or hide the search bar
        binding.homeToolbarSearchNotActive.setOnClickListener {
            Timber.d("Search button was clicked")
            switchSearchBarState(binding)
        }
        // Handle search button click to initiate search and hide the keyboard
        binding.homeToolbarSearchActive.setOnClickListener {
            viewModel.getRecipesByQuery(binding.homeToolbarSearchBar.text.toString())
            hideKeyboard()
            switchSearchBarState(binding)
        }

        // Observe recipesAmount LiveData and trigger fetching recipes when it changes
        viewModel.recipesAmount.observe(viewLifecycleOwner) {
            Timber.d("Amount recipes in fragment was changed to $it")
            viewModel.getAllRecipes()
        }

        return binding.root
    }

    /**
     * Switches the visibility state of the search bar components.
     *
     * @param binding The data binding object for the fragment.
     */
    private fun switchSearchBarState(binding: FragmentHomeBinding) {
        if (binding.homeToolbarSearchNotActive.visibility == View.VISIBLE) {
            binding.homeToolbarSearchBar.visibility = View.VISIBLE
            binding.homeToolbarSearchActive.visibility = View.VISIBLE
            binding.homeToolbarSearchNotActive.visibility = View.GONE
        } else {
            binding.homeToolbarSearchBar.visibility = View.GONE
            binding.homeToolbarSearchActive.visibility = View.GONE
            binding.homeToolbarSearchNotActive.visibility = View.VISIBLE
        }
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
    /**
     * Hides the keyboard.
     */
    private fun hideKeyboard() {
        val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }
}
