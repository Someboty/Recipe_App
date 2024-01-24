package com.example.recipeapp.ui.details.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.recipeapp.R
import com.example.recipeapp.data.ARG_RECIPE
import com.example.recipeapp.data.models.RecipeDetailed
import com.example.recipeapp.databinding.FragmentDetailsOverviewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
/**
 * Fragment responsible for displaying an overview of recipe details.
 */
@AndroidEntryPoint
class DetailsOverviewFragment : Fragment() {
    // ViewModel for handling recipe overview details and interactions
    private val viewModel : DetailsOverviewViewModel by viewModels<DetailsOverviewViewModel>()

    /**
     * Companion object providing a factory method to create a new instance of DetailsOverviewFragment and receive recipe.
     */
    companion object {
        fun newInstance(recipe: RecipeDetailed): DetailsOverviewFragment {
            Timber.i("Received recipe with id ${recipe.recipeId}")
            val fragment = DetailsOverviewFragment()
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
        val binding = FragmentDetailsOverviewBinding.inflate(inflater, container, false)
        // Fetch recipe details based on the provided arguments
        arguments?.getParcelable<RecipeDetailed>(ARG_RECIPE)?.let { viewModel.getRecipeDetails(it) }
        // Observe changes in the recipe details and update the UI accordingly
        viewModel.recipe.observe(viewLifecycleOwner) { recipe ->
            if (recipe != null) {
                bindContent(binding, recipe)
            }
        }

        return binding.root
    }
    /**
     * Binds the recipe details to the UI components.
     *
     * @param binding The binding object for the fragment.
     * @param recipe The recipe details to be displayed.
     */
    private fun bindContent(
        binding: FragmentDetailsOverviewBinding,
        recipe: RecipeDetailed
    ) {
        bindImage(binding, recipe)
        bindCharacteristicsIcons(binding, recipe)
        bindTexts(binding, recipe)
    }
    /**
     * Loads and displays the recipe image using Glide.
     *
     * @param binding The binding object for the fragment.
     * @param recipe The recipe details containing the image URL.
     */
    private fun bindImage(
        binding: FragmentDetailsOverviewBinding,
        recipe: RecipeDetailed
    ) {
        Glide.with(binding.detailsOverviewRecipeImage.context)
            .load(
                recipe.image
                    .toUri()
                    .buildUpon()
                    .scheme("https")
                    .build()
            ).apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(binding.detailsOverviewRecipeImage)
    }
    /**
     * Binds the characteristic icons and characteristic text colors to the UI.
     *
     * @param binding The binding object for the fragment.
     * @param recipe The recipe details containing the characteristic information.
     */
    private fun bindCharacteristicsIcons(
        binding: FragmentDetailsOverviewBinding,
        recipe: RecipeDetailed
    ) {
        bindCharacteristicsIconsColors(binding, recipe)

        bindCharacteristicsTextsColors(binding, recipe)
    }
    /**
     * Binds the colors of the characteristic icons based on the recipe details.
     *
     * @param binding The binding object for the fragment.
     * @param recipe The recipe details containing the characteristic information.
     */
    private fun bindCharacteristicsIconsColors(
        binding: FragmentDetailsOverviewBinding,
        recipe: RecipeDetailed
    ) {
        // Binding the icons with appropriate check images based on characteristics
        // (e.g., vegetarian, gluten-free, etc.)
        binding.detailsOverviewRecipeCharacteristicsVegetarian
            .setCompoundDrawablesRelativeWithIntrinsicBounds(
                viewModel.setCorrectCheckImage(recipe.isVegetarian), 0, 0, 0
            )

        binding.detailsOverviewRecipeCharacteristicsHealthy
            .setCompoundDrawablesRelativeWithIntrinsicBounds(
                viewModel.setCorrectCheckImage(recipe.isVeryHealthy), 0, 0, 0
            )


        binding.detailsOverviewRecipeCharacteristicsGlutenFree
            .setCompoundDrawablesRelativeWithIntrinsicBounds(
                viewModel.setCorrectCheckImage(recipe.isGlutenFree), 0, 0, 0
            )

        binding.detailsOverviewRecipeCharacteristicsVegan
            .setCompoundDrawablesRelativeWithIntrinsicBounds(
                viewModel.setCorrectCheckImage(recipe.isVegan), 0, 0, 0
            )

        binding.detailsOverviewRecipeCharacteristicsDairyFree
            .setCompoundDrawablesRelativeWithIntrinsicBounds(
                viewModel.setCorrectCheckImage(recipe.isDairyFree), 0, 0, 0
            )

        binding.detailsOverviewRecipeCharacteristicsCheap
            .setCompoundDrawablesRelativeWithIntrinsicBounds(
                viewModel.setCorrectCheckImage(recipe.isCheap), 0, 0, 0
            )
    }
    /**
     * Binds the text colors of the characteristic descriptions based on the recipe details.
     *
     * @param binding The binding object for the fragment.
     * @param recipe The recipe details containing the characteristic information.
     */
    private fun bindCharacteristicsTextsColors(
        binding: FragmentDetailsOverviewBinding,
        recipe: RecipeDetailed
    ) {
        // Binding the text colors with appropriate characteristic colors
        binding.detailsOverviewRecipeCharacteristicsVegetarian.setTextColor(
            viewModel.setCorrectCharacteristicColor(recipe.isVegetarian)
        )

        binding.detailsOverviewRecipeCharacteristicsGlutenFree.setTextColor(
            viewModel.setCorrectCharacteristicColor(recipe.isGlutenFree)
        )

        binding.detailsOverviewRecipeCharacteristicsHealthy.setTextColor(
            viewModel.setCorrectCharacteristicColor(recipe.isVeryHealthy)
        )

        binding.detailsOverviewRecipeCharacteristicsVegan.setTextColor(
            viewModel.setCorrectCharacteristicColor(recipe.isVegan)
        )

        binding.detailsOverviewRecipeCharacteristicsDairyFree.setTextColor(
            viewModel.setCorrectCharacteristicColor(recipe.isDairyFree)
        )

        binding.detailsOverviewRecipeCharacteristicsCheap.setTextColor(
            viewModel.setCorrectCharacteristicColor(recipe.isCheap)
        )
    }
    /**
     * Binds the text details of the recipe to the UI.
     *
     * @param binding The binding object for the fragment.
     * @param recipe The recipe details to be displayed.
     */
    private fun bindTexts(
        binding: FragmentDetailsOverviewBinding,
        recipe: RecipeDetailed
    ) {
        // Binding recipe title, likes, ready in minutes, and summary to corresponding UI elements
        binding.detailsOverviewRecipeTitle.text = recipe.title

        binding.detailsOverviewFavorites.text = recipe.likes.toString()

        binding.detailsOverviewReadyInMinutes.text = recipe.readyInMinutes.toString()

        binding.detailsOverviewSummary.text = recipe.summary.parseAsHtml()
    }
}
