package com.qianyue.wanandroidmvi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.FragmentHomeBinding
import com.qianyue.wanandroidmvi.ui.detailwebpage.DetailWebPageActivity
import com.qianyue.wanandroidmvi.ui.uiintent.HomeUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.HomeUiState
import com.qianyue.wanandroidmvi.viewmodel.HomeViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

/**
 * 首页fragment
 *
 * @author qy
 * @since 2023-08-14
 */
class HomeFragment : BaseFragment<HomeViewModel>() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var _concatAdapter: ConcatAdapter

    private lateinit var _articleAdapter: ArticleAdapter

    private lateinit var _headAdapter: HeadAdapter


    override fun lazyVM(): Lazy<HomeViewModel> =
        lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()
        initRefreshLayout()

        return root
    }

    private fun initRefreshLayout() {
        binding.refreshLayout.apply {
            setEnableRefresh(true)
            setEnableLoadMore(true)
            setRefreshFooter(ClassicsFooter(context))
            setRefreshHeader(ClassicsHeader(context))
            setOnRefreshListener {
                vm.sendUiIntent(HomeUiIntent.RefreshIntent)
            }
            setOnLoadMoreListener {
                vm.sendUiIntent(HomeUiIntent.LoadMore)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.sendUiIntent(HomeUiIntent.RefreshIntent)
    }

    override fun handleState(state: IUiState) {
        when (state) {
            is HomeUiState.RefreshState -> {
                _articleAdapter.submitList(state.articleList)
                binding.refreshLayout.finishRefresh()
            }
            is HomeUiState.LoadMoreState -> {
                _articleAdapter.addAll(state.articleList ?: emptyList())
                binding.refreshLayout.finishLoadMore()
            }
            is HomeUiState.LoadBanner -> {
                _headAdapter.setItem(state.bannerList, null)
            }
            is HomeUiState.ErrorState -> {
                Toaster.showShort("出错了！")
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        _headAdapter = HeadAdapter()
        _articleAdapter = ArticleAdapter()
        _concatAdapter = ConcatAdapter(_headAdapter, _articleAdapter)

        binding.recyclerView.adapter = _concatAdapter

        _articleAdapter.onItemClick = {
            DetailWebPageActivity.startActivity(binding.recyclerView.context, it.link)
        }

        _headAdapter.onBannerItemClick = {
            DetailWebPageActivity.startActivity(binding.recyclerView.context, it.url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}