package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.ui.uiintent.MineUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MineUiState

/**
 * @author QianYue
 * @since 2023/8/23
 */
class MineViewModel: BaseViewModel<MineUiIntent, MineUiState>() {
    override fun initState(): MineUiState = MineUiState()

    override suspend fun processIntent(uiIntent: MineUiIntent) {

    }
}