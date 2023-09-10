package com.qianyue.wanandroidmvi.ui.mycollected

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qianyue.wanandroidmvi.databinding.ItemWebaddressBinding
import com.qianyue.wanandroidmvi.model.bean.WebAddressItem

/**
 * @author QianYue
 * @since 2023/9/9
 */
class WebAddressAdapter: BaseQuickAdapter<WebAddressItem, WebAddressAdapter.ViewHolder>() {

    var onUncollectItemListener: ((WebAddressItem) -> Unit)? = null

    inner class ViewHolder(val binding: ItemWebaddressBinding, var cacheItem: WebAddressItem? = null): RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: WebAddressItem?) {
        holder.binding.apply {
            tvTitle.text = item?.name
            tvWebaddress.text = item?.link
        }
        holder.cacheItem = item
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemWebaddressBinding.inflate(LayoutInflater.from(context), parent, false)
        val holder = ViewHolder(binding)

        binding.ivCollect.setOnClickListener {
            onUncollectItemListener?.invoke(holder.cacheItem ?: return@setOnClickListener)
        }

        return holder
    }
}