package com.qianyue.wanandroidmvi.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.view.ViewCompat
import com.google.android.material.card.MaterialCardView
import com.qianyue.wanandroidmvi.R
import com.qianyue.wanandroidmvi.utils.WanLog
import kotlin.math.abs

/**
 * 侧滑容器， cardview 风格的
 *
 * @author QianYue
 * @since 2023/9/10
 */
class SlideMenuCardView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : MaterialCardView(context, attributeSet, defStyle) {

    var enableSlideMenu = false

    private val touchSlop: Int

    private var lastX: Float = 0f

    private var downX: Float = 0f

    private var isDragging: Boolean = false

    private var slideMenuLayout: View? = null

    private var maxSlideDistance: Float = 0f

    private val scroller: Scroller = Scroller(context)

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        foreachChild {
            if (it.tag == resources.getString(R.string.slide_menu_layout_tag) && it.visibility == View.VISIBLE) {
                slideMenuLayout = it
                maxSlideDistance = it.measuredWidth.toFloat()
                it.translationX = it.measuredWidth.toFloat()
            }
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child?.tag == resources.getString(R.string.slide_menu_layout_tag) && child.visibility == View.VISIBLE) {
            (params as LayoutParams).gravity = Gravity.END
        }
        super.addView(child, index, params)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (!scroller.isFinished) {
            return true
        }
        if (!enableSlideMenu) return false
        event ?: return false
        slideMenuLayout ?: return false

        var result = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isDragging = false
                downX = event.x
                lastX = 0f
                scroller.abortAnimation()
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - downX
                if (abs(dx) > touchSlop) {
                    result = true
                    isDragging = true
                }
            }
        }
        return result
    }

    fun hideSlideMenu() {
        slideMenuLayout?.translationX = maxSlideDistance
    }

    override fun computeScroll() {
        super.computeScroll()
        slideMenuLayout ?: return
        if (scroller.computeScrollOffset()) {
            slideMenuLayout!!.translationX = scroller.currX.toFloat()
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!enableSlideMenu) return false
        event ?: return false
        slideMenuLayout ?: return false
        if (maxSlideDistance <= 0) return false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isDragging = false
                downX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDragging && abs(event.x - downX) > touchSlop) {
                    isDragging = true
                }
                val dx = event.x - lastX
                if (isDragging && lastX != 0f) {
                    requestDisallowInterceptTouchEvent(true)
                    val tempX = slideMenuLayout!!.translationX + dx
                    slideMenuLayout!!.translationX = if (tempX > maxSlideDistance) maxSlideDistance else if (tempX < 0) 0f else tempX
                }
                lastX = event.x
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isDragging || !scroller.isFinished) {
                    val targetTransX = if (slideMenuLayout!!.translationX > maxSlideDistance / 2) {
                        maxSlideDistance
                    } else 0f
                    scroller.startScroll(slideMenuLayout!!.translationX.toInt(), 0, (targetTransX - slideMenuLayout!!.translationX).toInt(), 0)
                    ViewCompat.postInvalidateOnAnimation(this)
                } else if (event.actionMasked == MotionEvent.ACTION_UP && downX < measuredWidth - maxSlideDistance) {
                    slideMenuLayout?.translationX = maxSlideDistance
                }
            }
        }
        return true
    }

}