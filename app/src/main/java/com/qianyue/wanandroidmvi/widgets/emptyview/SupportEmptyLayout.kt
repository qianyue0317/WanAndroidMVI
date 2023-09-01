package com.qianyue.wanandroidmvi.widgets.emptyview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.widgets.foreachChild
import com.qianyue.wanandroidmvi.widgets.setSafeClickListener

/**
 * @author QianYue
 * @since 2023/8/31
 */
class SupportEmptyLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    private val ivEmptyData: ImageView

    private val ivEmptyRefresh: ImageView

    private val tvEmptyTip: TextView

    private val emptyView: View

    private val progressBar: ProgressBar

    init {
        val view = inflate(context, R.layout.empty_view, this)
        emptyView = view.findViewById(R.id.ll_empty_container)
        emptyView.updateLayoutParams<LayoutParams> { gravity = Gravity.CENTER }

        ivEmptyData = findViewById(R.id.iv_empty_data)
        ivEmptyRefresh = findViewById(R.id.iv_empty_refresh)
        tvEmptyTip = findViewById(R.id.tv_empty_tip)
        progressBar = findViewById(R.id.progress_bar)
    }

    fun showNoData(tip: String = "无数据") {
        emptyView.visibility = View.VISIBLE
        emptyView.bringToFront()
        ivEmptyRefresh.visibility = View.GONE
        ivEmptyData.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        tvEmptyTip.text = tip

        foreachChild {
            if (it != emptyView) {
                it.visibility = View.GONE
            }
        }

    }

    fun showRefresh(tip: String = "无数据，点击重试", onRefreshClick: (TextView) -> Unit = {}) {
        emptyView.visibility = View.VISIBLE
        emptyView.bringToFront()
        ivEmptyRefresh.visibility = View.VISIBLE
        ivEmptyData.visibility = View.GONE
        progressBar.visibility = View.GONE


        tvEmptyTip.text = tip

        foreachChild {
            if (it != emptyView) {
                it.visibility = View.GONE
            }
        }

        setSafeClickListener {
            ivEmptyRefresh.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            tvEmptyTip.text = "正在加载..."
            onRefreshClick(tvEmptyTip)
        }
    }

    fun showProgressBar(tip: String = "正在加载") {
        emptyView.visibility = View.VISIBLE
        emptyView.bringToFront()
        ivEmptyRefresh.visibility = View.GONE
        ivEmptyData.visibility = View.GONE
        progressBar.visibility = View.VISIBLE


        tvEmptyTip.text = tip

        foreachChild {
            if (it != emptyView) {
                it.visibility = View.GONE
            }
        }
    }


    fun showContent() {
        if (emptyView.visibility == View.GONE) {
            return
        }

        emptyView.visibility = View.GONE
        foreachChild {
            if (it != emptyView) {
                it.visibility = View.VISIBLE
            }
        }

        isClickable = false
    }
}