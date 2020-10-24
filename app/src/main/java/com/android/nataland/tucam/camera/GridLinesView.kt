package com.android.nataland.tucam.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.android.nataland.tucam.R

class GridLinesView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.grid_color)
        strokeWidth = 1f
    }

    override fun onDraw(canvas: Canvas) {
        if (width != 0 && height != 0) {
            val viewWidth = width.toFloat()
            val viewHeight = height.toFloat()
            val left = viewWidth / 3
            val right = viewWidth * 2 / 3
            val top = viewHeight / 3
            val bottom = viewHeight * 2 / 3

            canvas.drawLine(left, 0f, left, viewHeight, paint)
            canvas.drawLine(right, 0f, right, viewHeight, paint)
            canvas.drawLine(0f, top, viewWidth, top, paint)
            canvas.drawLine(0f, bottom, viewWidth, bottom, paint)
        }
    }
}
