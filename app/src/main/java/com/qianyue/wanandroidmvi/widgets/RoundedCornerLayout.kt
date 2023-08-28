package com.qianyue.wanandroidmvi.widgets

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import com.qianyue.wanandroidmvi.R

/**
 * 支持圆角的容器
 *
 * @author QianYue
 * @since 2023/8/28
 */
class RoundedCornerLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attributeSet, defStyle) {

    init {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.RoundedCornerLayout)

        do {
            val outlineRadius = typedArray.getDimensionPixelOffset(
                R.styleable.RoundedCornerLayout_roundOutLineRadius,
                -1
            )
            if (outlineRadius > 0) {
                clipToOutline = true
                outlineProvider = object : ViewOutlineProvider() {
                    override fun getOutline(view: View?, outline: Outline?) {
                        view?.let {
                            outline?.setRoundRect(
                                0,
                                0,
                                it.width,
                                it.height,
                                outlineRadius.toFloat()
                            )
                        }

                    }
                }
                break
            }
        } while (false)

        typedArray.recycle()
    }

}