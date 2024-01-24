package com.example.recipeapp.ui.details

import com.example.recipeapp.data.adapters.ViewPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentDetailsBinding
import com.example.recipeapp.ui.details.ingredients.DetailsIngredientsFragment
import com.example.recipeapp.ui.details.ingredients.DetailsIngredientsViewModel
import com.example.recipeapp.ui.details.instructions.DetailsInstructionsFragment
import com.example.recipeapp.ui.details.instructions.DetailsInstructionsViewModel
import com.example.recipeapp.ui.details.overview.DetailsOverviewFragment
import com.example.recipeapp.ui.details.overview.DetailsOverviewViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private const val ID_ARGS_KEY = "recipeId"
/**
 * Fragment responsible for displaying detailed information about a recipe.
 * Annotated with AndroidEntryPoint for Hilt integration.
 */
@AndroidEntryPoint
class FragmentDetails : Fragment() {
    // ViewModel for handling recipe details and interactions
    private val viewModel: RecipeDetailsViewModel by viewModels<RecipeDetailsViewModel>()
    /**
     * Overrides the default [Fragment.onCreateView] method to set up the UI components
     * and fetch recipe details based on the provided recipe ID.
     *
     * @param inflater The LayoutInflater to inflate the layout.
     * @param container The ViewGroup container for the fragment.
     * @param savedInstanceState The saved instance state.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using View Binding
        val binding = FragmentDetailsBinding.inflate(inflater, container, false)
        // Fetch recipe details based on the provided recipe ID
        viewModel.getRecipeDetails(
            arguments?.getLong(ID_ARGS_KEY)
                ?: throw IllegalArgumentException(getString(R.string.details_recipe_id_missing)))
        // Observe changes in the recipe details and update the UI accordingly
        viewModel.recipe.observe(viewLifecycleOwner) { recipeWithIngredients ->
            recipeWithIngredients?.let {
                // Create fragments for different sections of the recipe details
                val fragments = listOf(
                    DetailsOverviewFragment.newInstance(recipeWithIngredients.recipe),
                    DetailsIngredientsFragment.newInstance(recipeWithIngredients.ingredients),
                    DetailsInstructionsFragment.newInstance(recipeWithIngredients.recipe)
                )
                // Titles for the tabs corresponding to each fragment
                val titles = listOf(
                    getString(R.string.recipe_details_tab_overview_title),
                    getString(R.string.recipe_details_tab_ingredients_title),
                    getString(R.string.recipe_details_tab_instructions_title)
                )
                // Set up ViewPager and TabLayout using a custom adapter
                val adapter = ViewPagerAdapter(requireActivity(), fragments, titles)
                binding.detailsContent.adapter = adapter

                TabLayoutMediator(binding.detailsTabLayout, binding.detailsContent) { tab, position ->
                    tab.text = adapter.getPageTitle(position)
                }.attach()
                // Observe changes in the favorite status and update the toolbar icon accordingly
                viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
                    binding.detailsToolbarFavoritesIcon.setImageResource(
                        if (isFavorite) R.drawable.ic_favorite_checked else R.drawable.ic_favorite_uncheked
                    )
                }
                // Set up click listener for the favorites icon in the toolbar
                binding.detailsToolbarFavoritesIcon.setOnClickListener {
                    viewModel.changeFavorite(recipeWithIngredients.recipe)
                }
            }
        }
        // Observe if there was a failure to fetch the recipe and handle accordingly
        viewModel.recipeFailed.observe(viewLifecycleOwner) { isFailed ->
            if (isFailed == true) {
                //Display a Snackbar
                showCantGetRecipeSnackbar()
                //Navigate back to the previous fragment
                returnToPreviousFragment()
                viewModel.failedToReceiveRecipeCompleted()
            }
        }
        // Set up click listener for the back arrow in the toolbar
        binding.detailsToolbarBack.setOnClickListener {
            returnToPreviousFragment()
        }

        return binding.root
    }
    /**
     * Navigates back to the previous fragment using the navigation controller.
     */
    private fun returnToPreviousFragment() {
        findNavController().popBackStack()
    }
    /**
     * Displays a Snackbar indicating a failure to fetch the recipe.
     */
    private fun showCantGetRecipeSnackbar() {
        Snackbar.make(
            requireView(),
            getString(R.string.details_getting_recipe_failed_message_value),
            Snackbar.LENGTH_LONG
        ).show()
    }
}
