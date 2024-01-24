package com.example.recipeapp.ui.details.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipeapp.R
import com.example.recipeapp.data.ARG_RECIPE
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.databinding.FragmentDetailsInstructionsBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
/**
 * Fragment responsible for displaying an instructions of recipe details.
 */
@AndroidEntryPoint
class DetailsInstructionsFragment : Fragment() {
    // ViewModel for handling recipe details and interactions
    private val viewModel : DetailsInstructionsViewModel by viewModels<DetailsInstructionsViewModel>()
    /**
     * Companion object providing a factory method to create a new instance of DetailsInstructionsFragment and receive recipe.
     */
    companion object {
        fun newInstance(recipe: RecipeDetailed): DetailsInstructionsFragment {
            Timber.i("Received recipe with id ${recipe.recipeId}")
            val fragment = DetailsInstructionsFragment()
            val args = Bundle()
            args.putParcelable(ARG_RECIPE, recipe)
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
        val binding = FragmentDetailsInstructionsBinding.inflate(inflater, container, false)
        // Fetch recipe details based on the provided arguments
        arguments?.getParcelable<RecipeDetailed>(ARG_RECIPE)?.let { viewModel.getRecipeDetails(it) }
        // Observe changes in the recipe details and update the UI accordingly
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe.instructions?.isNotEmpty() == true) {
                binding.detailsInstructionsText.text = recipe.instructions.parseAsHtml()
            } else {
                //set placeholder if instructions are not available
                binding.detailsInstructionsText.text = getString(R.string.recipe_instructions_placeholder)
            }

        }

        return binding.root
    }
}
