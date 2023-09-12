package com.qianyue.wanandroidmvi.ui.home

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.databinding.ItemArticleBinding
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.utils.TimeFormat
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener
import java.lang.StringBuilder

/**
 * @author QianYue
 * @since 2023/8/16
 */
class ArticleAdapter(val enableSlideMenu: Boolean = false, val slideMenuLayoutId: Int = -1) :
    BaseQuickAdapter<ArticleItem, ArticleAdapter.ArticleHolder>() {

    companion object {
        const val REFRESH_COLLECT_ICON = 1
    }

    private val inflater by lazy { LayoutInflater.from(context) }

    var onItemClick: ((ArticleItem) -> Unit)? = null

    var onCollectClick: ((ArticleItem) -> Unit)? = null

    var setupSlideMenu: ((ArticleHolder) -> Unit)? = null

    class ArticleHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var cacheItem: ArticleItem? = null
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int, item: ArticleItem?) {
        holder.binding.apply {
            tvTagTop.visibility = if (item?.type == 1) View.VISIBLE else View.GONE
            tvAuthor.visibility = if (TextUtils.isEmpty(item?.author)) View.GONE else View.VISIBLE
            tvAuthor.text = item?.author
            tvTitle.text = Html.fromHtml(item?.title, Html.FROM_HTML_MODE_COMPACT)
            tvTime.text = TimeFormat.formatDateTime(item?.publishTime)
            val sb = StringBuilder(item?.superChapterName ?: "")
            if (sb.isNotEmpty() && !TextUtils.isEmpty(item?.chapterName)) sb.append("·")
            sb.append(item?.chapterName ?: "")
            tvDetailSource.text = sb.toString()
            ivFavor.setImageResource(if (item?.collect == true) R.drawable.ic_collect_checked else R.drawable.ic_collect)
            holder.cacheItem = item
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as ArticleHolder).binding.root.hideSlideMenu()
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ArticleHolder {
        val binding = ItemArticleBinding.inflate(inflater, parent, false)
        if (slideMenuLayoutId > 0) {
            inflater.inflate(slideMenuLayoutId, binding.root, true)
        }
        val holder = ArticleHolder(binding)
        setupSlideMenu?.invoke(holder)
        binding.root.enableSlideMenu = enableSlideMenu
        binding.itemContainer.setOnClickListener {
            onItemClick?.invoke(holder.cacheItem ?: return@setOnClickListener)
        }
        binding.ivFavor.setSafeClickListener {
            onCollectClick?.invoke(holder.cacheItem ?: return@setSafeClickListener)
        }
        return holder
    }

    override fun onBindViewHolder(
        holder: ArticleHolder,
        position: Int,
        item: ArticleItem?,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position, item)
        } else {
            if (payloads[0] == REFRESH_COLLECT_ICON) {
                holder.binding.ivFavor.setImageResource(if (item?.collect == true) R.drawable.ic_collect_checked else R.drawable.ic_collect)
            }
        }
    }
}