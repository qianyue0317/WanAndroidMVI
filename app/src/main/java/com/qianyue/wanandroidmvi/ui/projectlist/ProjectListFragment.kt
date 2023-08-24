package com.qianyue.wanandroidmvi.ui.projectlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentProjectListBinding
import com.qianyue.wanandroidmvi.ui.uiintent.ProjectUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.ProjectUiState
import com.qianyue.wanandroidmvi.viewmodel.ProjectViewModel

/**
 * @author QianYue
 * @since 2023/8/24
 */
class ProjectListFragment(var cid: Int): BaseFragment<ProjectViewModel>() {

    // TODO: 底部导航是replace方案，导致页面重建
    constructor(): this(-1)

    private var _binding: FragmentProjectListBinding? = null

    private val binding get() = _binding!!

    private var _adapter: ProjectAdapter? = null

    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectListBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("cid_", cid)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        cid = savedInstanceState?.getInt("cid_")?: -1
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            _adapter = ProjectAdapter()
            adapter = this@ProjectListFragment.adapter
        }
    }

    override fun lazyVM(): Lazy<ProjectViewModel> = viewModels()

    override fun handleState(state: IUiState) {
        when (state) {
            is ProjectUiState.InitState -> {
                vm.sendUiIntent(ProjectUiIntent.RefreshProjectList(cid))
            }
            is ProjectUiState.OnProjectListRefresh -> {
                adapter.submitList(state.projectList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}