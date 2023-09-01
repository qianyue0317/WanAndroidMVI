package com.qianyue.wanandroidmvi.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

/**
 * @author QianYue
 * @since 2023/8/15
 */
abstract class BaseFragment<VM : BaseViewModel<*, *>> : Fragment() {
    val vm: VM by lazyVM()

    abstract fun lazyVM(): Lazy<VM>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.uiStateFlow.collectIndexed { _, state ->
                    handleState(state)
                }
            }

        }
    }

    abstract suspend fun handleState(state: IUiState)

}