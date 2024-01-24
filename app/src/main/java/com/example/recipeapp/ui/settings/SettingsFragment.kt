package com.example.recipeapp.ui.settings

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipeapp.R
import com.example.recipeapp.data.RECIPE_AMOUNT_BY_DEFAULT
import com.example.recipeapp.data.RECIPE_AMOUNT_KEY
import com.example.recipeapp.data.SETTINGS_KEY
import com.example.recipeapp.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
/**
 * Fragment for displaying and updating application settings related to recipe amounts.
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {
    // ViewModel instance for handling settings logic
    private val viewModel : SettingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        // Retrieve shared preferences for storing user settings
        val sharedPreferences = requireContext().getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE)
        // Update ViewModel with the current recipe amount from shared preferences
        viewModel.updateAmount(
            sharedPreferences.getInt(RECIPE_AMOUNT_KEY, RECIPE_AMOUNT_BY_DEFAULT)
        )
        // Set the hint for the recipe amount edit text
        binding.settingsRecipeAmountEdit.hint = viewModel.recipeAmount.value.toString()
        // Handle save button click
        binding.settingsSaveButton.setOnClickListener {
            hideKeyboard()
            val input = binding.settingsRecipeAmountEdit.text.toString()
            if (input.isNotEmpty()) {
                val amountToUpdate = Integer.valueOf(input)
                    ?: throw IllegalArgumentException(
                        getString(R.string.settings_recipe_amount_incorrect_exception_text) + binding.settingsRecipeAmountEdit.text)
                // Check and update the recipe amount if it is within the valid range
                if (viewModel.checkRecipeAmount(amountToUpdate)) {
                    sharedPreferences
                        .edit()
                        .putInt(RECIPE_AMOUNT_KEY, amountToUpdate)
                        .apply()
                    viewModel.updateAmount(amountToUpdate)
                }
            } else showIncorrectAmountSnackbar()
        }
        // Observe the LiveData for incorrect amount flag and show the snackbar if needed
        viewModel.amountIncorrect.observe(viewLifecycleOwner) {
            if (it) {
                showIncorrectAmountSnackbar()
                viewModel.snackbarMessageCompleted()
            }
        }

        return binding.root
    }

    /**
     * Displays a snackbar with a message indicating an incorrect recipe amount.
     */
    private fun showIncorrectAmountSnackbar() {
        Snackbar.make(
            requireView(),
            getString(R.string.settings_recipe_amount_incorrect_snackbar_message_value),
            Snackbar.LENGTH_LONG
        ).show()
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
