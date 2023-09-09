package com.qianyue.wanandroidmvi.ui.projectlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentProjectBinding
import com.qianyue.wanandroidmvi.ui.uiintent.ProjectUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.ProjectUiState
import com.qianyue.wanandroidmvi.viewmodel.ProjectViewModel

/**
 * 项目fragment
 *
 * @author qy
 * @since 2023-08-14
 */
class ProjectFragment : BaseFragment<ProjectViewModel>() {

    private var _binding: FragmentProjectBinding? = null

    private val binding get() = _binding!!

    override fun lazyVM(): Lazy<ProjectViewModel> = viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.emptyLayout.showProgressBar()
        return root
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is ProjectUiState.InitState -> {
                vm.sendUiIntent(ProjectUiIntent.GetProjectCategories)
            }

            is ProjectUiState.OnCategoriesLoad -> {
                state.categories?.also {
                    val fragmentAdapter = object :FragmentPagerAdapter(this@ProjectFragment.childFragmentManager) {
                        override fun getCount() = it.size

                        override fun getItem(position: Int) = ProjectListFragment(it[position].id)

                        override fun getPageTitle(position: Int) = it[position].name
                    }
                    binding.viewPager.adapter = fragmentAdapter
                    binding.tabLayout.setupWithViewPager(binding.viewPager)
                    binding.emptyLayout.showContent()
                }?: run {
                    binding.emptyLayout.showRefresh("加载失败，点击重试") {
                        vm.sendUiIntent(ProjectUiIntent.GetProjectCategories)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}