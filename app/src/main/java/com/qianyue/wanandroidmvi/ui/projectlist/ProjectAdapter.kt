package com.qianyue.wanandroidmvi.ui.projectlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.qianyue.wanandroidmvi.databinding.ItemProjectBinding
import com.qianyue.wanandroidmvi.model.bean.ProjectItem
import com.qianyue.wanandroidmvi.utils.WanImageLoader

/**
 * @author QianYue
 * @since 2023/8/24
 */
class ProjectAdapter : BaseQuickAdapter<ProjectItem, ProjectAdapter.ViewHolder>() {

    var onItemClick: ((Int, ProjectItem)-> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: ProjectItem?) {
        holder.cacheItem = item
        holder.cacheIndex = position

        holder.binding.apply {
            WanImageLoader.loadImage(root.context, item?.envelopePic, ivProjectCover)
        }
    }


    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemProjectBinding.inflate(LayoutInflater.from(context), parent, false)
        val viewHolder = ViewHolder(binding)
        binding.root.setOnClickListener {
            onItemClick?.invoke(viewHolder.cacheIndex, viewHolder.cacheItem!!)
        }
        return viewHolder
    }

    inner class ViewHolder(val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root) {
        var cacheItem: ProjectItem? = null
        var cacheIndex: Int = -1
    }
}