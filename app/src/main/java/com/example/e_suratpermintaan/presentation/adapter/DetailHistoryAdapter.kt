package com.example.e_suratpermintaan.presentation.adapter

import android.os.Build
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailHistory
import com.e_suratpermintaan.core.domain.entities.responses.PersyaratanItem
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.ItemDetailHistorySpaBinding
import com.example.e_suratpermintaan.databinding.ItemDetailHistorySpbBinding
import com.example.e_suratpermintaan.databinding.ItemDetailHistorySpsBinding
import com.example.e_suratpermintaan.databinding.ItemSimpleCheckboxBinding
import com.example.e_suratpermintaan.framework.utils.animations.SlideAnimation

class DetailHistoryAdapter : RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder>() {

    val historyList: ArrayList<ItemsDetailHistory> = arrayListOf()
    val viewType: ArrayList<String> = arrayListOf()
    val persyaratanList = mutableMapOf<String, String>()

    companion object {
        const val SPA = 0
        const val SPB = 1
        const val SPS = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SPA -> ViewHolderSPA(ItemDetailHistorySpaBinding.inflate(inflater, parent, false))
            SPB -> ViewHolderSPB(ItemDetailHistorySpbBinding.inflate(inflater, parent, false))
            else -> ViewHolderSPS(ItemDetailHistorySpsBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historyList[position]
        when (getItemViewType(position)) {
            SPA -> {
                val viewHolderSPA = holder as ViewHolderSPA

                viewHolderSPA.bind(data)
            }

            SPB -> {
                val viewHolderSPB = holder as ViewHolderSPB

                viewHolderSPB.bind(data)
            }

            SPS -> {
                val viewHolderSPS = holder as ViewHolderSPS

                viewHolderSPS.bind(data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            viewType[position] == "SPA" -> {
                SPA
            }
            viewType[position] == "SPB" -> {
                SPB
            }
            else -> {
                SPS
            }
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderSPA(private val binding: ItemDetailHistorySpaBinding) :
        ViewHolder(binding.root) {

        fun bind(data: ItemsDetailHistory) {
            binding.tvJenisDetailSPA.text = data.idBarang
            binding.tvJenisBarangSPA.text = data.idBarang
            binding.labelKategori.visibility = View.GONE
            binding.tvKategoriSPA.visibility = View.GONE
            binding.tvKodeDetailSPA.text = data.kodePekerjaan
            binding.tvSatuanDetailSPA.text = data.idSatuan
            binding.tvKapasitasDetailSPA.text = data.kapasitas
            binding.tvWaktuDetailSPA.text = data.waktuPemakaian
            binding.tvMerkDetailSPA.text = data.merk
            binding.tvQtySPA.text = data.qty
            var dataKeterangan: String? = ""

            data.keterangan?.forEach {
                dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
            }
            binding.tvKeteranganDetailSPA.text = dataKeterangan

            binding.expandedParentSPA.setOnClickListener {
                if (binding.expandedChildSPA.visibility == View.GONE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(
                            binding.cardViewSPA,
                            TransitionSet()
                                .addTransition(ChangeBounds())
                        )
                    }
                    binding.expandedChildSPA.visibility = View.VISIBLE
                    binding.imgExpandedParentSPA.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imgExpandedParentSPA.context,
                            R.drawable.ic_arrow_up
                        )
                    )
//                        viewHolderSPA.imageExpandedParentSPA.setImageDrawable(
//                            viewHolderSPA.imageExpandedParentSPA.context.resources.getDrawable(
//                                R.drawable.ic_arrow_up
//                            )
//                        )
                } else {
                    val animation = animation(binding.expandedChildSPA)

                    binding.expandedChildSPA.animation = animation
                    binding.expandedChildSPA.startAnimation(animation)
                    binding.imgExpandedParentSPA.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imgExpandedParentSPA.context,
                            R.drawable.ic_arrow_down
                        )
                    )
                }

            }
        }
    }

