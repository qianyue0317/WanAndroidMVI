package com.qianyue.wanandroidmvi.ui.mycollected

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ListDataLayoutBinding
import com.qianyue.wanandroidmvi.ui.detailwebpage.DetailWebPageActivity
import com.qianyue.wanandroidmvi.ui.home.ArticleAdapter
import com.qianyue.wanandroidmvi.ui.safeAddAll
import com.qianyue.wanandroidmvi.ui.uiintent.MyCollectedIntent
import com.qianyue.wanandroidmvi.ui.uistate.MyCollectedState
import com.qianyue.wanandroidmvi.viewmodel.MyCollectedViewModel
import com.qianyue.wanandroidmvi.widgets.classicConfig

/**
 * @author QianYue
 * @since 2023/8/31
 */
class MyCollectedArticleFragment(): BaseFragment<MyCollectedViewModel>() {

    private var _binding: ListDataLayoutBinding? = null

    private val binding: ListDataLayoutBinding get() = _binding!!

    private var adapter: ArticleAdapter? = null
    override fun lazyVM(): Lazy<MyCollectedViewModel> = viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListDataLayoutBinding.inflate(inflater, container, false)

        binding.emptyLayout.showRefresh {
            vm.sendUiIntent(MyCollectedIntent.RefreshArticleData())
        }

        binding.refreshLayout.classicConfig(onRefresh = {
            vm.sendUiIntent(MyCollectedIntent.RefreshArticleData())
        }, onLoadMore = {
            vm.sendUiIntent(MyCollectedIntent.LoadMoreArticleData())
        })

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this@MyCollectedArticleFragment.adapter = ArticleAdapter()
            adapter = this@MyCollectedArticleFragment.adapter
        }
        adapter?.onCollectClick = {
            vm.sendUiIntent(MyCollectedIntent.UncollectArticle(it.originId))
        }
        adapter?.onItemClick = {
            DetailWebPageActivity.startActivity(requireContext(), it.link, it.title)
        }
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is MyCollectedState.Init -> {
                binding.emptyLayout.showProgressBar()
                vm.sendUiIntent(MyCollectedIntent.RefreshArticleData())
            }
            is MyCollectedState.OnRefreshArticleList -> {
                if (state.list == null) {
                    binding.emptyLayout.showRefresh("加载失败，请重试") {  }
                    return
                }
                if (state.list!!.isEmpty()) {
                    binding.emptyLayout.showNoData()
                    return
                }
                adapter?.submitList(state.list)
                binding.refreshLayout.finishRefresh()
                binding.emptyLayout.showContent()
            }
            is MyCollectedState.OnLoadMoreArticleList -> {
                adapter?.safeAddAll(state.list)
                binding.refreshLayout.finishLoadMore()
                binding.emptyLayout.showContent()
            }
            is MyCollectedState.ChangeLastPageState -> {
                binding.refreshLayout.setEnableLoadMore(!state.isListPage)
            }
            is MyCollectedState.UncollectResult -> {
                if (state.successful) {
                    adapter?.removeAt(state.position)
                    if (adapter?.items?.isEmpty() != false) {
                        binding.emptyLayout.showNoData()
                    }
                }
                else Toaster.showShort("操作失败 -- ${state.errorMsg}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}