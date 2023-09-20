package com.qianyue.wanandroidmvi.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.base.BaseActivity
import com.qianyue.wanandroidmvi.base.IUiState
import com.qianyue.wanandroidmvi.databinding.ActivitySearchBinding
import com.qianyue.wanandroidmvi.databinding.ItemHistoryKeywordBinding
import com.qianyue.wanandroidmvi.ext.showAlertDialogSuspend
import com.qianyue.wanandroidmvi.ui.uiintent.SearchUiIntent
import com.qianyue.wanandroidmvi.ui.uistate.SearchUiState
import com.qianyue.wanandroidmvi.viewmodel.SearchViewModel
import com.qianyue.wanandroidmvi.widgets.RoundedDrawable
import com.qianyue.wanandroidmvi.widgets.dp2px
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener
import kotlinx.coroutines.launch

/**
 * 搜索页面
 *
 * @author QianYue
 * @since 2023/9/9
 */
class SearchActivity : BaseActivity<SearchViewModel>() {

    private val tagColorIds = arrayOf(
        R.color.tag_bg_color1,
        R.color.tag_bg_color2,
        R.color.tag_bg_color3,
        R.color.tag_bg_color4,
        R.color.tag_bg_color5,
        R.color.tag_bg_color6
    )

    private var _binding: ActivitySearchBinding? = null

    private val binding: ActivitySearchBinding get() = _binding!!

    private var historyAdapter: HistoryWordAdapter? = null

    override fun lazyVM(): Lazy<SearchViewModel> = viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "搜索"
        }

        historyAdapter = HistoryWordAdapter()
        binding.recyclerView.also {
            it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            it.adapter = historyAdapter
        }

        binding.tvClearHistory.setSafeClickListener {
            lifecycleScope.launch {
                val result = showAlertDialogSuspend("确定要清空搜索记录吗？", "")
                if (result == true) vm.sendUiIntent(SearchUiIntent.ClearHistory())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun onDeleteKeyWordHistory(pos: Int) {
        vm.sendUiIntent(SearchUiIntent.RemoveKeyword(pos))
    }

    @SuppressLint("NotifyDataSetChanged")
    override suspend fun handleState(state: IUiState) {
        when (state) {
            is SearchUiState.Init -> {
                vm.sendUiIntent(SearchUiIntent.LoadHistory())
                vm.sendUiIntent(SearchUiIntent.GetWords())
            }

            is SearchUiState.OnLoadHistoryWord -> {
                historyAdapter?.submitList(state.keywordList)
            }

            is SearchUiState.OnSaveHistory -> {
                if (state.fromPos < 0) historyAdapter?.notifyItemInserted(0)
                else historyAdapter?.notifyItemMoved(state.fromPos, state.toPos)
            }

            is SearchUiState.OnClearHistory -> {
                historyAdapter?.notifyDataSetChanged()
            }

            is SearchUiState.OnDeleteKeyword -> {
                historyAdapter?.notifyItemRemoved(state.pos)
            }

            is SearchUiState.WordsResult -> {
                state.list?.forEachIndexed { index, item ->
                    binding.flowLayout.lineSpace = 8f.dp2px()
                    binding.flowLayout.itemSpace = 10f.dp2px()
                    binding.flowLayout.addView(AppCompatTextView(this).apply {
                        text = item.name
                        setTextColor(Color.WHITE)
                        setPadding(
                            8f.dp2px().toInt(),
                            4f.dp2px().toInt(),
                            8f.dp2px().toInt(),
                            4f.dp2px().toInt()
                        )
                        background = RoundedDrawable().also {
                            it.color =
                                resources.getColor(tagColorIds[index % tagColorIds.size], theme)
                            it.semicircle = true
                        }
                        setSafeClickListener {
                            query(text.toString().takeIf { it.isNotEmpty() }
                                ?: return@setSafeClickListener)
                        }
                    })
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        vm.sendUiIntent(SearchUiIntent.SaveHistoryLocal())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.run {
            maxWidth = Integer.MAX_VALUE
            onActionViewExpanded()
            queryHint = "输入关键字搜索"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(word: String?): Boolean {
                    word?.let { query(it) }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            isSubmitButtonEnabled = true
            val ivGoBtn = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_go_btn)
            ivGoBtn.setImageResource(R.drawable.ic_search)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun query(keyword: String) {
        vm.sendUiIntent(SearchUiIntent.SaveHistory(keyword))
        SearchResultActivity.startActivity(this, keyword)
    }

    inner class HistoryWordAdapter : BaseQuickAdapter<String, ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int, item: String?) {
            holder.binding.tvKeyWord.text = item
            holder.cacheItem = item
        }

        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            val binding =
                ItemHistoryKeywordBinding.inflate(LayoutInflater.from(context), parent, false)
            val viewHolder = ViewHolder(binding)
            binding.ivClose.setSafeClickListener {
                this@SearchActivity.onDeleteKeyWordHistory(viewHolder.bindingAdapterPosition)
            }
            binding.root.setSafeClickListener {
                query(viewHolder.cacheItem ?: return@setSafeClickListener)
            }
            return viewHolder
        }
    }

    class ViewHolder(val binding: ItemHistoryKeywordBinding, var cacheItem: String? = null) :
        RecyclerView.ViewHolder(binding.root)
}