    inner class ViewHolderSPB(private val binding: ItemDetailHistorySpbBinding) :
        ViewHolder(binding.root) {
        fun bind(data: ItemsDetailHistory) {
            binding.tvJenisDetailSPB.text = data.idBarang
            binding.tvKodeDetailSPB.text = data.kodePekerjaan
            binding.tvJenisBarangSPB.text = data.idBarang
            binding.labelKategori.visibility = View.GONE
            binding.tvKategoriSPB.visibility = View.GONE
            binding.tvSatuanDetailSPB.text = data.idSatuan
            binding.tvFungsiDetailSPB.text = data.fungsi
            binding.tvTargetDetailSPB.text = data.target
            binding.tvQtySPB.text = data.qty
            var dataKeterangan: String? = ""

            data.keterangan?.forEach {
                dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
            }
            binding.tvKeteranganDetailSPB.text = dataKeterangan

            binding.expandedParentSPB.setOnClickListener {
                if (binding.expandedChildSPB.visibility == View.GONE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(
                            binding.cardViewSPB,
                            TransitionSet()
                                .addTransition(ChangeBounds())
                        )
                    }
                    binding.expandedChildSPB.visibility = View.VISIBLE
                    binding.imgExpandedParentSPB.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imgExpandedParentSPB.context,
                            R.drawable.ic_arrow_up
                        )
                    )
                } else {
                    val animation = animation(binding.expandedChildSPB)

                    binding.expandedChildSPB.animation = animation
                    binding.expandedChildSPB.startAnimation(animation)
                    binding.imgExpandedParentSPB.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imgExpandedParentSPB.context,
                            R.drawable.ic_arrow_down
                        )
                    )
                }
            }
        }
    }

    inner class ViewHolderSPS(private val binding: ItemDetailHistorySpsBinding) :
        ViewHolder(binding.root) {
        fun bind(data: ItemsDetailHistory) {
            binding.tvJenisDetailSPS.text = data.idBarang
            binding.tvKodeDetailSPS.text = data.kodePekerjaan
            binding.tvJenisBarangSPS.text = data.idBarang
            binding.labelKategori.visibility = View.GONE
            binding.tvKategoriSPS.visibility = View.GONE
            binding.tvSatuanDetailSPS.text = data.idSatuan
            binding.tvWaktuDetailSPS.text = data.waktuPelaksanaan
            binding.tvQtySPS.text = data.qty


            val parent = binding.tvPersyaratanDetailSPS
            parent.removeAllViews()
            persyaratanList.forEach { itemMaster ->

                val view = ItemSimpleCheckboxBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                var isChecked = false
                data.persyaratan?.forEach { persyaratanItem ->
                    val dataSyarat = persyaratanItem as PersyaratanItem
                    if (itemMaster.key == dataSyarat.persyaratan) {
                        isChecked = true
                    }
                }

                if (isChecked) {
                    view.checkbox.isChecked = true
                }

                view.checkbox.text = itemMaster.value
                view.checkbox.setOnCheckedChangeListener { _, checked ->
                    view.checkbox.isChecked = !checked
                }

                parent.addView(view.root)
            }


            var dataKeterangan: String? = ""

            data.keterangan?.forEach {
                dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
            }
            binding.tvKeteranganDetailSPS.text = dataKeterangan

            binding.expandedParentSPS.setOnClickListener {
                if (binding.expandedChildSPS.visibility == View.GONE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(
                            binding.cardViewSPS,
                            TransitionSet()
                                .addTransition(ChangeBounds())
                        )
                    }
                    binding.expandedChildSPS.visibility = View.VISIBLE
                    binding.imgExpandedParentSPS.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imgExpandedParentSPS.context,
                            R.drawable.ic_arrow_up
                        )
                    )
                } else {
                    val animation = animation(binding.expandedChildSPS)

                    binding.expandedChildSPS.animation = animation
                    binding.expandedChildSPS.startAnimation(animation)
                    binding.imgExpandedParentSPS.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.imgExpandedParentSPS.context,
                            R.drawable.ic_arrow_down
                        )
                    )
                }
            }
        }
    }

    private fun animation(expanded: ConstraintLayout): Animation {
        val animation: Animation =
            SlideAnimation(
                expanded,
                expanded.height,
                0
            )

        animation.interpolator = DecelerateInterpolator()
        animation.duration = 150
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                animation?.setAnimationListener(null)
                expanded.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })

        return animation
    }
}