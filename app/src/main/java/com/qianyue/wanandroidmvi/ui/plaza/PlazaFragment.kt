package com.qianyue.wanandroidmvi.ui.plaza

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentPlazaBinding
import com.qianyue.wanandroidmvi.ui.detailwebpage.DetailWebPageActivity
import com.qianyue.wanandroidmvi.ui.home.ArticleAdapter
import com.qianyue.wanandroidmvi.ui.safeAddAll
import com.qianyue.wanandroidmvi.ui.uiintent.PlazaUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.PlazaUiState
import com.qianyue.wanandroidmvi.viewmodel.PlazaViewModel
import com.qianyue.wanandroidmvi.widgets.classicConfig

/**
 * 广场tab fragment
 *
 * @author qy
 * @since 2023-08-14
 */
class PlazaFragment : BaseFragment<PlazaViewModel>() {

    private var _binding: FragmentPlazaBinding? = null

    private val binding get() = _binding!!
    override fun lazyVM(): Lazy<PlazaViewModel> = viewModels()

    private var _adapter: ArticleAdapter? = null

    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPlazaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()

        binding.refreshLayout.classicConfig(
            onRefresh = {
                vm.sendUiIntent(PlazaUiIntent.RefreshIntent())
            },
            onLoadMore = {
                vm.sendUiIntent(PlazaUiIntent.LoadMoreIntent())
            }
        )

        return root
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            _adapter = ArticleAdapter()
            adapter = this@PlazaFragment.adapter
        }

        adapter.onItemClick = {
            DetailWebPageActivity.startActivity(binding.recyclerView.context, it.link)
        }
    }


    override fun handleState(state: IUiState) {
        when (state) {
            is PlazaUiState.InitState -> {
                vm.sendUiIntent(PlazaUiIntent.RefreshIntent())
            }
            is PlazaUiState.OnRefreshState -> {
                adapter.submitList(state.list)
                binding.refreshLayout.finishRefresh()
            }
            is PlazaUiState.OnLoadMoreState -> {
                adapter.safeAddAll(state.list)
                binding.refreshLayout.finishLoadMore()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}