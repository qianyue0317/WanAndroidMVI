package com.qianyue.wanandroidmvi

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.hjq.toast.Toaster
import java.util.ArrayDeque

/**
 * @author QianYue
 * @since 2023/8/23
 */
@Navigator.Name("fragment")
class HideShowNavigator(
    private val mContext: Context,
    private val mFragmentManager: FragmentManager,
    private val mContainerId: Int
) : FragmentNavigator(mContext, mFragmentManager, mContainerId) {

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        if (mFragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already saved its state"
            )
            return
        }
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
    }

    // copy from super class
    private fun navigate(
        entry: NavBackStackEntry,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ) {
        val initialNavigation = state.backStack.value.isEmpty()


        var savedIds: MutableSet<String>?
        this.javaClass.superclass.getDeclaredField("savedIds").apply {
            isAccessible = true
            savedIds = get(this@HideShowNavigator) as MutableSet<String>?
        }


        val restoreState = (
                navOptions != null && !initialNavigation &&
                        navOptions.shouldRestoreState() &&
                        savedIds?.remove(entry.id) ?: false
                )
        if (restoreState) {
            // Restore back stack does all the work to restore the entry
            mFragmentManager.restoreBackStack(entry.id)
            state.pushWithTransition(entry)
            return
        }
        val ft = createFragmentTransaction(entry, navOptions)

        if (!initialNavigation) {
            ft.addToBackStack(entry.id)
        }

        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.commit()

        state.pushWithTransition(entry)
    }

    private fun createFragmentTransaction(
        entry: NavBackStackEntry,
        navOptions: NavOptions?
    ): FragmentTransaction {
        val destination = entry.destination as Destination
        val args = entry.arguments
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }

        val ft = mFragmentManager.beginTransaction()
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        val fragment = mFragmentManager.primaryNavigationFragment
        if (fragment != null) {
            ft.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
            ft.hide(fragment)
        }
        var frag: Fragment? = null
        val tag = entry.id
        frag = mFragmentManager.findFragmentByTag(tag)
        if (frag != null) {
            ft.setMaxLifecycle(frag, Lifecycle.State.RESUMED)
            ft.show(frag)
        } else {
            frag = instantiateFragment(mContext, mFragmentManager, className, args)
            frag.arguments = args
            ft.add(mContainerId, frag, tag)
        }

        ft.setPrimaryNavigationFragment(frag)
        ft.setReorderingAllowed(true)
        return ft
    }

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        Toaster.showShort("执行到2")
        if (mFragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already"
                        + " saved its state"
            )
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = mContext.packageName + className
        }
        //final Fragment frag = instantiateFragment(mContext, mManager,
        //       className, args);
        //frag.setArguments(args);
        val ft = mFragmentManager.beginTransaction()
        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
        val fragment = mFragmentManager.primaryNavigationFragment
        if (fragment != null) {
            ft.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
            ft.hide(fragment)
        }
        var frag: Fragment? = null
        val tag = destination.id.toString()
        frag = mFragmentManager.findFragmentByTag(tag)
        if (frag != null) {
            ft.setMaxLifecycle(frag, Lifecycle.State.RESUMED)
            ft.show(frag)
        } else {
            frag = instantiateFragment(mContext, mFragmentManager, className, args)
            frag.arguments = args
            ft.add(mContainerId, frag, tag)
        }
        //ft.replace(mContainerId, frag);
        ft.setPrimaryNavigationFragment(frag)
        @IdRes val destId = destination.id
        var mBackStack: ArrayDeque<Int>? = null
        try {
            val field = FragmentNavigator::class.java.getDeclaredField("mBackStack")
            field.isAccessible = true
            mBackStack = field[this] as ArrayDeque<Int>
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val initialNavigation = mBackStack!!.isEmpty()
        // TODO Build first class singleTop behavior for fragments
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)
        val isAdded: Boolean
        isAdded = if (initialNavigation) {
            true
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mFragmentManager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            true
        }
        if (navigatorExtras is Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key!!, value!!)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        return if (isAdded) {
            mBackStack.add(destId)
            destination
        } else {
            null
        }
    }

    private fun generateBackStackName(backStackindex: Int, destid: Int): String {
        return "$backStackindex-$destid"
    }

    companion object {
        private const val TAG = "fragment_navigator"
    }

}