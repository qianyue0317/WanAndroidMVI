package com.qianyue.wanandroidmvi.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

/**
 * @author qy
 * @since 2023-08-14
 */
abstract class BaseActivity<VM : BaseViewModel<*, *>> : AppCompatActivity() {

    val vm: VM by lazyVM()

    abstract fun lazyVM(): Lazy<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.viewModelScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                vm.uiStateFlow.collectIndexed { _, value ->
                    handleState(value)
                }
            }
        }
    }

    abstract suspend fun handleState(state: IUiState)

}