package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.framework.utils.animations.SlideAnimation
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder
import kotlinx.android.synthetic.main.item_surat_permintaan_item_row.view.*

class EditItemSuratPermintaanViewHolder(private val rootView: View) :
    BaseViewHolder(rootView) {

    companion object {
        const val BTN_HAPUS = "btnHapus"
        const val BTN_EDIT = "btnEdit"
        const val BTN_PENUGASAN = "btnPenugasan"
        const val BTN_PROCESS = "btnProcess"
        const val BTN_UNPROCESS = "btnUnProcess"
    }

    init {
        this.setIsRecyclable(false)
    }

    override fun bind(item: Any?, position: Int, listener: (Any?, String?) -> Unit) {
        val data = item as ItemsDetailSP

        rootView.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        rootView.tvKode.text = data.kodePekerjaan
        rootView.tvJenisBarang.text = data.idBarang
        rootView.tvVolume.text = data.qty
        rootView.tvSatuan.text = data.idSatuan

        if (data.tombolEditItem == 1) {
            rootView.btnEdit.visibility = View.VISIBLE
            rootView.btnEdit.setOnClickListener {
                listener.invoke(data, BTN_EDIT)
            }
        } else {
            rootView.btnEdit.visibility = View.GONE
        }

        if (data.tombolHapusItem == 1) {
            rootView.btnHapus.visibility = View.VISIBLE
            rootView.btnHapus.setOnClickListener {
                listener.invoke(data, BTN_HAPUS)
            }
        } else {
            rootView.btnHapus.visibility = View.GONE
        }

        if (data.tombolPenugasan == 1) {
            rootView.btnPenugasan.visibility = View.VISIBLE
            rootView.btnPenugasan.setOnClickListener {
                listener.invoke(data, BTN_PENUGASAN)
            }
        } else {
            rootView.btnPenugasan.visibility = View.GONE
        }

        if (data.tombolProcess == 1) {
            rootView.btnProcess.visibility = View.VISIBLE
            rootView.btnProcess.setOnClickListener {
                listener.invoke(data, BTN_PROCESS)
            }
        } else {
            rootView.btnProcess.visibility = View.GONE
        }

        if (data.tombolUnProcess == 1) {
            rootView.btnUnProcess.visibility = View.VISIBLE
            rootView.btnUnProcess.setOnClickListener {
                listener.invoke(data, BTN_UNPROCESS)
            }
        } else {
            rootView.btnUnProcess.visibility = View.GONE
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
                rootView.expandableIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        rootView.context,
                        R.drawable.ic_arrow_up
                    )
                )
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
                rootView.expandableIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        rootView.context,
                        R.drawable.ic_arrow_down
                    )
                )
            }

        }
    }
}