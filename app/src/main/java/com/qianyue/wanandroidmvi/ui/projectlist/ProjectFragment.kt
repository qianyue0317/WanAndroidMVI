package com.qianyue.wanandroidmvi.ui.projectlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentProjectBinding
import com.qianyue.wanandroidmvi.ui.uiintent.ProjectUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.ProjectUiState
import com.qianyue.wanandroidmvi.utils.WanLog
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

    override fun lazyVM(): Lazy<ProjectViewModel> =
        lazy { ViewModelProvider(this)[ProjectViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun handleState(state: IUiState) {
        when (state) {
            is ProjectUiState.InitState -> {
                vm.sendUiIntent(ProjectUiIntent.GetProjectCategories)
            }

            is ProjectUiState.OnCategoriesLoad -> {
                WanLog.d(msg = "state: $state")
                state.categories?.also {
                    val fragmentAdapter = object :FragmentStatePagerAdapter(this@ProjectFragment.childFragmentManager) {
                        override fun getCount() = it.size

                        override fun getItem(position: Int) = ProjectListFragment(it[position].id)

                        override fun getPageTitle(position: Int) = it[position].name
                    }
                    binding.viewPager.adapter = fragmentAdapter
                    binding.tabLayout.setupWithViewPager(binding.viewPager)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}