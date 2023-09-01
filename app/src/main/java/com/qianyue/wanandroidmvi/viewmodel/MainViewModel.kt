package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.ui.uiintent.MainUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MainUiState

/**
 * @author QianYue
 * @since 2023/8/15
 */
class MainViewModel: BaseViewModel<MainUiIntent, MainUiState>() {


    override suspend fun processIntent(uiIntent: MainUiIntent) {
    }

    override fun initState() = MainUiState()

}