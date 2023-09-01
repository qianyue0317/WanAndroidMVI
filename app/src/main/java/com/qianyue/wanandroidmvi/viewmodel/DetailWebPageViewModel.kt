package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.ui.uiintent.DetailWebPageIntent
import com.qianyue.wanandroidmvi.ui.uistate.DetailWebPageState

/**
 * @author QianYue
 * @since 2023/8/23
 */
class DetailWebPageViewModel: BaseViewModel<DetailWebPageIntent, DetailWebPageState>() {
    override fun initState(): DetailWebPageState = DetailWebPageState()

    override suspend fun processIntent(uiIntent: DetailWebPageIntent) {

    }
}