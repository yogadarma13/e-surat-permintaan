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
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.e_suratpermintaan.core.domain.entities.responses.PersyaratanItemDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.databinding.DetailHistorySpaItemBinding
import com.example.e_suratpermintaan.databinding.DetailHistorySpbItemBinding
import com.example.e_suratpermintaan.databinding.DetailHistorySpsItemBinding
import com.example.e_suratpermintaan.databinding.ItemSimpleCheckboxBinding
import com.example.e_suratpermintaan.external.constants.RoleConstants
import com.example.e_suratpermintaan.framework.utils.animations.SlideAnimation

class ItemSuratPermintaanAdapter : RecyclerView.Adapter<ItemSuratPermintaanAdapter.ViewHolder>() {

    companion object {
        const val SPA = 0
        const val SPB = 1
        const val SPS = 2
    }

    val itemList: ArrayList<ItemsDetailSP> = arrayListOf()
    val viewType: ArrayList<String> = arrayListOf()
    val persyaratanList = mutableMapOf<String, String>()
    var idRole: String? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemSuratPermintaanAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SPA -> ViewHolderSPA(DetailHistorySpaItemBinding.inflate(inflater, parent, false))
            SPB -> ViewHolderSPB(DetailHistorySpbItemBinding.inflate(inflater, parent, false))
            else -> ViewHolderSPS(DetailHistorySpsItemBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemSuratPermintaanAdapter.ViewHolder, position: Int) {
        val data = itemList[position]
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

    inner class ViewHolderSPA(private val binding: DetailHistorySpaItemBinding) :
        ViewHolder(binding.root) {

        fun bind(data: ItemsDetailSP) {
            binding.tvKodeDetailSPA.text = data.kodePekerjaan
            binding.tvJenisBarangSPA.text = data.idBarang
            binding.tvKategoriSPA.text = data.kategori
            binding.tvJenisDetailSPA.text = data.idBarang
            binding.tvSatuanDetailSPA.text = data.idSatuan
            binding.tvKapasitasDetailSPA.text = data.kapasitas
            binding.tvWaktuDetailSPA.text = data.waktuPemakaian
            binding.tvMerkDetailSPA.text = data.merk
            binding.tvQtySPA.text = data.qty
            binding.tvPenugasanDetailSPA.text = data.kepada
            binding.tvStatusPenugasanDetailSPA.text = data.penugasan
            binding.tvProcessByPenugasanDetailSPA.text = data.processBy

            // 3 -> CC  |  7 -> IT  |  8 -> GA
            if (idRole.equals(RoleConstants.CC) || idRole.equals(RoleConstants.IT) || idRole.equals(
                    RoleConstants.GA
                )
            ) {
                binding.labelPenugasanSPA.visibility = View.VISIBLE
                binding.labelStatusPenugasanSPA.visibility = View.VISIBLE
                binding.tvPenugasanDetailSPA.visibility = View.VISIBLE
                binding.tvStatusPenugasanDetailSPA.visibility = View.VISIBLE
            } else {
                binding.labelPenugasanSPA.visibility = View.GONE
                binding.labelStatusPenugasanSPA.visibility = View.GONE
                binding.tvPenugasanDetailSPA.visibility = View.GONE
                binding.tvStatusPenugasanDetailSPA.visibility = View.GONE
            }

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

    inner class ViewHolderSPB(private val binding: DetailHistorySpbItemBinding) :
        ViewHolder(binding.root) {
        fun bind(data: ItemsDetailSP) {
            binding.tvKodeDetailSPB.text = data.kodePekerjaan
            binding.tvJenisBarangSPB.text = data.idBarang
            binding.tvKategoriSPB.text = data.kategori
            binding.tvJenisDetailSPB.text = data.idBarang
            binding.tvSatuanDetailSPB.text = data.idSatuan
            binding.tvFungsiDetailSPB.text = data.fungsi
            binding.tvTargetDetailSPB.text = data.target
            binding.tvQtySPB.text = data.qty
            binding.tvPenugasanDetailSPB.text = data.kepada
            binding.tvStatusPenugasanDetailSPB.text = data.penugasan
            binding.tvProcessByPenugasanDetailSPB.text = data.processBy

            // 3 -> CC  |  7 -> IT  |  8 -> GA
            if (idRole.equals(RoleConstants.CC) || idRole.equals(RoleConstants.IT) || idRole.equals(
                    RoleConstants.GA
                )
            ) {
                binding.labelPenugasanSPB.visibility = View.VISIBLE
                binding.labelStatusPenugasanSPB.visibility = View.VISIBLE
                binding.tvPenugasanDetailSPB.visibility = View.VISIBLE
                binding.tvStatusPenugasanDetailSPB.visibility = View.VISIBLE
            } else {
                binding.labelPenugasanSPB.visibility = View.GONE
                binding.labelStatusPenugasanSPB.visibility = View.GONE
                binding.tvPenugasanDetailSPB.visibility = View.GONE
                binding.tvStatusPenugasanDetailSPB.visibility = View.GONE
            }

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

    inner class ViewHolderSPS(private val binding: DetailHistorySpsItemBinding) :
        ViewHolder(binding.root) {

        fun bind(data: ItemsDetailSP) {
            binding.tvKodeDetailSPS.text = data.kodePekerjaan
            binding.tvJenisBarangSPS.text = data.idBarang
            binding.tvKategoriSPS.text = data.kategori
            binding.tvJenisDetailSPS.text = data.idBarang
            binding.tvSatuanDetailSPS.text = data.idSatuan
            binding.tvWaktuDetailSPS.text = data.waktuPelaksanaan
            binding.tvQtySPS.text = data.qty
            binding.tvPenugasanDetailSPS.text = data.kepada
            binding.tvStatusPenugasanDetailSPS.text = data.penugasan
            binding.tvProcessByPenugasanDetailSPS.text = data.processBy

            // 3 -> CC  |  7 -> IT  |  8 -> GA
            if (idRole.equals(RoleConstants.CC) || idRole.equals(RoleConstants.IT) || idRole.equals(
                    RoleConstants.GA
                )
            ) {
                binding.labelPenugasanSPS.visibility = View.VISIBLE
                binding.labelStatusPenugasanSPS.visibility = View.VISIBLE
                binding.tvPenugasanDetailSPS.visibility = View.VISIBLE
                binding.tvStatusPenugasanDetailSPS.visibility = View.VISIBLE
            } else {
                binding.labelPenugasanSPS.visibility = View.GONE
                binding.labelStatusPenugasanSPS.visibility = View.GONE
                binding.tvPenugasanDetailSPS.visibility = View.GONE
                binding.tvStatusPenugasanDetailSPS.visibility = View.GONE
            }

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
                    val dataSyarat = persyaratanItem as PersyaratanItemDetailSP
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