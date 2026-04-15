package com.gooseco.technicalrulesquiz.ui.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val matrix = Matrix()
    private var currentScale = 1f
    private val minScale = 1f
    private val maxScale = 5f

    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var isDragging = false

    private val scaleDetector = ScaleGestureDetector(context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val scaleFactor = detector.scaleFactor
                val newScale = (currentScale * scaleFactor).coerceIn(minScale, maxScale)
                val actualScaleFactor = newScale / currentScale
                currentScale = newScale
                matrix.postScale(actualScaleFactor, actualScaleFactor, detector.focusX, detector.focusY)
                clamp()
                imageMatrix = matrix
                return true
            }
        }
    )

    init {
        scaleType = ScaleType.MATRIX
        imageMatrix = matrix
    }

    override fun setImageBitmap(bm: Bitmap?) {
        matrix.reset()
        currentScale = 1f
        super.setImageBitmap(bm)
        imageMatrix = matrix
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)

        parent?.requestDisallowInterceptTouchEvent(currentScale > 1f || scaleDetector.isInProgress)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
                isDragging = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging && !scaleDetector.isInProgress && currentScale > 1f) {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY
                    matrix.postTranslate(dx, dy)
                    clamp()
                    imageMatrix = matrix
                }
                lastTouchX = event.x
                lastTouchY = event.y
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
            }
        }

        return true
    }

    private fun clamp() {
        val values = FloatArray(9)
        matrix.getValues(values)

        val transX = values[Matrix.MTRANS_X]
        val transY = values[Matrix.MTRANS_Y]
        val scaleX = values[Matrix.MSCALE_X]
        val scaleY = values[Matrix.MSCALE_Y]

        val viewWidth = width.toFloat()
        val viewHeight = height.toFloat()

        val bitmapWidth = (drawable?.intrinsicWidth ?: 0) * scaleX
        val bitmapHeight = (drawable?.intrinsicHeight ?: 0) * scaleY

        var fixTransX = 0f
        var fixTransY = 0f

        if (bitmapWidth <= viewWidth) {
            // Center horizontally if bitmap is smaller than view
            fixTransX = (viewWidth - bitmapWidth) / 2f - transX
        } else {
            // Prevent panning past edges horizontally
            if (transX > 0f) {
                fixTransX = -transX
            } else if (transX + bitmapWidth < viewWidth) {
                fixTransX = viewWidth - (transX + bitmapWidth)
            }
        }

        if (bitmapHeight <= viewHeight) {
            // Center vertically if bitmap is smaller than view
            fixTransY = (viewHeight - bitmapHeight) / 2f - transY
        } else {
            // Prevent panning past edges vertically
            if (transY > 0f) {
                fixTransY = -transY
            } else if (transY + bitmapHeight < viewHeight) {
                fixTransY = viewHeight - (transY + bitmapHeight)
            }
        }

        if (fixTransX != 0f || fixTransY != 0f) {
            matrix.postTranslate(fixTransX, fixTransY)
        }
    }
}
