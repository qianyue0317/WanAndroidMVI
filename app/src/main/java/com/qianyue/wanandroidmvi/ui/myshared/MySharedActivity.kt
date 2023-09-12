package com.qianyue.wanandroidmvi.ui.myshared

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.hjq.toast.Toaster
import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ListDataLayoutBinding
import com.qianyue.wanandroidmvi.ui.detailwebpage.DetailWebPageActivity
import com.qianyue.wanandroidmvi.ui.home.ArticleAdapter
import com.qianyue.wanandroidmvi.ui.safeAddAll
import com.qianyue.wanandroidmvi.ui.showContentOrEmpty
import com.qianyue.wanandroidmvi.ui.uiintent.MySharedUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.MySharedUiState
import com.qianyue.wanandroidmvi.viewmodel.MySharedViewModel
import com.qianyue.wanandroidmvi.widgets.classicConfig
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener

/**
 * @author QianYue
 * @since 2023/9/9
 */
class MySharedActivity: BaseActivity<MySharedViewModel>() {
    private var _adapter: ArticleAdapter? = null

    private var _binding: ListDataLayoutBinding? = null

    private val binding: ListDataLayoutBinding get() = _binding!!

    override fun lazyVM(): Lazy<MySharedViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ListDataLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "我的分享"
            setDisplayHomeAsUpEnabled(true)
        }


        binding.emptyLayout.showProgressBar()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.refreshLayout.classicConfig(onRefresh = {
            vm.sendUiIntent(MySharedUiIntent.Refresh())
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            _adapter = ArticleAdapter(true, R.layout.my_share_article_slide_menu).apply {
                setupSlideMenu = { holder ->
                    holder.binding.root.findViewById<TextView>(R.id.tv_menu_1).setSafeClickListener {
                        vm.sendUiIntent(
                            MySharedUiIntent.DeleteShareArticle(
                                holder.cacheItem?.id ?: return@setSafeClickListener
                            )
                        )
                    }
                }
            }
            adapter = _adapter
        }
        _adapter?.onItemClick = {
            DetailWebPageActivity.startActivity(this, it.link, it.title)
        }
    }

    override suspend fun handleState(state: IUiState) {
        when (state) {
            is MySharedUiState.Init -> vm.sendUiIntent(MySharedUiIntent.Refresh())
            is MySharedUiState.ShareResult -> {
                if (state.success) vm.sendUiIntent(MySharedUiIntent.Refresh())
                Toaster.showShort(state.success.takeIf { it }?.let { "分享成功" }
                    ?: "分享失败--${state.msg}")
            }

            is MySharedUiState.OnRefresh -> {
                binding.refreshLayout.finishRefresh()
                _adapter?.submitList(state.list)
                _adapter?.showContentOrEmpty(binding.emptyLayout)
                binding.refreshLayout.setEnableLoadMore(!state.noMore)
            }

            is MySharedUiState.OnLoadMore -> {
                binding.refreshLayout.finishLoadMore()
                _adapter?.safeAddAll(state.list)
                binding.refreshLayout.setEnableLoadMore(!state.noMore)
            }

            is MySharedUiState.RefreshError -> {
                binding.refreshLayout.finishRefresh()
                binding.emptyLayout.showRefresh("加载失败，点击重试") { vm.sendUiIntent(MySharedUiIntent.Refresh()) }
            }

            is MySharedUiState.LoadMoreError -> {
                binding.refreshLayout.finishLoadMore()
                Toaster.showShort("加载失败！")
            }

            is MySharedUiState.DeleteResult -> {
                if (state.successful) {
                    val pos = _adapter?.items?.indexOfFirst { it.id == state.id } ?: return
                    _adapter?.items?.toMutableList()?.apply {
                        removeAt(pos)
                        _adapter?.items = this
                    }
                    _adapter?.notifyItemRemoved(pos)
                } else {
                    Toaster.showShort("删除失败--${state.errorMsg}")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu_plus, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_plus -> {
                BottomSheetDialog(this, R.style.WanBottomSheetDialog).apply {
                    setContentView(R.layout.bottom_sheet_share)

                    val inputTitle = findViewById<TextInputLayout>(R.id.til_title)
                    val inputLink = findViewById<TextInputLayout>(R.id.til_link)


                    findViewById<TextView>(R.id.tv_share)!!.setSafeClickListener {
                        if (validInput(inputTitle!!, inputLink!!)) {
                            vm.sendUiIntent(
                                MySharedUiIntent.Share(
                                    inputTitle.editText!!.text.toString().trim(),
                                    inputLink.editText!!.text.toString().trim()
                                )
                            )
                            dismiss()
                        }
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validInput(inputTitle: TextInputLayout, inputLink: TextInputLayout): Boolean {
        if (TextUtils.isEmpty(inputTitle.editText!!.text.toString().trim())) {
            inputTitle.error = "标题不能为空"
            return false
        }
        if (TextUtils.isEmpty(inputLink.editText!!.text.toString().trim())) {
            inputLink.error = "链接不能为空"
            return false
        }
        return true
    }
}