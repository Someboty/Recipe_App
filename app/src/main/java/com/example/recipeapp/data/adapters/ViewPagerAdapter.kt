package com.example.recipeapp.data.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
/**
 * Adapter class for managing fragments in a ViewPager2 within a FragmentActivity.
 *
 * @param fragmentActivity The FragmentActivity hosting the ViewPager2.
 * @param fragments List of fragments to be displayed in the ViewPager2.
 * @param titles List of titles corresponding to each fragment.
 */
class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Fragment>,
    private val titles: List<String>
) : FragmentStateAdapter(fragmentActivity) {
    /**
     * Returns the total number of fragments managed by the adapter.
     *
     * @return The total number of fragments.
     */
    override fun getItemCount() = fragments.size
    /**
     * Creates a new fragment for the given position.
     *
     * @param position The position of the fragment in the ViewPager2.
     * @return The newly created fragment.
     */
    override fun createFragment(position: Int): Fragment = fragments[position]
    /**
     * Returns the title of the fragment at the specified position.
     *
     * @param position The position of the fragment.
     * @return The title of the fragment.
     */
    fun getPageTitle(position: Int): CharSequence = titles[position]
}
