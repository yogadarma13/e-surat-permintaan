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

class ItemSuratPermintaanViewHolder(private val binding: ItemSuratPermintaanItemRowBinding) :
    BaseViewHolder(binding.root) {

    companion object {
        const val BTN_HAPUS = "btnHapus"
        const val BTN_EDIT = "btnEdit"
    }

    init {
        this.setIsRecyclable(false)
    }

    // TransitionManager.beginDelayedTransition() :
    // Android cardview expandable transition : https://www.youtube.com/watch?v=19E85PngufY&vl=en

    // For View Visibility.GONE transition, not working using beginDelayedTransition():
    // Jadi implement custom animation aja, referensi dari sini :
    // https://coderwall.com/p/35xi3w/layout-change-animations-sliding-height

    // Atau kalau males nulis kodingan taruh di xml aja animateLayoutChanges di binding atau di view itu sendiri
    // or just set it at xml, with animateLayoutChanges=true :
    // https://stackoverflow.com/questions/22454839/android-adding-simple-animations-while-setvisibilityview-gone

    override fun bind(
        item: Any?,
        position: Int,
        listener: (Any?, String?) -> Unit
    ) {

        val data = item as ItemsDetailSP

        binding.root.setOnClickListener {
            listener.invoke(data, ROOTVIEW)
        }

        binding.tvKode.text = data.kodePekerjaan
        binding.tvJenisBarang.text = data.idBarang
        binding.tvVolume.text = data.qty
        binding.tvSatuan.text = data.idSatuan

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