package com.qianyue.wanandroidmvi.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.qianyue.wanandroidmvi.databinding.ItemHeaderBinding
import com.qianyue.wanandroidmvi.model.bean.BannerItem
import com.qianyue.wanandroidmvi.utils.WanLog

/**
 * 头部adapter，用于添加banner
 *
 * @author QianYue
 * @since 2023/8/22
 */
class HeadAdapter : BaseSingleItemAdapter<List<BannerItem>, HeadAdapter.BannerHolder>() {

    private val _inflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    private var _bannerAdapter: BannerAdapter? = null

    private val headCache: HeadCache = HeadCache()

    var onBannerItemClick: ((BannerItem) -> Unit)? = null

    inner class BannerHolder(val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: BannerHolder, item: List<BannerItem>?) {
        if (_bannerAdapter == null) {
            _bannerAdapter = BannerAdapter()
        }
        holder.binding.banner.setOnPageClickListener { _, position ->
            onBannerItemClick?.invoke(item?.get(position) ?: return@setOnPageClickListener)
        }
        holder.binding.banner.setAdapter(_bannerAdapter).create(item ?: return)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): BannerHolder {
        val binding = ItemHeaderBinding.inflate(_inflater, parent, false)
        binding.banner.setCanLoop(true)
        return BannerHolder(binding)
    }


    inner class HeadCache : RecyclerView.ViewCacheExtension() {

        var cacheHolder: RecyclerView.ViewHolder? = null

        override fun getViewForPositionAndType(
            recycler: RecyclerView.Recycler,
            position: Int,
            type: Int
        ): View? {
            return cacheHolder?.itemView
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.setViewCacheExtension(headCache)
    }
}