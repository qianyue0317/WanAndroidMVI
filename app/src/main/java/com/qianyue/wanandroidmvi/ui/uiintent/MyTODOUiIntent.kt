package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/9/10
 */
sealed class MyTODOUiIntent : IUiIntent {
    class Refresh : MyTODOUiIntent()
    class AddTodoItem(
        val title: String,
        val desc: String,
        val date: String?,
        val level: Int?,
        val type: Int?
    ) : MyTODOUiIntent()

    class UpdateTodoItem(
        val id: Int,
        val status: Int,
        val title: String,
        val desc: String,
        val date: String,
        val level: Int,
        val type: Int
    ) : MyTODOUiIntent()

    class LoadMore : MyTODOUiIntent()
    class Delete(val id: Int) : MyTODOUiIntent()
    class Finish(val id: Int, val status: Int) : MyTODOUiIntent()
}