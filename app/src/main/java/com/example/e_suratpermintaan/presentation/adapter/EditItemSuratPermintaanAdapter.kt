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
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.e_suratpermintaan.core.domain.entities.responses.ItemsDetailSP
import com.e_suratpermintaan.core.domain.entities.responses.PersyaratanItemDetailSP
import com.example.e_suratpermintaan.R
import com.example.e_suratpermintaan.external.constants.RoleConstants
import com.example.e_suratpermintaan.framework.utils.animations.SlideAnimation
import kotlinx.android.synthetic.main.item_simple_checkbox.view.*

class EditItemSuratPermintaanAdapter(): RecyclerView.Adapter<EditItemSuratPermintaanAdapter.ViewHolder>() {

    companion object {
        val SPA = 0
        val SPB = 1
        val SPS = 2
        const val BTN_HAPUS = "btnHapus"
        const val BTN_EDIT = "btnEdit"
        const val BTN_PENUGASAN = "btnPenugasan"
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
            SPA -> ViewHolderSPA(inflater.inflate(R.layout.detail_history_spa_item, parent, false))
            SPB -> ViewHolderSPB(inflater.inflate(R.layout.detail_history_spb_item, parent, false))
            else -> ViewHolderSPS(inflater.inflate(R.layout.detail_history_sps_item, parent, false))
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: EditItemSuratPermintaanAdapter.ViewHolder, position: Int) {
        val data = itemList.get(position)

        when (getItemViewType(position)) {
            SPA -> {
                val viewHolderSPA = holder as ViewHolderSPA

                if (data.tombolEditItem == 1) {
                    viewHolderSPA.btnEditSPA.visibility = View.VISIBLE
                    viewHolderSPA.btnEditSPA.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_EDIT)
                    }
                } else {
                    viewHolderSPA.btnEditSPA.visibility = View.GONE
                }
                if (data.tombolHapusItem == 1) {
                    viewHolderSPA.btnHapusSPA.visibility = View.VISIBLE
                    viewHolderSPA.btnHapusSPA.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_HAPUS)
                    }
                } else {
                    viewHolderSPA.btnHapusSPA.visibility = View.GONE
                }
                if (data.tombolPenugasan == 1) {
                    viewHolderSPA.btnPenugasanSPA.visibility = View.VISIBLE
                    viewHolderSPA.btnPenugasanSPA.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_PENUGASAN)
                    }
                } else {
                    viewHolderSPA.btnPenugasanSPA.visibility = View.GONE
                }

                viewHolderSPA.kodeDetailSPA.text = data.kodePekerjaan
                viewHolderSPA.jenisBarangSPA.text = data.idBarang
                viewHolderSPA.kategoriDetailSPA.text = data.kategori
                viewHolderSPA.jenisDetailSPA.text = data.idBarang
                viewHolderSPA.satuanDetailSPA.text = data.idSatuan
                viewHolderSPA.kapasitasDetailSPA.text = data.kapasitas
                viewHolderSPA.waktuDetailSPA.text = data.waktuPemakaian
                viewHolderSPA.merkDetailSPA.text = data.merk
                viewHolderSPA.qtyDetailSPA.text = data.qty
                viewHolderSPA.penugasanSPA.text = data.kepada
                viewHolderSPA.statusPenugasanSPA.text = data.penugasan

                if (idRole.equals(RoleConstants.CC) || idRole.equals(RoleConstants.IT) || idRole.equals(RoleConstants.GA)){
                    viewHolderSPA.labelPenugasanSPA.visibility = View.VISIBLE
                    viewHolderSPA.labelStatusPenugasanSPA.visibility = View.VISIBLE
                    viewHolderSPA.penugasanSPA.visibility = View.VISIBLE
                    viewHolderSPA.statusPenugasanSPA.visibility = View.VISIBLE
                }

                var dataKeterangan: String? = ""

                data.keterangan?.forEach {
                    dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
                }
                viewHolderSPA.keteranganDetailSPA.text = dataKeterangan

                viewHolderSPA.expandedParentSPA.setOnClickListener {
                    if (viewHolderSPA.expandedChildSPA.visibility == View.GONE) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(
                                viewHolderSPA.cardViewSPA,
                                TransitionSet()
                                    .addTransition(ChangeBounds())
                            )
                        }
                        viewHolderSPA.expandedChildSPA.visibility = View.VISIBLE
                        viewHolderSPA.imageExpandedParentSPA.setImageDrawable(
                            viewHolderSPA.imageExpandedParentSPA.context.resources.getDrawable(
                                R.drawable.ic_arrow_up
                            )
                        )
                    } else {
                        val animation = animation(viewHolderSPA.expandedChildSPA)

                        viewHolderSPA.expandedChildSPA.animation = animation
                        viewHolderSPA.expandedChildSPA.startAnimation(animation)
                        viewHolderSPA.imageExpandedParentSPA.setImageDrawable(
                            viewHolderSPA.imageExpandedParentSPA.context.resources.getDrawable(
                                R.drawable.ic_arrow_down
                            )
                        )
                    }

                }
            }

            SPB -> {
                val viewHolderSPB = holder as ViewHolderSPB

                if (data.tombolEditItem == 1) {
                    viewHolderSPB.btnEditSPB.visibility = View.VISIBLE
                    viewHolderSPB.btnEditSPB.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_EDIT)
                    }
                } else {
                    viewHolderSPB.btnEditSPB.visibility = View.GONE
                }
                if (data.tombolHapusItem == 1) {
                    viewHolderSPB.btnHapusSPB.visibility = View.VISIBLE
                    viewHolderSPB.btnHapusSPB.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_HAPUS)
                    }
                } else {
                    viewHolderSPB.btnHapusSPB.visibility = View.GONE
                }
                if (data.tombolPenugasan == 1) {
                    viewHolderSPB.btnPenugasanSPB.visibility = View.VISIBLE
                    viewHolderSPB.btnPenugasanSPB.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_PENUGASAN)
                    }
                } else {
                    viewHolderSPB.btnPenugasanSPB.visibility = View.GONE
                }

                viewHolderSPB.kodeDetailSPB.text = data.kodePekerjaan
                viewHolderSPB.jenisBarangSPB.text = data.idBarang
                viewHolderSPB.kategoriDetailSPB.text = data.kategori
                viewHolderSPB.jenisDetailSPB.text = data.idBarang
                viewHolderSPB.satuanDetailSPB.text = data.idSatuan
                viewHolderSPB.fungsiDetailSPB.text = data.fungsi
                viewHolderSPB.targetDetailSPB.text = data.target
                viewHolderSPB.qtyDetailSPB.text = data.qty
                viewHolderSPB.penugasanSPB.text = data.kepada
                viewHolderSPB.statusPenugasanSPB.text = data.penugasan

                if (idRole.equals(RoleConstants.CC) || idRole.equals(RoleConstants.IT) || idRole.equals(RoleConstants.GA)){
                    viewHolderSPB.labelPenugasanSPB.visibility = View.VISIBLE
                    viewHolderSPB.labelStatusPenugasanSPB.visibility = View.VISIBLE
                    viewHolderSPB.penugasanSPB.visibility = View.VISIBLE
                    viewHolderSPB.statusPenugasanSPB.visibility = View.VISIBLE
                }

                var dataKeterangan: String? = ""

                data.keterangan?.forEach {
                    dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
                }
                viewHolderSPB.keteranganDetailSPB.text = dataKeterangan

                viewHolderSPB.expandedParentSPB.setOnClickListener {
                    if (viewHolderSPB.expandedChildSPB.visibility == View.GONE) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(
                                viewHolderSPB.cardViewSPB,
                                TransitionSet()
                                    .addTransition(ChangeBounds())
                            )
                        }
                        viewHolderSPB.expandedChildSPB.visibility = View.VISIBLE
                        viewHolderSPB.imageExpandedParentSPB.setImageDrawable(
                            viewHolderSPB.imageExpandedParentSPB.context.resources.getDrawable(
                                R.drawable.ic_arrow_up
                            )
                        )
                    } else {
                        val animation = animation(viewHolderSPB.expandedChildSPB)

                        viewHolderSPB.expandedChildSPB.animation = animation
                        viewHolderSPB.expandedChildSPB.startAnimation(animation)
                        viewHolderSPB.imageExpandedParentSPB.setImageDrawable(
                            viewHolderSPB.imageExpandedParentSPB.context.resources.getDrawable(
                                R.drawable.ic_arrow_down
                            )
                        )
                    }
                }
            }

            SPS -> {
                val viewHolderSPS = holder as ViewHolderSPS

                if (data.tombolEditItem == 1) {
                    viewHolderSPS.btnEditSPS.visibility = View.VISIBLE
                    viewHolderSPS.btnEditSPS.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_EDIT)
                    }
                } else {
                    viewHolderSPS.btnEditSPS.visibility = View.GONE
                }
                if (data.tombolHapusItem == 1) {
                    viewHolderSPS.btnHapusSPS.visibility = View.VISIBLE
                    viewHolderSPS.btnHapusSPS.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_HAPUS)
                    }
                } else {
                    viewHolderSPS.btnHapusSPS.visibility = View.GONE
                }
                if (data.tombolPenugasan == 1) {
                    viewHolderSPS.btnPenugasanSPS.visibility = View.VISIBLE
                    viewHolderSPS.btnPenugasanSPS.setOnClickListener {
                        onItemClickListener.invoke(data, BTN_PENUGASAN)
                    }
                } else {
                    viewHolderSPS.btnPenugasanSPS.visibility = View.GONE
                }

                viewHolderSPS.kodeDetailSPS.text = data.kodePekerjaan
                viewHolderSPS.jenisBarangSPS.text = data.idBarang
                viewHolderSPS.kategoriDetailSPS.text = data.kategori
                viewHolderSPS.jenisDetailSPS.text = data.idBarang
                viewHolderSPS.satuanDetailSPS.text = data.idSatuan
                viewHolderSPS.waktuDetailSPS.text = data.waktuPelaksanaan
                viewHolderSPS.qtyDetailSPS.text = data.qty
                viewHolderSPS.penugasanSPS.text = data.kepada
                viewHolderSPS.statusPenugasanSPS.text = data.penugasan

                if (idRole.equals(RoleConstants.CC) || idRole.equals(RoleConstants.IT) || idRole.equals(RoleConstants.GA)){
                    viewHolderSPS.labelPenugasanSPS.visibility = View.VISIBLE
                    viewHolderSPS.labelStatusPenugasanSPS.visibility = View.VISIBLE
                    viewHolderSPS.penugasanSPS.visibility = View.VISIBLE
                    viewHolderSPS.statusPenugasanSPS.visibility = View.VISIBLE
                }


                val parent = viewHolderSPS.persyaratanDetailSPS
                parent.removeAllViews()
                persyaratanList.forEach { itemMaster ->

                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_simple_checkbox, parent, false)

                    var isChecked = false
                    data.persyaratan?.forEach { persyaratanItem ->
                        val dataSyarat = persyaratanItem as PersyaratanItemDetailSP
                        if (itemMaster.key == dataSyarat.persyaratan){
                            isChecked = true
                        }
                    }

                    if (isChecked){
                        view.checkbox.isChecked = true
                    }

                    view.checkbox.text = itemMaster.value
                    view.checkbox.setOnCheckedChangeListener { _, checked ->
                        view.checkbox.isChecked = !checked
                    }

                    parent.addView(view)
                }



                var dataKeterangan: String? = ""

                data.keterangan?.forEach {
                    dataKeterangan += "${it?.tanggal}  ${it?.namaUser} - ${it?.roleUser} : ${it?.keterangan}\n"
                }
                viewHolderSPS.keteranganDetailSPS.text = dataKeterangan

                viewHolderSPS.expandedParentSPS.setOnClickListener {
                    if (viewHolderSPS.expandedChildSPS.visibility == View.GONE) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            TransitionManager.beginDelayedTransition(
                                viewHolderSPS.cardViewSPS,
                                TransitionSet()
                                    .addTransition(ChangeBounds())
                            )
                        }
                        viewHolderSPS.expandedChildSPS.visibility = View.VISIBLE
                        viewHolderSPS.imageExpandedParentSPS.setImageDrawable(
                            viewHolderSPS.imageExpandedParentSPS.context.resources.getDrawable(
                                R.drawable.ic_arrow_up
                            )
                        )
                    } else {
                        val animation = animation(viewHolderSPS.expandedChildSPS)

                        viewHolderSPS.expandedChildSPS.animation = animation
                        viewHolderSPS.expandedChildSPS.startAnimation(animation)
                        viewHolderSPS.imageExpandedParentSPS.setImageDrawable(
                            viewHolderSPS.imageExpandedParentSPS.context.resources.getDrawable(
                                R.drawable.ic_arrow_down
                            )
                        )
                    }
                }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (viewType[position].equals("SPA")) {
            return SPA
        } else if (viewType[position].equals("SPB")) {
            return SPB
        } else {
            return SPS
        }
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderSPA(itemView: View) : ViewHolder(itemView) {
        val kodeDetailSPA: TextView = itemView.findViewById(R.id.tvKodeDetailSPA)
        val jenisBarangSPA: TextView = itemView.findViewById(R.id.tvJenisBarangSPA)
        val kategoriDetailSPA: TextView = itemView.findViewById(R.id.tvKategoriSPA)
        val jenisDetailSPA: TextView = itemView.findViewById(R.id.tvJenisDetailSPA)
        val satuanDetailSPA: TextView = itemView.findViewById(R.id.tvSatuanDetailSPA)
        val kapasitasDetailSPA: TextView = itemView.findViewById(R.id.tvKapasitasDetailSPA)
        val merkDetailSPA: TextView = itemView.findViewById(R.id.tvMerkDetailSPA)
        val waktuDetailSPA: TextView = itemView.findViewById(R.id.tvWaktuDetailSPA)
        val qtyDetailSPA: TextView = itemView.findViewById(R.id.tvQtySPA)
        val keteranganDetailSPA: TextView = itemView.findViewById(R.id.tvKeteranganDetailSPA)
        val expandedChildSPA: ConstraintLayout = itemView.findViewById(R.id.expandedChildSPA)
        val expandedParentSPA: ConstraintLayout = itemView.findViewById(R.id.expandedParentSPA)
        val cardViewSPA: CardView = itemView.findViewById(R.id.cardViewSPA)
        val imageExpandedParentSPA: ImageView = itemView.findViewById(R.id.imgExpandedParentSPA)
        val btnEditSPA: Button = itemView.findViewById(R.id.btnEditSPA)
        val btnHapusSPA: Button = itemView.findViewById(R.id.btnHapusSPA)
        val btnPenugasanSPA: Button = itemView.findViewById(R.id.btnPenugasanSPA)
        val labelPenugasanSPA: TextView = itemView.findViewById(R.id.labelPenugasanSPA)
        val labelStatusPenugasanSPA: TextView = itemView.findViewById(R.id.labelStatusPenugasanSPA)
        val penugasanSPA: TextView = itemView.findViewById(R.id.tvPenugasanDetailSPA)
        val statusPenugasanSPA: TextView = itemView.findViewById(R.id.tvStatusPenugasanDetailSPA)
    }

    inner class ViewHolderSPB(itemView: View) : ViewHolder(itemView) {
        val kodeDetailSPB: TextView = itemView.findViewById(R.id.tvKodeDetailSPB)
        val jenisBarangSPB: TextView = itemView.findViewById(R.id.tvJenisBarangSPB)
        val kategoriDetailSPB: TextView = itemView.findViewById(R.id.tvKategoriSPB)
        val jenisDetailSPB: TextView = itemView.findViewById(R.id.tvJenisDetailSPB)
        val satuanDetailSPB: TextView = itemView.findViewById(R.id.tvSatuanDetailSPB)
        val fungsiDetailSPB: TextView = itemView.findViewById(R.id.tvFungsiDetailSPB)
        val targetDetailSPB: TextView = itemView.findViewById(R.id.tvTargetDetailSPB)
        val qtyDetailSPB: TextView = itemView.findViewById(R.id.tvQtySPB)
        val keteranganDetailSPB: TextView = itemView.findViewById(R.id.tvKeteranganDetailSPB)
        val expandedChildSPB: ConstraintLayout = itemView.findViewById(R.id.expandedChildSPB)
        val expandedParentSPB: ConstraintLayout = itemView.findViewById(R.id.expandedParentSPB)
        val cardViewSPB: CardView = itemView.findViewById(R.id.cardViewSPB)
        val imageExpandedParentSPB: ImageView = itemView.findViewById(R.id.imgExpandedParentSPB)
        val btnEditSPB: Button = itemView.findViewById(R.id.btnEditSPB)
        val btnHapusSPB: Button = itemView.findViewById(R.id.btnHapusSPB)
        val btnPenugasanSPB: Button = itemView.findViewById(R.id.btnPenugasanSPB)
        val labelPenugasanSPB: TextView = itemView.findViewById(R.id.labelPenugasanSPB)
        val labelStatusPenugasanSPB: TextView = itemView.findViewById(R.id.labelStatusPenugasanSPB)
        val penugasanSPB: TextView = itemView.findViewById(R.id.tvPenugasanDetailSPB)
        val statusPenugasanSPB: TextView = itemView.findViewById(R.id.tvStatusPenugasanDetailSPB)

    }

    inner class ViewHolderSPS(itemView: View) : ViewHolder(itemView) {
        val kodeDetailSPS: TextView = itemView.findViewById(R.id.tvKodeDetailSPS)
        val jenisBarangSPS: TextView = itemView.findViewById(R.id.tvJenisBarangSPS)
        val kategoriDetailSPS: TextView = itemView.findViewById(R.id.tvKategoriSPS)
        val jenisDetailSPS: TextView = itemView.findViewById(R.id.tvJenisDetailSPS)
        val satuanDetailSPS: TextView = itemView.findViewById(R.id.tvSatuanDetailSPS)
        val persyaratanDetailSPS: LinearLayout = itemView.findViewById(R.id.tvPersyaratanDetailSPS)
        val waktuDetailSPS: TextView = itemView.findViewById(R.id.tvWaktuDetailSPS)
        val qtyDetailSPS: TextView = itemView.findViewById(R.id.tvQtySPS)
        val keteranganDetailSPS: TextView = itemView.findViewById(R.id.tvKeteranganDetailSPS)
        val expandedChildSPS: ConstraintLayout = itemView.findViewById(R.id.expandedChildSPS)
        val expandedParentSPS: ConstraintLayout = itemView.findViewById(R.id.expandedParentSPS)
        val cardViewSPS: CardView = itemView.findViewById(R.id.cardViewSPS)
        val imageExpandedParentSPS: ImageView = itemView.findViewById(R.id.imgExpandedParentSPS)
        val btnEditSPS: Button = itemView.findViewById(R.id.btnEditSPS)
        val btnHapusSPS: Button = itemView.findViewById(R.id.btnHapusSPS)
        val btnPenugasanSPS: Button = itemView.findViewById(R.id.btnPenugasanSPS)
        val labelPenugasanSPS: TextView = itemView.findViewById(R.id.labelPenugasanSPS)
        val labelStatusPenugasanSPS: TextView = itemView.findViewById(R.id.labelStatusPenugasanSPS)
        val penugasanSPS: TextView = itemView.findViewById(R.id.tvPenugasanDetailSPS)
        val statusPenugasanSPS: TextView = itemView.findViewById(R.id.tvStatusPenugasanDetailSPS)
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