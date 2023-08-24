package com.qianyue.wanandroidmvi

import android.view.View
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.hjq.toast.Toaster

/**
 * @author QianYue
 * @since 2023/8/24
 */
class NavHostHideShow : NavHostFragment() {

    override fun createFragmentNavigator(): Navigator<FragmentNavigator.Destination> {
        return HideShowNavigator(requireContext(), childFragmentManager, containerId)
    }


    private val containerId: Int
        get() {
            val id = id
            return if (id != 0 && id != View.NO_ID) {
                id
                // Fallback to using our own ID if this Fragment wasn't added via
                // add(containerViewId, Fragment)
            } else androidx.navigation.fragment.R.id.nav_host_fragment_container
        }
}