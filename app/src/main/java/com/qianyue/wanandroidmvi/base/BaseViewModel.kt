package com.qianyue.wanandroidmvi.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author qy
 * @since 2023-08-14
 */
@Suppress("MemberVisibilityCanBePrivate", "LeakingThis")
abstract class BaseViewModel<UiIntent: IUiIntent, UiState: IUiState> : ViewModel() {
    // TODO: 内存泄漏吗，在哪里close
    private val _uiIntentChannel: Channel<UiIntent> = Channel()

    val uiIntentFlow: Flow<UiIntent> = _uiIntentChannel.receiveAsFlow()

    private val _uiStateFlow = MutableStateFlow(initState())

    val uiStateFlow: StateFlow<UiState> = _uiStateFlow

    /**
     * view层调用此方法，传入意图
     *
     * @param uiIntent view层发起的意图
     */
    fun sendUiIntent(uiIntent: UiIntent) {
        viewModelScope.launch {
            _uiIntentChannel.send(uiIntent)
        }
    }

    init {
        viewModelScope.launch {
            uiIntentFlow.collectIndexed { _, value ->
                processIntent(value)
            }
        }
    }

    /**
     * 初始化状态
     *
     * @return 初始状态实例
     */
    abstract fun initState(): UiState

    /**
     * 收到view层发来的意图，处理之。（运行在主线程）
     *
     * @param uiIntent 收到的意图对象
     */
    abstract suspend fun processIntent(uiIntent: UiIntent)

    /**
     * 通知状态变更，通常在view层接受监听状态变更
     *
     * @param block 根据旧的值生成新的状态。
     */
    protected fun sendUiState(block: UiState.()-> UiState) {
        _uiStateFlow.update {
            block(it)
        }
    }
}