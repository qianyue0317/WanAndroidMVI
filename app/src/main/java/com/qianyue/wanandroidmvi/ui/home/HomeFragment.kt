package com.qianyue.wanandroidmvi.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseFragment
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ListDataLayoutBinding
import com.qianyue.wanandroidmvi.ext.observeBetter
import com.qianyue.wanandroidmvi.ui.detailwebpage.DetailWebPageActivity
import com.qianyue.wanandroidmvi.ui.login.LoginActivity
import com.qianyue.wanandroidmvi.ui.safeAddAll
import com.qianyue.wanandroidmvi.ui.uiintent.HomeUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.HomeUiState
import com.qianyue.wanandroidmvi.user.User
import com.qianyue.wanandroidmvi.viewmodel.HomeViewModel
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader

/**
 * 首页fragment
 *
 * @author qy
 * @since 2023-08-14
 */
class HomeFragment : BaseFragment<HomeViewModel>() {

    private var _binding: ListDataLayoutBinding? = null

    private val binding get() = _binding!!

    private lateinit var _concatAdapter: ConcatAdapter

    private lateinit var _articleAdapter: ArticleAdapter

    private lateinit var _headAdapter: HeadAdapter


    override fun lazyVM(): Lazy<HomeViewModel> = viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ListDataLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.emptyLayout.showProgressBar()
        initRecyclerView()
        initRefreshLayout()

        User.userStateData.observeBetter(viewLifecycleOwner) {
            vm.sendUiIntent(HomeUiIntent.RefreshIntent)
        }

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

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is HomeUiState.Init -> vm.sendUiIntent(HomeUiIntent.RefreshIntent)
            is HomeUiState.RefreshState -> {
                binding.refreshLayout.finishRefresh()
                if (state.articleList?.isEmpty() != false) {
                    binding.emptyLayout.showRefresh(tip = "加载失败，点击重试") {
                        vm.sendUiIntent(HomeUiIntent.RefreshIntent)
                    }
                    return
                }
                binding.emptyLayout.showContent()
                _articleAdapter.submitList(state.articleList)
                _headAdapter.setItem(state.bannerList, null)
            }
            is HomeUiState.LoadMoreState -> {
                _articleAdapter.safeAddAll(state.articleList)
                binding.refreshLayout.finishLoadMore()
            }
            is HomeUiState.ErrorState -> {
                Toaster.showShort("出错了！")
            }
            is HomeUiState.CollectResult -> {
                if (state.successful) {
                    _articleAdapter.notifyItemChanged(state.position, ArticleAdapter.REFRESH_COLLECT_ICON)
                } else Toaster.showShort("操作失败 -- ${state.errorMsg}")
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
            DetailWebPageActivity.startActivity(binding.recyclerView.context, it.link, it.title)
        }

        _articleAdapter.onCollectClick = {
            if (User.isLoginSuccess()) {
                vm.sendUiIntent(HomeUiIntent.CollectOperate(!it.collect, it.id))
            } else startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        _headAdapter.onBannerItemClick = {
            DetailWebPageActivity.startActivity(binding.recyclerView.context, it.url, it.title)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}