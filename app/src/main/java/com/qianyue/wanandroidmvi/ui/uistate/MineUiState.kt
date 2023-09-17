package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState

/**
 * @author QianYue
 * @since 2023/8/23
 */
sealed class MineUiState: IUiState {
    object Init: MineUiState()
    class OnGetCoin(val coinCount: Int) : MineUiState()
}