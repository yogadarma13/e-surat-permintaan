package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import com.e_suratpermintaan.core.domain.entities.responses.FileLampiranDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.utils.animations.SlideAnimation
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_file_lampiran_row.view.*

class EditFileSuratPermintaanViewHolder(private val rootView: View) :
    BaseViewHolder(rootView) {

    companion object {
        const val BTN_FILE = "btnFile"
        const val BTN_HAPUS = "btnHapusFile"
        const val BTN_EDIT = "btnEditFile"
    }

    override fun bind(item: Any?, position: Int, listener: (Any?, String?) -> Unit) {
        val data = item as FileLampiranDetailSP

        rootView.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        rootView.tvKeteranganFile.text = data.keterangan

        if (data.tombolEditFile == 1){
            rootView.btnEditFile.visibility = View.VISIBLE
            rootView.btnEditFile.setOnClickListener {
                listener.invoke(data, BTN_EDIT)
            }
        }

        if (data.tombolHapusFile == 1){
            rootView.btnHapusFile.visibility = View.VISIBLE
            rootView.btnHapusFile.setOnClickListener {
                listener.invoke(data, BTN_HAPUS)
            }
        }

        rootView.btnFile.setOnClickListener {
            listener.invoke(data, BTN_FILE)
        }

        rootView.constraintLayoutFile.setOnClickListener {

            if (rootView.expandableLayoutFile.visibility == View.GONE) {
                // Kenapa pakai ini, karna kalau mau pake custom SlideAnimation,
                // kita gak tau dia viewnya mau dikasi height berapa
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(
                        rootView.cardView,
                        TransitionSet()
                            .addTransition(ChangeBounds())
                    )
                }
                rootView.expandableLayoutFile.visibility = View.VISIBLE
                rootView.expandableIcon.setImageDrawable(rootView.context.resources.getDrawable(R.drawable.ic_up))
            } else {
                val animation: Animation =
                    SlideAnimation(rootView.expandableLayoutFile, rootView.expandableLayoutFile.height, 0)

                animation.interpolator = DecelerateInterpolator()
                animation.duration = 150
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        animation?.setAnimationListener(null)
                        rootView.expandableLayoutFile.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })
                rootView.expandableLayoutFile.animation = animation
                rootView.expandableLayoutFile.startAnimation(animation)
                rootView.expandableIcon.setImageDrawable(rootView.context.resources.getDrawable(R.drawable.ic_down))
            }

        }
    }
}