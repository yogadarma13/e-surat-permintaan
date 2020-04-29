package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.animations.SlideAnimation
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_surat_permintaan_item_row.view.*

class ItemSuratPermintaanViewHolder(private val rootView: View) :
    BaseViewHolder(rootView) {

    companion object {
        const val BTN_HAPUS = "btnHapus"
        const val BTN_EDIT = "btnEdit"
    }

    // TransitionManager.beginDelayedTransition() :
    // Android cardview expandable transition : https://www.youtube.com/watch?v=19E85PngufY&vl=en

    // For View Visibility.GONE transition, not working using beginDelayedTransition():
    // Jadi implement custom animation aja, referensi dari sini :
    // https://coderwall.com/p/35xi3w/layout-change-animations-sliding-height

    // Atau kalau males nulis kodingan taruh di xml aja animateLayoutChanges di rootView atau di view itu sendiri
    // or just set it at xml, with animateLayoutChanges=true :
    // https://stackoverflow.com/questions/22454839/android-adding-simple-animations-while-setvisibilityview-gone

    override fun bind(
        item: Any?,
        position: Int,
        listener: (item: Any?, actionString: String?) -> Unit
    ) {
        val data = item as ItemsDetailSP

        rootView.setOnClickListener {
            listener(data, null)
        }

        rootView.tvKode.text = data.kodePekerjaan
        rootView.tvJenisBarang.text = data.idBarang
        rootView.tvVolume.text = data.qty
        rootView.tvSatuan.text = data.idSatuan

        if (data.tombolEditItem == 1){
            rootView.btnEdit.visibility = View.VISIBLE
            rootView.btnEdit.setOnClickListener {
                listener(data, BTN_EDIT)
            }
        }

        if (data.tombolHapusItem == 1){
            rootView.btnHapus.visibility = View.VISIBLE
            rootView.btnHapus.setOnClickListener {
                listener(data, BTN_HAPUS)
            }
        }

        rootView.constraintLayout.setOnClickListener {

            if (rootView.expandableLayout.visibility == View.GONE) {
                // Kenapa pakai ini, karna kalau mau pake custom SlideAnimation,
                // kita gak tau dia viewnya mau dikasi height berapa
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(
                        rootView.cardView,
                        TransitionSet()
                            .addTransition(ChangeBounds())
                    )
                }
                rootView.expandableLayout.visibility = View.VISIBLE
                rootView.expandableIcon.setImageDrawable(rootView.context.resources.getDrawable(R.drawable.ic_up))
            } else {
                val animation: Animation =
                    SlideAnimation(rootView.expandableLayout, rootView.expandableLayout.height, 0)

                animation.interpolator = DecelerateInterpolator()
                animation.duration = 150
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        animation?.setAnimationListener(null)
                        rootView.expandableLayout.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })
                rootView.expandableLayout.animation = animation
                rootView.expandableLayout.startAnimation(animation)
                rootView.expandableIcon.setImageDrawable(rootView.context.resources.getDrawable(R.drawable.ic_down))
            }

        }
    }

}