package com.qianyue.wanandroidmvi.ui.uiintent

import com.qianyue.wanandroidmvi.base.IUiIntent

/**
 * 项目外层fragment和内层fragment公用一套 ViewModel State Intent
 *
 * @author QianYue
 * @since 2023/8/15
 */
sealed class ProjectUiIntent: IUiIntent {
    object GetProjectCategories: ProjectUiIntent()

    class RefreshProjectList(val cid: Int) : ProjectUiIntent()

    class LoadMoreProjectList(val cid: Int): ProjectUiIntent()
}