package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.MineUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MineUiState
import com.qianyue.wanandroidmvi.user.User

/**
 * @author QianYue
 * @since 2023/8/23
 */
class MineViewModel : BaseViewModel<MineUiIntent, MineUiState>() {
    override fun initState(): MineUiState = MineUiState.Init

    override suspend fun processIntent(uiIntent: MineUiIntent) {
        when (uiIntent) {
            is MineUiIntent.GetUserCoin -> {
                val res = request { API_SERVICE.getCoinCount() }
                res.takeIf { it.isSuccessful() && it.data != null }
                    ?.let {
                        User.updateCoin(it.data!!.coinCount)
                        sendUiState { MineUiState.OnGetCoin(it.data.coinCount) }
                    }
            }
        }
    }
}