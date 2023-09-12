package com.qianyue.wanandroidmvi.widgets

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable

/**
 * 支持圆角的背景，并可以设置描边。
 *
 * @author QianYue
 * @since 2023/8/28
 */
class RoundedDrawable: Drawable() {
    private val _paint = Paint().apply { style = Paint.Style.FILL }
    private val _strokePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
    }

    private val _path = Path()
    private val _copyBounds = Rect()

    var semicircle = false
    var radius = 0f
        set(value) {
            field = value
            radiusLT = field
            radiusLB = field
            radiusRT = field
            radiusRB = field
        }
    var radiusLT = 0f
    var radiusRT = 0f
    var radiusLB = 0f
    var radiusRB = 0f
    var color = Color.TRANSPARENT
        set(value) {
            field = value
            _paint.color = field
        }
    var strokeWidth = 0f
        set(value) {
            field = value
            _strokePaint.strokeWidth = field
        }
    var strokeColor = Color.WHITE
        set(value) {
            field = value
            _strokePaint.color = field
        }

    override fun draw(canvas: Canvas) {
        val insetValue = (strokeWidth / 2).toInt()
        _copyBounds.set(bounds)
        _copyBounds.inset(insetValue, insetValue)
        resetPath(_path, _copyBounds)
        if (color != Color.TRANSPARENT) {
            canvas.drawPath(_path, _paint)
        }
        if (strokeWidth > 0) {
            canvas.drawPath(_path, _strokePaint)
        }
    }

    private fun resetPath(path: Path, rect: Rect) {
        _path.reset()
        val semicircleRadius = rect.height() / 2
        path.addRoundRect(
            rect.left.toFloat(),
            rect.top.toFloat(),
            rect.right.toFloat(),
            rect.bottom.toFloat(),
            floatArrayOf(
                if (semicircle) semicircleRadius.toFloat() else radiusLT,
                if (semicircle) semicircleRadius.toFloat() else radiusLT,
                if (semicircle) semicircleRadius.toFloat() else radiusRT,
                if (semicircle) semicircleRadius.toFloat() else radiusRT,
                if (semicircle) semicircleRadius.toFloat() else radiusRB,
                if (semicircle) semicircleRadius.toFloat() else radiusRB,
                if (semicircle) semicircleRadius.toFloat() else radiusLB,
                if (semicircle) semicircleRadius.toFloat() else radiusLB
            ),
            Path.Direction.CW
        )
    }

    override fun setAlpha(alpha: Int) {
        _paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        _paint.colorFilter = colorFilter
    }

    override fun getOpacity() = PixelFormat.OPAQUE

}