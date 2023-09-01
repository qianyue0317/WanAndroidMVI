package com.qianyue.wanandroidmvi.viewmodel

import com.qianyue.wanandroidmvi.base.BaseViewModel
import com.qianyue.wanandroidmvi.model.network.API_SERVICE
import com.qianyue.wanandroidmvi.ui.uiintent.ProjectUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.ProjectUiState

/**
 * 项目外层fragment和内层fragment公用一套 ViewModel State Intent
 *
 * @author Qy
 * @since 2023/8/24
 */
class ProjectViewModel : BaseViewModel<ProjectUiIntent, ProjectUiState>() {
    private var _currentPageIndex = 1

    override fun initState(): ProjectUiState = ProjectUiState.InitState

    override suspend fun processIntent(uiIntent: ProjectUiIntent) {
        when (uiIntent) {
            is ProjectUiIntent.GetProjectCategories -> {
                val categoriesRes = request { API_SERVICE.getProjectCategories() }
                categoriesRes.apply { sendUiState { ProjectUiState.OnCategoriesLoad(data) } }
            }

            is ProjectUiIntent.RefreshProjectList -> {
                _currentPageIndex = 1
                val projectListRes =
                    request { API_SERVICE.getProjectList(_currentPageIndex, uiIntent.cid) }
                projectListRes.apply { sendUiState { ProjectUiState.OnProjectListRefresh(data?.datas) } }
            }

            is ProjectUiIntent.LoadMoreProjectList -> {
                _currentPageIndex++
                val projectListRes =
                    request { API_SERVICE.getProjectList(_currentPageIndex, uiIntent.cid) }
                projectListRes.apply { sendUiState { ProjectUiState.OnProjectListLoadMore(data?.datas) } }
            }
        }

    }

}