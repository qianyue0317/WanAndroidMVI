package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.MyTODOUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyTODOUiState
import kotlin.math.abs

/**
 * @author QianYue
 * @since 2023/9/10
 */
class MyTODOViewModel: BaseViewModel<MyTODOUiIntent, MyTODOUiState>() {
    companion object {
        val TYPE_LIST = arrayOf("工作", "生活", "编程", "面试", "娱乐", "剪辑")
        val PRIORITY = arrayOf("低", "中", "高")
    }

    private var _currentIndex = 1

    override fun initState(): MyTODOUiState = MyTODOUiState.Init()

    override suspend fun processIntent(uiIntent: MyTODOUiIntent) {
        when (uiIntent) {
            is MyTODOUiIntent.Refresh -> {
                _currentIndex = 1
                val res = request { API_SERVICE.getTodoList(_currentIndex) }
                sendUiState { MyTODOUiState.OnRefresh(res.data?.datas) }
            }

            is MyTODOUiIntent.AddTodoItem -> {
                val res = request {
                    API_SERVICE.addTodoItem(
                        uiIntent.title,
                        uiIntent.desc,
                        uiIntent.date,
                        uiIntent.type ?: 1,
                        uiIntent.level ?: 1
                    ) }
                sendUiState { MyTODOUiState.OnAddResult(res.isSuccessful(), res.errorMsg) }
            }

            is MyTODOUiIntent.UpdateTodoItem -> {
                val res = request {
                    API_SERVICE.updateTodo(
                        uiIntent.id,
                        uiIntent.title,
                        uiIntent.desc,
                        uiIntent.date,
                        uiIntent.status,
                        uiIntent.type,
                        uiIntent.level
                    )
                }
                sendUiState { MyTODOUiState.OnUpdateResult(res.isSuccessful(), res.errorMsg) }
            }

            is MyTODOUiIntent.Finish -> {
                val res = request { API_SERVICE.updateStatus(uiIntent.id, abs(uiIntent.status - 1)) }
                sendUiState { MyTODOUiState.OnUpdateResult(res.isSuccessful(), res.errorMsg) }
            }

            is MyTODOUiIntent.LoadMore -> {
                _currentIndex++
                val res = request { API_SERVICE.getTodoList(_currentIndex) }
                sendUiState { MyTODOUiState.OnLoadMore(res.data?.datas) }
            }

            is MyTODOUiIntent.Delete -> {
                val res = request { API_SERVICE.deleteTodo(uiIntent.id) }
                sendUiState { MyTODOUiState.OnDeleteResult(res.isSuccessful(), res.errorMsg) }
            }
        }
    }
}