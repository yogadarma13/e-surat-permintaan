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
import com.example.e_suratpermintaan.databinding.ItemSuratPermintaanItemRowBinding
import com.example.e_suratpermintaan.framework.utils.animations.SlideAnimation
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class EditItemSuratPermintaanViewHolder(private val binding: ItemSuratPermintaanItemRowBinding) :
    BaseViewHolder(binding.root) {

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

        binding.root.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        binding.tvKode.text = data.kodePekerjaan
        binding.tvJenisBarang.text = data.idBarang
        binding.tvVolume.text = data.qty
        binding.tvSatuan.text = data.idSatuan

        if (data.tombolEditItem == 1) {
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnEdit.setOnClickListener {
                listener.invoke(data, BTN_EDIT)
            }
        } else {
            binding.btnEdit.visibility = View.GONE
        }

        if (data.tombolHapusItem == 1) {
            binding.btnHapus.visibility = View.VISIBLE
            binding.btnHapus.setOnClickListener {
                listener.invoke(data, BTN_HAPUS)
            }
        } else {
            binding.btnHapus.visibility = View.GONE
        }

        if (data.tombolPenugasan == 1) {
            binding.btnPenugasan.visibility = View.VISIBLE
            binding.btnPenugasan.setOnClickListener {
                listener.invoke(data, BTN_PENUGASAN)
            }
        } else {
            binding.btnPenugasan.visibility = View.GONE
        }

        if (data.tombolProcess == 1) {
            binding.btnProcess.visibility = View.VISIBLE
            binding.btnProcess.setOnClickListener {
                listener.invoke(data, BTN_PROCESS)
            }
        } else {
            binding.btnProcess.visibility = View.GONE
        }

        if (data.tombolUnProcess == 1) {
            binding.btnUnProcess.visibility = View.VISIBLE
            binding.btnUnProcess.setOnClickListener {
                listener.invoke(data, BTN_UNPROCESS)
            }
        } else {
            binding.btnUnProcess.visibility = View.GONE
        }

        binding.constraintLayout.setOnClickListener {

            if (binding.expandableLayout.visibility == View.GONE) {
                // Kenapa pakai ini, karna kalau mau pake custom SlideAnimation,
                // kita gak tau dia viewnya mau dikasi height berapa
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(
                        binding.cardView,
                        TransitionSet()
                            .addTransition(ChangeBounds())
                    )
                }
                binding.expandableLayout.visibility = View.VISIBLE
                binding.expandableIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.ic_arrow_up
                    )
                )
            } else {
                val animation: Animation =
                    SlideAnimation(binding.expandableLayout, binding.expandableLayout.height, 0)

                animation.interpolator = DecelerateInterpolator()
                animation.duration = 150
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        animation?.setAnimationListener(null)
                        binding.expandableLayout.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })
                binding.expandableLayout.animation = animation
                binding.expandableLayout.startAnimation(animation)
                binding.expandableIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.ic_arrow_down
                    )
                )
            }

        }
    }
}