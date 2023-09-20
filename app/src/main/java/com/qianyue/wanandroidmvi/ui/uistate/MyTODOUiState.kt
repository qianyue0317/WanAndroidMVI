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
    class OnAddResult(val successful: Boolean, val msg: String? = null) : MyTODOUiState()
    class OnDeleteResult(val successful: Boolean, val msg: String? = null) : MyTODOUiState()
    class OnUpdateResult(val successful: Boolean, val errorMsg: String) : MyTODOUiState()
    class OnLoadMore(val list: List<TodoItem>?) : MyTODOUiState()
}