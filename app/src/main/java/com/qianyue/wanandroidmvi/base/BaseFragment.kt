package com.qianyue.wanandroidmvi.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.qianyue.wanandroidmvi.utils.WanLog
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

/**
 * @author QianYue
 * @since 2023/8/15
 */
abstract class BaseFragment<VM : BaseViewModel<*, *>> : Fragment() {
    val vm: VM by lazyVM()

    abstract fun lazyVM(): Lazy<VM>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WanLog.d(msg = "${javaClass.simpleName}-onCreate")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        WanLog.d(msg = "${javaClass.simpleName}-onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        WanLog.d(msg = "${javaClass.simpleName}-onDetach")
    }


    override fun onDestroy() {
        super.onDestroy()
        WanLog.d(msg = "${javaClass.simpleName}-onDestroy")
    }

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