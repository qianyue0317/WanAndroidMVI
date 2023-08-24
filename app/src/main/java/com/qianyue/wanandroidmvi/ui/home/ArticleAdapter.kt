package com.qianyue.wanandroidmvi.ui.home

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.databinding.ItemArticleBinding
import com.qianyue.wanandroidmvi.model.bean.ArticleItem
import com.qianyue.wanandroidmvi.utils.TimeFormat
import java.lang.StringBuilder

/**
 * @author QianYue
 * @since 2023/8/16
 */
class ArticleAdapter :
    BaseMultiItemAdapter<ArticleItem>() {

    companion object {
        const val TYPE_ARTICLE = 0
    }

    private val inflater by lazy { LayoutInflater.from(context) }

    var onItemClick: ((ArticleItem) -> Unit)? = null

    init {
        addItemType(TYPE_ARTICLE, object : OnMultiItemAdapterListener<ArticleItem, ArticleHolder> {
            override fun onBind(holder: ArticleHolder, position: Int, item: ArticleItem?) {
                holder.binding.apply {
                    tvTagTop.visibility = if (item?.type == 1) View.VISIBLE else View.GONE
                    tvAuthor.visibility = if (TextUtils.isEmpty(item?.author)) View.GONE else View.VISIBLE
                    tvAuthor.text = item?.author
                    tvTitle.text = item?.title
                    tvTime.text = TimeFormat.formatDateTime(item?.publishTime)
                    val sb = StringBuilder(item?.superChapterName?:"")
                    if (sb.isNotEmpty() && !TextUtils.isEmpty(item?.chapterName)) sb.append("·")
                    sb.append(item?.chapterName ?: "")
                    tvDetailSource.text = sb.toString()
                    ivFavor.setImageResource(if (item?.collect == true) R.drawable.ic_collect_checked else R.drawable.ic_collect)
                    holder.cacheItem = item
                }
            }

            override fun onCreate(
                context: Context,
                parent: ViewGroup,
                viewType: Int
            ): ArticleHolder {
                val binding = ItemArticleBinding.inflate(inflater, parent, false)
                val holder = ArticleHolder(binding)
                binding.root.setOnClickListener {
                    onItemClick?.invoke(holder.cacheItem ?: return@setOnClickListener)
                }
                return holder
            }

        }).onItemViewType { _, _->
            TYPE_ARTICLE
        }
    }

    inner class ArticleHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        var cacheItem: ArticleItem? = null
    }

}