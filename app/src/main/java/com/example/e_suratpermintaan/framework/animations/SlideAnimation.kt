package com.example.e_suratpermintaan.framework.animations

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class SlideAnimation(view: View, fromHeight: Int, toHeight: Int) : Animation() {

    // https://coderwall.com/p/35xi3w/layout-change-animations-sliding-height

    var mFromHeight: Int = fromHeight
    var mToHeight: Int = toHeight
    var mView: View = view

    override fun applyTransformation(
        interpolatedTime: Float,
        transformation: Transformation?
    ) {
        val newHeight: Int
        if (mView.height !== mToHeight) {
            newHeight = (mFromHeight + (mToHeight - mFromHeight) * interpolatedTime).toInt()
            mView.layoutParams.height = newHeight
            mView.requestLayout()
        }
    }

    override fun initialize(
        width: Int,
        height: Int,
        parentWidth: Int,
        parentHeight: Int
    ) {
        super.initialize(width, height, parentWidth, parentHeight)
    }

    override fun willChangeBounds(): Boolean {
        return true
    }

}