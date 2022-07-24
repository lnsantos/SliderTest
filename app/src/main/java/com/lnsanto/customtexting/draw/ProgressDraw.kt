package com.lnsanto.customtexting.draw

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Build
import com.lnsanto.customtexting.SliderCustomView

internal class ProgressDraw constructor(
    private val resource: Resources
) {

    fun drawProgress(
        progressItem: SliderCustomView.ProgressItem
    ) {

    }

    fun drawThumb(
        size: Int,
        backgroundColor: Int,
        borderColor: Int
    ) = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        setSize(size, size)
        cornerRadii = floatArrayOf(
            0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = resource.getColorStateList(
                backgroundColor,
                null
            )
        } else {
            setColor(
                resource.getColor(backgroundColor)
            )
        }

        setStroke(
            10,
            borderColor
        )
    }

}