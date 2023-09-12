package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.MyTODOUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyTODOUiState

/**
 * @author QianYue
 * @since 2023/9/10
 */
class MyTODOViewModel: BaseViewModel<MyTODOUiIntent, MyTODOUiState>() {
    private var _currentIndex = 1

    override fun initState(): MyTODOUiState = MyTODOUiState.Init()

    override suspend fun processIntent(uiIntent: MyTODOUiIntent) {
        when (uiIntent) {
            is MyTODOUiIntent.Refresh -> {
                _currentIndex = 1
                val res = request { API_SERVICE.getTodoList(_currentIndex) }
                sendUiState { MyTODOUiState.OnRefresh(res.data?.datas) }
            }
        }
    }
}