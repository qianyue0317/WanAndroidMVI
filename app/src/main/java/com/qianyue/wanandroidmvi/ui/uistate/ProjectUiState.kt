package com.qianyue.wanandroidmvi.ui.uistate

import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.model.bean.ProjectCategory
import com.qianyue.wanandroidmvi.model.bean.ProjectItem

/**
 * @author QianYue
 * @since 2023/8/15
 */
sealed class ProjectUiState: IUiState {
    object InitState : ProjectUiState()
    class OnCategoriesLoad(val categories: List<ProjectCategory>?): ProjectUiState()

    class OnProjectListRefresh(val projectList: List<ProjectItem>? = null): ProjectUiState()
    class OnProjectListLoadMore(val projectList: List<ProjectItem>? = null): ProjectUiState()
}