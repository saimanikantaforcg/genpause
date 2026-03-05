package com.genpause.app.overlay

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Custom View that draws a breathing ring with a cyan->purple gradient.
 * Animates scale to create an inhale/exhale effect.
 */
class BreathingRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 16f
        maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)
    }

    private var breathScale = 0.85f
    private var animator: ValueAnimator? = null

    // Colors
    private val cyanColor = Color.parseColor("#00E5FF")
    private val purpleColor = Color.parseColor("#B100FF")

    init {
        // Required for blur effect
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startBreathingAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    fun startBreathingAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0.85f, 1.0f).apply {
            duration = 4000L  // 4 seconds per cycle
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { anim ->
                breathScale = anim.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    fun stopAnimation() {
        animator?.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = (minOf(width, height) / 2f) - 24f
        val radius = baseRadius * breathScale

        // Create gradient shader
        val gradient = SweepGradient(
            cx, cy,
            intArrayOf(cyanColor, purpleColor, cyanColor),
            floatArrayOf(0f, 0.5f, 1f)
        )

        // Draw glow
        glowPaint.shader = gradient
        glowPaint.alpha = (80 * breathScale).toInt()
        canvas.drawCircle(cx, cy, radius, glowPaint)

        // Draw main ring
        ringPaint.shader = gradient
        ringPaint.alpha = 255
        canvas.drawCircle(cx, cy, radius, ringPaint)

        // Draw inner subtle ring
        ringPaint.shader = null
        ringPaint.color = cyanColor
        ringPaint.alpha = (40 * breathScale).toInt()
        ringPaint.strokeWidth = 2f
        canvas.drawCircle(cx, cy, radius * 0.7f, ringPaint)

        // Reset stroke width
        ringPaint.strokeWidth = 8f
    }
}
