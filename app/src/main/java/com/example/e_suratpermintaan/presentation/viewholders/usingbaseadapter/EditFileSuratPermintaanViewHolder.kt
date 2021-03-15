package com.example.e_suratpermintaan.presentation.viewholders.usingbaseadapter

import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import com.e_suratpermintaan.core.domain.entities.responses.FileLampiranDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ItemFileLampiranRowBinding
import com.example.e_suratpermintaan.framework.utils.animations.SlideAnimation
import com.example.e_suratpermintaan.presentation.base.BaseViewHolder

class EditFileSuratPermintaanViewHolder(private val binding: ItemFileLampiranRowBinding) :
    BaseViewHolder(binding.root) {

    companion object {
        const val BTN_FILE = "btnFile"
        const val BTN_HAPUS = "btnHapusFile"
        const val BTN_EDIT = "btnEditFile"
    }

    override fun bind(item: Any?, position: Int, listener: (Any?, String?) -> Unit) {
        val data = item as FileLampiranDetailSP

        binding.root.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        binding.tvKeteranganFile.text = data.keterangan

        if (data.tombolEditFile == 1) {
            binding.btnEditFile.visibility = View.VISIBLE
            binding.btnEditFile.setOnClickListener {
                listener.invoke(data, BTN_EDIT)
            }
        } else {
            binding.btnEditFile.visibility = View.GONE
        }

        if (data.tombolHapusFile == 1) {
            binding.btnHapusFile.visibility = View.VISIBLE
            binding.btnHapusFile.setOnClickListener {
                listener.invoke(data, BTN_HAPUS)
            }
        } else {
            binding.btnHapusFile.visibility = View.GONE
        }

        binding.btnFile.setOnClickListener {
            listener.invoke(data, BTN_FILE)
        }

        binding.constraintLayoutFile.setOnClickListener {

            if (binding.expandableLayoutFile.visibility == View.GONE) {
                // Kenapa pakai ini, karna kalau mau pake custom SlideAnimation,
                // kita gak tau dia viewnya mau dikasi height berapa
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(
                        binding.cardView,
                        TransitionSet()
                            .addTransition(ChangeBounds())
                    )
                }
                binding.expandableLayoutFile.visibility = View.VISIBLE
                binding.expandableIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.ic_arrow_up
                    )
                )
            } else {
                val animation: Animation =
                    SlideAnimation(
                        binding.expandableLayoutFile,
                        binding.expandableLayoutFile.height,
                        0
                    )

                animation.interpolator = DecelerateInterpolator()
                animation.duration = 150
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        animation?.setAnimationListener(null)
                        binding.expandableLayoutFile.visibility = View.GONE
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })
                binding.expandableLayoutFile.animation = animation
                binding.expandableLayoutFile.startAnimation(animation)
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