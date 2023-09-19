package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * @author QianYue
 * @since 2023/9/10
 */
sealed class MyTODOUiIntent : IUiIntent {
    class Refresh : MyTODOUiIntent()
    class AddTodoItem(val title: String, val desc: String, val date: String) : MyTODOUiIntent()
    class LoadMore : MyTODOUiIntent()
}