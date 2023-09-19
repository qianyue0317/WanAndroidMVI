package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.TodoItem

/**
 * @author QianYue
 * @since 2023/9/10
 */
sealed class MyTODOUiState: IUiState {
    class Init : MyTODOUiState()
    class OnRefresh(val list: List<TodoItem>?) : MyTODOUiState()
    class OnAddSuccess : MyTODOUiState()
}