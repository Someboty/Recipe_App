package com.example.recipeapp.ui.details.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipeapp.data.ARG_INGREDIENTS
import com.example.recipeapp.data.ARG_RECIPE
import com.example.recipeapp.data.models.Ingredient
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.data.models.RecipeWithIngredients
import com.example.recipeapp.databinding.FragmentDetailsIngredientsBinding
import com.example.recipeapp.ui.details.overview.DetailsOverviewFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
/**
 * Fragment responsible for displaying an ingredients of recipe details.
 */
@AndroidEntryPoint
class DetailsIngredientsFragment : Fragment() {
    // ViewModel for handling recipe details and interactions
    private val viewModel : DetailsIngredientsViewModel by viewModels<DetailsIngredientsViewModel>()
    /**
     * Companion object providing a factory method to create a new instance of DetailsIngredientsFragment and receive ingredients.
     */
    companion object {
        fun newInstance(ingredients: List<Ingredient>): DetailsIngredientsFragment {
            Timber.i("Received ingredients with size ${ingredients.size}")
            val fragment = DetailsIngredientsFragment()
            val args = Bundle()
            args.putParcelableArrayList(ARG_INGREDIENTS, ArrayList(ingredients))
            fragment.arguments = args
            return fragment
        }
    }
    /**
     * Overrides the default [Fragment.onCreateView] method to set up the UI components
     * and fetch recipe details based on the provided arguments.
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
        val binding = FragmentDetailsIngredientsBinding.inflate(inflater, container, false)
        // Fetch recipe details based on the provided arguments
        arguments?.getParcelableArrayList<Ingredient>(ARG_INGREDIENTS)?.let { viewModel.getRecipeDetails(it) }
        // Observe changes in the recipe ingredients and update the UI accordingly
        viewModel.ingredients.observe(viewLifecycleOwner) { ingredients ->
                binding.detailsIngredientsText.text = buildIngredientListString(ingredients)
            }

        return binding.root
    }
    /**
     * Builds the recipe ingredients string to bind to the UI components.
     *
     * @param ingredients The list of [Ingredient] objects containing recipe ingredients.
     * @return [String] with all recipes information
     */
    private fun buildIngredientListString(ingredients: List<Ingredient>): String {
        val stringBuilder = StringBuilder()

        for (ingredient in ingredients) {
            stringBuilder.append("${ingredient.name}: ${ingredient.amount} ${ingredient.unit}\n")
        }

        return stringBuilder.toString()
    }
}
