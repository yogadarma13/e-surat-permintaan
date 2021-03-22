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

class EditItemSuratPermintaanAdapter :
    RecyclerView.Adapter<EditItemSuratPermintaanAdapter.ViewHolder>() {

    companion object {
        const val SPA = 0
        const val SPB = 1
        const val SPS = 2
        const val BTN_HAPUS = "btnHapus"
        const val BTN_EDIT = "btnEdit"
        const val BTN_PENUGASAN = "btnPenugasan"
        const val BTN_PROCESS = "btnProcess"
        const val BTN_UNPROCESS = "btnUnProcess"
        const val BTN_REJECT = "btnReject"
        const val BTN_ROLLBACK = "btnRollback"
    }

    val itemList: ArrayList<ItemsDetailSP> = arrayListOf()
    val viewType: ArrayList<String> = arrayListOf()
    val persyaratanList = mutableMapOf<String, String>()
    var idRole: String? = null

    private var onItemClickListener: ((Any?, String?) -> Unit) = { _, _ -> }

    fun setOnItemClickListener(listener: (item: Any?, actionString: String?) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditItemSuratPermintaanAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SPA -> ViewHolderSPA(DetailHistorySpaItemBinding.inflate(inflater, parent, false))
            SPB -> ViewHolderSPB(DetailHistorySpbItemBinding.inflate(inflater, parent, false))
            else -> ViewHolderSPS(DetailHistorySpsItemBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(
        holder: EditItemSuratPermintaanAdapter.ViewHolder,
        position: Int
    ) {
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
            if (data.tombolEditItem == 1) {
                binding.btnEditSPA.visibility = View.VISIBLE
                binding.btnEditSPA.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_EDIT)
                }
            } else {
                binding.btnEditSPA.visibility = View.GONE
            }
            if (data.tombolHapusItem == 1) {
                binding.btnHapusSPA.visibility = View.VISIBLE
                binding.btnHapusSPA.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_HAPUS)
                }
            } else {
                binding.btnHapusSPA.visibility = View.GONE
            }
            if (data.tombolPenugasan == 1) {
                binding.btnPenugasanSPA.visibility = View.VISIBLE
                binding.btnPenugasanSPA.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_PENUGASAN)
                }
            } else {
                binding.btnPenugasanSPA.visibility = View.GONE
            }
            if (data.tombolProcess == 1) {
                binding.btnProcessSPA.visibility = View.VISIBLE
                binding.btnProcessSPA.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_PROCESS)
                }
            } else {
                binding.btnProcessSPA.visibility = View.GONE
            }
            if (data.tombolUnProcess == 1) {
                binding.btnUnProcessSPA.visibility = View.VISIBLE
                binding.btnUnProcessSPA.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_UNPROCESS)
                }
            } else {
                binding.btnUnProcessSPA.visibility = View.GONE
            }
            if (data.tombolTolakItem == 1) {
                binding.btnRejectItem.visibility = View.VISIBLE
                binding.btnRejectItem.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_REJECT)
                }
            } else {
                binding.btnRejectItem.visibility = View.GONE
            }

            if (data.tombolRollbackItem == 1) {
                binding.btnRollback.visibility = View.VISIBLE
                binding.btnRollback.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_ROLLBACK)
                }
            } else {
                binding.btnRollback.visibility = View.GONE
            }

            binding.tvStatusItem.text = data.statusPd
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
            if (data.tombolEditItem == 1) {
                binding.btnEditSPB.visibility = View.VISIBLE
                binding.btnEditSPB.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_EDIT)
                }
            } else {
                binding.btnEditSPB.visibility = View.GONE
            }
            if (data.tombolHapusItem == 1) {
                binding.btnHapusSPB.visibility = View.VISIBLE
                binding.btnHapusSPB.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_HAPUS)
                }
            } else {
                binding.btnHapusSPB.visibility = View.GONE
            }
            if (data.tombolPenugasan == 1) {
                binding.btnPenugasanSPB.visibility = View.VISIBLE
                binding.btnPenugasanSPB.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_PENUGASAN)
                }
            } else {
                binding.btnPenugasanSPB.visibility = View.GONE
            }
            if (data.tombolProcess == 1) {
                binding.btnProcessSPB.visibility = View.VISIBLE
                binding.btnProcessSPB.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_PROCESS)
                }
            } else {
                binding.btnProcessSPB.visibility = View.GONE
            }
            if (data.tombolUnProcess == 1) {
                binding.btnUnProcessSPB.visibility = View.VISIBLE
                binding.btnUnProcessSPB.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_UNPROCESS)
                }
            } else {
                binding.btnUnProcessSPB.visibility = View.GONE
            }
            if (data.tombolTolakItem == 1) {
                binding.btnRejectItem.visibility = View.VISIBLE
                binding.btnRejectItem.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_REJECT)
                }
            } else {
                binding.btnRejectItem.visibility = View.GONE
            }

            if (data.tombolRollbackItem == 1) {
                binding.btnRollback.visibility = View.VISIBLE
                binding.btnRollback.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_ROLLBACK)
                }
            } else {
                binding.btnRollback.visibility = View.GONE
            }

            binding.tvStatusItem.text = data.statusPd
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
            if (data.tombolEditItem == 1) {
                binding.btnEditSPS.visibility = View.VISIBLE
                binding.btnEditSPS.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_EDIT)
                }
            } else {
                binding.btnEditSPS.visibility = View.GONE
            }
            if (data.tombolHapusItem == 1) {
                binding.btnHapusSPS.visibility = View.VISIBLE
                binding.btnHapusSPS.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_HAPUS)
                }
            } else {
                binding.btnHapusSPS.visibility = View.GONE
            }
            if (data.tombolPenugasan == 1) {
                binding.btnPenugasanSPS.visibility = View.VISIBLE
                binding.btnPenugasanSPS.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_PENUGASAN)
                }
            } else {
                binding.btnPenugasanSPS.visibility = View.GONE
            }
            if (data.tombolProcess == 1) {
                binding.btnProcessSPS.visibility = View.VISIBLE
                binding.btnProcessSPS.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_PROCESS)
                }
            } else {
                binding.btnProcessSPS.visibility = View.GONE
            }
            if (data.tombolUnProcess == 1) {
                binding.btnUnProcessSPS.visibility = View.VISIBLE
                binding.btnUnProcessSPS.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_UNPROCESS)
                }
            } else {
                binding.btnUnProcessSPS.visibility = View.GONE
            }

            if (data.tombolTolakItem == 1) {
                binding.btnRejectItem.visibility = View.VISIBLE
                binding.btnRejectItem.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_REJECT)
                }
            } else {
                binding.btnRejectItem.visibility = View.GONE
            }

            if (data.tombolRollbackItem == 1) {
                binding.btnRollback.visibility = View.VISIBLE
                binding.btnRollback.setOnClickListener {
                    onItemClickListener.invoke(data, BTN_ROLLBACK)
                }
            } else {
                binding.btnRollback.visibility = View.GONE
            }

            binding.tvStatusItem.text = data.statusPd
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

            if (idRole.equals(RoleConstants.CC) || idRole.equals(RoleConstants.IT) || idRole.equals(
                    RoleConstants.GA
                )
            ) {
                binding.labelPenugasanSPS.visibility = View.VISIBLE
                binding.labelStatusPenugasanSPS.visibility = View.VISIBLE
                binding.tvPenugasanDetailSPS.visibility = View.VISIBLE
                binding.tvStatusPenugasanDetailSPS.visibility = View.VISIBLE
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