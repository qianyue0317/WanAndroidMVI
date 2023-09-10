package com.qianyue.wanandroidmvi.ui.projectlist

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentProjectListBinding
import com.qianyue.wanandroidmvi.ext.dp2px
import com.qianyue.wanandroidmvi.ui.detailwebpage.DetailWebPageActivity
import com.qianyue.wanandroidmvi.ui.safeAddAll
import com.qianyue.wanandroidmvi.ui.uiintent.ProjectUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.ProjectUiState
import com.qianyue.wanandroidmvi.viewmodel.ProjectViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader

/**
 * @author QianYue
 * @since 2023/8/24
 */
class ProjectListFragment(var cid: Int) : BaseFragment<ProjectViewModel>() {

    constructor() : this(-1)

    private var _binding: FragmentProjectListBinding? = null

    private val binding get() = _binding!!

    private var _adapter: ProjectAdapter? = null

    private val adapter: ProjectAdapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectListBinding.inflate(inflater, container, false)
        initRecyclerView()
        initRefreshLayout()
        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("cid_", cid)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        cid = savedInstanceState?.getInt("cid_") ?: -1
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val pos = parent.getChildAdapterPosition(view)
                    outRect.top = context?.dp2px(16f)?.toInt() ?: 0
                    if (pos.mod(2) == 0) {
                        outRect.left = context?.dp2px(16f)?.toInt() ?: 0
                        outRect.right = context?.dp2px(8f)?.toInt() ?: 0
                    } else {
                        outRect.left = context?.dp2px(8f)?.toInt() ?: 0
                        outRect.right = context?.dp2px(16f)?.toInt() ?: 0
                    }
                }
            })
            _adapter = ProjectAdapter()
            adapter = this@ProjectListFragment.adapter
            _adapter?.onItemClick = { _, value ->
                DetailWebPageActivity.startActivity(this.context, value.link, value.title)
            }
        }
    }

    private fun initRefreshLayout() {
        binding.projectRefreshLayout.apply {
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setRefreshHeader(ClassicsHeader(this.context))
            setRefreshFooter(ClassicsFooter(this.context))
            setOnRefreshListener { vm.sendUiIntent(ProjectUiIntent.RefreshProjectList(cid)) }
            setOnLoadMoreListener { vm.sendUiIntent(ProjectUiIntent.LoadMoreProjectList(cid)) }
        }
    }

    override fun lazyVM(): Lazy<ProjectViewModel> = viewModels()

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is ProjectUiState.InitState -> vm.sendUiIntent(ProjectUiIntent.RefreshProjectList(cid))
            is ProjectUiState.OnProjectListRefresh -> {
                adapter.submitList(state.projectList)
                binding.projectRefreshLayout.finishRefresh()
            }
            is ProjectUiState.OnProjectListLoadMore -> {
                adapter.safeAddAll(state.projectList)
                binding.projectRefreshLayout.finishLoadMore()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}