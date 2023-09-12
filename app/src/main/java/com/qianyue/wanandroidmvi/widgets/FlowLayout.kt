package com.qianyue.wanandroidmvi.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.forEachIndexed
import kotlin.math.max

/**
 * 流式布局，暂未对padding处理
 *
 * @author QianYue
 * @since 2023/9/10
 */
class FlowLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
): ViewGroup(context, attributeSet, defStyle) {


    var itemSpace: Float = 8f.dp2px()

    var lineSpace: Float = 4f.dp2px()

    private var lineCount: Int = 0

    private var currentLineNum = 0

    private var currentLineWidth = 0

    private var currentHeight = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        currentHeight = 0
        currentLineNum = 0
        currentLineWidth = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val presupposeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val presupposeHeight = MeasureSpec.getSize(heightMeasureSpec)

        var resultWidthMeasureSpec = 0
        var resultHeightMeasureSpec = 0

        val childWidthSpec = MeasureSpec.makeMeasureSpec(presupposeWidth, MeasureSpec.AT_MOST)
        val childHeightSpec = MeasureSpec.makeMeasureSpec(presupposeHeight, MeasureSpec.AT_MOST)

        var measured = false

        if (widthMode == MeasureSpec.EXACTLY) {
            resultWidthMeasureSpec = widthMeasureSpec
        } else {
            measured = true
            foreachChild {
                it.measure(childWidthSpec, childHeightSpec)
                currentLineWidth += (it.measuredWidth + itemSpace.toInt())
            }
            currentLineWidth - itemSpace.toInt()
            resultWidthMeasureSpec = MeasureSpec.makeMeasureSpec(currentLineWidth, MeasureSpec.EXACTLY)
            lineCount = 1
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            resultHeightMeasureSpec = heightMeasureSpec
        } else {
            if (MeasureSpec.getMode(resultWidthMeasureSpec) != MeasureSpec.EXACTLY) {
                var lineHeight = 0
                foreachChild {
                    if (!measured) it.measure(childWidthSpec, childHeightSpec)
                    lineHeight = max(it.measuredHeight, lineHeight)
                }
                resultHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lineHeight, MeasureSpec.EXACTLY)
            } else {
                val expectedWidth = MeasureSpec.getSize(resultWidthMeasureSpec)
                var lineMaxHeight = 0
                var lastNoSpaceNum = -100
                forEachIndexed { index, view ->
                    if (!measured) view.measure(childWidthSpec, childHeightSpec)
                    val tempWidth = currentLineWidth + view.measuredWidth + itemSpace.toInt()
                    if (tempWidth > expectedWidth) {
                        if (tempWidth - itemSpace.toInt() <= expectedWidth && lastNoSpaceNum != (index - 1)) {
                            lineMaxHeight = max(view.measuredHeight, lineMaxHeight)
                            currentLineWidth += view.measuredWidth
                            lastNoSpaceNum = index
                        } else {
                            lastNoSpaceNum = -100
                            currentHeight += (lineMaxHeight + lineSpace.toInt())
                            lineMaxHeight = view.measuredHeight
                            // 换到下一行
                            currentLineNum++
                            currentLineWidth = (view.measuredWidth + itemSpace.toInt())
                        }
                    } else {
                        lastNoSpaceNum = -100
                        lineMaxHeight = max(view.measuredHeight, lineMaxHeight)
                        currentLineWidth += (view.measuredWidth + itemSpace.toInt())
                    }
                }
                lineCount = currentLineNum + 1
                val resultLineHeight = currentHeight + lineMaxHeight
                resultHeightMeasureSpec =
                    MeasureSpec.makeMeasureSpec(currentHeight + lineMaxHeight, MeasureSpec.EXACTLY)
            }
        }

        if (!measured) {
            foreachChild {
                it.measure(childWidthSpec, childHeightSpec)
            }
        }

        super.onMeasure(resultWidthMeasureSpec, resultHeightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        currentHeight = 0
        currentLineNum = 0
        currentLineWidth = 0
        var lineMaxHeight = 0
        var lastNoSpaceNum = -100
        forEachIndexed { index, view ->

            var childLeft = 0

            val tempWidth = currentLineWidth + view.measuredWidth + itemSpace.toInt()
            if (tempWidth > measuredWidth) {
                if (tempWidth - itemSpace.toInt() <= measuredWidth && lastNoSpaceNum != (index - 1)) {
                    lineMaxHeight = max(view.measuredHeight, lineMaxHeight)
                    childLeft = currentLineWidth
                    currentLineWidth += view.measuredWidth
                    lastNoSpaceNum = index
                } else {
                    lastNoSpaceNum = -100
                    currentHeight += (lineMaxHeight + lineSpace.toInt())
                    lineMaxHeight = view.measuredHeight
                    // 换到下一行
                    currentLineNum++
                    currentLineWidth = (view.measuredWidth + itemSpace.toInt())
                    childLeft = 0
                }
            } else {
                lastNoSpaceNum = -100
                lineMaxHeight = max(view.measuredHeight, lineMaxHeight)
                childLeft = currentLineWidth
                currentLineWidth += (view.measuredWidth + itemSpace.toInt())
            }



            val childTop = currentHeight
            view.layout(childLeft, childTop, view.measuredWidth + childLeft, view.measuredHeight + childTop)

        }
    }
}