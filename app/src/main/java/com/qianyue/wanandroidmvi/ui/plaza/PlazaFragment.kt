package com.qianyue.wanandroidmvi.ui.plaza

import android.content.Intent
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
import com.qianyue.wanandroidmvi.ext.observeBetter
import com.qianyue.wanandroidmvi.ui.detailwebpage.DetailWebPageActivity
import com.qianyue.wanandroidmvi.ui.home.ArticleAdapter
import com.qianyue.wanandroidmvi.ui.login.LoginActivity
import com.qianyue.wanandroidmvi.ui.safeAddAll
import com.qianyue.wanandroidmvi.ui.uiintent.PlazaUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.PlazaUiState
import com.qianyue.wanandroidmvi.user.User
import com.qianyue.wanandroidmvi.viewmodel.PlazaViewModel
import com.qianyue.wanandroidmvi.widgets.classicConfig

/**
 * 广场tab fragment
 *
 * @author qy
 * @since 2023-08-14
 */
class PlazaFragment : BaseFragment<PlazaViewModel>() {

    private var _binding: ListDataLayoutBinding? = null

    private val binding get() = _binding!!
    override fun lazyVM(): Lazy<PlazaViewModel> = viewModels()

    private var _adapter: ArticleAdapter? = null

    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ListDataLayoutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initRecyclerView()

        binding.emptyLayout.showProgressBar()
        binding.refreshLayout.classicConfig(
            onRefresh = {
                vm.sendUiIntent(PlazaUiIntent.RefreshIntent())
            },
            onLoadMore = {
                vm.sendUiIntent(PlazaUiIntent.LoadMoreIntent())
            }
        )

        User.userStateData.observeBetter(viewLifecycleOwner) {
            vm.sendUiIntent(PlazaUiIntent.RefreshIntent())
        }

        return root
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            _adapter = ArticleAdapter()
            adapter = this@PlazaFragment.adapter
        }

        adapter.onItemClick = {
            DetailWebPageActivity.startActivity(binding.recyclerView.context, it.link, it.title)
        }

        adapter.onCollectClick = {
            if (User.isLoginSuccess()) {
                vm.sendUiIntent(PlazaUiIntent.CollectOperate(!it.collect, it.id))
            } else startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }


    override suspend fun handleState(state: IUiState) {
        when (state) {
            is PlazaUiState.InitState -> {
                vm.sendUiIntent(PlazaUiIntent.RefreshIntent())
            }

            is PlazaUiState.OnRefreshState -> {
                if (state.list == null) {
                    binding.emptyLayout.showRefresh("加载失败，点击重试") {
                        vm.sendUiIntent(
                            PlazaUiIntent.RefreshIntent()
                        )
                    }
                    return
                }
                binding.emptyLayout.showContent()
                adapter.submitList(state.list)
                binding.refreshLayout.finishRefresh()
            }

            is PlazaUiState.OnLoadMoreState -> {
                adapter.safeAddAll(state.list)
                binding.refreshLayout.finishLoadMore()
            }

            is PlazaUiState.CollectResult -> {
                if (state.successful) {
                    adapter.notifyItemChanged(state.position, ArticleAdapter.REFRESH_COLLECT_ICON)
                } else Toaster.showShort("操作失败 -- ${state.errorMsg}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }
}