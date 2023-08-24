package com.qianyue.wanandroidmvi.ui.home

import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.ext.loadImage
import com.qianyue.wanandroidmvi.model.bean.BannerItem
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
 * @author QianYue
 * @since 2023/8/22
 */
class BannerAdapter: BaseBannerAdapter<BannerItem>() {

    override fun bindData(
        holder: BaseViewHolder<BannerItem>?,
        data: BannerItem?,
        position: Int,
        pageSize: Int
    ) {
        val context = (holder?: return).itemView.context
        context.loadImage(data?.imagePath, holder.findViewById(R.id.iv_banner))
    }

    override fun getLayoutId(viewType: Int) = R.layout.item_home_banner
}