package com.qianyue.wanandroidmvi.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ListDataLayoutBinding
import com.qianyue.wanandroidmvi.ui.detailwebpage.DetailWebPageActivity
import com.qianyue.wanandroidmvi.ui.home.ArticleAdapter
import com.qianyue.wanandroidmvi.ui.login.LoginActivity
import com.qianyue.wanandroidmvi.ui.safeAddAll
import com.qianyue.wanandroidmvi.ui.showContentOrEmpty
import com.qianyue.wanandroidmvi.ui.uiintent.SearchUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.SearchUiState
import com.qianyue.wanandroidmvi.user.User
import com.qianyue.wanandroidmvi.viewmodel.SearchViewModel
import com.qianyue.wanandroidmvi.widgets.classicConfig

/**
 * @author QianYue
 * @since 2023/9/12
 */
class SearchResultActivity : BaseActivity<SearchViewModel>() {
    private var _binding: ListDataLayoutBinding? = null

    private val binding: ListDataLayoutBinding get() = _binding!!

    private var _adapter: ArticleAdapter? = null

    private val adapter: ArticleAdapter get() = _adapter!!

    override fun lazyVM(): Lazy<SearchViewModel> = viewModels()

    companion object {
        @JvmStatic
        fun startActivity(context: Context, keyword: String) {
            context.startActivity(
                Intent(
                    context,
                    SearchResultActivity::class.java
                ).run { putExtra("keyWord", keyword) })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ListDataLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        intent.getStringExtra("keyWord")?.let { vm.keyWord = it }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = vm.keyWord
        }

        initRecyclerView()

        binding.refreshLayout.classicConfig(
            onRefresh = {
                vm.sendUiIntent(SearchUiIntent.Refresh())
            },
            onLoadMore = {
                vm.sendUiIntent(SearchUiIntent.LoadMore())
            })

    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            _adapter = ArticleAdapter()
            adapter = _adapter
        }
        adapter.onCollectClick = {
            if (User.isLoginSuccess()) {
                vm.sendUiIntent(SearchUiIntent.CollectOperate(!it.collect, it.id))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        adapter.onItemClick = {
            DetailWebPageActivity.startActivity(this, it.link, it.title)
        }
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is SearchUiState.Init -> {
                binding.emptyLayout.showProgressBar()
                vm.sendUiIntent(SearchUiIntent.Refresh())
            }

            is SearchUiState.OnRefresh -> {
                adapter.submitList(state.list)
                adapter.showContentOrEmpty(binding.emptyLayout)
                binding.refreshLayout.finishRefresh()
            }

            is SearchUiState.OnLoadMore -> {
                adapter.safeAddAll(state.list)
                adapter.showContentOrEmpty(binding.emptyLayout)
                binding.refreshLayout.finishLoadMore()
            }

            is SearchUiState.CollectResult -> {
                if (state.successful) {
                    adapter.notifyItemChanged(
                        state.position,
                        ArticleAdapter.REFRESH_COLLECT_ICON
                    )
                } else Toaster.showShort("操作失败 -- ${state.errorMsg}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _adapter = null
    }
}